package main;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import main.MainSplit.SplitOutput;
import engine.ReadFileIntoByteArray;
import engine.SecretShare.ShareInfo;


/**Shamir Secret Sharing Share Object**/

public class ShamirShare {

	private String fileName = null;
	private BigInteger prime = null;
	private int threshold = 2;
	private int noOfShares = 3;
	private ArrayList<Share> shareArr = new ArrayList<Share>();
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ArrayList<Share> getShareArr() {
		return shareArr;
	}
	public void setShareArr(ArrayList<Share> shareArr) {
		this.shareArr = shareArr;
	}

	public BigInteger getPrime() {
		return prime;
	}
	public void setPrime(BigInteger prime) {
		this.prime = prime;
	}
	public int getThreshold() {
		return threshold;
	}
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	public int getNoOfShares() {
		return noOfShares;
	}
	public void setNoOfShares(int noOfShares) {
		this.noOfShares = noOfShares;
	}
	
	public ShamirShare() {};
	
	public void split(File file){
		
		this.setFileName(file.getName());
		ArrayList<Share> shareList = new ArrayList<Share>();
		String secret = null;
		try {
			secret = ReadFileIntoByteArray.getBytesFromFile(file).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] arguments ={"-k", Integer.toString(threshold), "-n", Integer.toString(noOfShares), "-sS", secret , "-primeCustom"};
		
		SplitOutput output = MainSplit.split(arguments);
        List<ShareInfo> tempList = output.retrieveShares();
        for(int i = 0; i<tempList.size(); i++){
        	Share share = new Share(tempList.get(i).getIndex(), tempList.get(i).getShare());
        	shareList.add(share);
        }
		this.setShareArr(shareList);
		this.setPrime(output.getPrime());

	}
	
	public static File combine(ShamirShare ss){
		File combinedFile = null;
		BigInteger fileByteBigInt;
		byte[] fileByte;
    	
    	
		String[] arguments ={"-k", Integer.toString(ss.getThreshold()), "-n", Integer.toString(ss.getNoOfShares()),
				"-primeN",ss.getPrime().toString(),
				"-s1","242795834786098480354491126",
				"-s2","441956801228623581432992908",
				"-s3","707808009237847326782800116"
				};
		fileByteBigInt = MainCombine.combine(arguments);
		fileByte = fileByteBigInt.toByteArray();
		try {
			ReadFileIntoByteArray.byteArrayToFile(fileByte,ss.getFileName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			combinedFile = ReadFileIntoByteArray.byteArrayToFile(fileByte,ss.getFileName());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		return combinedFile; 
	}
}
