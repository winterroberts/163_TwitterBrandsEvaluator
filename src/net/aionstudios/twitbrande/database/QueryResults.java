package net.aionstudios.twitbrande.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that collects all results from a MySQL query, specifically designed in conjunction with {@link DatabaseUtils#prepareAndExecute}.
 * @author Winter Roberts
 */
public class QueryResults {
	
	private List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
	private String table;
	
	/**
	 * Creates a new {@link QueryResults} set.
	 * @param resultset A list of mappings of strings by name to objects provided by each row in the result set of a MySQL query.
	 * @param tableName The name of the table which the results came from.
	 */
	public QueryResults(List<Map<String, Object>> resultset, String tableName) {
		results = resultset;
		table = tableName;
	}
	
	/**
	 * @return The result set from this {@link QueryResults} set in the form of a list of mappings of strings by name to objects provided by each row in the result set of a MySQL query.
	 */
	public List<Map<String, Object>> getResults() {
		return results;
	}

	/**
	 * @return The table from which the result set of this {@link QueryResults} set was pulled.
	 */
	public String getTableName() {
		return table;
	}

	/**
	 * @return An integer representing the number of rows returned by this a MySQL query.
	 */
	public int getRowCount() {
		return results.size();
	}
	
	/**
	 * @return An integer representing the number of columns per row returned by a MySQL query.
	 */
	public int getColumnsPerRow() {
		if(results.size()>0) {
			return results.get(0).size();
		}
		return 0;
	}
	
	/**
	 * @return An integer representing the count of individual results returned by a MySQL query.
	 */
	public int getTotalResults() {
		return getRowCount() * getColumnsPerRow();
	}
	
	public String getColumnName(int columnNumber) {
		if(results.size()>0&&results.get(0).size()>columnNumber-1) {
			return (String) results.get(0).keySet().toArray()[columnNumber];
		}
		return null;
	}
	
	/**
	 * Provides the name of the object type in a column of the results returned by a MySQL query.
	 * @param columnNumber The column from which to reveal an object type.
	 * @return A string representing an object type.
	 */
	public String getColumnType(int columnNumber) {
		if(results.size()>0&&results.get(0).size()>columnNumber-1) {
			return (String) results.get(0).get(results.get(0).keySet().toArray()[columnNumber]).getClass().getSimpleName();
		}
		return null;
	}
	
	/**
	 * Provides the name of the object type in a column of the results returned by a MySQL query.
	 * @param columnName The column from which to reveal an object type.
	 * @return A string representing an object type.
	 */
	public String getColumnType(String columnName) {
		if(results.size()>0&&results.get(0).size()>0) {
			return (String) results.get(0).get(columnName).getClass().getSimpleName();
		}
		return null;
	}

}