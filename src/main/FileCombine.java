package main;

public class FileCombine {

	public static void main(String args[]){
    	String threshold = "3";
    	String noOfShares = "6";
    	
		String[] arguments ={"-k", threshold, "-n", noOfShares,"-primeN" };
		MainCombine.main(arguments);
	}
}
