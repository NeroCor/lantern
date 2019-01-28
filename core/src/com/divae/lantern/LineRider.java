package com.divae.lantern;

import com.badlogic.gdx.Game;
import com.divae.lantern.Screens.GameScreen;

public class LineRider extends Game {

	@Override
	public void create() {
		this.setScreen(new GameScreen(this));
//		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

//	public void dispose() {
//		skin.dispose();
//	}

}
