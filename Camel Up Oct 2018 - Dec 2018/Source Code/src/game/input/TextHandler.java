package game.input;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import game.board.Camel;
import game.board.CamelStack;
import game.board.GameManager;
import game.board.Race;
import game.player.Player;
import game.tiles.DesertTile;
import game.tiles.LegBetTile;
import game.tiles.LegBetTileStack;
import game.tiles.RaceBetTile;

public class TextHandler implements InputHandler {
	
	private Scanner in;
	
	public TextHandler() {
		in = new Scanner(System.in);
	}
	
	@Override
	public void displayPlayerStartTurn(GameManager gm) {
		System.out.println("Player " + (gm.getCurrentPlayer().getID() + 1) + "'s Turn: \n");
	}
	@Override
	public int requestTypeOfTurn(GameManager gm) {
		int choice = 0;
		
		System.out.println("Pick your play:");
		int index = 1;
		TreeMap<Integer, String> types = new TreeMap<>();
		
		System.out.println(index + ". \tPick up a Leg Betting Tile");
		types.put(index++, "LegBetTile");
		
		System.out.println(index + ". \tPlace/Move Desert Tile");
		types.put(index++, "DesertTile");
		
		if (gm.getGameBoard().getPyrTiles().size() > 0) {
			System.out.println(index + ". \tPick up a Pyramid Tile");
			types.put(index++, "PyramidTile");
		}
		
		if (gm.getCurrentPlayer().getRaceBets().size() > 0) {
			System.out.println(index + ". \tBet on a Camel");
			types.put(index++, "RaceBetTile");
		}
		
		System.out.println("\nYour choice: ");
		choice = in.nextInt();
		
		while (choice < 1 || choice >= index) {
			System.out.println("Pick a valid choice: ");
			choice = in.nextInt();
		}
		
		switch (types.get(choice)) {
		case "LegBetTile":
			return 1;
		case "DesertTile":
			return 2;
		case "PyramidTile":
			return 3;
		case "RaceBetTile":
			return 4;
		}
		return -1;
	}
	@Override
	public LegBetTile requestLegBettingTile(GameManager gm) {
		LegBetTileStack[] legBetStacks = gm.getGameBoard().getLegBetsStacks();
		Race race = gm.getRace();
		
		ArrayList<LegBetTile> legs = new ArrayList<>();
		for (int i = 0; i < legBetStacks.length; i++) {
			LegBetTile top = legBetStacks[i].seeTop();
			if (top == null)
				continue;
			legs.add(top);
		}
		
		int choice = 0;
		
		while (choice < 1 || choice > legs.size()) {
			System.out.println("Pick from these available Leg Betting Tiles:");
			for (int i = 0; i < legs.size(); i++) {
				LegBetTile top = legs.get(i);
				System.out.println(i + 1 + ". \t" + race.getCamelByID(top.getCamelID()).getCamelColorString() + " Camel, " + top.getValue());
			}
			System.out.println();
			System.out.println("Your choice: ");
			choice = in.nextInt();
		}
		
		return legs.get(choice - 1);
	}
	@Override
	public Object[] requestDesertTilePlacement(GameManager gm) {
		TreeSet<Integer> possibleSpots = new TreeSet<>();
		for (int i = 0; i < 16; i++) 
			possibleSpots.add(i);
		
		CamelStack[] track = gm.getRace().getFullTrack();
		for (int i = 0; i < track.length; i++) 
			if (track[i].getNumberOfCamels() > 0) 
				possibleSpots.remove(i);
		
		ArrayList<DesertTile> dst = Player.getAllDesertTiles();
		for (int i = 0; i < dst.size(); i++) {
			DesertTile dt = dst.get(i);
			if (dt.getPlayerID() == gm.getCurrentPlayer().getID())
				continue;
			if (dt.isOnBoard()) {
				possibleSpots.remove(dt.getTileNumber() + 1);
				possibleSpots.remove(dt.getTileNumber());
				possibleSpots.remove(dt.getTileNumber() - 1);
			}
		}
		
		System.out.println("Pick from these Available Spots to place down your Desert Tile:");
		int w = 1;
		for (int spot : possibleSpots) {
			System.out.print(spot + 1 + (w % 3 == 0 ? "\n":"\t"));
			w++;
		}
		System.out.println("\nYour choice: ");
		int spotChoice = in.nextInt();
		
		while (!possibleSpots.contains(spotChoice - 1)) {
			System.out.println("Please choose a valid Spot: ");
			spotChoice = in.nextInt();
			System.out.println();
		}
		
		System.out.println("\nWould you like to place your Desert Tile Oasis Up or Mirage Up? ");
		System.out.println("1. \tOasis Up");
		System.out.println("2. \tMirage Up");
		System.out.println("\nYour choice: ");
		int placementChoice = in.nextInt();
		
		while (placementChoice < 1 || placementChoice > 2) {
			System.out.println("Please choose a valid choice: ");
			placementChoice = in.nextInt();
			System.out.println();
		}
		
		
		Object[] ret = new Object[2];
		ret[0] = spotChoice - 1;
		ret[1] = placementChoice == 1;
		return ret;
	}
	@Override
	public Object[] requestRaceBetType(GameManager gm) {
		ArrayList<RaceBetTile> raceBets = gm.getCurrentPlayer().getRaceBets();
		System.out.println("Which Camel would you like to bet on?");
		for (int i = 0; i < raceBets.size(); i++) 
			System.out.println(i + 1 + ". \t" + Camel.getColors()[raceBets.get(i).getCamelID()] + " Camel");
		int camelChoice = in.nextInt();
		while (camelChoice < 1 || camelChoice > raceBets.size()) {
			System.out.println("Choose a valid Camel: ");
			camelChoice = in.nextInt();
			System.out.println();
		}
		camelChoice--;
		
		System.out.println("Would you like to place your Bet in the Winner Betting Stack or the Loser Betting Stack? ");
		System.out.println("1. \tWinner Stack");
		System.out.println("2. \tLoser Stack");
		int stackChoice = in.nextInt();
		
		while (stackChoice < 1 || stackChoice > 2) {
			System.out.println("Choose a valid Stack: ");
			stackChoice = in.nextInt();
			System.out.println();
		}
		System.out.println();
		
		Object[] ret = new Object[2];
		ret[0] = raceBets.get(camelChoice);
		ret[1] = stackChoice == 1;
		return ret;
	}

	@Override
	public void displayRoundEnd(GameManager gm) {
		// TODO Auto-generated method stub
		
	}
}
