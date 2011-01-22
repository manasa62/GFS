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



public class RouterThread implements Runnable{
	
	private Socket thisConnection;
	private Router router;
	
	public RouterThread(Socket socket, Router router){
		this.thisConnection = socket;
		this.router = router;
	}

	@Override
	public void run() {
		
	//	Socket connection = null;
		System.out.println("Connection received from "
				+ thisConnection.getInetAddress().getHostName());
		router.getClientTable().put(thisConnection.getInetAddress().getHostName(), thisConnection.getInetAddress());
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try {
			out = new ObjectOutputStream(thisConnection.getOutputStream());
			in = new ObjectInputStream(thisConnection.getInputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}

		writeToQueue(in);
		try {
			in.close();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		try {
			readFromQueue();
		} catch (IOException e2) {

			e2.printStackTrace();
		}

		
		
	}
	
	private void writeToQueue(ObjectInputStream in) {
		String msg = "first";
		
		
		while (!msg.equals("bye")) {
			try {
				msg = (String) in.readObject();
				System.out.println("Client>> " + msg);
				router.getMsgQueue().add(msg);
				

			} catch (IOException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
		}
		
	}

	public void readFromQueue() throws IOException {

		
		String thisLine;
		System.out.println("Contents of the message queue");

		System.out.println(router.getMsgQueue().toString());
	
	
	}

	public void start() {
		this.start();
		
	}

}
