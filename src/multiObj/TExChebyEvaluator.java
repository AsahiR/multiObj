package multiObj;
import java.util.ArrayList;
import myUtil.MyMatrixUtil;
import matrix2017.*;
import myUtil.*;

public class TExChebyEvaluator extends TEvaluator{
	final private ArrayList<TFunction> fFunctions;
	final private double fAlpha = 1.0e-10;

	/** vector of mins of each function */
	final private TCMatrix fMins;
	
	/** weigths for fFunctions */
	private ArrayList<Double> fWeights;
	
	/** for fileName */
	private String fName;
	
	/**
	 * constructor
	 * @param functions MultiObjective funtions
	 * @param mins vector of each function min
	 */
	public TExChebyEvaluator(ArrayList<TFunction> functions, TCMatrix mins)  {
		fFunctions = functions;
		fMins = mins;
		setName();
	}
	
	@Override
	public void setWeights(ArrayList<Double> weights) {
		assert fFunctions.size() == weights.size();
		fWeights = weights;
	}
	
	/**
	 * 
	 * @param indv set evalVector and evalationValue into indv
	 */
	public void evaluateIndv(TIndividual indv) {
		setEvaluationVector(indv);
		setEvaluationValue(indv);
	}
	
	/**
	 * @param indv evaluate indv by each function from fFunctions
	 */
	private void setEvaluationVector(TIndividual indv) {
		int frontDimension = fFunctions.size();
		TCMatrix evalVector = new TCMatrix(frontDimension);
		MyMatrixUtil.calcFront(indv.getVector(), fFunctions, evalVector);
		indv.setEvaluationVector(evalVector);
	}
	
	/**
	 * @param indv evaluate indv by scalarized single function from fFunctions and fWeights
	 */
	private void setEvaluationValue(TIndividual indv) {
		double eval = 0;
        double tmp = 0;
        int frontDimension = fFunctions.size();
        TCMatrix evalVector = indv.getEvaluationVector();
        for(int i = 0; i < frontDimension; i++) {
          tmp += fWeights.get(i) * (evalVector.getValue(i) - fMins.getValue(i));
        }

        tmp *= fAlpha;
        eval += tmp;
        double max = Double.MIN_VALUE;
      
        for(int i = 0; i < frontDimension; i++) {
          tmp = fWeights.get(i) * Math.abs(evalVector.getValue(i) - fMins.getValue(i));
          max = max > tmp ? max : tmp;
        }
        eval += max;
        indv.setEvaluationValue(eval);
	}

	/**
	 * @param pop evaluate population
	 */
	public void evaluate(TPopulation pop) {
		int popSize = pop.getPopulationSize();
		for(int i = 0; i < popSize; i++) {
			evaluateIndv(pop.getIndividual(i));
		}
	}
	
	public void setName() {
		fName = "ExChebyshvNorm" + TMultiObjUtil.makeFunctionsName(fFunctions);
	}
	
	public String getName() {
		return fName;
	}
	
}
