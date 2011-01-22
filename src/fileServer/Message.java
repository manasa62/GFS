package fileServer;

import java.net.InetAddress;

public class Message {

	private String srcHostname;
	private InetAddress srcIP;
	private String destHostname;
	private InetAddress destIP;
	private String payload;

	public Message(String srchostname, InetAddress srcIP, String desthostname,
			InetAddress destIP, String payload) {
		this.srcHostname = srchostname;
		this.srcIP = srcIP;
		this.destHostname = desthostname;
		this.destIP = destIP;
		this.payload = payload;

	}

	public String toString(Message m) {
		String msg;
		msg = "Source Hostname: " + m.srcHostname + ", Source IP: "
		+ m.srcIP.toString()+ "Destination Hostname: " + m.destHostname + ", Destination IP: "
				+ m.destIP.toString() + ", Data: " + m.payload;
		return msg;
	}
}
