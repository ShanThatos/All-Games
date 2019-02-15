package coltexpress.player.special;

import java.awt.Point;

import coltexpress.audio.AudioManager;
import coltexpress.game.GameManager;
import coltexpress.loot.Loot;
import coltexpress.player.Animator;
import coltexpress.player.Player;
import coltexpress.ui.cButton;

public class Cheyenne extends Player {
	
	public Cheyenne(int ctc, int playerID) {
		super(ctc, playerID, 1);
	}
	
	protected void punch4(cButton lootPicked, GameManager gm) {
//		System.out.println("CHEYENNE PUNCH");
		animator.animating = true;
		animator.currentAnimation = 2;
		animator.vector2 = new Point(0, 0);
		Animator.allAnimations[animator.playerAnimID][animator.currentAnimation].currentTime = 1;
		int playerPunched = -1;
		for (int i = 0; i < gm.plys.length; i++) {
			if (gm.plys[i].animPlayerID == punchedPerson.animButtonID) {
				playerPunched = i;
			}
		}
		animator.flipped = (gm.plys[playerPunched].animator.origPoint.x < animator.origPoint.x);
		
		AudioManager.playAudioNTimes("punch", 0.5, 4, 200);
		
		while (animator.animating) {
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		gm.plys[playerPunched].currentTrainCar = dirPunched.trainPointingTo;
		
		for (int i = 0; i < gm.allLoot.size(); i++) {
			if (!gm.allLoot.get(i).inTrainCar && gm.allLoot.get(i).animPlayerID == punchedPerson.animButtonID && gm.allLoot.get(i).getRowNum() == lootPicked.lootType) {
				Loot l = gm.allLoot.get(i);
				l.animPlayerID = animPlayerID;
				break;
			}
		}
		
		punchedPerson = null;
		dirPunched = null;
		ActionDoing = "punch4";
	}
}
