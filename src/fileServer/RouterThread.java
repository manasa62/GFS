package fileServer;

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

		System.out.println("Router Listening on port :" + portNum);
		try {
			this.thisConnection = new DatagramSocket(portNum);
			
			System.out.println("Connection to -->"
					+ thisConnection.getLocalAddress().getHostName() + ":"
					+ thisConnection.getLocalPort());
		} catch (IOException e) {
			System.out.println("Listening failed on the port: " + portNum);
			e.printStackTrace();
		}

		/*
		 * try { out = new ObjectOutputStream(thisConnection.getOutputStream());
		 * in = new ObjectInputStream(thisConnection.getInputStream()); } catch
		 * (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		System.out.println("Inside Router Thread-- Attempting to write");
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
		String msg=null;
		byte[] buf = new byte[1000];
		DatagramPacket recvdPkt = new DatagramPacket(buf, buf.length);
				
		System.out.println("In write to queue");
		try {
			thisConnection.receive(recvdPkt);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Packet received from "
				+ recvdPkt.getAddress().getHostName());

		if (!(router.getClientTable().containsKey(recvdPkt.getAddress().getHostName()))) {
			router.getClientTable().put(recvdPkt.getAddress().getHostName(),
					recvdPkt.getAddress());
			
		if (!(router.getClientStatus().containsKey(recvdPkt.getAddress().getHostName()))) {
				router.getClientStatus().put(recvdPkt.getAddress().getHostName(),
						Router.CLIENTSTATE.UP);
			}
			printRouterClientTable();
		}

		
		
		msg = new String(recvdPkt.getData());
		System.out.println("packet data received is :"+msg);
		while (!msg.equals("bye")) {
			
			router.getMsgQueue().add(msg);
			try {
				buf = new byte[1000];
				recvdPkt = new DatagramPacket(buf, buf.length);
				thisConnection.receive(recvdPkt);
				
				if (!(router.getClientTable().containsKey(recvdPkt.getAddress().getHostName()))) {
					router.getClientTable().put(recvdPkt.getAddress().getHostName(),
							recvdPkt.getAddress());
				
				if (!(router.getClientStatus().containsKey(recvdPkt.getAddress().getHostName()))) {
					router.getClientStatus().put(recvdPkt.getAddress().getHostName(),
							Router.CLIENTSTATE.UP);
				}
					printRouterClientTable();
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			msg =null;
			msg = new String(recvdPkt.getData());
			System.out.println("packet data received is :" +msg);
			System.out.println("Packet received from "
					+ recvdPkt.getAddress().getHostName());
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

	public void start() {
		this.start();

	}

}
