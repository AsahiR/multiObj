package multiObj;

import csv2013.TCCsvData;
import java.util.Comparator;
import matrix2017.TCMatrix;
import myUtil.MyMatrixUtil;
import myUtil.TTmpFunction;
import random2013.TCAbstractRandom;
import random2013.TCJava48BitLcg;
import random2013.ICRandom;

import java.awt.datatransfer.FlavorMap;
import java.io.PrintWriter;
import java.lang.reflect.Executable;
import java.util.Random;
import java.util.function.Function;

public class TXNes {
	/** best Eval */
	private double fBestEvaluationValue;
	
	/** evaluationNum */
	private int fEvaluationNum;
	
	/** generation */
	private int fGeneration;
	
	/** sigma */
	private double fSigma;;
	
	/** center vector*/
	private TCMatrix fM;
	
	/** matrixB */
	private TCMatrix fB;
	
	/** rng for z */
	private ICRandom fZRng;
	
	/** Population */
	private TPopulation fPopulation;
	
	/** EigValues */
	private double[] fEigValues;
	
	/** rankWeight */
	private double[] fWeightArray;
	
	/** learning rate for fM, fB, fSigma*/
	private double fMLRate;
	private double fBLRate;
	private double fSigmaLRate;

	/** initial element of fM from [fMin, fMax] */
	private double fMin;
	private double fMax;
	
	private final int fDimension;
	private final int fLambda;

	/** for calc */
	private final double fDDimension;
	private final double fDLambda;
	
	private TEvaluator fEvaluator;
	private Comparator<TIndividual> fComparator;

	public TXNes(double min, double max, double[] weightArray,
			double bLRate, double mLRate, double sigmaLRate, int dimension, int lambda,
			TEvaluator evaluator, Comparator<TIndividual> comparator) {
		fWeightArray = weightArray;
		fMax = max; 
		fMin = min;
		fMLRate = mLRate;
		fBLRate = bLRate;
		fSigmaLRate = sigmaLRate;
		fDimension = dimension;
		fLambda = lambda;
		fDDimension = (double)dimension;
		fDLambda = (double)lambda;
		fEvaluator = evaluator;
		fComparator = comparator;
	}

	
	/**
	 * calc slope for fM, fB, fSigma, and then calc them.
	 * @return
	 */
	public void setParam() {
		TCMatrix mSlope = getMSlope();
		TCMatrix gM = getGM();
		double sigmaSlope = getSigmaSlope(gM);
		TCMatrix bSlope = getBSlope(gM, sigmaSlope);
		
		fM.add(mSlope.times(fMLRate));	//mSlope change
		fSigma *= Math.exp(fSigmaLRate / 2 * sigmaSlope);
		fB.times(bSlope.times(fBLRate / 2).expm(true)); //bSlope change
		TCMatrix tmp = fB.clone().times(fB.tclone(), fSigma * fSigma);
		fEigValues =  tmp.eig().getRealEigenvalues();
	}

	/**
	 * init mutable data filed
	 */
	public void init() {
		fZRng = new TCJava48BitLcg();
		fM = new TCMatrix(fDimension);
		ICRandom mRng = new TCJava48BitLcg();
		fEvaluationNum = 0;
		fGeneration = 0;

		/** uniform random min to max */
		fM.rand(mRng);
		fM.times(fMax - fMin).add(fMin);

		fSigma = (fMax - fMin) / 2.0;

		/** init fB IMatrix */
		fB = new TCMatrix(fDimension, fDimension).eye();
		
		/** set each individual (fM-copy,fM-copy) in fPopulation */
		fPopulation = new TPopulation(fLambda);
		for(int i = 0; i < fLambda ; i++) {
			fPopulation.getIndividual(i).setVector(fM);
			fPopulation.getIndividual(i).setZVector(fM);
		}
	}
	
	/**
	 * make population by normal ditribution rng 'fZRng'
	 * @return
	 */
	public void makePopulation() {
		int lambda = fPopulation.getPopulationSize();
		for(int i = 0; i < lambda ; i++) {
				TIndividual indv = fPopulation.getIndividual(i);
				TCMatrix zVector = indv.getZVector();
				zVector.randn(fZRng);
				TCMatrix xVector = fPopulation.getIndividual(i).getVector();
				xVector.times(fB, zVector).times(fSigma);
				xVector.add(fM);
		}
	}
	
	/**
	 * evaluate fPopulation
	 * sort  and set fBestEvaluationValue
	 * @return
	 */
	public void evaluate() {
		fEvaluator.evaluate(fPopulation);
		fPopulation.sort(fComparator);
		fBestEvaluationValue = fPopulation.getIndividual(0).getEvaluationValue();
	}
	
