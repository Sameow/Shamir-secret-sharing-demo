package main;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import engine.ReadFileIntoByteArray;


/**Shamir Secret Sharing Share Object**/

public class ShamirShare {

	private BigInteger share = null;
	private int shareIndex = 0;
	private BigInteger prime = null;
	private int threshold = 0;
	private int noOfShares = 0;
	private ArrayList<ShamirShare> shareArr = new ArrayList<ShamirShare>();
	
	public ArrayList<ShamirShare> getShareArr() {
		return shareArr;
	}
	public void setShareArr(ArrayList<ShamirShare> shareArr) {
		this.shareArr = shareArr;
	}
	public BigInteger getShare() {
	return share;
	}
	public void setShare(BigInteger share) {
		this.share = share;
	}
	public int getShareIndex() {
		return shareIndex;
	}
	public void setShareIndex(int shareIndex) {
		this.shareIndex = shareIndex;
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
	
	public ShamirShare(ArrayList<ShamirShare> s){
		this.shareArr = s;
	};
	
	public ShamirShare(int threshold, int noOfShares, BigInteger prime, int shareIndex, BigInteger share){
		this.threshold = threshold;
		this.noOfShares = noOfShares;
		this.prime = prime;
		this.shareIndex = shareIndex;
		this.share = share;
	};
	
	public ShamirShare split(File file){
		ShamirShare ss = new ShamirShare();
		ArrayList<ShamirShare> shareList = new ArrayList<ShamirShare>();
		byte[] secretByte = null;
		
		try {
			secretByte = ReadFileIntoByteArray.getBytesFromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String secret = secretByte.toString();
    	int threshold = 3;
    	int noOfShares = 6;
    	ss.setThreshold(threshold);
    	ss.setNoOfShares(noOfShares);
    	
    	String strThreshold = Integer.toString(threshold);
    	String strNoOfShares = Integer.toString(noOfShares);


		String[] arguments ={"-k", strThreshold, "-n", strNoOfShares, "-sS", secret , "-primeCustom"};
		
		shareList = MainSplit.split(arguments);
		ss.setShareArr(shareList);
		
		return ss;
	}
	
	public void combine(){
		
	}
}
