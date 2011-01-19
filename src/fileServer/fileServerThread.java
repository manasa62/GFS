package fileServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class fileServerThread implements Runnable{
	
	private Socket thisConnection;
	private File sharedFile;
	
	public fileServerThread(Socket socket, File sharedFile){
		this.thisConnection = socket;
		this.sharedFile = sharedFile;
		
	}

	@Override
	public void run() {
		
	//	Socket connection = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		System.out.println("Connection received from "
				+ thisConnection.getInetAddress().getHostName());
		try {
			out = new ObjectOutputStream(thisConnection.getOutputStream());
			in = new ObjectInputStream(thisConnection.getInputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}

		writeToFile(in);
		try {
			readFromFile();
		} catch (IOException e2) {

			e2.printStackTrace();
		}

		try {
			in.close();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
	private void writeToFile(ObjectInputStream in) {
		String msg = "first";
		BufferedWriter file = null;
		try {
			file = new BufferedWriter(new FileWriter(this.sharedFile));
		} catch (IOException e3) {
			System.out.println("Could not open the file for writing");
			e3.printStackTrace();
		}
		while (!msg.equals("bye")) {
			try {
				msg = (String) in.readObject();
				System.out.println("Client>> " + msg);

				file.append(msg + "\n");

			} catch (IOException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
		}
		try {
			file.close();
		} catch (IOException e3) {

			e3.printStackTrace();
		}
	}

	public void readFromFile() throws IOException {

		BufferedReader file1 = new BufferedReader(new FileReader(
				this.sharedFile));
		String thisLine;
		System.out.println("Printing contents of file");

		while ((thisLine = file1.readLine()) != null) {
			System.out.println(thisLine);
		}
		file1.close();

	}

	public void start() {
		this.start();
		
	}

}
