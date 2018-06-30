package me.theeninja.islandroyale.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.theeninja.islandroyale.IslandRoyaleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;

		config.fullscreen = true;
		config.vSyncEnabled = true;

		IslandRoyaleGame islandRoyaleGame = new IslandRoyaleGame();

		new LwjglApplication(islandRoyaleGame, config);
	}
}