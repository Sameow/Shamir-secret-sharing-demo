package metadataSecurity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import main.ShamirShare;

public class SendingThread extends Thread {
	private PrintWriter output;
	private BufferedReader input;
	private ShamirShare shamir;
	private int shareIndex;
	private boolean sent;
	private Socket socket;
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public SendingThread(Socket socket, ShamirShare shamir, int i) {
		this.shamir=shamir;
		this.shareIndex=i;
		this.setSocket(socket);
		try {
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			output = pw;
			input = br;
		} catch (UnknownHostException e) {
            System.err.println("Don't know about host " + socket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                socket.getInetAddress());
        }
		
	}
	
	public void run(){
    		output.println("Sending shares.");
    		output.println(shamir.getFileName());
			output.println(shamir.getNoOfShares());
			output.println(new String(shamir.getPrime().toByteArray()));
			output.println(shamir.getThreshold());
			output.println(new String(shamir.getShareArr().get(shareIndex).getShare().toByteArray()));
			output.println(shamir.getShareArr().get(shareIndex).getShareIndex());
			
    		String inputLine;
    		try {
				while ((inputLine = input.readLine()) != null) {
				 	if (inputLine.equals("Acknowledged.")){
				 		System.out.println("Sent to servers.");
				 		this.setSent(true);
				 		this.socket.close();
				 		break;
				 	}
				 	
				 	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
