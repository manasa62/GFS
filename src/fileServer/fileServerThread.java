package fileServer;

import java.net.Socket;

public class fileServerThread implements Runnable{
	
	private Socket thisSocket;
	
	public fileServerThread(Socket socket){
		this.thisSocket = socket;
		
	}

	@Override
	public void run() {
		
		
	}

}
