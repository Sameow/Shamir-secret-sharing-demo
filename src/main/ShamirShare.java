package main;

import java.math.BigInteger;


/**Shamir Secret Sharing Share Object**/

public class ShamirShare {

	private BigInteger share = null;
	private int shareIndex = 0;
	private String prime = null;
	private String threshold = null;
	private String noOfShares = null;
	
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
	public String getPrime() {
		return prime;
	}
	public void setPrime(String prime) {
		this.prime = prime;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getNoOfShares() {
		return noOfShares;
	}
	public void setNoOfShares(String noOfShares) {
		this.noOfShares = noOfShares;
	}
	
	public ShamirShare() {};
	public ShamirShare(String threshold, String noOfShares, String prime){
		this.threshold = threshold;
		this.noOfShares = noOfShares;
		this.prime = prime;
	};
	public ShamirShare(int shareIndex, BigInteger share){
		this.shareIndex = shareIndex;
		this.share = share;
	};
	public ShamirShare(BigInteger share){
		this.share = share;
	}
}
