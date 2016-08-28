package com.eli.calc.shape.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.eli.calc.shape.domain.CalculationRequest;
import com.eli.calc.shape.domain.CalculationResult;
import com.eli.calc.shape.service.CalculatedResults;

@Component
public class CalculatedResultsImpl implements CalculatedResults {

	private final Set<CalculationResult> results = new HashSet<CalculationResult>();

	public void deleteAllResults() {
		results.clear();
	}

	public void putResult(CalculationResult result) {
		results.add(result);
	}

	public void removeResult(CalculationResult result) {
		results.remove(result);
	}

	public boolean containsRequest(CalculationRequest request) {
		return false;
	}

	public List<CalculationResult> getResults() {
		return new ArrayList<CalculationResult>(results);
	}


}