package myUtil;

import matrix2017.*;

public abstract class TFunction {
	private final String fName = "";


	public abstract double calc(TCMatrix x);
	
	public String getName() {
		return fName;
	}

	public boolean equals(TFunction f) {
		return fName == f.getName();
	}
	
}

