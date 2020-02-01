package com.world_war_z.angel_flores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Authenticator {
	
	public String getToken() throws Exception{
		String token = "";
		File authen = new File("authorization.txt");
		if(!authen.exists()) {
			System.out.println("There doesn't seem to exist an authorization.txt file.\n "
					+ "Please enter the discord token : \n");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			token = reader.readLine();
			PrintWriter writer = new PrintWriter("authorization.txt", "UTF-8");
			writer.println(token);
			writer.close();
			reader.close();
		}
		else {
			BufferedReader reader = new BufferedReader(new FileReader(authen));
			token = reader.readLine();
			reader.close();
		}
		return token;
	}

	
	
}
