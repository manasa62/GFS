package part1;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Router implements Runnable {

	public enum CLIENTSTATE {
		UP, BLOCKED, DOWN
	};

	public LinkedBlockingQueue<String> msgQueue;
	public static HashMap<String, Router.CLIENTSTATE> clientStatus;
	public static HashMap<String, String> clientTable;

	private int portNum;

	static {
		clientStatus = new HashMap<String, Router.CLIENTSTATE>();
		clientTable = new HashMap<String, String>();
	}

	public Router(int portNum) {
		this.msgQueue = new LinkedBlockingQueue<String>();
		this.portNum = portNum;
		new Thread(this).start();
	}

	public LinkedBlockingQueue<String> getMsgQueue() {
		return this.msgQueue;
	}

	public void setMsgQueue(LinkedBlockingQueue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}

	public HashMap<String, Router.CLIENTSTATE> getClientStatus() {
		return this.clientStatus;
	}

	public void setClientStatus(HashMap<String, Router.CLIENTSTATE> clientStatus) {
		this.clientStatus = clientStatus;
	}

	public HashMap<String, String> getClientTable() {
		return this.clientTable;
	}

	public void setClientTable(HashMap<String, String> clientTable) {
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
			if (msgQueue.size() >= 2) {
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

				// sendTerminateMessage("END_OF_CONNECTION", requestSocket);

			}
		}

	}

	private void sendMessage(String msg, DatagramSocket requestSocket)
			throws IOException {

		byte buf[] = new byte[1000];
		String[] msgparts = null;
		DatagramPacket newpkt = null;
		InetAddress ip = null;
		try {
			//System.out.println("Trying to send message.. ");
			msgparts = new String[5];
			msgparts = msg.split(":", 2);
			if(Router.clientStatus.get(msgparts[0]).equals(CLIENTSTATE.UP)) {
			String hostname = Router.clientTable.get(msgparts[0]);
			try {
				ip = InetAddress.getByName(hostname);
			} catch (UnknownHostException e) {
				System.out
						.println("The host name you are trying to resolve is incorrect!");
			}
			if (msgparts[0].equals("FS")) {
				newpkt = new DatagramPacket(buf, buf.length, ip,
						GFSConstants.FileServerPort);
			} else {
				newpkt = new DatagramPacket(buf, buf.length, ip,
						GFSConstants.ClientPort);
			}
			newpkt.setData(msgparts[1].getBytes());
			requestSocket.send(newpkt);

		} 
			else if(Router.clientStatus.get(msgparts[0]).equals(CLIENTSTATE.BLOCKED)){
				this.msgQueue.add(msg);
			}
			
		}catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = new String();
		String[] addr = new String[2];

		System.out
				.println("Enter the details of the client hostnames and IP address");
		System.out.println("Format: <Label>,<Hostname>");
		System.out.println("Type \"done\" in the end");

		input = br.readLine();

		while (!input.equals("done")) {

			addr = input.split(",", 2);
			Router.clientTable.put(addr[0], addr[1]);
			Router.clientStatus.put(addr[0], CLIENTSTATE.UP);

			input = br.readLine();

		}

		Iterator iterator = Router.clientTable.keySet().iterator();
		System.out.println("Routing table:");
		System.out.println("--------------");
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = Router.clientTable.get(key).toString();

			System.out.println(key + " : " + value);
		}

		startStatusMaintainThread();

		new Router(GFSConstants.RouterSendPort).listen();

	}

	private static void startStatusMaintainThread() {
		MaintainLinkStates w;
		w = new MaintainLinkStates();
		Thread t = new Thread(w);
		t.start();

	}

}
