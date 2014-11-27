package metadataSecurity;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public Server(){
		 try (ServerSocket serverSocket = new ServerSocket(4444)) { 
			 System.out.println("Server started.");
	            while (true) {
	            	System.out.println("Server awaiting connection.");
	                new ServerThread(serverSocket.accept()).start();
	                System.out.println("Client connected.");
	            }
	        } catch (IOException e) {
	            System.err.println("Could not listen on port " + 4444);
	        }
	}
	
	 public static void main(String[] args) throws IOException {
	        new Server();
	    }
}
