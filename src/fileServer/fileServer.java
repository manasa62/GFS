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

	public void listen() throws SocketException {

		fileServerThread w;

		// server.accept returns a client connection
		w = new fileServerThread(portNum, this.sharedFile);
		Thread t = new Thread(w);
		t.start();

	}

	/*
	 * private void forkThread(ServerSocket server, String sharedFilename) {
	 * 
	 * fileServerThread newConnection = new fileServerThread(server,
	 * sharedFile); newConnection.start();
	 * 
	 * }
	 */

	public static void main(String args[]) throws IOException {

		new fileServer(GFSConstants.FileServerPort).listen();

	}

}