	public void execute() {
		makePopulation();
		evaluate();
		setParam();
		fEvaluationNum += fPopulation.getPopulationSize();
		fGeneration ++;
	}
	

	/**
	 * @return slope for fM
	 */
	private TCMatrix getMSlope() {
		TCMatrix wSum = new TCMatrix(fM.getRowDimension());
		TCMatrix tmp = wSum.clone();
		for(int i = 0; i < fWeightArray.length; i++) {
			TCMatrix zVector = fPopulation.getIndividual(i).getZVector();
			wSum.add(tmp.times(zVector, fSigma * fWeightArray[i]));
		}
		return tmp.times(fB, wSum);
	}
	
	/**
	 * @return GM. need for slope of fB and fSigma
	 */
	private TCMatrix getGM() {
		int lambda = fWeightArray.length;
		int dimension = fM.getRowDimension();
		TCMatrix result = new TCMatrix(dimension, dimension);
		TCMatrix iMatrix = result.clone().eye();

		TCMatrix workMatrix = result.clone();
		for(int i = 0; i < lambda; i++) {
			TCMatrix z = fPopulation.getIndividual(i).getZVector();
			workMatrix.times(z, z.tclone()).sub(iMatrix).times(fWeightArray[i]);
			result.add(workMatrix);
		}
		return result;
	}
	
	/**
	 * @param gM
	 * @return slope of fSigma
	 */
	private double getSigmaSlope(TCMatrix gM) {
		return gM.trace() / (double)gM.getRowDimension();
	}
	
	/**
	 * @param gM
	 * @param slope of fB
	 * @return
	 */
	private TCMatrix getBSlope(TCMatrix gM, double sigmaSlope) {
		TCMatrix result = gM.clone();
		TCMatrix iMatrix = gM.clone().eye();
		result.sub(iMatrix.times(sigmaSlope));
		return result;
	}
	
	/**
	 * @return eigenvalues array of fSigma^2 * fB.fB^t
	 */
	public double[] getEigValues() {
		return fEigValues;
	}
	
	
	/**
	 * @return
	 */
	public double getBestEvaluationValue() {
		return fBestEvaluationValue;
	}

	/**
	 * @return
	 */
	public int getEvaluationNum() {
		return fEvaluationNum;
	}
	
	/**
	 * @return
	 */
	public int getGeneration() {
		return fGeneration;
	}

	/**
	 * @param generation
	 */
	public void setGeneration(int generation) {
		fGeneration = generation;
	}
	
	/**
	 * @return
	 */
	public TPopulation getPopulation() {
		return fPopulation;
	}
	
	/**
	 * write for viewer
	 * @param pw
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(fGeneration);
		pw.println(fEvaluationNum);
		pw.println(fBestEvaluationValue);
		pw.print(MyMatrixUtil.toString(fM));
		pw.println(fSigma);
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		for(int i = 0; i < fEigValues.length; i++) {
			if(i > 0) {
				prefix = " ";
			}
            sb.append(prefix);
            sb.append(fEigValues[i]);
		}
		pw.println(sb.toString());
	}
	
	/**
	 * @return
	 */
	public TCMatrix getM() {
		return fM;
	}
	
	/**
	 * @return
	 */
	public double getSigma() {
		return fSigma;
	}
	
	/**
	 * @return
	 */
	public TCMatrix getB() {
		return fB;
	}
	
	/**
	 *  set rankWeight into argument array
	 * @param array. result is set into this array
	 */
	public static void setRankWeight(double[] rankWeight) {
		double denominator = 0;
		double eps = 1.0e-10;
		int lambda = rankWeight.length;
		double dLambda = (double)lambda;
		for(int i = 0; i < lambda; i++) {
			double tmp = Math.log(dLambda / 2.0 + 1.0) - Math.log(i + 1.0);
			denominator += Math.max(0, tmp);
		}
		denominator = denominator == 0 ? eps : denominator;
		
		for(int i = 0; i < lambda; i++) {
			double tmp =  Math.log(dLambda / 2.0 + 1.0) - Math.log(i + 1.0);
			tmp = Math.max(0, tmp) / denominator - 1.0 / dLambda ;
			rankWeight[i] = tmp;
		}
	}
	
	public void setEvaluator(TEvaluator evaluator) {
		fEvaluator = evaluator;
	}
	
	/**
	 * use for filename
	 * @return info for dimension, populationSize, fMin, and fMax
	 */
	public String getName() {
		int lambda = fPopulation.getPopulationSize();
		int dimension = fM.getRowDimension();
		StringBuilder sb = new StringBuilder("Xnes");
		sb.append("Dim");
		sb.append(dimension);
		sb.append("Pop");
		sb.append(lambda);
		sb.append("From");
		sb.append(fMin);
		sb.append("To");
		sb.append(fMax);
		return sb.toString();
	}
}
