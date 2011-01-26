package part1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

public class Client implements Runnable {

	private int portNum;

	public Client(int portNum) {
		this.portNum = portNum;
		new Thread(this).start();
	}

	public void request() {

		DatagramSocket requestSocket = null;

		String message = null;
		String msg = null;

		try {
			requestSocket = new DatagramSocket();

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * try { out = new ObjectOutputStream(requestSocket.getOutputStream());
		 * out.flush(); System.out.println("Got the output stream"); } catch
		 * (IOException e1) {
		 * 
		 * e1.printStackTrace(); } finally {
		 * 
		 * } try { in = new ObjectInputStream(requestSocket.getInputStream());
		 * System.out.println("Got the input stream"); } catch (IOException e) {
		 * 
		 * e.printStackTrace(); } /* try {
		 * System.out.println("Reading input object"); message =
		 * (String)in.readObject(); System.out.println("Read the input object");
		 * } catch (IOException e) {
		 * 
		 * e.printStackTrace(); } catch (ClassNotFoundException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		
		// sendHelloMessage(requestSocket);
		msg = "dummy";

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (!msg.equals("bye")) {
			System.out
					.println("Enter the message and the destination to send in the format <Destination Name>:<message payload>");
			System.out.print(">>>");
			try {
				msg = br.readLine();
			} catch (IOException e1) {
				System.out.println("Failed to read message");
				e1.printStackTrace();
			}
			/*
			 * String[] msgparts = new String[10]; msgparts = msg.split(":", 3);
			 * System.out.println("Split message:" + msgparts[0] + "," +
			 * msgparts[1] + "," + msgparts[2]);
			 */
			sendMessage(msg, requestSocket);
		}
		sendMessage(msg, requestSocket);

	}

	private void sendMessage(String msg, DatagramSocket requestSocket) {
		{

			byte buf[] = new byte[1000];
			try {
				System.out.println("Sending message -- " + msg);

				DatagramPacket datapkt = new DatagramPacket(buf, buf.length,
						InetAddress.getByName(GFSConstants.routerName),
						this.portNum);
				datapkt.setData(msg.getBytes());

				requestSocket.send(datapkt);
				System.out.println("client>" + msg);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}

	}

	private void sendHelloMessage(DatagramSocket requestSocket) {
		String helloMsg = "Hello";
		sendMessage(helloMsg, requestSocket);

	}

	private void sendTerminateMessage(DatagramSocket requestSocket) {
		String terminateMsg = "Bye";
		sendMessage(terminateMsg, requestSocket);
	}

	public byte[] toBytes(DatagramPacket object) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		try {
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(
					baos);
			oos.writeObject(object);
		} catch (java.io.IOException ioe) {

		}
		System.out.println("byte array: " + baos.toByteArray());
		return baos.toByteArray();
	}

	public void run() {
		DatagramSocket thisConnection = null;
		byte[] buf = new byte[1000];
		
		DatagramPacket recvdpkt = new DatagramPacket(buf, buf.length);

		try {
			thisConnection = new DatagramSocket(GFSConstants.ClientPort);
			
		} catch (IOException e) {
			System.out.println("Listening failed on the client");
			e.printStackTrace();
		}
		while (true) {
			try {
				thisConnection.receive(recvdpkt);
				String msg = new String(recvdpkt.getData());
				//msgparts = msg.split(":", 2);
				System.out.println("Message content is: " + msg);
				System.out.println("Packet received from host " + recvdpkt.getAddress().getHostName());
				
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public static void main(String args[]) {
		Client client = new Client(GFSConstants.RouterSendPort);

		System.out.println("The available clients are: C1, C2, C3, C4, C5");
		System.out.println("The File Server is : FS");

		client.request();
	}

}
