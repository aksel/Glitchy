package glitchy.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * This class handles reading and writing to the config file
 * 
 * @author Rasmus
 *
 */
public class Config {
	
	/**
	 * This map contains a list of parameters from the config file, with an apropiate name for easy getting
	 */
	private HashMap<String, String> configLines = new HashMap<String, String>();
	
	/**
	 * Reads the config file
	 */
	public Config() {
		readConfig();
	}
	
	/**
	 * @return The map of parameters
	 */
	public HashMap<String, String> getParamaters() {
		return configLines;
	}
	
	/**
	 * This methods actually saves the config file with the current data from the configLines list
	 */
	public void saveConfig() {	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("glitchy.ini", false));
		
		for (String p : configLines.keySet()) {
			
			writer.append(p + " " + configLines.get(p));
			
			writer.newLine();
			
		}
		
		writer.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Saves a parameter to the list, overwrites the current if exists
	 * @param key The key of the parameter
	 * @param param the Value of the parameter
	 */
	public void saveParameter(String key, String param) {
		if (param != null)
			configLines.put(key, param);	
	}
	
	/**
	 * Config parameters put into a map with key for easy identifying later
	 */
	private void readConfig() {
		
		//This will probably be changed later, when actual data is saved in this file
		ArrayList<String> lines = new ArrayList<String>();
		try {
			lines = getFileArray("glitchy.ini");			
		} catch (IOException e) {
			createNewIni();
		}
		
		for (String line : lines) {

			String[] splitted = line.split("\\s+");

			configLines.put(splitted[0], splitted[1]);
			
		}
		
	}
	
	/**
	 * Create a new config.ini file if no one exists.
	 */
	private void createNewIni() {	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("glitchy.ini"));
			writer.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Used by readConfig() to get each line from the file out in an ArrayList
	 * @param path The path of the file
	 * @return ArrayList with the lines from the file
	 * @throws IOException If file isnt found
	 */
	private ArrayList<String> getFileArray(String path) throws IOException {	
		BufferedReader read = new BufferedReader(new FileReader(path));
		
		String currentLine;
		ArrayList<String> ret = new ArrayList<String>();

		while((currentLine = read.readLine()) != null) {
			
			ret.add(currentLine);
			
		}
		
		read.close();
		
		return ret;
	}
	
}
