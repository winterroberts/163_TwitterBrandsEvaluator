package net.aionstudios.twitbrande.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.aionstudios.twitbrande.database.DatabaseConnector;
import net.aionstudios.twitbrande.database.QueryResults;

/**
 * A class providing utilities for working with databases.
 * @author Winter Roberts
 */
public class DatabaseUtils {
	
	/**
	 * Uses a prepared statement for security and inserts provided elements for a query.
	 * @param preparedStatement A prepared MySQL statement.
	 * @param logError Whether or not to log MySQL errors for this query.
	 * @param elements An array of objects to be added to the query.
	 * @return A List of {@link QueryResults}.
	 */
	public static List<QueryResults> prepareAndExecute(String preparedStatement, boolean logError, Object... elements) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<QueryResults> resultSet = null;
		try {
			connection = DatabaseConnector.getDatabase();
			if(!connection.isValid(1)) {
				DatabaseConnector.refreshConnection();
			}
			statement = connection.prepareStatement(preparedStatement);
			for(int i = 0; i < elements.length; i++) {
				statement.setObject(i+1, elements[i]);
			}
			statement.execute();
			resultSet = new ArrayList<QueryResults>();
			ResultSet rs = statement.getResultSet();
			while(rs!=null) {
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			    Map<String, Object> row = null;

			    ResultSetMetaData metaData = rs.getMetaData();
			    Integer columnCount = metaData.getColumnCount();
			    while (rs.next()) {
			        row = new HashMap<String, Object>();
			        for (int i = 1; i <= columnCount; i++) {
			            row.put(metaData.getColumnName(i), rs.getObject(i));
			        }
			        resultList.add(row);
			    }
			    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
			    resultSet.add(new QueryResults(resultList, metaData.getTableName(1)));
			    rs=null;
			    if(statement.getMoreResults()) {
			    	rs=statement.getResultSet();
			    }
			}
			if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
			return resultSet;
		} catch (SQLException e) {
			if(logError) {
				System.err.println("Error executing SQL query.");
				e.printStackTrace();
			}
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
		}
		if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
		return null;
	}
	
	/**
	 * Uses a prepared statement for security without additional elements.
	 * @param preparedStatement A prepared MySQL statement.
	 * @param logError Whether or not to log MySQL errors for this query.
	 * @return A List of {@link QueryResults}.
	 */
	public static List<QueryResults> prepareAndExecute(String preparedStatement, boolean logError) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<QueryResults> resultSet = null;
		try {
			connection = DatabaseConnector.getDatabase();
			if(!connection.isValid(1)) {
				DatabaseConnector.refreshConnection();
			}
			statement = connection.prepareStatement(preparedStatement);
			statement.execute();
			resultSet = new ArrayList<QueryResults>();
			ResultSet rs = statement.getResultSet();
			while(rs!=null) {
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			    Map<String, Object> row = null;

			    ResultSetMetaData metaData = rs.getMetaData();
			    Integer columnCount = metaData.getColumnCount();
			    while (rs.next()) {
			        row = new HashMap<String, Object>();
			        for (int i = 1; i <= columnCount; i++) {
			            row.put(metaData.getColumnName(i), rs.getObject(i));
			        }
			        resultList.add(row);
			    }
			    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
			    resultSet.add(new QueryResults(resultList, metaData.getTableName(1)));
			    rs=null;
			    if(statement.getMoreResults()) {
			    	rs=statement.getResultSet();
			    }
			}
			if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
			return resultSet;
		} catch (SQLException e) {
			if(logError) {
				System.err.println("Error executing SQL query.");
				e.printStackTrace();
			}
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
		}
		if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
		return null;
	}

}