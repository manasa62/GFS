package fileServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

		RouterThread w;
		w = new RouterThread(this.portNum, this);
		Thread t = new Thread(w);
		t.start();

	}

	public void run() {
		while (true) {
			if (msgQueue.size() > 3) {
				System.out
						.println("Router requesting connection to FileServer... ");
				DatagramSocket requestSocket = null;
				String message = null;
				String msg = null;

				try {
					requestSocket = new DatagramSocket();
					System.out.println("Connection established!!");
				} catch (IOException e) {
					e.printStackTrace();
				}

				/*
				 * try { out = new
				 * ObjectOutputStream(requestSocket.getOutputStream());
				 * out.flush(); System.out.println("Got the output stream"); }
				 * catch (IOException e1) {
				 * 
				 * e1.printStackTrace(); } finally {
				 * 
				 * System.out.println("Some exception caught in finally"); } try
				 * { in = new ObjectInputStream(requestSocket.getInputStream());
				 * System.out.println("Got the input stream"); } catch
				 * (IOException e) {
				 * 
				 * e.printStackTrace(); }
				 */

				System.out.println("router>" + message);
				try {
					sendMessage("I want a connection", requestSocket);
				} catch (IOException e2) {

					e2.printStackTrace();
				}
				msg = "1";

				while (!this.msgQueue.isEmpty()) {
					try {
						msg = this.msgQueue.remove();
						try {
							sendMessage(msg, requestSocket);
						} catch (IOException e) {

							e.printStackTrace();
						}

					} catch (NoSuchElementException e) {

						// System.out.println("Queue empty");
						/*
						 * try { out.close(); } catch (IOException e1) {
						 * System.out.println("Failed to close output stream");
						 * e.printStackTrace(); } try { in.close(); } catch
						 * (IOException e1) {
						 * System.out.println("Failed to close input stream");
						 * e.printStackTrace(); }
						 */

					}

				}

				try {
					sendMessage("END_OF_CONNECTION", requestSocket);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	private void sendMessage(String msg, DatagramSocket requestSocket)
			throws IOException {

		byte buf[] = new byte[1000];
		try {
			System.out.println("Trying to send message.. ");
			DatagramPacket newpkt = new DatagramPacket(buf, buf.length,
					InetAddress.getByName(GFSConstants.fileServerName), GFSConstants.FileServerPort);
			newpkt.setData(msg.getBytes());
			requestSocket.send(newpkt);
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {

		new Router(GFSConstants.RouterSendPort).listen();

	}

}
