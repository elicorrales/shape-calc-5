package com.eli.calc.shape.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.service.PendingRequests;

@Component
public class PendingRequestsImpl implements PendingRequests {

	private static final Logger logger = LoggerFactory.getLogger(PendingRequestsImpl.class);

	private final Set<CalculationRequest> requests = new HashSet<CalculationRequest>();

	public void deleteAllRequests() {
		logger.debug("\n\ndeleteAllRequests\n\n");
		requests.clear();
	}

	public List<CalculationRequest> getRequests() {
		return new ArrayList<CalculationRequest>(requests);
	}

	public void putRequest(CalculationRequest request) {
		logger.debug("\n\nputRequest\n\n");
		requests.add(request);
	}

	public void removeRequest(CalculationRequest request) {
		requests.remove(request);
	}

	public long getNumRequests() {
		return requests.size();
	}


}