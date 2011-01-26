package part1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

public class RouterThread implements Runnable {

	private DatagramSocket thisConnection;
	private Router router;
	private int portNum;

	public RouterThread(int portNum, Router router) {
		this.thisConnection = null;
		this.router = router;
		this.portNum = portNum;
	}

	@Override
	public void run() {

		
		try {
			this.thisConnection = new DatagramSocket(portNum);

			
		} catch (IOException e) {
			System.out.println("Connection failed on the port: " + portNum);
			e.printStackTrace();
		}

		/*
		 * try { out = new ObjectOutputStream(thisConnection.getOutputStream());
		 * in = new ObjectInputStream(thisConnection.getInputStream()); } catch
		 * (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		
		try {
			writeToQueue();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			readFromQueue();
		} catch (IOException e2) {

			e2.printStackTrace();
		}

	}

	private void writeToQueue() throws UnknownHostException {
		String msg = null;
		byte[] buf = new byte[1000];
		String[] msgparts = null;

		DatagramPacket recvdPkt = new DatagramPacket(buf, buf.length);

		
		try {
			
			thisConnection.receive(recvdPkt);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		msg = new String(recvdPkt.getData());

		msgparts = new String[2];
		msgparts = msg.split(":", 2);
		System.out.println("Split message:" + msgparts[0] + "," + msgparts[1]);

		System.out.println("Packet received from "
				+ msgparts[0]);

		while (!msgparts[1].equals("bye")) {

			router.getMsgQueue().add(msg);
			try {
				buf= new byte[1000];
				recvdPkt = new DatagramPacket(buf, buf.length);
				thisConnection.receive(recvdPkt);

				printRouterClientTable();
			}

			catch (IOException e1) {
				e1.printStackTrace();
			}

			msg = null;
			msg = new String(recvdPkt.getData());
			System.out.println("packet data received is :" + msg);
			System.out.println("Packet received from " + msgparts[0]);
		}

	}

	private void printRouterClientTable() {
		Iterator iterator = router.getClientTable().keySet().iterator();
		System.out.println("Routing table contents-->");
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = router.getClientTable().get(key).toString();

			System.out.println(key + " " + value);
		}

	}

	public void readFromQueue() throws IOException {

		System.out.println("Contents of the message queue");

		System.out.println(router.getMsgQueue().toString());

	}

	public DatagramPacket toDatagramPacket(byte[] bytes) {
		Object obj = null;
		System.out.println("Bytes recived: " + bytes);
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			System.out.println("Returning Object: " + obj);
		} catch (IOException ex) {
			System.out.println("IO Exception");
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			System.out.println("Class not found Exception");
			ex.printStackTrace();
		}
		System.out.println("Returning Object later: " + obj);
		return (DatagramPacket) obj;
	}

	public void start() {
		this.start();

	}

}
