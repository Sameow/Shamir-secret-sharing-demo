package main;

import java.math.BigInteger;

public class Share {
	private BigInteger share;
	private int shareIndex;
	
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
	
	public Share(int shareIndex, BigInteger share){
		this.shareIndex = shareIndex;
		this.share = share;
	}

}
