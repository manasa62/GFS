package part1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

public class MaintainLinkStates implements Runnable {
	public MaintainLinkStates() {

	}

	private static void printActiveClients() {
		int i = 1;
		Iterator iterator = Router.clientStatus.keySet().iterator();
		System.out.println("Active registered clients");
		System.out.println("--------------------------");
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = Router.clientStatus.get(key).toString();

			System.out.println(i + " Host " + i + ":" + key + " "
					+ "Link status " + value);

		}

	}

	public void run() {
		while (true) {
			printActiveClients();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String input = new String();
			String state = new String();
			System.out.println("Do you want to change the link status?(y/n)");
			try {
				input = br.readLine();
				while (input.equals("y")) {
					System.out
							.println("Enter the host whose state should be changed");
					try {
						input = br.readLine();
					} catch (IOException e) {

						e.printStackTrace();
					}

					System.out.println("Enter the state:UP/DOWN/BLOCK ");
					try {
						state = br.readLine();
					} catch (IOException e) {

						e.printStackTrace();
					}
					if (state.equals("UP")) {
						Router.clientStatus.put(input, Router.CLIENTSTATE.UP);
					} else if (state.equals("DOWN")) {
						Router.clientStatus.put(input, Router.CLIENTSTATE.DOWN);
					} else {
						Router.clientStatus.put(input,
								Router.CLIENTSTATE.BLOCKED);
					}

					System.out
							.println("Do you want to change the link status?(y/n)");
					input = br.readLine();

				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			} catch (IOException e1) {

				e1.printStackTrace();
			}

			
		}
	}

	public void start() {
		this.start();

	}
}
