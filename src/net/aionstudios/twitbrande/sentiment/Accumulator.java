package net.aionstudios.twitbrande.sentiment;

import java.util.Map;

import net.aionstudios.twitbrande.database.QueryResults;
import net.aionstudios.twitbrande.util.DatabaseUtils;

public class Accumulator {
	
	private static Accumulator self;

	private Accumulator() {
		
	}
	
	private String brandsQuery = "SELECT DISTINCT(`symbol`) FROM `twitbrands`.`stock_volume`;";
	private String stockVolumesQuery = "SELECT `matchingID`,`stock_price_current`,`tweet_volume`,`unix_timestamp`,`currency` FROM `twitbrands`.`stock_volume` WHERE `symbol` = ?;";
	private String tweetsMatchingQuery = "SELECT `tweet` FROM `twitbrands`.`tweets` WHERE `matchingID` = ?;";
	
	private String accumulateInsertQuery = "INSERT INTO `twitbrands`.`accumulate_tb`\r\n" + 
			"(`matchingID`,\r\n" +
			"`stock_symbol`,\r\n" + 
			"`stock_price_last`,\r\n" + 
			"`stock_price`,\r\n" + 
			"`tweet_volume`,\r\n" + 
			"`unix_diff`,\r\n" + 
			"`currency`,\r\n" + 
			"`positivity_percentage`)\r\n" + 
			"VALUES\r\n" + 
			"(?,\r\n" + 
			"?,\r\n" + 
			"?,\r\n" + 
			"?,\r\n" + 
			"?,\r\n" + 
			"?,\r\n" + 
			"?,\r\n" +
			"?);";
	
	public void accumulateAll() {
		long startTime = System.currentTimeMillis();
		long outputTableRows = 0;
		System.out.println("Accumulate starting...");
		QueryResults qr = DatabaseUtils.prepareAndExecute(brandsQuery, true).get(0);
		int countbrands = qr.getRowCount();
		int brandOutput = 0;
		System.out.println("Periods: "+countbrands);
		for(Map<String, Object> brand : qr.getResults()) {
			brandOutput++;
			String symbol = (String) brand.get("symbol");
			double lastPrice = -1.0;
			long unixLast = 0;
			System.out.println("Accumulating symbol '"+symbol+"' "+brandOutput+"/"+countbrands);
			int resSymbol = 0;
			for(Map<String, Object> sv : DatabaseUtils.prepareAndExecute(stockVolumesQuery, true, symbol).get(0).getResults()) {
				if(lastPrice<0) {
					lastPrice = (Double) sv.get("stock_price_current");
					unixLast = (Long) Long.parseLong((String) sv.get("unix_timestamp"));
					continue;
				}
				resSymbol++;
				int totalTweets = 0;
				float positivitySum = 0.0f;
				String matchingID = (String) sv.get("matchingID");
				for(Map<String, Object> tm : DatabaseUtils.prepareAndExecute(tweetsMatchingQuery, true, matchingID).get(0).getResults()) {
					totalTweets++;
					positivitySum += Evaluator.getInstance().sentiment((String) tm.get("tweet"));
				}
				float positivityAverage = (float) positivitySum / totalTweets;
				double priceCurrent = (Double) sv.get("stock_price_current");
				long unixC = (Long) Long.parseLong((String) sv.get("unix_timestamp"));
				DatabaseUtils.prepareAndExecute(accumulateInsertQuery, true, matchingID, symbol, lastPrice, priceCurrent,
						sv.get("tweet_volume"), unixC-unixLast, sv.get("currency"), positivityAverage);
				lastPrice = priceCurrent;
				unixLast = unixC;
				outputTableRows++;
			}
			System.out.println("Outputs: "+resSymbol);
		}
		System.out.println("Accumulate completed! "+((double)(System.currentTimeMillis()-startTime)/1000)+" seconds");
		System.out.println("Output contains "+outputTableRows+" rows.");
	}
	
	public static Accumulator getInstance() {
		if(self==null) {
			self = new Accumulator();
		}
		return self;
	}
	
}
