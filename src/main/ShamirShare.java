package main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

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
//		try {
//			
//			//TODO: check length of file and split file and shamir it. 
//			//also think of how to send to server (format)
//			int filelen = (int) file.length(); //get length
//			if(filelen>1000){
//				int secretlen = 1000;
//				secret= new String(ReadFileIntoByteArray.getBytesFromFile(file));
//				for(int i = 1000 ; i<filelen/1000;i++){
//					String[] arguments ={"-k", Integer.toString(threshold), "-n", Integer.toString(noOfShares), "-sS", secret , "-primeCustom"};
//					MainSplit.split(arguments);
//					secretlen+= +1000;
//				}
//			}else
//			secret= new String(ReadFileIntoByteArray.getBytesFromFile(file));
//			
//			System.out.println("Secret = "+secret);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//main argument (to split)
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
	
	public void combine(ShamirShare ss) throws UnsupportedEncodingException{
		BigInteger fileByteBigInt;
		byte[] fileByte;
		String prime = ""+ss.getPrime();
		ArrayList<Share> share = ss.getShareArr();
		ss.setFileName(ss.getFileName());
		
		if (ss.getShareArr().size()>=ss.getThreshold()){
		String[] arguments ={"-k", Integer.toString(ss.getThreshold()), "-n", Integer.toString(ss.getNoOfShares()),
				"-primeN",prime,
				"-s"+share.get(0).getShareIndex(),share.get(0).getShare().toString(),
				"-s"+share.get(1).getShareIndex(),share.get(1).getShare().toString()
				};
		fileByteBigInt = MainCombine.combine(arguments);
		fileByte = fileByteBigInt.toByteArray();
		try {
			
			ReadFileIntoByteArray.byteArrayToFile(fileByte,ss.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}
		else {
			System.out.println("Not enough shares la!");
		}
	}
	
}
