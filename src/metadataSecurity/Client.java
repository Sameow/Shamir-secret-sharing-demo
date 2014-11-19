package metadataSecurity;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
	private InetAddress server;  
	private PrintWriter output;
	private BufferedReader input;
	private boolean fileSplited;
	private ClientThread clientThread;
	
	public boolean isFileSplited() {
		return fileSplited;
	}

	public void setFileSplited(boolean fileSplited) {
		this.fileSplited = fileSplited;
	}

	public Client() throws IOException {
		 try {
				server = InetAddress.getLocalHost();
				//get all server ip n put in arrayList
				ArrayList<InetAddress> serverIP = new ArrayList<InetAddress>();
				ArrayList<ClientThread> ClientThreads = new ArrayList<ClientThread>();
				for (int i=0; i<serverIP.size(); i++){
					ClientThread lookForActiveServers = new ClientThread(new Socket(serverIP.get(i), 4444));
					lookForActiveServers.start();
					ClientThreads.add(lookForActiveServers);
				}
				for (int i=0; i<ClientThreads.size(); i++){
					if(ClientThreads.get(i).isThisOneConnectedLiao()){
						this.clientThread=ClientThreads.get(i);
					}
					else {
						ClientThreads.get(i).getSocket().close();
					}
				}
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + server);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	                server);
	            System.exit(1);
	        }
			this.output = new PrintWriter(this.clientThread.getSocket().getOutputStream(), true);
			this.input = new BufferedReader(new InputStreamReader(this.clientThread.getSocket().getInputStream()));
	 }

	public void sendFile(File file) throws IOException{
		output.println("Split file.");
		output.println(file.getName());
		output.println(file.length());
		FileInputStream userInput = new FileInputStream(file);
        if (userInput != null) {
            output.println(userInput);
        }      
        String serverResult;
        while ((serverResult = input.readLine()) != null) {
            if (serverResult.equals("File splitting done.")) {
            	setFileSplited(true);   	
                break;	
            }
            System.out.println(serverResult);
        }
	}
	
	public File getFile() throws IOException {
		output.println("Combine file.");
		String fileName = input.readLine();
		File combinedFile = new File(fileName);
		FileOutputStream fos = new FileOutputStream(combinedFile,true);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] mybytearray = new byte[1024];
        InputStream is = this.clientThread.getSocket().getInputStream();
        int bytesRead = is.read(mybytearray, 0, mybytearray.length);
        bos.write(mybytearray, 0, bytesRead);
	    fos.flush();
	    bos.close();
	    fos.close();
	    return combinedFile;   
        }
	
	 public static void main(String[] args) throws IOException {
//	       new Client(new File("TextFile.txt"));
	    }
	 
}
