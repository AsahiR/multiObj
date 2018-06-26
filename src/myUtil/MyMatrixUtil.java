package myUtil;

import java.io.BufferedReader;
import java.util.function.Function;
import java.io.PrintWriter;
import java.io.IOException;

import matrix2017.TCMatrix;
import java.util.ArrayList;

public class MyMatrixUtil {
	public static String toString(TCMatrix matrix) {
		String suffix = " ";
		StringBuilder sb = new StringBuilder("");
		sb.append(matrix.getRowDimension());
		sb.append("\n");
		sb.append(matrix.getColumnDimension());
		sb.append("\n");
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				if(j == matrix.getColumnDimension()) {
					suffix = "\n";
				}
				sb.append(matrix.getValue(i,j) + suffix);
			}
		}
		
		return sb.toString();
	}
	
	public static TCMatrix readFrom(BufferedReader br) throws IOException {
		int rowSize = Integer.parseInt(br.readLine());
		int columnSize = Integer.parseInt(br.readLine());

		TCMatrix matrix = new TCMatrix(rowSize, columnSize);
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			String[] tokens = br.readLine().split(" ");
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				double value = Double.parseDouble(tokens[j]);
				matrix.setValue(i, j, value);
			}
		}
		
		return matrix;
	}
	
	/**
	 * @param size
	 * @return
	 */
	public static TCMatrix makeIMatrix(int size) {
		TCMatrix iMatrix = new TCMatrix(size, size);
		for(int i = 0; i < size; i++) {
			iMatrix.setValue(i, i, 1.0);
		}
		return iMatrix;
	}
	
	/**
	 * @param v Argument for function
	 * @param functions
	 * @param front Set function.apply(v) into here
	 */
	public static void calcFront(TCMatrix v, ArrayList<TFunction> functions,
			TCMatrix front) {
		int frontDimension = functions.size();
		assert frontDimension == front.getRowDimension();
		for(int i = 0; i < frontDimension; i++) {
			double eval = functions.get(i).calc(v);
			front.setValue(i, eval);
		}
	}
	
	public static boolean copyPossible(TCMatrix src, TCMatrix dst) {
		if(dst.getRowDimension() == src.getRowDimension()) {
			dst.copyFrom(src);
			return true;
		}
		return false;
	}
}
