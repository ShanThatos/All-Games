package coltexpress.main;

import java.awt.AWTException;
import java.io.IOException;

import coltexpress.audio.AudioManager;
import coltexpress.card.PlayerCard;
import coltexpress.card.RoundCard;
import coltexpress.game.GameFrame;
import coltexpress.game.PlayerInfo;
import coltexpress.loot.Loot;
import coltexpress.player.Animator;
import coltexpress.rules.Rules;
import coltexpress.rules.RulesPanel;
import coltexpress.train.TrainCar;
import coltexpress.ui.CharacterButton;
import coltexpress.ui.cButton;

public class Runner {
	
	public static boolean debugMode = false;
	
	public static GameFrame gf;
	public static Rules rf;
	
	public static void main(String[] args) throws IOException, AWTException {
		readAllInfo();
		
		new AudioManager();
		
		rf = new Rules("Instructions");
		gf = new GameFrame("Colt Express");
	}
	
	private static void readAllInfo() throws IOException {
		// Read in all data necessary to play game
		// Pictures, Cards, Loot, Trains...
		Loot.readAllLootValues();
		RoundCard.readAllParts();
		TrainCar.readAllTrains();
		cButton.readAllButtons();
		CharacterButton.readAllCharacterButtons();
		Animator.readAllAnimations();
		PlayerCard.readAllIcons();
		PlayerInfo.readAllInfo();
		AudioManager.readAllAudio();
		RulesPanel.readAllRules();
	}
}


/* Things left to add in the end: 
	* Add in player info topRight for what loot each person has
	* Fix "Marshal" button
	* Finish Ending screen
	* Audio?
	* Beginning Window??
*/

/*
	* Special Player Stuff:
	* 
	* Done	Ghost has invisible card for first turn of each round
	* Done	Belle can't be a target unless she's is the only available target
	* Done, In testing???	Cheyenne steals when punching
	* Done	Tuco can shoot upwards & downwards
	* Done	Django shooting makes people fly back
	* Done 	Doc has 7 cards in beginning of each round
*/