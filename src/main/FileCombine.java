package main;

import java.io.File;
import java.util.ArrayList;

public class FileCombine {
	
	

	public static void main(String args[]){
    	String threshold = "3";
    	String noOfShares = "6";
    	
		String[] arguments ={"-k", threshold, "-n", noOfShares,"-primeN" };
		MainCombine.main(arguments);
	}
	
	public static File combine(ArrayList<ShamirShare> share, String prime){
		File combinedFile = null;
		ShamirShare ssShare = new ShamirShare();
		
		return combinedFile;
	}
}
