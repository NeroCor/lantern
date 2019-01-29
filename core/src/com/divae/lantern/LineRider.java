package com.divae.lantern;

import com.badlogic.gdx.Game;
import com.divae.lantern.Screens.GameScreen;
import com.divae.lantern.util.Pair;

import java.util.List;

public class LineRider extends Game {

    private GameScreen gameScreen;

    @Override
	public void create() {
        gameScreen = new GameScreen(this);
        this.setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render();
	}

    public void onExtracted(int width, int height, List<Pair<Integer, Integer>> coords) {
        gameScreen.onExtracted(width, height, coords);
    }

//	public void dispose() {
//		skin.dispose();
//	}

}
