package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private int portNum;
	private ObjectOutputStream out = null;
 	private ObjectInputStream in = null;
	
 	public Client(int portNum){
		this.portNum = portNum;
	}
	
	public void request(){
		System.out.println("Client requesting... ");
		Socket requestSocket = null;
		
	 	String message = null;
	 	String msg = null;
	 	
	 	try {
			requestSocket = new Socket("127.0.0.1", portNum);
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
		} finally {
			System.out.println("Some exception caught in finally");
		}
		try {
			in = new ObjectInputStream(requestSocket.getInputStream());
			System.out.println("Got the input stream");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		/*try {
			System.out.println("Reading input object");
			message = (String)in.readObject();
			System.out.println("Read the input object");
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}*/	
		System.out.println("server>" + message);
		sendMessage("I want a connection");
		msg = "1";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(!msg.equals("bye")){
		System.out.println("Enter the message to send: ");
		try {
			msg = br.readLine();
		} catch (IOException e1) {
			System.out.println("Failed to read message");
			e1.printStackTrace();
		}
		
		sendMessage(msg);
		}
		sendMessage("bye");
	 	try {
			out.close();
		} catch (IOException e) {
		 System.out.println("Failed to close output stream");
			e.printStackTrace();
		}
	 	try {
			in.close();
		} catch (IOException e) {
			System.out.println("Failed to close input stream");
			e.printStackTrace();
		}
	}

	private void sendMessage(String msg){
		{
			try{
				System.out.println("Trying to send message.. ");
				out.writeObject(msg);
				out.flush();
				System.out.println("client>" + msg);
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}

		
	}
	
	public static void main(String args[])
	{
		Client client = new Client(1234);
		client.request();
	}

}
