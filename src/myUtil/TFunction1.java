package myUtil;
import matrix2017.*;

/** this  and TFunction2 are in inverse proportion */
public class TFunction1 extends TFunction {
	private final String fName = "Function1";

	public double calc(TCMatrix x) {
		double result = 0.0;
		double e1 = x.getValue(0);
		double e2 = x.getValue(1);
		result += Math.pow(e1, 2) + Math.pow(e2, 2) - e1 * e2;
		result += -(e1 + e2) + 1;
		return result;
	}

	public String getName() {
		return fName;
	}
}
