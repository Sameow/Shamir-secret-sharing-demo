package main;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class TEST {
	public static void main(String[] args) throws UnsupportedEncodingException{
		File file = new File("TextFile.txt");
		ShamirShare test = new ShamirShare();
		test.split(file);
	//	printStuff(test);
		test.combine(test);
	}
	
	public static void printStuff(ShamirShare test){
		
		System.out.println("Filename: "+test.getFileName());
		System.out.println("Prime: "+test.getPrime());
		System.out.println("Threshold, NoOfShares:"+test.getThreshold()+","+test.getNoOfShares());
		
		for(int i=0; i<test.getShareArr().size(); i++){
			System.out.println(test.getShareArr().get(i).getShareIndex()+","+test.getShareArr().get(i).getShare());
		//Write to a new file with all the info
			//Send file to other server
		}
	}
}
