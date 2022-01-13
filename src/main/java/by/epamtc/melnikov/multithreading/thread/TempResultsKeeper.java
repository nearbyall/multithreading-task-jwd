package by.epamtc.melnikov.multithreading.thread;

import java.util.HashMap;
import java.util.Map;

public class TempResultsKeeper {

	private Map<Integer, String> tempResults = new HashMap<>();

	private TempResultsKeeper() {

	}

	private static class Holder {
		private final static TempResultsKeeper instance = new TempResultsKeeper();
	}

	public static TempResultsKeeper getInstance() {
		return TempResultsKeeper.Holder.instance;
	}

	public Map<Integer, String> getTempResults() {
		return tempResults;
	}
	
}
