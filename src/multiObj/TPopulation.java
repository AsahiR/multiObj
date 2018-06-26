package multiObj;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class TPopulation {
	private TIndividual[] fIndividuals;
	
	/**
	 * default constructor
	 */
	public TPopulation() {
		fIndividuals = new TIndividual[0];
	}
	
	/**
	 * constructor2
	 * @param populationSize
	 */
	public TPopulation(int populationSize) {
		fIndividuals = new TIndividual[populationSize];
		for(int i = 0;i < fIndividuals.length; i++) {
			fIndividuals[i] = new TIndividual();
		}
	}
	

	/**
	 * copy constructor
	 * @param src
	 */
	public TPopulation(TPopulation src) {
		fIndividuals = new TIndividual[src.fIndividuals.length];
		for(int i = 0; i < fIndividuals.length; i++) {
			fIndividuals[i] = src.fIndividuals[i];
		}
	}
	
	@Override
	public TPopulation clone() {
		return new TPopulation(this);
	}
	
	/**
	 * @param populationSize
	 */
	public void setPopulationSize(int popSize) {
		if(fIndividuals.length != popSize) {
			fIndividuals = new TIndividual[popSize];
			for(int i = 0; i < popSize; i++) {
				fIndividuals[i] = new TIndividual();
			}
		}
	}
	
	/**
	 * Copy without new
	 * @param TPopulation src
	 * @return TPopulation
	 */
	public TPopulation copyFrom(TPopulation src) {
		setPopulationSize(src.fIndividuals.length);
		for(int i = 0; i < fIndividuals.length; i++) {
			fIndividuals[i].copyFrom(src.fIndividuals[i]);
		}
		return this;
	}
	
	/**
	 * @return populationSize
	 */
	public int getPopulationSize() {
		return fIndividuals.length;
	}
	
	/**
	 * @param PrintWriter pw
	 */
	public void writeTo(PrintWriter pw) {
		pw.println(getPopulationSize());
		for(int i = 0; i < getPopulationSize(); i++) {
			fIndividuals[i].writeTo(pw);
			pw.println("");
		}
	}
	
	/**
	 * @param BufferedReader
	 * @throws IOException
	 */
	public void readFrom(BufferedReader br) throws IOException{
		setPopulationSize(Integer.parseInt(br.readLine()));
		for(int i = 0; i < getPopulationSize(); i++) {
			fIndividuals[i].readFrom(br);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(getPopulationSize());
		for(int i = 0; i < getPopulationSize(); i++) {
			str.append("\n");
			str.append(fIndividuals[i].toString());
		}
		return str.toString();
	}
	
	/**
	 * @param index
	 * @return TIndividual
	 */
	public TIndividual getIndividual(int index) {
		return fIndividuals[index];
	}
	
	/**
	 * sort fIndividuals by fEvaluationValue
	 */
	public void sort(Comparator<TIndividual> comparator) {
		Arrays.sort(fIndividuals, comparator);
	}
	
	
	/**
	 * copy writeSize indvs of source into this from start position
	 * @param start start index for dest(= this)
	 * @param writeSize
	 * @param source 
	 */
	public void writeIndividuals(int start, int writeSize, TPopulation source) {
		assert  0 <= start && start + writeSize <= getPopulationSize();
		for(int i = 0; i < writeSize; i++) {
			int index = i + start;
			fIndividuals[index].copyFrom(source.getIndividual(i));
		}
		
	}
}
