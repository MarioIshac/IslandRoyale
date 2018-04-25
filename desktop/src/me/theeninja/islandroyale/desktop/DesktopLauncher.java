package me.theeninja.islandroyale.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.theeninja.islandroyale.IslandRoyaleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.fullscreen = true;

		IslandRoyaleGame islandRoyaleGame = new IslandRoyaleGame();

		config.vSyncEnabled = true;

		new LwjglApplication(islandRoyaleGame, config);
	}
}
