package metadataSecurity;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class Client {
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	private boolean fileSplited;
	private boolean fileCombined;
	private boolean noConnection=true;
	private ClientThread clientThread;
	
	public boolean isFileSplited() {
		return fileSplited;
	}

	public void setFileSplited(boolean fileSplited) {
		this.fileSplited = fileSplited;
	}

	public boolean isFileCombined() {
		return fileCombined;
	}

	public void setFileCombined(boolean fileCombined) {
		this.fileCombined = fileCombined;
	}

	public Client() throws IOException {
		 try {
				ArrayList<InetAddress> serverIP = getServerIP();
				ArrayList<ClientThread> ClientThreads = new ArrayList<ClientThread>();
				for (int i=0; i<serverIP.size(); i++){
					ClientThread lookForActiveServers = new ClientThread(new Socket(serverIP.get(i), 4444));
					lookForActiveServers.start();
					ClientThreads.add(lookForActiveServers);
				}
				
				try {
				    Thread.sleep(5000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				
				for (int i=0; i<ClientThreads.size(); i++){
					if(ClientThreads.get(i).isThisOneConnectedLiao() && noConnection){
						this.clientThread=ClientThreads.get(i);
						this.socket=ClientThreads.get(i).getSocket();
						noConnection=false;
					}
					else {
						ClientThreads.get(i).getSocket().close();
					}
				}
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + this.clientThread.getSocket().getInetAddress());
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	            		this.clientThread.getSocket().getInetAddress());
	            System.exit(1);
	        } 
			this.output = new PrintWriter(this.clientThread.getSocket().getOutputStream(), true);
			this.input = new BufferedReader(new InputStreamReader(this.clientThread.getSocket().getInputStream()));
	 }

	private ArrayList<InetAddress> getServerIP() {
		ArrayList<InetAddress> serverIPs = new ArrayList<InetAddress>();
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
		return serverIPs;
	}

	public void sendFile(File file) throws IOException{
		output.println("Split file.");
		output.println(file.getName());
		output.println(file.length());
		byte[] fileByte = new byte[(int) file.length()];
		FileInputStream userInput = new FileInputStream(file);
		userInput.read(fileByte);
		for (int i=0; i<fileByte.length; i++){
			System.out.print((char)fileByte[i]);
		}
        this.socket.getOutputStream().write(fileByte);
        userInput.close();
             
        String serverResult;
        while ((serverResult = input.readLine()) != null) {
            if (serverResult.equals("File splitting done.")) {
            	setFileSplited(true);   	
                break;	
            }
            System.out.println(serverResult);
        }
	}
	
	public void getFile() throws IOException {
		output.println("Combine file.");
		File combinedFile = new File(input.readLine());
		FileOutputStream fos = new FileOutputStream(combinedFile,true);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] mybytearray = new byte[Integer.parseInt(input.readLine())];
        InputStream is = this.clientThread.getSocket().getInputStream();
        int bytesRead = is.read(mybytearray, 0, mybytearray.length);
        bos.write(mybytearray, 0, bytesRead);
	    fos.flush();
	    bos.close();
	    fos.close();  
	    this.setFileCombined(true);
        }
	
	 public static void main(String[] args) throws IOException {
//	       new Client(new File("TextFile.txt"));
	    }
	 
}
