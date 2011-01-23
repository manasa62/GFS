package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import fileServer.GFSConstants;

public class Client {

	private int portNum;

	public Client(int portNum) {
		this.portNum = portNum;
	}

	public void request() {
		System.out.println("Client requesting... ");
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
		System.out.println("server>" + message);
		sendHelloMessage(requestSocket);
		msg = "dummy";

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (!msg.equals("bye")) {
			System.out.println("Enter the message to send: ");
			try {
				msg = br.readLine();
			} catch (IOException e1) {
				System.out.println("Failed to read message");
				e1.printStackTrace();
			}

			sendMessage(msg, requestSocket);
		}
		sendTerminateMessage(requestSocket);

	}

	private void sendMessage(String msg, DatagramSocket requestSocket) {
		{
			byte buf[] = new byte[1000];
			try {
				System.out.println("Sending message -- " + msg);
				DatagramPacket newpkt = new DatagramPacket(buf, buf.length,
						InetAddress.getByName(GFSConstants.routerName),
						this.portNum);
				newpkt.setData(msg.getBytes());
				requestSocket.send(newpkt);
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

	public static void main(String args[]) {
		Client client = new Client(GFSConstants.RouterSendPort);
		client.request();
	}

}
