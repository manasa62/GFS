package fileServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

public class MaintainLinkStates {

	
	
	
	
	public static void main(String args[]) throws IOException{
		
		
		printActiveClients();
		changeLinkState();
		
	}

	private static void changeLinkState() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = new String();
		String state = new String();
		System.out.println("Enter the hostname whose state should be changed");
		input = br.readLine();
		System.out.println("Enter the state:UP/DOWN/BLOCK ");
		state = br.readLine();
		if(state.equals("UP")){
		Router.clientStatus.put(input, Router.CLIENTSTATE.UP);
		}
		else if (state.equals("DOWN")){
			Router.clientStatus.put(input, Router.CLIENTSTATE.DOWN);	
		}
		else{
			Router.clientStatus.put(input, Router.CLIENTSTATE.BLOCKED);
		}
		
		
	}

	private static void printActiveClients() {
		int i =1;
		 Iterator iterator = Router.clientTable.keySet().iterator();  
		   System.out.println("Active registered clients");
		   System.out.println("--------------------------");
		   while (iterator.hasNext()) {  
		       String key = iterator.next().toString();  
		       String value = Router.clientTable.get(key).toString();  
		       
		       System.out.println(i+ " Hostname "+i+":"+key + " " + "IP Address of "+i+":"+value); 
		       i++;
		       
		   }  
		
	}
}
