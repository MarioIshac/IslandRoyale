package me.theeninja.islandroyale.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import me.theeninja.islandroyale.IslandRoyaleGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
	    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

	    Graphics.DisplayMode primaryMonitorDisplayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();

		config.useVsync(true);
		config.setFullscreenMode(primaryMonitorDisplayMode);

		IslandRoyaleGame islandRoyaleGame = new IslandRoyaleGame();

		new Lwjgl3Application(islandRoyaleGame, config);
	}
}