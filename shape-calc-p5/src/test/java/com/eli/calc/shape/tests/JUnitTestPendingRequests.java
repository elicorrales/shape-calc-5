package com.eli.calc.shape.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.PendingRequests;
import com.eli.calc.shape.service.impl.PendingRequestsImpl;

public class JUnitTestPendingRequests {

	private static final Logger logger = LoggerFactory.getLogger(JUnitTestPendingRequests.class);

	private PendingRequests requests;
	
	@Before
	public void setUp() throws Exception {
		requests = new PendingRequestsImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRequestsForExceptionsDuringPossibleRaceConditions() {


		// this class will run the Runnable tasks (see further down)
		// in a coordinated (with main thread) fashion
		final class LatchedThread extends Thread {
			private CountDownLatch _readyLatch;
			private CountDownLatch _startLatch;
			private CountDownLatch _stopLatch;
			LatchedThread(Runnable runnable, List<LatchedThread> threads){
				super(runnable);
				threads.add(this);
			}

			void setReadyLatch(CountDownLatch l) { _readyLatch = l; }

			void setStartLatch(CountDownLatch l) { _startLatch = l; }

			void setStopLatch(CountDownLatch l) { _stopLatch = l; }

			public void start() {
				if (null==_readyLatch) { throw new IllegalArgumentException("_readyLatch not set"); }
				if (null==_startLatch) { throw new IllegalArgumentException("_startLatch not set"); }
				if (null==_stopLatch) { throw new IllegalArgumentException("_stopLatch not set"); }
				super.start();
			}

			public void run() {
				try {
					_readyLatch.countDown(); //this thread signals its readiness 
					_startLatch.await();     //this thread waits to run
					super.run();
					_stopLatch.countDown();  //this thread signals its finished
				} catch (InterruptedException ie) {}
			}
		}

		int loopMax = 100000;

		Runnable deleteAllRequestsTask = () -> {
			logger.debug("\n\ndelete\n\n");
			for (int i=0; i<loopMax; i++) {
				requests.deleteAllRequests();
			}
		};
		
		Runnable addRequestsTask = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				logger.debug("\n\n"+dimension+"\n\n");
				requests.putRequest(new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, (double)dimension));
			}
		};

		Runnable addRequestsTask2 = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				logger.debug("\n\n"+dimension+"\n\n");
				requests.putRequest(new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, (double)dimension));
			}
		};

		Runnable addRequestsTask3 = () -> {
			for (int dimension=0; dimension<loopMax; dimension++) {
				logger.debug("\n\n"+dimension+"\n\n");
				requests.putRequest(new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, (double)dimension));
			}
		};


		final List<LatchedThread> threads = new ArrayList<LatchedThread>();
		//new LatchedThread(deleteAllRequestsTask,threads);
		new LatchedThread(addRequestsTask,threads);
		new LatchedThread(addRequestsTask2,threads);
		new LatchedThread(addRequestsTask3,threads);

		CountDownLatch readyLatch = new CountDownLatch(threads.size());
		CountDownLatch startLatch = new CountDownLatch(threads.size());
		CountDownLatch stopLatch = new CountDownLatch(threads.size());

		//do the initial start...
		for (LatchedThread t : threads) {
			t.setReadyLatch(readyLatch);
			t.setStartLatch(startLatch);
			t.setStopLatch(stopLatch);
			t.start();
		}

		logger.debug("\n\nMain thread has started child threads..waiting...\n\n");

		//wait until all threads are in position to start
		// each thread will count down the ready latch, and main thread will
		// move beyond this point
		try { readyLatch.await(); } catch (InterruptedException ie) {}
		
		logger.debug("\n\nAll child threads read to run - Main thread will set them off.....\n\n");


		//now the main thread will count down the start latch, so that the
		//task threads can all leave the starting gate
		for (Thread t : threads) {
			startLatch.countDown();
		}
	
		logger.debug("\n\nMain thread waiting for child threads to finish.....\n\n");

		//now the main thread must wait (to exit)
		//until all the task threads are done
		try { stopLatch.await(); } catch (InterruptedException ie) {}
		
		logger.debug("\n\nMain thread testing some results.....\n\n");

		long numRequests = requests.getNumRequests();
		assertEquals(loopMax*3 ,numRequests);

	
	}

}
