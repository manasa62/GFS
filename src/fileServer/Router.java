package fileServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Router implements Runnable {

	private enum CLIENTSTATE {
		UP, DOWN
	};

	public LinkedBlockingQueue<String> msgQueue;
	public HashMap<String, Router.CLIENTSTATE> clientStatus;
	public HashMap<String, InetAddress> clientTable;
	private int portNum;
	private static final int FSportNum = 4444;

	public Router(int portNum) {
		this.msgQueue = new LinkedBlockingQueue<String>();
		this.clientStatus = new HashMap<String, Router.CLIENTSTATE>();
		this.portNum = portNum;
		this.clientTable = new HashMap<String, InetAddress>();
		new Thread(this).start();
	}

	public LinkedBlockingQueue<String> getMsgQueue() {
		return msgQueue;
	}

	public void setMsgQueue(LinkedBlockingQueue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}

	public HashMap<String, Router.CLIENTSTATE> getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(HashMap<String, Router.CLIENTSTATE> clientStatus) {
		this.clientStatus = clientStatus;
	}

	public HashMap<String, InetAddress> getClientTable() {
		return clientTable;
	}

	public void setClientTable(HashMap<String, InetAddress> clientTable) {
		this.clientTable = clientTable;
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

		Socket connection = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		while (true) {
			RouterThread w;
			try {
				// server.accept returns a client connection
				w = new RouterThread(server.accept(), this);
				Thread t = new Thread(w);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 1234");
				System.exit(-1);
			}
		}

	}

	public void run() {
		while(true){
		if(msgQueue.size() > 3) {
		System.out.println("Router requesting connection to FileServer... ");
		Socket requestSocket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		String message = null;
		String msg = null;

		try {
			requestSocket = new Socket("127.0.0.1", this.FSportNum);
			System.out.println("Connection established!!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			System.out.println("Got the output stream");
		} catch (IOException e1) {

			e1.printStackTrace();
		} finally  {
			
			System.out.println("Some exception caught in finally");
		}
		try {
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Got the input stream");
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println("router>" + message);
		try {
			sendMessage("I want a connection", out);
		} catch (IOException e2) {

			e2.printStackTrace();
		}
		msg = "1";

		
		while(!this.msgQueue.isEmpty()) {
			try {
				msg = this.msgQueue.remove();
				try {
					sendMessage(msg, out);
				} catch (IOException e) {

					e.printStackTrace();
				}
			

			} catch (NoSuchElementException e) {
				
			//	System.out.println("Queue empty");
			/*try {
					out.close();
				} catch (IOException e1) {
					System.out.println("Failed to close output stream");
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e1) {
					System.out.println("Failed to close input stream");
					e.printStackTrace();
				}*/
				
			  }

			}
		
		 try {
			sendMessage("END_OF_CONNECTION", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}
	}

		

	}

	private void sendMessage(String msg, ObjectOutputStream out)
			throws IOException {

		System.out.println("Trying to send message.. ");
		out.writeObject(msg);
		out.flush();
		System.out.println("client>" + msg);

	}

	public static void main(String args[]) throws IOException {

		new Router(1234).listen();

	}

}
