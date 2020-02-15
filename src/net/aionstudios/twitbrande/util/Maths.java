package net.aionstudios.twitbrande.util;

public class Maths {
	
	public static float computePositivityPercentage(int positivityScore, int negativityScore) {
		/*
		 * These weights are the solution to the following matrix:
		 * 
		 * [5 -1 | 1]
		 * [1 -5 | 0]
		 * 
		 * 1/-1 mean no positivity / negativity respectively and
		 * 5/-5 the maximum of each.
		 * This confines the values to a float between 0.0f and 1.0f
		 */
		return (float) ((float) positivityScore*5.0f/24.0f) + ((float) negativityScore*1.0f/24.0f);
	}

}
