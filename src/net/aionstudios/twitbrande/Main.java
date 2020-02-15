package net.aionstudios.twitbrande;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

import net.aionstudios.aionlog.AnsiOut;
import net.aionstudios.aionlog.Logger;
import net.aionstudios.aionlog.StandardOverride;
import net.aionstudios.twitbrande.sentiment.Accumulator;

public class Main {
	
	public static void main(String[] args) {
		File f = new File("./logs/");
		f.mkdirs();
		Logger.setup();
		AnsiOut.initialize();
		AnsiOut.setStreamPrefix("Twitter Brands Evaluator");
		StandardOverride.enableOverride();
		try {
			TwitterBrandsInfo.readConfigsAtStart();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TwitterBrandsInfo.setupDB();
		Accumulator.getInstance().accumulateAll();
	}

}