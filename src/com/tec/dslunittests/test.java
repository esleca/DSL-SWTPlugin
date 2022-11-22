package com.tec.dslunittests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test {
	public static String path = "C:\\Users\\karin\\eclipse-workspace\\Console_ATM\\src\\com\\bank\\atm\\dal\\ClientRepository.java";

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File(path);
		Scanner scanner = new Scanner(file);
		String currentLine;

		while(scanner.hasNext())
		{
			currentLine = scanner.next();
		    if(currentLine.indexOf("package") == 0)
		    {
		    	String next = scanner.next();
		    	String[] arrOfStr = next.split(";", 2);
		    	System.out.println(arrOfStr[0]);
		    }
		}
	}

}
