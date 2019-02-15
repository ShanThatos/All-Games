package coltexpress.card;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import coltexpress.round.RoundPart;

public class RoundCard {
	
	// Holds all Round Cards
	private static RoundPart[][] parts;
	private static String[] specialCards;
	public static boolean[] cardUsed;
	public static int numCards;
	// Index for specific Round Card & Special Moves
	public int cardNum;
	public int currentRoundPart;
	
	public RoundCard(int cardID) {
		cardNum = cardID;
		currentRoundPart = 0;
		cardUsed[cardID] = true;
	}
	
	public RoundPart getFirstRoundPart() {
		return parts[cardNum][0];
	}
	public RoundPart nextRoundPart() {
		currentRoundPart++;
		if (currentRoundPart >= parts[cardNum].length)
			return null;
		return parts[cardNum][currentRoundPart];
	}
	
	public static void readAllParts() throws IOException {
		Scanner in = new Scanner(new File("Files/Cards/allRoundCards.dat"));
		numCards = in.nextInt();
		parts = new RoundPart[numCards][];
		specialCards = new String[numCards];
		cardUsed = new boolean[numCards];
		for (int i = 0; i < numCards; i++) {
			specialCards[i] = in.next().trim();
			parts[i] = new RoundPart[in.nextInt()];
			for (int j = 0; j < parts[i].length; j++) {
				String part = in.next().trim();
				parts[i][j] = new RoundPart(part.contains("U"), part.length() == 2, part.contains("R"));
			}
			in.nextLine();
		}
		in.close();
	}
}
