package main;

import java.io.File;
import java.io.IOException;
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
//		share = split(file);
//		for(int i =0; i<share.size(); i++){
//			System.out.println(share.get(i).getShareIndex()+","+share.get(i).getShare());
//		}
		File file = new File("C:/Users/L335a10/Desktop/Hello.txt");
		ArrayList<ShamirShare> share = new ArrayList<ShamirShare>();
		ShamirShare test = new ShamirShare();
		test.split(file);
		
		
		//for testing output
//		share = split();
//		for(int i =0; i<share.size(); i++){
//			System.out.println(share.get(i).getShareIndex()+","+share.get(i).getShare());
//		}
		
	}
	
	

}
