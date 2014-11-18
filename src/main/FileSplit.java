package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import engine.ReadFileIntoByteArray;

public class FileSplit {

	File file = null;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	public FileSplit(){};
	public FileSplit(File file){
		this.file = file;
	};
	
	public static void main(String args[]){
		ArrayList<ShamirShare> share = new ArrayList<ShamirShare>();
		File file = new File("C:/Users/L335a10/Desktop/Hello.txt");
		share = split(file);
		for(int i =0; i<share.size(); i++){
			System.out.println(share.get(i).getShareIndex()+","+share.get(i).getShare());
		}
		//for testing output
//		share = split();
//		for(int i =0; i<share.size(); i++){
//			System.out.println(share.get(i).getShareIndex()+","+share.get(i).getShare());
//		}
		
	}
	
	public static ArrayList<ShamirShare> split(File file){
		ShamirShare shares = new ShamirShare();
		ArrayList<ShamirShare> share = new ArrayList<ShamirShare>();
		byte[] secretByte = null;
		
		try {
			secretByte = ReadFileIntoByteArray.getBytesFromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String secret = secretByte.toString();
    	String threshold = "3";
    	String noOfShares = "6";
    	
		String[] arguments ={"-k", threshold, "-n", noOfShares, "-sS", secret , "-primeCustom"};
		
		share = MainSplit.split(arguments);
		
		return share;

	}
}
