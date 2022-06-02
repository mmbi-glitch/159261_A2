package com.mystudio.mystforest.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Scenes.HUD;
import com.mystudio.mystforest.Sprites.Enemies.Enemy;
import com.mystudio.mystforest.Sprites.Items.Item;
import com.mystudio.mystforest.Sprites.Player;
import com.mystudio.mystforest.Tools.Box2DWorldCreator;
import com.mystudio.mystforest.Tools.WorldContactListener;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

public class InGameScreen extends BasicGameScreen {
    public static int ID = 4;
    // screen variables
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private HUD hud;
    private final float lerp = 0.8f;

    // tiled map variables
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Box2D variables
    private World world;
    private Box2DWorldCreator creator;
    private Box2DDebugRenderer b2dr;

    // sprite + texture objects
    private Player player;
    private TextureAtlas playerAtlas;
    private TextureAtlas mushroomAtlas;
    private TextureAtlas coinAtlas;

    // asset management
    private AssetManager manager;
    private Music music;

    // --------------- constructor -------------------- //
    public InGameScreen(AssetManager manager) {
        this.manager = manager;
    }


    // ------------------- getters ------------------------------ //

    public int getId() {
        return ID;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public AssetManager getManager() {
        return manager;
    }

    public TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }

    public TextureAtlas getMushroomAtlas() {
        return mushroomAtlas;
    }

    public TextureAtlas getCoinAtlas() {
        return coinAtlas;
    }

    // ------------------------------screen related methods-----------------------//

    public void initialise(GameContainer gc) {
        // create cam used to follow character
        gameCam = new OrthographicCamera();

        // create a FitViewport to main virtual aspect ratio in different screens
        gamePort = new FitViewport(MystForest.V_WIDTH / MystForest.PPM, MystForest.V_HEIGHT / MystForest.PPM, gameCam);

        // update the viewport initially with the original screen height and width
        gamePort.update(MystForest.V_WIDTH, MystForest.V_HEIGHT);

        // load texture packs
        playerAtlas = new TextureAtlas("sprites/Hero_Sprites.pack");
        mushroomAtlas = new TextureAtlas("sprites/Mushroom_Sprites.pack");
        coinAtlas = new TextureAtlas("sprites/Coin_Sprites.pack");
    }

    // move our player using linear impulses
    public void handleKeyInputs(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && player.jumps() < Player.MAXIMUM_JUMPS) {
            player.b2body.applyLinearImpulse(new Vector2(0, 150f*delta), player.b2body.getWorldCenter(), true); // wake it up
            player.incrementJumps();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.jumps() < Player.MAXIMUM_JUMPS) {
            player.b2body.applyLinearImpulse(new Vector2(0, 150f*delta), player.b2body.getWorldCenter(), true); // wake it up
            player.incrementJumps();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 1) {
            player.b2body.applyLinearImpulse(new Vector2(3f*delta, 0), player.b2body.getWorldCenter(), true); // wake it up
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 1) {
            player.b2body.applyLinearImpulse(new Vector2(3f*delta, 0), player.b2body.getWorldCenter(), true); // wake it up
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -1) {
            player.b2body.applyLinearImpulse(new Vector2(-3f*delta, 0), player.b2body.getWorldCenter(), true); // wake it up
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -1) {
            player.b2body.applyLinearImpulse(new Vector2(-3f*delta, 0), player.b2body.getWorldCenter(), true); // wake it up
        }
    }

    public void update(GameContainer gc, ScreenManager screenManager, float delta) {

        // handle inputs
        handleKeyInputs(delta);

        // takes 1 step in the physics simulation
        world.step(1f/60f, 6, 2);

        // update player's position
        player.update(delta);

        // update mushrooms' position

        for (Enemy enemy: creator.getMushrooms()) {
            enemy.update(delta);
        }

        // update coins
        for (Item item: creator.getCoins()) {
            item.update(delta);
        }
        // attach the gameCam to player's x-coordinate (add a bit of linear interpolation as well)
        gameCam.position.x += (player.b2body.getPosition().x - gameCam.position.x) * lerp * delta;

        // clamp position of gameCam to world edges
        if (gameCam.position.x < gamePort.getWorldWidth()/2) {
            gameCam.position.x = gamePort.getWorldWidth()/2;
        }
        if (gameCam.position.x > gamePort.getWorldWidth() * 10 - gamePort.getWorldWidth()/2) {
            gameCam.position.x = gamePort.getWorldWidth() * 10 - gamePort.getWorldWidth()/2;
        }

        // update the hud
        hud.update(delta);

        // update gameCam with correct coordinates after changes
        gameCam.update();

        // tell renderer to draw only what the camera sees
        mapRenderer.setView(gameCam);

        // go to game end screen if game is over
        if (gameOver()) {
            GameEndScreen.statusMessage = "You Lost!";
            screenManager.enterGameScreen(GameEndScreen.ID, new FadeOutTransition(), new FadeInTransition());
        }
        if (levelUp()) {
            GameEndScreen.statusMessage = "You Won!";
            GameEndScreen.endMessage = "Final Score: " + String.format("%05d", HUD.getScore());
            screenManager.enterGameScreen(GameEndScreen.ID, new FadeOutTransition(), new FadeInTransition());
        }
    }

    public boolean gameOver() {
        return player.hasDied() && player.getStateTimer() > 3;
    }

    public boolean levelUp() { return player.hasReachedDoor() && player.getStateTimer() > 3;}


    public void interpolate(GameContainer gc, float alpha) { }

    public void render(GameContainer gc, Graphics g) {
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render game map
        mapRenderer.render();

        // render our box2d debug lines
//        b2dr.render(world, gameCam.combined);

        // draw our sprites here
        MystForest.batch.setProjectionMatrix(gameCam.combined);
        MystForest.batch.begin();
        player.draw(MystForest.batch);
        for (Enemy enemy: creator.getMushrooms()) {
            enemy.draw(MystForest.batch);
        }
        for (Item item: creator.getCoins()) {
            item.draw(MystForest.batch);
        }
        MystForest.batch.end();

        // set cam to hud and then draw it
        MystForest.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        // load map and set up map renderer
        map = manager.get("maps/level1.tmx", TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MystForest.PPM);
        // create hud for time/score/etc info
        hud = new HUD(MystForest.batch);
        // set cam to be initially centered correctly in our game world
        gameCam.position.set((gamePort.getWorldWidth()) / 2f, gamePort.getWorldHeight() / 2f, 0);
        // world stuff
        world = new World(new Vector2(0, -5), true);
        b2dr = new Box2DDebugRenderer();
        // set up the Box2D game world
        creator = new Box2DWorldCreator(this);
        // create a new player in the game world
        player = new Player(this, manager);
        world.setContactListener(new WorldContactListener(this));
        // play music
        music = manager.get("audio/music/game_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
        // reset game end messages (not the best way of doing it)
        GameEndScreen.statusMessage = "";
        GameEndScreen.endMessage = "";
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onResize(int width, int height) {
        gamePort.update(width, height);
    }

}