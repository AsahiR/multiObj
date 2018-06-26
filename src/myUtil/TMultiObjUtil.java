package myUtil;
import java.util.ArrayList;
import javafx.util.Pair;

public class TMultiObjUtil {
	/**
	 * @param functions
	 * @return name such as func1Count(func1)func2Count(func2)....
	 */
	public static String makeFunctionsName(ArrayList<TFunction> functions) {
		ArrayList<String> sortArray = new ArrayList<>();
		for(TFunction function : functions) {
			sortArray.add(function.getName());
		}
		/** for O(NlogN) */
		sortArray.sort(null);
		/** Pair(funcName, count) */
		ArrayList<Pair<String, Integer>> pairList = new ArrayList<>();
		/** sentinel is different from end of sortArray */
		String sentinel = sortArray.get(sortArray.size() - 1).equals("") ? "@" : "";
		sortArray.add(sentinel);
		/** prev is last loop's funcName from now loop */
		String prev = sortArray.get(0);
		int counter = 0;
		for(String now : sortArray) {
			if(prev.equals(now)) {
				/** same funcName continue */
				counter ++;
			}else {
				/** element change, then add (prev, count) into list and initialize count */
				Pair<String, Integer> pair = new Pair<>(prev, counter);
				pairList.add(pair);
				counter = 1;
			}
			prev = now;
		}
		
		StringBuilder sb = new StringBuilder();
		for(Pair<String, Integer> pair : pairList ) {
			sb.append(pair.getKey());
			sb.append("_");
			sb.append(pair.getValue());
		}
		return sb.toString();
	}

}
