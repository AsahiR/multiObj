package multiObj;

import java.util.ArrayList;
import java.util.function.Function;

import matrix2017.*;

public class TMultiStartScalar {
	private final int fSolutionsSize;

	/** set of weights */
	private ArrayList<ArrayList<Double>> fWeightList;
	private TPopulation fParetoSolutions;
	/** evaluate population using this */
	private TEvaluator fEvaluator;
	/** operator for generating population */
	private TXNes fXNes;
	/** index for weight in fWeightList */
	private int fWeightsIndex;

	
	public TMultiStartScalar(ArrayList<ArrayList<Double>> weightList, TEvaluator evaluator, TXNes xNes) {
		fWeightList = weightList;
		fSolutionsSize = fWeightList.size();
		fEvaluator = evaluator;
		fXNes = xNes;
	}
	
	public void init() {
        fParetoSolutions = new TPopulation(fSolutionsSize);
        fWeightsIndex = 0;
	}
	
	/**
	 * get one optimal indv for now weight and add it into fParetoSolutions
	 */
	public void execute() {
		final int maxEvaluationNum = (int)1e3;
		/** no use ???*/
		final double minEvaluationValue = 1e-10;

		for(int i = 0; i < fSolutionsSize; i++) {
			int evaluationNum = 0;
			fXNes.init();
            fEvaluator.setWeights(fWeightList.get(i));
			while(evaluationNum < maxEvaluationNum) {
                fXNes.setEvaluator(fEvaluator);
                fXNes.execute();
                evaluationNum = fXNes.getEvaluationNum();
			}
            TPopulation pop = fXNes.getPopulation();
            TIndividual bestIndv = pop.getIndividual(0);
            fParetoSolutions.getIndividual(i).copyFrom(bestIndv);
            fWeightsIndex ++;
		}
	}
	
	TPopulation getSolutions() {
		return fParetoSolutions;
	}
	
	public String makeName() {
		StringBuilder sb = new StringBuilder();
		sb.append("SolSize");
		sb.append(fSolutionsSize);
		sb.append("_");
		sb.append(fEvaluator.getName());
		sb.append("_XNes");
		return sb.toString();
	}
}
