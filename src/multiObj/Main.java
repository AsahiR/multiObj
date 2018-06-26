package multiObj;

import myUtil.*;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.IOException;

import matrix2017.*;
import java.util.Comparator;

public class Main {
	private TMultiStartScalar fMultiStart;
	private int fTrialNum;
	private String fPopulationLogFileName;
	private String fPopulationLogDirName = "popLog/";
	
	Main(TMultiStartScalar multiStart, int trialNum){
		fMultiStart = multiStart;
		fTrialNum = trialNum;
		fPopulationLogFileName = fPopulationLogDirName + getName() ;
	}
	
	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append(fMultiStart.makeName());
		sb.append("_trial");
		sb.append(fTrialNum);
		return sb.toString();
	}

	/**
	 * exexute one trial and log optimal solutions
	 */
	void execute() {
		fMultiStart.init();
		fMultiStart.execute();
		TPopulation solutions = fMultiStart.getSolutions();
        try(PrintWriter pw = new PrintWriter(fPopulationLogFileName + "_Evl" + fTrialNum)){
            solutions.writeTo(pw);
            System.out.println(solutions);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}

	public static void main(String[] argv) {

		double min = -5.0;
		double max = 5.0;
		int lambda = 8;
		
		ArrayList<TFunction> functions = new ArrayList<>();
		functions.add(new TFunction1());
		functions.add(new TFunction2());

		int trialSize = 2;
		int solutionDimension = 2;
		int frontDimension = 2;
		int solutionSize  = 100;

		ArrayList<ArrayList<Double>> weightsList = new ArrayList<>();
        final double weightUnit = 1.0 / (double)solutionSize;

        /** only when frontDimension == 2 */
		for(int i = 0; i < solutionSize ; i++) {
            ArrayList<Double> weights = new ArrayList<>();
            double firstWeight = weightUnit * (double)i;
            weights.add(firstWeight);
            weights.add(1.0 - firstWeight);
            weightsList.add(weights);
		}
		
		double bLRate = 0.6 * (3.0 + Math.log(solutionDimension)) / solutionDimension
				/ Math.sqrt(solutionDimension);
		double sigmaLRate = bLRate;
		double mLRate = 1.0;
		double[] weightArray = new double[lambda];
		TXNes.setRankWeight(weightArray);
		
		/** vector. its element is min of each functions */
		TCMatrix z = new TCMatrix(frontDimension); 
		TEvaluator evaluator = new TExChebyEvaluator(functions, z);
		Comparator<TIndividual> comparator = new TComparatorWithFeasibility();

		TXNes xNes = new TXNes(min, max, weightArray, bLRate, mLRate, sigmaLRate, 
				solutionDimension, lambda, evaluator, comparator);

		TMultiStartScalar multiStart = new TMultiStartScalar(weightsList, evaluator, xNes);
		for(int i = 0; i < trialSize; i++) {
			Main trial = new Main(multiStart, i);
			trial.execute();
		}
	}
}
