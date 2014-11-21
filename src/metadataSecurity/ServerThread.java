package metadataSecurity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.ShamirShare;

public class ServerThread extends Thread{
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	private ArrayList<InetAddress> otherServerIP;
	
	public ServerThread(Socket socket) {
		this.socket=socket;
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
		output.println("Connected");
		String inputLine;
         try {
			while ((inputLine = input.readLine()) != null) {
			 	if (inputLine.equals("Combine file.")){
			 		combineFile();
			 	}
			 	if (inputLine.equals("Split file.")){ 
			 		String fileName = input.readLine();
			 		int fileSize = Integer.parseInt(input.readLine());
			 		splitFile(fileName, fileSize, socket);
			 	}	
			 	if (inputLine.equals("Sending shares.")){ 
			 		ShamirShare secretShare = new ShamirShare();
			 		= input.readLine();
			 		//Store somewhere
			 	}
			 	
			 	}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void splitFile(String fileName, int fileSize, Socket clientSocket) throws IOException{
		File receivedFile = new File(fileName);
		FileOutputStream fos = new FileOutputStream(receivedFile,true);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] mybytearray = new byte[fileSize];
        int bytesRead = clientSocket.getInputStream().read(mybytearray, 0, mybytearray.length);
        bos.write(mybytearray, 0, bytesRead);
	    fos.flush();
	    bos.close();
	    fos.close();
	  
	    ShamirShare shamir = new ShamirShare();
	    shamir.split(receivedFile);
	    localFileSlice(shamir);
	    sendSharesToOthers(shamir);
	    output.println("File splitting done.");
	    receivedFile.delete();
	}
	
	private void localFileSlice(ShamirShare shamir) throws IOException {
		File file = new File("FileShare.txt");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(shamir.getNoOfShares());
		bw.newLine();
		bw.write(shamir.getPrime().toString());
		bw.newLine();
		bw.write(shamir.getThreshold());
		bw.newLine();
		bw.write(shamir.getShareArr().get(0).getShare().toString());
		bw.newLine();
		bw.write(shamir.getShareArr().get(0).getShareIndex());
		bw.newLine();
		bw.close();
		shamir.getShareArr().remove(0);
        
	}
	
	private void combineFile(){
			
		}
	
	private void sendSharesToOthers(ShamirShare shamir) throws IOException {
		try {
			otherServerIP = getOtherServerIP();
		} catch (UnknownHostException e) {
			System.out.println("Cannot get other server's IP.");
			e.printStackTrace();
		}
		 for (int i=0; i<otherServerIP.size(); i++) {
		 SendingThread serverthread = new SendingThread(new Socket(otherServerIP.get(i), 4444), shamir, i);
		 serverthread.start();
		 }

	}
	private ArrayList<InetAddress> getOtherServerIP() throws UnknownHostException {
		//get all the server IP
		ArrayList<InetAddress> serverIPs =new ArrayList<InetAddress>();
		for (int i=0; i<serverIPs.size(); i++){
			if (serverIPs.get(i).equals(InetAddress.getLocalHost())){
				serverIPs.remove(i);
				break;
			}
		}
		return serverIPs;	
	}
		
	}
