package part1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class fileServerThread implements Runnable {

	private int portNum;
	private DatagramSocket thisConnection;
	private File sharedFile;
	private static final String endOfConn = "END_OF_CONNECTION";

	public fileServerThread(int portNum, File sharedFile)
			throws SocketException {
		this.portNum = portNum;
		this.sharedFile = sharedFile;
		this.thisConnection = null;

	}

	@Override
	public void run() {

		System.out.println("Server Listening on port :" + portNum);
		try {
			this.thisConnection = new DatagramSocket(this.portNum);
		} catch (IOException e) {
			System.out.println("Listening failed on the port: " + portNum);
			e.printStackTrace();
		}
		while (true) {
			try {
				writeToFile();
			} catch (IOException e1) {

				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {

				e1.printStackTrace();
			}
			try {
				readFromFile();
			} catch (IOException e2) {

				e2.printStackTrace();
			}
		}
	}

	private void writeToFile() throws IOException, ClassNotFoundException {

		BufferedWriter file = null;
		byte[] buf = new byte[1000];
		DatagramPacket recvdPkt = new DatagramPacket(buf, buf.length);
		try {
			thisConnection.receive(recvdPkt);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			file = new BufferedWriter(new FileWriter(this.sharedFile, true));
		} catch (IOException e3) {
			System.out.println("Could not open the file for writing");
			e3.printStackTrace();
		}
		String msg = new String(recvdPkt.getData());

		try {
			System.out.println("Client>> " + msg);
			if (msg != null)
				file.append(msg + "\n");

		/*	try {
				thisConnection.receive(recvdPkt);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			msg = new String(recvdPkt.getData());*/

		} catch (IOException e) {

			e.printStackTrace();
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
