package multiObj;

import matrix2017.TCMatrix;
import java.util.Comparator;

public class TComparatorWithFeasibility implements Comparator<TIndividual>{
	@Override
	public int compare(TIndividual o1, TIndividual o2){
		double result;
		double eval1 = o1.getEvaluationValue();
		double eval2 = o2.getEvaluationValue();
		if(eval1 == Double.MAX_VALUE &&
				eval2 == Double.MAX_VALUE) {
			TCMatrix zVector1 = o1.getZVector();
			TCMatrix zVector2 = o2.getZVector();
			result =  zVector1.normL2() - zVector2.normL2();
		}else {
			result = eval1 - eval2;
		}

		if (result > 0.0) {
			return 1;
		}
		else if (result < 0.0){
			return -1;
		}
		else {
			return 0;
		}
	}
}
