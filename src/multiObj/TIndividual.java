package multiObj;

import matrix2017.TCMatrix;
import myUtil.MyMatrixUtil;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;


public class TIndividual{
	/** xVector */
	private TCMatrix fVector;
	
	/** zVector */
	private TCMatrix fZVector;
	
	/** evaluationValue */
	private TCMatrix fEvaluationVector;
	
	/** evaluationValue */
	private double fEvaluationValue = Double.NaN;
	
	/** frontRank */
	private int fRank = Integer.MAX_VALUE;
	
	/** crowded-Distance */
	private double fCDistance = 0;
	
	/**
	 * default constructor
	 */
	public TIndividual() {
		fVector = new TCMatrix();
		fZVector = new TCMatrix();
		fEvaluationVector = new TCMatrix();
	}
	
	/**
	 * constructor2
	 * @param row size
	 */
	public TIndividual(int row) {
		fVector = new TCMatrix(row);
		fZVector = new TCMatrix(row);
		fEvaluationVector = new TCMatrix(row);
	}
	
	/**
	 * copy constructor
	 * @param TInvidiaual src
	 */
	public TIndividual(TIndividual src) {
		fVector = src.getVector().clone();
		fZVector = src.fZVector.clone();
		fEvaluationVector = src.fEvaluationVector.clone();
		fRank = src.fRank;
		fCDistance = src.fCDistance;
	}
	
	/**
	 * @return deep copied of this
	 */
	public TIndividual clone() {
		return new TIndividual(this);
	}
	
	/**
	 * efficient copy. No use 'new'
	 * @param src
	 * @return this
	 */
	public TIndividual copyFrom(TIndividual src) {
		if(! MyMatrixUtil.copyPossible(src.getVector(), fVector)) fVector = src.getVector().clone();
		if(! MyMatrixUtil.copyPossible(src.getZVector(), fZVector)) fZVector = src.getZVector().clone();
		if(! MyMatrixUtil.copyPossible(src.getEvaluationVector(), fEvaluationVector)) fEvaluationVector = src.getEvaluationVector().clone();
		fRank = src.fRank;
		fCDistance = src.fCDistance;
		fEvaluationValue = src.fEvaluationValue;
		return this;
	}
	
	/**
	 * @param pw
	 */
	public void writeTo(PrintWriter pw) {
		pw.print(toString());
	}
	
	/**
	 * @param br
	 */
	public void readFrom(BufferedReader br) throws IOException{
		fRank = Integer.parseInt(br.readLine());
		fCDistance = Double.parseDouble(br.readLine());
		fEvaluationValue = Double.parseDouble(br.readLine());
		fEvaluationVector = MyMatrixUtil.readFrom(br);
		fVector = MyMatrixUtil.readFrom(br);
		fZVector = MyMatrixUtil.readFrom(br);
	}
	
	/**
	 * @return string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(fRank);
		sb.append("\n");
		sb.append(fCDistance);
		sb.append("\n");
		sb.append(fEvaluationValue);
		sb.append("\n");
		sb.append(MyMatrixUtil.toString(fEvaluationVector));
		sb.append("\n");
		sb.append(MyMatrixUtil.toString(fVector));
		sb.append("\n");
		sb.append(MyMatrixUtil.toString(fZVector));
		return sb.toString();
	}

	/**
	 * @return
	 */
	public TCMatrix getEvaluationVector() {
		return fEvaluationVector;
	}
	
	/**
	 * @return fVector
	 */
	public TCMatrix getVector() {
		return fVector;
	}
	
	
	/**
	 * @param evaluationValue
	 */
	public void setEvaluationVector(TCMatrix vector) {
		fEvaluationVector = new TCMatrix(vector);
	}
	
	/**
	 * @param vector
	 */
	public void setVector(TCMatrix src) {
		assert src.getColumnDimension() == 1;
		fVector = new TCMatrix(src);
	}
	
	/**
	 * @param vector
	 */
	public void setZVector(TCMatrix src) {
		assert src.getColumnDimension() == 1;
		fZVector = new TCMatrix(src);
	}
	
	public TCMatrix getZVector() {
		return fZVector;
	}

	
	public int getRank() {
		return fRank;
	}
	
	public void setRank(int rank) {
		fRank = rank;
	}
	
	public void setCDistance(double cDistance) {
		fCDistance = cDistance;
	}
	
	public double getCDistance() {
		return fCDistance;
	}
	
	public void setEvaluationValue(double evaluationValue) {
		fEvaluationValue = evaluationValue;
	}
	
	public double getEvaluationValue() {
		return fEvaluationValue;
	}
	
}
