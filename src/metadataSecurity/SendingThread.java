package metadataSecurity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendingThread extends Thread {
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	private Shamir shamir;
	
	public SendingThread(Socket socket, Shamir shamir) {
		this.socket=socket;
		this.shamir=shamir;
		try {
			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(
		            new InputStreamReader(socket.getInputStream()));
		} 
	 	catch (UnknownHostException e) {
            System.err.println("Don't know about host " + socket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                socket.getInetAddress());
        }
		
	}
	
	public void run(){
    		output.println("Sending shares.");
			//	output.println(shares n relevant info);
    		String inputLine;
    		while ((inputLine = input.readLine()) != null) {
			 	if (inputLine.equals("Acknowledged.")){
			 		break;
			 	}
			 	
			 	}
	}
}
