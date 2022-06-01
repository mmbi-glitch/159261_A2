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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

public class MenuScreen extends BasicGameScreen implements InputProcessor {

    public static int ID = 2;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Texture texture;
    private TextButton playBtn;
    private TextButton aboutBtn;
    private TextButton exitBtn;
    Label.LabelStyle style;
    Label titleLabel;

    private AssetManager manager;
    ScreenManager<GameScreen> screenManager;

    public MenuScreen(AssetManager manager, ScreenManager<GameScreen> screenManager) {
        this.screenManager = screenManager;
        this.manager = manager;
    }

    @Override
    public void initialise(GameContainer gc) {
        viewport = new FitViewport(MystForest.V_WIDTH, MystForest.V_HEIGHT, new OrthographicCamera());
        viewport.update(MystForest.V_WIDTH, MystForest.V_HEIGHT);
        stage = new Stage(viewport, MystForest.batch);
        texture = manager.get("sprites/background-darker.jpg", Texture.class);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Haberdines.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        style = new Label.LabelStyle(font, Color.CHARTREUSE);
        skin = manager.get("skins/rainbowui/rainbow-ui.json", Skin.class);
        titleLabel = new Label("MYSTIC FOREST", style);
        Table table = new Table();
        table.top(); // align stuff to top
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
        table.add(titleLabel).expandX().padTop(15).padBottom(10);
        table.row();
        playBtn = new TextButton("Play", skin, "small-toggle");
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.enterGameScreen(InGameScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
        });
        table.add(playBtn).uniformX().padTop(5);
        table.row();
        aboutBtn = new TextButton("About", skin, "small-toggle");
        aboutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.enterGameScreen(AboutScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
        });
        table.add(aboutBtn).uniformX().padTop(5);
        table.row();
        exitBtn = new TextButton("Exit", skin, "small-toggle");
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitBtn).uniformX().padTop(5);
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
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // draw stuff
        stage.draw();
    }

    @Override
    public void preTransitionIn(Transition transitionIn) {
        // future edit: need to implement a multiplexer to capture both keyboard and stage inputs
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        playBtn.setChecked(false);
        aboutBtn.setChecked(false);
        exitBtn.setChecked(false);
    }

    @Override
    public void preTransitionOut(Transition transitionOut) {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void onResize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            checkButtonsUp();
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            checkButtonsDown();
        }
        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.BACKSPACE) {
            if (playBtn.isChecked()) {
                screenManager.enterGameScreen(InGameScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
            if (aboutBtn.isChecked()) {
                screenManager.enterGameScreen(AboutScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
            if (exitBtn.isChecked()) {
                Gdx.app.exit();
            }
        }
        return false;
    }

    public void checkButtonsDown() {
        if (playBtn.isChecked() && !aboutBtn.isChecked()) {
            playBtn.setChecked(false);
            aboutBtn.setChecked(true);
        }
        else if (aboutBtn.isChecked() && !exitBtn.isChecked()) {
            aboutBtn.setChecked(false);
            exitBtn.setChecked(true);
        }
        else {
            playBtn.setChecked(true);
            aboutBtn.setChecked(false);
            exitBtn.setChecked(false);
        }
    }

    public void checkButtonsUp() {
        if (exitBtn.isChecked() && !aboutBtn.isChecked()) {
            exitBtn.setChecked(false);
            aboutBtn.setChecked(true);
        }
        else if (aboutBtn.isChecked() && !playBtn.isChecked()) {
            aboutBtn.setChecked(false);
            playBtn.setChecked(true);
        }
        else {
            playBtn.setChecked(false);
            aboutBtn.setChecked(false);
            exitBtn.setChecked(true);
        }
    }

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
