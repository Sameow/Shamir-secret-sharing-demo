package main;


/**Shamir Secret Sharing Share Object**/

public class ShamirShare {

	private String share = null;
	private String shareIndex = null;
	private String prime = null;
	private String threshold = null;
	private String noOfShares = null;
	
	public String getShare() {
	return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public String getShareIndex() {
		return shareIndex;
	}
	public void setShareIndex(String shareIndex) {
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
}
