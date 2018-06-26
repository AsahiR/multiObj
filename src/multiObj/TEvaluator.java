package multiObj;
import java.util.ArrayList;

public abstract class TEvaluator {
	public abstract void evaluate(TPopulation pop);
	public String getName() {
		return "";
	}
	public void setWeights(ArrayList<Double> weights) {
		//do nothing;
	}
}
