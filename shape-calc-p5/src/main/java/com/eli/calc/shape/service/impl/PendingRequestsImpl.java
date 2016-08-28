package com.eli.calc.shape.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.service.PendingRequests;

@Component
public class PendingRequestsImpl implements PendingRequests {

	private final Set<CalculationRequest> requests = new HashSet<CalculationRequest>();

	public void deleteAllRequests() {
		requests.clear();
	}

	public List<CalculationRequest> getRequests() {
		return new ArrayList<CalculationRequest>(requests);
	}

	public void putRequest(CalculationRequest request) {
		requests.add(request);
	}

	public void removeRequest(CalculationRequest request) {
		requests.remove(request);
	}

	public long getNumRequests() {
		return requests.size();
	}


}