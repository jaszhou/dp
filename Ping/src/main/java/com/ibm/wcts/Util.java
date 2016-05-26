package com.ibm.wcts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
	
	public static void main(String[] args){
		System.out.println(System.getProperty("file.encoding"));
	}

	public String getPropValues() throws IOException {

		InputStream inputStream = null;

		String port = "80";

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			
			

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			port = prop.getProperty("port");

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return port;
	}

	public  String getPropValues(String name) throws IOException {

		InputStream inputStream = null;

		String port = "80";

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			
			

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			port = prop.getProperty(name);

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return port;
	}

	
}
