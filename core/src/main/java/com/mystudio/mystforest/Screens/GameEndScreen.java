package com.mystudio.mystforest.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mystudio.mystforest.MystForest;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

public class GameEndScreen extends BasicGameScreen implements InputProcessor {
    public static int ID = 5;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Texture texture;
    private TextButton replayBtn;
    private TextButton menuBtn;
    Label.LabelStyle style;
    public static String statusMessage = "";
    public static String endMessage = "";
    Label endStatusLabel;
    Label endMessageLabel;

    private AssetManager manager;
    ScreenManager<GameScreen> screenManager;

    // --------------------- constructor ----------------------------------- //

    public GameEndScreen(AssetManager manager, ScreenManager<GameScreen> screenManager) {
        this.screenManager = screenManager;
        this.manager = manager;
    }

    // ------------------------------screen related methods-----------------------//

    @Override
    public void initialise(GameContainer gc) {
        viewport = new FitViewport(MystForest.V_WIDTH, MystForest.V_HEIGHT, new OrthographicCamera());
        viewport.update(MystForest.V_WIDTH, MystForest.V_HEIGHT);
        stage = new Stage(viewport, MystForest.batch);
        texture = new Texture("sprites/background-faded.jpg");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quinquefive.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        style = new Label.LabelStyle(font, Color.CYAN);
        Table table = new Table();
        table.center(); // align stuff to center
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
        skin = manager.get("skins/rainbowui/rainbow-ui.json", Skin.class);
        endStatusLabel = new Label("", style);
        table.add(endStatusLabel).expandX().padTop(10).padBottom(20);
        table.row();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        style = new Label.LabelStyle(font, Color.WHITE);
        endMessageLabel = new Label("", style);
        table.add(endMessageLabel).padBottom(10);
        table.row();
        replayBtn = new TextButton("Replay", skin, "small-toggle");
        replayBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.enterGameScreen(InGameScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
        });
        table.add(replayBtn).uniformX().padTop(10);
        table.row();
        menuBtn = new TextButton("Menu", skin, "small-toggle");
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
        });
        table.add(menuBtn).uniformX().padTop(10);
        stage.addActor(table);
        generator.dispose();
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {

    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // draw stage
        stage.draw();
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        // future edit: need to implement a multiplexer to capture both keyboard and stage inputs
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        replayBtn.setChecked(false);
        menuBtn.setChecked(false);
        endStatusLabel.setText(statusMessage);
        endMessageLabel.setText(endMessage);
    }

    @Override
    public void preTransitionOut(Transition transitionOut) {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void onResize(int width, int height) {
        viewport.update(width, height, true);
    }

    // ------------------------------handling input-----------------------//

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            checkButtons();
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            checkButtons();
        }
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE) {
            screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(), new FadeInTransition());
        }
        if (keycode == Input.Keys.ENTER) {
            if (replayBtn.isChecked()) {
                screenManager.enterGameScreen(InGameScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
            if (menuBtn.isChecked()) {
                screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
        }
        return false;
    }

    public void checkButtons() {
        if (!replayBtn.isChecked() && menuBtn.isChecked()) {
            replayBtn.setChecked(true);
            menuBtn.setChecked(false);
        }
        else if (!menuBtn.isChecked() && replayBtn.isChecked()) {
            replayBtn.setChecked(false);
            menuBtn.setChecked(true);
        }
        else {
            replayBtn.setChecked(true);
        }
    }

    // these methods not used (but must still be implemented)
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
