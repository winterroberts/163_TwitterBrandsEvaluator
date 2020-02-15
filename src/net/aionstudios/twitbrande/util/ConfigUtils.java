package net.aionstudios.twitbrande.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigUtils {
	
	/**
	 * Creates a new {@link JSONObject}, forcing it to use a LinkedHashMap instead of an unlinked one.
	 * @return An empty {@link JSONObject} with modified structure.
	 */
	public static JSONObject getLinkedJsonObject() {
		JSONObject j = new JSONObject();
		Field map;
		try {
			map = j.getClass().getDeclaredField("map");
			map.setAccessible(true);
			map.set(j, new LinkedHashMap<>());
			map.setAccessible(false);
		} catch (NoSuchFieldException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (SecurityException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * Writes the provided {@link JSONObject} to the file system, optimistically as a configuration file.
	 * @param j	The {@link JSONObject} to be serialized into the file system.
	 * @param f	The {@link File} object identifying where the {@link JSONObject} should be saved onto the file system.
	 * @return True if the file was written without error, false otherwise.
	 */
	public static boolean writeConfig(JSONObject j, File f) {
		try {
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
				System.out.println("Created config file '"+f.toString()+"'!");
			}
			PrintWriter writer;
			File temp = File.createTempFile("temp_json", null, f.getParentFile());
			writer = new PrintWriter(temp.toString(), "UTF-8");
			writer.println(j.toString(2));
			writer.close();
			Files.deleteIfExists(f.toPath());
			temp.renameTo(f);
			return true;
		} catch (IOException e) {
			System.err.println("Encountered an IOException while writing config: '"+f.toString()+"'!");
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			System.err.println("Encountered a JSONException while writing config: '"+f.toString()+"'!");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Deserializes a {@link JSONObject} from a file on the file system and returns it.
	 * @param f	The {@link File} object, representing a file containing JSON data on the file system.
	 * @return	A {@link JSONObject} representing the file provided or null if it could not be read.
	 */
	public static JSONObject readConfig(File f) {
		if(!f.exists()) {
			System.err.println("Failed reading config: '"+f.toString()+"'. No such file!");
			return null;
		}
		String jsonString = "";
		try (BufferedReader br = new BufferedReader(new FileReader(f.toString()))) {
		    for (String line; (line = br.readLine()) != null;) {
		    	jsonString += line;
		    }
		    br.close();
		    return new JSONObject(jsonString);
		} catch (IOException e) {
			System.err.println("Encountered an IOException while reading config: '"+f.toString()+"'!");
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			System.err.println("Encountered a JSONException while reading config: '"+f.toString()+"'!");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the content of the named {@link File} in the file system.
	 * @param f The {@link File} to be read from.
	 * @return The contents, a String, of the named {@link File}
	 */
	public static String readFile(File f) {
		if(!f.exists()) {
			System.err.println("Failed reading file: '"+f.toString()+"'. No such file!");
			return null;
		}
		String jsonString = "";
		try (BufferedReader br = new BufferedReader(new FileReader(f.toString()))) {
		    for (String line; (line = br.readLine()) != null;) {
		    	jsonString += line;
		    }
		    br.close();
		    return jsonString;
		} catch (IOException e) {
			System.err.println("Encountered an IOException while reading file: '"+f.toString()+"'!");
			e.printStackTrace();
			return null;
		}
	}

}
