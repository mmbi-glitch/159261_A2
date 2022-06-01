package com.mystudio.mystforest.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import com.mystudio.mystforest.MystForest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(MystForest.GAME_IDENTIFIER);
		config.title = "Mystic Forest";
		config.backgroundFPS = -1;
		config.foregroundFPS = 60;
		config.width = 768;
		config.height = 418;
//		config.resizable = false;
		config.pauseWhenMinimized = true;
		config.pauseWhenBackground = true;
		config.vSyncEnabled = true;
		config.forceExit = false;
		new DesktopMini2DxGame(new MystForest(), config);
	}
}
