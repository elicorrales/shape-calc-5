package com.eli.calc.shape.tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import com.eli.calc.shape.config.AppConfig;
import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;
import com.eli.calc.shape.service.ShapeCalculatorService;

public class JUnitTest {

	private ApplicationContext ctx;
	private ShapeCalculatorService calculator;
	
	@Rule
	public ExpectedException illegalArgThrown = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		
		ctx = new AnnotationConfigApplicationContext(AppConfig.class);

		((AbstractApplicationContext)ctx).registerShutdownHook();

		calculator = ctx.getBean(ShapeCalculatorService.class);     //by the interface
		
	}

	@After
	public void tearDown() throws Exception {
		((AbstractApplicationContext)ctx).close();
	}

	@Test
	public void testQueueRequestWithNullShapeName() {
		
		illegalArgThrown.expect(IllegalArgumentException.class);
		double dimension = 0;
		calculator.queueCalculationRequest(null, CalcType.CALC_AREA, dimension);
	}

	@Test
	public void testQueueRequestWithNullCalcType() {
		
		illegalArgThrown.expect(IllegalArgumentException.class);
		double dimension = 0;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, null, dimension);
	}

	@Test
	public void testQueueRequestWithNegativeDimension() {
		
		illegalArgThrown.expect(IllegalArgumentException.class);
		double dimension = -0.01;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
	}

	@Test
	public void testQueueRequestAndRetrievePendingRequest() {

		calculator.deleteAllPendingRequests();

		double dimension = 0;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		
		assertNotNull(requests);
		assertEquals(1,requests.size());
		
		calculator.deleteAllPendingRequests();

		requests = calculator.getAllPendingRequests();

		assertNotNull(requests);
		assertEquals(0,requests.size());
	}


	@Test
	public void testQueueRequestAndRetrievePendingMultipleSameRequests() {

		calculator.deleteAllPendingRequests();

		double dimension = 0;

		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);

		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		
		assertNotNull(requests);
		assertEquals(1,requests.size());
		
		calculator.deleteAllPendingRequests();

		requests = calculator.getAllPendingRequests();

		assertNotNull(requests);
		assertEquals(0,requests.size());
	}


	@Test
	public void testQueueRequestAndRetrievePendingMultipleDifferentRequests() {

		calculator.deleteAllPendingRequests();

		double dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);

		dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);


		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(6,requests.size());
		
		calculator.deleteAllPendingRequests();
		requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(0,requests.size());
	}


	@Test
	public void testQueueRequestAndRunNoStop() {

		calculator.deleteAllPendingRequests();

		double dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);

		dimension = 0.000;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension);
		dimension = 0.001;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension);
		dimension = 0.002;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);


		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(6,requests.size());
	
		int numRun = calculator.runAllPendingRequestsNoStopOnError();
		assertEquals(6,numRun);

		requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(0,requests.size());
		
		List<CalculationResult> results = calculator.getAllCalculationResults();
		assertNotNull(results);
		assertEquals(6,results.size());

		calculator.deleteAllResults();
		results = calculator.getAllCalculationResults();
		assertNotNull(results);
		assertEquals(0,results.size());
	}

	@Test
	public void testTestForCorrectCalculatedResults() {

		calculator.deleteAllPendingRequests();

		double dimension = 1;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
		dimension = 2;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension);
		dimension = 3;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension);
		dimension = 4;
		calculator.queueCalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, dimension);
		dimension = 5;
		calculator.queueCalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_AREA, dimension);
		dimension = 6;
		calculator.queueCalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_VOLUME, dimension);
		dimension = 1;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension);
		dimension = 2;
		calculator.queueCalculationRequest(ShapeName.SPHERE, CalcType.CALC_VOLUME, dimension);
		dimension = 3;
		calculator.queueCalculationRequest(ShapeName.CUBE, CalcType.CALC_AREA, dimension);
		dimension = 4;
		calculator.queueCalculationRequest(ShapeName.CUBE, CalcType.CALC_VOLUME, dimension);
		dimension = 5;
		calculator.queueCalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_AREA, dimension);
		dimension = 6;
		calculator.queueCalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_VOLUME, dimension);


		List<CalculationRequest> requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(12,requests.size());
	
		int numRun = calculator.runAllPendingRequestsNoStopOnError();
		assertEquals(12,numRun);

		requests = calculator.getAllPendingRequests();
		assertNotNull(requests);
		assertEquals(0,requests.size());
		
		List<CalculationResult> results = calculator.getAllCalculationResults();
		assertNotNull(results);
		assertEquals(12,results.size());

		//for direct access so we can test for correct result
		Map<CalculationRequest,CalculationResult> resultsMap = new HashMap<CalculationRequest, CalculationResult>(results.size());
		for (CalculationResult res : results) {
			resultsMap.put(res.getRequest(),res);
		}

		dimension = 1;
		assertEquals(3.14,resultsMap.get(new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 2;
		assertEquals(0,resultsMap.get(new CalculationRequest(ShapeName.CIRCLE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 3;
		assertEquals(9,resultsMap.get(new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_AREA, dimension)).getResult(),0.0);
		dimension = 4;
		assertEquals(0,resultsMap.get(new CalculationRequest(ShapeName.SQUARE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 5;
		assertEquals(10.83,resultsMap.get(
				new CalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 6;
		assertEquals(0,resultsMap.get(
				new CalculationRequest(ShapeName.EQUILATERALTRIANGLE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 1;
		assertEquals(12.57,resultsMap.get(new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 2;
		assertEquals(33.51,resultsMap.get(new CalculationRequest(ShapeName.SPHERE, CalcType.CALC_VOLUME, dimension)).getResult(),0.01);
		dimension = 3;
		assertEquals(54,resultsMap.get(new CalculationRequest(ShapeName.CUBE, CalcType.CALC_AREA, dimension)).getResult(),0.0);
		dimension = 4;
		assertEquals(64,resultsMap.get(new CalculationRequest(ShapeName.CUBE, CalcType.CALC_VOLUME, dimension)).getResult(),0.0);
		dimension = 5;
		assertEquals(43.3,resultsMap.get(new CalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_AREA, dimension)).getResult(),0.01);
		dimension = 6;
		assertEquals(25.46,resultsMap.get(new CalculationRequest(ShapeName.TETRAHEDRON, CalcType.CALC_VOLUME, dimension)).getResult(),0.01);


		calculator.deleteAllResults();
		results = calculator.getAllCalculationResults();
		assertNotNull(results);
		assertEquals(0,results.size());
	}


	@Test
	public void testDeleteAllResults() {
		calculator.deleteAllResults();
	}

	@Test
	public void testQueueCalculationRequest() {
		double dimension = 0;
		calculator.queueCalculationRequest(ShapeName.CIRCLE, CalcType.CALC_AREA, dimension);
	}

	@Test
	public void testGetAllPendingRequests() {
		calculator.getAllPendingRequests();
	}

	@Test
	public void testGetAllCalculationResults() {
		calculator.getAllCalculationResults();
	}

	@Test
	public void testRunAllPendingRequestsStopOnError() {
		calculator.runAllPendingRequestsStopOnError();
	}

	@Test
	public void testRunAllPendingRequestsNoStopOnError() {
		calculator.runAllPendingRequestsNoStopOnError();
	}

}