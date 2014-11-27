package metadataSecurity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;

import main.ShamirShare;
import main.Share;

public class ServerToServerThread extends Thread {
	private Socket socket = null;
	private Share share;
	 public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public ServerToServerThread(Socket socket) {
		this.socket=socket;
	}

	public void run() {
	        try (	
	            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader input = new BufferedReader(
	                new InputStreamReader(
	                    socket.getInputStream()));
	        ) {
	        	output.println("Give me slice.");
//	            	slice=new ShamirShare();
//	            	slice.setFileName(input.readLine());
//	            	slice.setNoOfShares(Integer.parseInt(input.readLine()));
//	            	slice.setPrime(new BigInteger(input.readLine().getBytes()));
//	        		slice.setThreshold(Integer.parseInt(input.readLine()));
	            	int shareIndex = Integer.parseInt(input.readLine());
	            	BigInteger secretShare = new BigInteger(input.readLine().getBytes());
	            	setShare(new Share(shareIndex, secretShare));
	            	
	            }
	         catch (IOException e) {
				e.printStackTrace();
				
			} 
	    }
}
