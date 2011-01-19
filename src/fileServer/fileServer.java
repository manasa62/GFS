package fileServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.io.*;

public class fileServer implements Runnable {

	private int portNum;
	File sharedFile;
	String sharedFileName;

	public fileServer(int portNum) throws IOException {
		this.portNum = portNum;
		this.sharedFileName = "sharedfile";
		this.sharedFile = new File(this.sharedFileName);
	}

	public void run() {

		ServerSocket server = null;
		Socket connection = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		System.out.println("Server Listening on port :" + portNum);
		try {
			server = new ServerSocket(portNum);
		} catch (IOException e) {
			System.out.println("Listening failed on the port: " + portNum);
			e.printStackTrace();
		}

		try {
			connection = server.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());
		} catch (IOException e) {
			System.out.println("Failed to accept!!!");
			e.printStackTrace();
		}
		try {
			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());
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
			file = new BufferedWriter(new FileWriter(this.sharedFileName));
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
				this.sharedFileName));
		String thisLine;
		System.out.println("Printing contents of file");

		while ((thisLine = file1.readLine()) != null) {
			System.out.println(thisLine);
		}
		file1.close();

	}

	public static void main(String args[]) throws IOException {

		new Thread(new fileServer(1234)).start();

	}

}
