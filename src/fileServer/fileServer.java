package fileServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.io.*;

public class fileServer {

	private int portNum;
	File sharedFile;
	String sharedFileName;

	public fileServer(int portNum) throws IOException {
		this.portNum = portNum;
		this.sharedFileName = "sharedfile";
		this.sharedFile = new File(this.sharedFileName);
	}

	public void listen() {

		ServerSocket server = null;
		

		System.out.println("Server Listening on port :" + portNum);
		try {
			server = new ServerSocket(portNum);
		} catch (IOException e) {
			System.out.println("Listening failed on the port: " + portNum);
			e.printStackTrace();
		}

		while(true){
		    fileServerThread w;
		    try{
		//server.accept returns a client connection
		      w = new fileServerThread(server.accept(), this.sharedFile);
		      Thread t = new Thread(w);
		      t.start();
		    } catch (IOException e) {
		      System.out.println("Accept failed: 1234");
		      System.exit(-1);
		    }
		}
		

	}

	

	/*private void forkThread(ServerSocket server, String sharedFilename) {
		
		fileServerThread newConnection = new fileServerThread(server, sharedFile);
		newConnection.start();
		
	}*/

	public static void main(String args[]) throws IOException {

		new fileServer(1234).listen();

	}

}
