package by.epamtc.melnikov.multithreading.thread;

import java.util.HashMap;
import java.util.Map;

public class TempInfo {

	private Map<Integer, String> tempResults = new HashMap<>();

	private TempInfo() {

	}

	private static class Holder {
		private final static TempInfo instance = new TempInfo();
	}

	public static TempInfo getInstance() {
		return TempInfo.Holder.instance;
	}

	public Map<Integer, String> getTempResults() {
		return tempResults;
	}
	
}