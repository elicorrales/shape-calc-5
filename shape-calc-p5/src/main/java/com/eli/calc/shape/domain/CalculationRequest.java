package com.eli.calc.shape.domain;

import com.eli.calc.shape.model.CalcType;
import com.eli.calc.shape.model.ShapeName;

public final class CalculationRequest {

	private ShapeName shapeName;
	
	private CalcType calcType;
	
	private Double dimension;
	
	public CalculationRequest(ShapeName shapeName, CalcType calcType, Double dimension) {
		
		this.shapeName = shapeName;
		this.calcType = calcType;
		this.dimension = dimension;
	}

	public ShapeName getShapeName() {
		return shapeName;
	}

	public void setShapeName(ShapeName shapeName) {
		this.shapeName = shapeName;
	}

	public CalcType getCalcType() {
		return calcType;
	}

	public void setCalcType(CalcType calcType) {
		this.calcType = calcType;
	}

	public Double getDimension() {
		return dimension;
	}

	public void setDimension(Double dimension) {
		this.dimension = dimension;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calcType == null) ? 0 : calcType.hashCode());
		result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
		result = prime * result + ((shapeName == null) ? 0 : shapeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalculationRequest other = (CalculationRequest) obj;
		if (calcType != other.calcType)
			return false;
		if (dimension == null) {
			if (other.dimension != null)
				return false;
		} else if (!dimension.equals(other.dimension))
			return false;
		if (shapeName != other.shapeName)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CalculationRequest [shapeName=" + shapeName + ", calcType=" + calcType + ", dimension=" + dimension
				+ "]";
	}



}