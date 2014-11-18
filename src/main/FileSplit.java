package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
		ShamirShare shares = new ShamirShare();
    	String threshold = "3";
    	String noOfShares = "6";
    	String secret = "hello";
    	
		String[] arguments ={"-k", threshold, "-n", noOfShares, "-sS", secret , "-primeCustom"};
		MainSplit.main(arguments);
		

	}
	
	String getByteArray(){
		byte[] data = null;
		String byteString = null;
		File file = this.getFile();
		
        try {
              FileInputStream fileInputStream = new FileInputStream(file);
              fileInputStream.read(data);
              for (int i = 0; i < data.length; i++) {
                          System.out.print((char)data[i]);
               }
         } catch (FileNotFoundException e) {
                     System.out.println("File Not Found.");
                     e.printStackTrace();
         }
         catch (IOException e1) {
                  System.out.println("Error Reading The File.");
                   e1.printStackTrace();
         }
		
		
		
		return byteString;
	}
}
