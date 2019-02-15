package coltexpress.player.special;

import java.util.ArrayList;

import coltexpress.game.GameManager;
import coltexpress.player.Player;
import coltexpress.ui.CharacterButton;

public class Tuco extends Player {
	
	public Tuco(int ctc, int playerID) {
		super(ctc, playerID, 5);
	}
	
	protected void shoot(GameManager gm) {
		ArrayList<Integer> availablePlayers = null;
		if (onTopOfTrain) {
			availablePlayers = gm.getAllPlayerOnTrainForShoot(currentTrainCar);
		} else {
			availablePlayers = gm.getAllPlayersInTrainCar(currentTrainCar - 1);
			availablePlayers.addAll(gm.getAllPlayersInTrainCar(currentTrainCar + 1));
		}
		
		for (int i = availablePlayers.size() - 1; i >= 0; i--) {
			if (gm.plys[availablePlayers.get(i)].onTopOfTrain ^ onTopOfTrain) {
				availablePlayers.remove(i);
			}
		}
		
		ArrayList<Integer> hisTrainCar = gm.getAllPlayersInTrainCar(currentTrainCar);
		for (int i = hisTrainCar.size() - 1; i >= 0; i--) {
			if (gm.plys[hisTrainCar.get(i)].onTopOfTrain == onTopOfTrain) {
				hisTrainCar.remove(i);
			}
		}
		availablePlayers.addAll(hisTrainCar);
		
		// Belle
		if (availablePlayers.size() > 1) {
			for (int i = availablePlayers.size() - 1; i >= 0; i--) {
				if (gm.plys[availablePlayers.get(i)].animPlayerID == 0) {
					availablePlayers.remove(i);
				}
			}
		}
		
		if (!availablePlayers.isEmpty()) {
			int xx = 300, yy = 150;
			int width = 100, height = 100;
			for (int id : availablePlayers) {
				if (id == playerID)
					continue;
				chsButtons.add(new CharacterButton(xx, yy, gm.plys[id].animPlayerID));
				chsButtons.get(chsButtons.size() - 1).setDimensions(width, height);
				xx += width + 10;
			}
		}
		ActionDoing = "shoot";
	}
}
