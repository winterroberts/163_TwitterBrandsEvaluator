package net.aionstudios.twitbrande.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.aionstudios.twitbrande.util.DatabaseUtils;

/**
 * A class for assisting in connecting to and executing queries on MySQL databases using JDBC.
 * @author Winter Roberts
 */
public class DatabaseConnector {
	
	private static String host = "";
	private static String user = "";
	private static String password = "";
	private static String database = "";
	
	private static boolean loadedJDBC = false;
	
	private static Connection db;
	
	/**
	 * Connects and logs into a MySQL database.
	 * @param hostname The hostname of the database server.
	 * @param databaseName The name of database to be logged in to.
	 * @param databasePort The port on which the database accepts connections.
	 * @param databaseUser The username of a database user with access to the provided database.
	 * @param databasePassword The password of the database user provided in the databaseUser argument.
	 * @return True if the database is functioning and accepted the connection, false otherwise.
	 */
	public static boolean setupDatabase(String hostname, String databaseName, String databasePort, String databaseUser, String databasePassword, boolean autoReconnect, String timezone) {
		if(host=="") {
			host = "jdbc:mysql://"+hostname+":"+databasePort+"/?autoReconnect="+Boolean.toString(autoReconnect)+"&serverTimezone="+timezone;
			database = databaseName;
			user = databaseUser;
			password = databasePassword;
			try {
				//?autoReconnect=true&useSSL=false&serverTimezone=PDT
				loadJDBC();
				db = DriverManager.getConnection(host, user, password);
			} catch (SQLException e1) {
				System.err.println("DatabaseConnector failed while connecting to database '"+database+"'!");
				e1.printStackTrace();
				return false;
			}
			try {
				DatabaseUtils.prepareAndExecute("CREATE DATABASE IF NOT EXISTS `"+database+"` CHARACTER SET latin1 COLLATE latin1_general_cs;", false);
				getDatabase().setCatalog(database);
			} catch (SQLException e) {
				System.err.println("DatabaseConnector failed while switching to database '"+database+"'!");
				e.printStackTrace();
				return false;
			}
			return true;
		}
		System.err.println("Only one database can be connected per instance.");
		return false;
	}
	
	public static void refreshConnection() throws SQLException {
		db.close();
		db = DriverManager.getConnection(host, user, password);
		getDatabase().setCatalog(database);
	}
	
	/**
	 * Connects and logs into a MySQL database using the default port.
	 * @param hostname The hostname of the database server.
	 * @param databaseName The name of database to be logged in to.
	 * @param databaseUser The username of a database user with access to the provided database.
	 * @param databasePassword The password of the database user provided in the databaseUser argument.
	 * @return True if the database is functioning and accepted the connection, false otherwise.
	 */
	public static boolean setupDatabase(String hostname, String databaseName, String databaseUser, String databasePassword, boolean autoReconnect, String timezone) {
		if(host=="") {
			host = "jdbc:mysql://"+hostname+"/?autoReconnect="+Boolean.toString(autoReconnect)+"&serverTimezone="+timezone;
			database = databaseName;
			user = databaseUser;
			password = databasePassword;
			try {
				loadJDBC();
				db = DriverManager.getConnection(host, user, password);
			} catch (SQLException e1) {
				System.err.println("DatabaseConnector failed while connecting to database '"+database+"'!");
				e1.printStackTrace();
			}
			try {
				DatabaseUtils.prepareAndExecute("CREATE DATABASE IF NOT EXISTS `"+database+"` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ai_cs;", false);
				getDatabase().setCatalog(database);
			} catch (SQLException e) {
				System.err.println("DatabaseConnector failed while switching to database '"+database+"'!");
				e.printStackTrace();
			}
			return true;
		}
		System.err.println("Only one database can be connected per instance.");
		return false;
	}
	
	/**
	 * Loads the database drive for JDBC if it hasn't been already.
	 * <p>
	 * This will cause newer version of Java to print a warning about how the
	 * driver is automatically added, which will result in this older version
	 * of the the driver not loading.
	 */
	public static void loadJDBC() {
		if(loadedJDBC==false) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				System.out.println("Failed loading JDBC");
				e1.printStackTrace();
			}
		}
		loadedJDBC = true;
	}
	
	/**
	 * Gets an instance of the database to query through.
	 * @return	A database connection.
	 */
	public static Connection getDatabase() {
		return db;
	}

}