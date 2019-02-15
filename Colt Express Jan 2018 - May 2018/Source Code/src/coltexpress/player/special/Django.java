package coltexpress.player.special;

import java.awt.Point;

import coltexpress.audio.AudioManager;
import coltexpress.game.GameManager;
import coltexpress.player.Animator;
import coltexpress.player.Player;
import coltexpress.train.Train;
import coltexpress.ui.CharacterButton;

public class Django extends Player {
	
	public Django(int ctc, int playerID) {
		super(ctc, playerID, 2);
	}
	
	protected void shoot2(CharacterButton cb, GameManager gm) {
		int playerShot = -1;
		animator.animating = true;
		animator.currentAnimation = 4;
		animator.vector2 = new Point(0, 0);
		Animator.allAnimations[animator.playerAnimID][animator.currentAnimation].currentTime = 1;
		for (int i = 0; i < gm.plys.length; i++) {
			if (gm.plys[i].animPlayerID == cb.animButtonID) {
				playerShot = i;
			}
		}
		
		animator.flipped = (gm.plys[playerShot].animator.origPoint.x < animator.origPoint.x);
		
		AudioManager.playAudioNTimes("fire", 0.5, 4, 200);
		
		while (animator.animating) {
			try {Thread.sleep(10);} catch (Exception e) {}
		}
		
		gm.plys[playerShot].deck.add(bullets.remove(0));
		if (gm.plys[playerShot].currentTrainCar < currentTrainCar) {
			gm.plys[playerShot].currentTrainCar = Math.max(gm.plys[playerShot].currentTrainCar - 1, 0);
		} else {
			gm.plys[playerShot].currentTrainCar = Math.min(gm.plys[playerShot].currentTrainCar + 1, Train.NUM_TRAIN_CARS - 1);
		}
	}
}
