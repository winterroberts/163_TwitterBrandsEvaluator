package net.aionstudios.twitbrande.sentiment;

import net.aionstudios.twitbrande.util.Maths;
import uk.ac.wlv.sentistrength.SentiStrength;

public class Evaluator {

	private static Evaluator self;
	private SentiStrength sentiStrength;
	
	private Evaluator() {
		sentiStrength = new SentiStrength();
		String ssthInitialisation[] = {"sentidata", "ss/"};
		sentiStrength.initialise(ssthInitialisation);
	}
	
	public float sentiment(String sentence) {
		String[] results = sentiStrength.computeSentimentScores(sentence).split("\\s+");
		int positivity = Integer.parseInt(results[0]);
		int negativity = Integer.parseInt(results[1]);
		return Maths.computePositivityPercentage(positivity, negativity);
	}
	
	public static Evaluator getInstance() {
		if(self==null) {
			self = new Evaluator();
		}
		return self;
	}
}
