package metadataSecurity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import main.ShamirShare;
import main.Share;

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
			 		ShamirShare fileShares = getAllFileSlice();
			 		ShamirShare toCombine = new ShamirShare();
			 		toCombine.combine(fileShares);
			 	}
			 	if (inputLine.equals("Split file.")){ 
			 		String fileName = input.readLine();
			 		int fileSize = Integer.parseInt(input.readLine());
			 		splitFile(fileName, fileSize, socket);
			 	}	
			 	if (inputLine.equals("Sending shares.")){ 
			 		ShamirShare secretShare = new ShamirShare();
			 		secretShare.setFileName(input.readLine());
			 		secretShare.setNoOfShares(Integer.parseInt((input.readLine())));
			 		secretShare.setPrime(new BigInteger(input.readLine().getBytes()));
			 		secretShare.setThreshold(Integer.parseInt((input.readLine())));
			 		BigInteger tempPrime = new BigInteger(input.readLine().getBytes());
			 		int tempIndex = Integer.parseInt((input.readLine()));
			 		Share tempShare = new Share(tempIndex, tempPrime);
			 		ArrayList<Share> tempArray = new ArrayList<Share>();
			 		tempArray.add(tempShare);
			 		secretShare.setShareArr(tempArray);
			 		output.println("Acknowledged");
			 		localFileSlice(secretShare);
			 	}
			 	if (inputLine.equals("Give me slice.")){
			 		ShamirShare local = getLocalSlice();
			 		output.println(""+local.getShareArr().get(0).getShareIndex());
			 		output.println(new String(local.getShareArr().get(0).getShare().toByteArray()));
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
	private ShamirShare getAllFileSlice() throws IOException {
		ShamirShare local = getLocalSlice();
		
		ArrayList<InetAddress> serverIPs = null;
		try {
			serverIPs = getOtherServerIP();
		}
		catch (UnknownHostException e) {
			System.out.println("Cannot get other server's IP.");
			e.printStackTrace();
		}
		ArrayList<ServerToServerThread> ServerToServerThreads = new ArrayList<ServerToServerThread>();
		for (int i=0; i<serverIPs.size(); i++){
			ServerToServerThread askForSlice = new ServerToServerThread(new Socket(serverIPs.get(i), 4444));
			askForSlice.start();
			ServerToServerThreads.add(askForSlice);
		}
		
		for (int i=0; i<ServerToServerThreads.size(); i++){
			Share anotherShare = ServerToServerThreads.get(i).getShare();
			local.getShareArr().add(anotherShare);
		}
		return local;	
	}
	
	private ShamirShare getLocalSlice() throws IOException {
		File localSlice = new File("fileSlice.txt");
		FileReader fileReader = new FileReader(localSlice);
		BufferedReader br = new BufferedReader(fileReader);
		ShamirShare local = new ShamirShare();
		local.setFileName(br.readLine());
		local.setNoOfShares(Integer.parseInt(br.readLine()));
		local.setPrime(new BigInteger(br.readLine().getBytes()));
 		local.setThreshold(Integer.parseInt((br.readLine())));
 		local.getShareArr().get(0).setShare(new BigInteger(br.readLine().getBytes()));
 		local.getShareArr().get(0).setShareIndex(Integer.parseInt((br.readLine())));
		fileReader.close();
		return local;
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
	//    receivedFile.delete();
	}
	
	private void localFileSlice(ShamirShare shamir) throws IOException {
		File file = new File("fileSlice.txt");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(shamir.getFileName());
		bw.newLine();
		bw.write(""+shamir.getNoOfShares());
		bw.newLine();
		bw.write(new String(shamir.getPrime().toByteArray()));
		bw.newLine();
		bw.write(""+shamir.getThreshold());
		bw.newLine();
		bw.write(new String(shamir.getShareArr().get(0).getShare().toByteArray()));
		bw.newLine();
		bw.write(""+shamir.getShareArr().get(0).getShareIndex());
		bw.newLine();
		bw.close();
		shamir.getShareArr().remove(0);
        
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
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("serverIP.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			Enumeration<?> e = prop.elements();
			while (e.hasMoreElements()) {
				InetAddress serverIP = InetAddress.getByName((String) e.nextElement());
				serverIPs.add(serverIP);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i=0; i<serverIPs.size(); i++){
			if (serverIPs.get(i).equals(InetAddress.getLocalHost())){
				serverIPs.remove(i);
				break;
			}
		}
		return serverIPs;	
	}
		
	}
