package fileServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class test {
	
	public static void main(String args[]) throws IOException{
	
	File test = new File("test");
	BufferedWriter file1 = new BufferedWriter(new FileWriter(
			test));
	file1.write("test chars\n");
	file1.write("Second line");
	file1.close();
	
	BufferedReader file = new BufferedReader(new FileReader(
			test));
	String thisLine;
	System.out.println("Printing contents of file");
	thisLine = file.readLine();
	while (thisLine != null) {
		System.out.println(thisLine);
		thisLine = file.readLine();
	}
	
	file.close();
	

	}
}
