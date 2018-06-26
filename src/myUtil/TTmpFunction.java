package myUtil;

import matrix2017.TCMatrix;
import java.util.function.Function;

public enum TTmpFunction {
	Sphere("Sphere"),
	Ellipsoid("Ellipsoid"),
	RosenbrockChain("RosenbrockChain"),
	KTablet("KTablet");
	
	/** function name */
	private String fFuncName;
	
	/** function */
	private Function<TCMatrix, Double> fFunction;

	private TTmpFunction(String name) {
		this.fFuncName = name;
		setFunction();
	}
	
	public Function<TCMatrix, Double> getFunction() {
		return fFunction;
	}
	
	public void setFunction() {
		switch(this) {
		case Sphere:
			fFunction = (x) -> {
				double result = 0.0;
				int dimension = x.getRowDimension();
				for(int i = 0; i < dimension; i++) {
					result += Math.pow(x.getValue(i,0), 2);
				}
				return result;
			};
		case RosenbrockChain:
			fFunction = (x) -> {
                double result = 0.0;
                int dimensionSize = x.getRowDimension();
                for(int i = 0; i < dimensionSize - 1; i++) {
                  double e1 = x.getValue(i, 0);
                  double e2 = x.getValue(i + 1, 0);
                  double tmp1 = e2 - Math.pow(e1, 2);
                  double tmp2 = Math.pow(e1 - 1, 2);
                  result += 100.0 * Math.pow(tmp1, 2) + Math.pow(tmp2, 2);
                }
                return result;
			};
			
		case KTablet:
			fFunction = (x) -> {
				double result = 0.0;
				int dimension = x.getRowDimension();
				int k = dimension / 4;

				for(int i = 0; i < k; i++) {
					result += Math.pow(x.getValue(i, 0), 2);
				}
				for(int i = k; i < dimension; i++) {
					result += Math.pow(x.getValue(i, 0) * 100.0, 2);
				}
				return result;
			};
			
		case Ellipsoid:
			fFunction = (x) -> {
				double result = 0.0;
				int dimension = x.getRowDimension();
				for(int i = 0; i < dimension; i++) {
					double tmp = Math.pow(1000, i / (dimension - 1.0));
					result += Math.pow(tmp * x.getValue(i, 0), 2);
				}
				return result;
			};
		}
		
	}
	
}
