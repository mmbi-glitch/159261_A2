package com.mystudio.mystforest;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mystudio.mystforest.Screens.*;
import org.mini2Dx.core.game.ScreenBasedGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

public class MystForest extends ScreenBasedGame {
    public static final String GAME_IDENTIFIER = "com.mystudio.mystforest";
    // global viewport/world variables
    public static final int V_WIDTH = 384; // viewport width = 10% of map's width
    public static final int V_HEIGHT = 208; // viewport height = 100% of map's height
    public static final float PPM = 100; // 100 pixels per meter

    // global filter variables
    public static short NOTHING_BIT = 0; // possibly might need in future
    public static short GROUND_BIT = 1;
    public static short GROUND_BOUNDS_BIT = 2;
    public static short DOOR_BIT = 4;
    public static short COIN_BIT = 8;
    public static short DESTROYED_BIT = 16; // possibly might need in future
    public static short PLAYER_BIT = 32;
    public static short ENEMY_BIT = 64;
    public static short ENEMY_HEAD_BIT = 128;

    // global spritebatch
    public static SpriteBatch batch;

    // NOT global asset manager (passing this around everywhere)
    private AssetManager manager;

    @Override
    public void initialise() {

        // init sprite batch
        batch = new SpriteBatch();

        // init asset manager and load all assets
        manager = new AssetManager();
        manager.load("audio/music/game_music.ogg", Music.class);
        manager.load("audio/music/menu_music.ogg", Music.class);
        manager.load("audio/sounds/coin_collect.wav", Sound.class);
        manager.load("audio/sounds/jump.mp3", Sound.class);
        manager.load("audio/sounds/take_hit.wav", Sound.class);
        manager.load("audio/sounds/stomp_enemy.wav", Sound.class);
        manager.load("audio/sounds/player_die.wav", Sound.class);
        manager.load("audio/sounds/player_level_up.wav", Sound.class);
        manager.load("skins/rainbowui/rainbow-ui.json", Skin.class);
        manager.load("sprites/background-darker.jpg", Texture.class);
        manager.load("sprites/background-faded.jpg", Texture.class);
        manager.load("sprites/table.png", Texture.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("maps/level1.tmx", TiledMap.class);
        manager.finishLoading();

        // add all the different screens (remember to pass in the manager!)
        addScreen(new LoadingScreen(manager));
        addScreen(new InGameScreen(manager));
        addScreen(new GameEndScreen(manager, getScreenManager()));
        addScreen(new MenuScreen(manager, getScreenManager()));
        addScreen(new AboutScreen(manager, getScreenManager()));

        // finally, enter the loading screen
        enterGameScreen(getInitialScreenId(), new FadeOutTransition(), new FadeInTransition());
    }

    @Override
    public int getInitialScreenId() {
        return LoadingScreen.ID;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void interpolate(float alpha) {}

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
