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

	private BigInteger prime = null;
	private int threshold = 2;
	private int noOfShares = 3;
	private ArrayList<Share> shareArr = new ArrayList<Share>();
	
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
	
//	public ShamirShare(ArrayList<Share> s){
//		this.shareArr = s;
//	};
//	
//	public ShamirShare(int threshold, int noOfShares, BigInteger prime){
//		this.threshold = threshold;
//		this.noOfShares = noOfShares;
//		this.prime = prime;
//	};
	
	public void split(File file){
		ArrayList<Share> shareList = new ArrayList<Share>();
		byte[] secretByte = null;
		
		try {
			secretByte = ReadFileIntoByteArray.getBytesFromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String secret = secretByte.toString();
    	
    	String strThreshold = Integer.toString(threshold);
    	String strNoOfShares = Integer.toString(noOfShares);

		String[] arguments ={"-k", strThreshold, "-n", strNoOfShares, "-sS", secret , "-primeCustom"};
		
		SplitOutput output = MainSplit.split(arguments);
        List<ShareInfo> tempList = output.retrieveShares();
        for(int i = 0; i<tempList.size(); i++){
        	Share share = new Share(tempList.get(i).getIndex(), tempList.get(i).getShare());
        	shareList.add(share);
        }
		this.setShareArr(shareList);
		this.setPrime(output.getPrime());

	}
	
	public File combine(){
		//ShamirShare share = share.getShareArr();
		File combinedFile = null;
		BigInteger fileByteBigInt = null;
		byte[] fileByte = null;
    	String threshold = "3";
    	String noOfShares = "6";
    	String prime = "2545358851974595661189810777";
    	
		String[] arguments ={"-k", threshold, "-n", noOfShares,"-primeN",prime,
				"-s1","242795834786098480354491126",
				"-s2","441956801228623581432992908",
				"-s3","707808009237847326782800116"};
		fileByteBigInt = MainCombine.combine(arguments);
		fileByte = fileByteBigInt.toByteArray();
		try {
			combinedFile = ReadFileIntoByteArray.byteArrayToFile(fileByte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return combinedFile; 
	}
}
