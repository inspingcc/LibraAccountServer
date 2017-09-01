package com.insping.LibraAccountServer;

public class Testa {
	private Testa() {
		// TODO Auto-generated constructor stub
	}

	private static Testa instance = new Testa();

	public static Testa getInstance() {
		return instance;
	}
	
	
	
	public String modify(String[] result) {
		result[0] = "succcccccccccccc";
		return "0000";
	}
}
