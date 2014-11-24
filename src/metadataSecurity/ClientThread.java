package metadataSecurity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
	private Socket socket = null;
	private boolean thisOneConnectedLiao=false;
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isThisOneConnectedLiao() {
		return thisOneConnectedLiao;
	}

	public void setThisOneConnectedLiao(boolean thisOneConnectedLiao) {
		this.thisOneConnectedLiao = thisOneConnectedLiao;
	}

	    public ClientThread(Socket socket) {
	        this.socket = socket;
	    }
	    
	    public void run() {
	        try (	
	            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader input = new BufferedReader(
	                new InputStreamReader(
	                    socket.getInputStream()));
	        ) {
	            String inputLine;
	            while ((inputLine = input.readLine()) != null) {
	            	if (inputLine.equals("Connected")){
	            		setThisOneConnectedLiao(true);
	            	}
	                
	            }
	        } catch (IOException e) {
		      System.err.println("Socket closed.");
			} 
	    }
}
