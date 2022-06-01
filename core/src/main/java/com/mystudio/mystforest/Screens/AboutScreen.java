package com.mystudio.mystforest.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
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

public class AboutScreen extends BasicGameScreen implements InputProcessor {

    public static int ID = 3;
    private TextButton backBtn;
    private Skin skin;
    Label titleLabel;
    private Viewport viewport;
    private Stage stage;
    private Texture texture;
    private AssetManager manager;
    private ScreenManager<GameScreen> screenManager;

    public AboutScreen(AssetManager manager, ScreenManager<GameScreen> screenManager) {
        this.manager = manager;
        this.screenManager = screenManager;
    }

    @Override
    public void initialise(GameContainer gc) {
        viewport = new FitViewport(MystForest.V_WIDTH, MystForest.V_HEIGHT, new OrthographicCamera());
        viewport.update(MystForest.V_WIDTH, MystForest.V_HEIGHT);
        stage = new Stage(viewport, MystForest.batch);
        texture = manager.get("sprites/background-darker.jpg", Texture.class);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quinquefive.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.CYAN);
        Table table = new Table();
        table.top(); // align stuff to top
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
        skin = manager.get("skins/rainbowui/rainbow-ui.json", Skin.class);
        titleLabel = new Label("How To Play", labelStyle);
        table.add(titleLabel).expandX().padTop(10).padBottom(10);
        parameter.size = 8;
        font = generator.generateFont(parameter);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        table.row();
        String[] howToPlayLines = new String[] {
                "Controls: Use W-A-D or arrow keys",
                "Stomp on Mushrooms to Kill Them",
                "Collect Coins to earn Points",
                "Bump into Mushroom = Lose A Life",
                "Instant Death in Some Situations",
                "Reach the Door at the End to Win!"
        };
        for (int i = 0; i < howToPlayLines.length; i++) {
            table.row();
            table.add(new Label((i+1) + ". " + howToPlayLines[i], labelStyle)).pad(3);
        }
        backBtn = new TextButton("Back", skin, "small-toggle");
        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(), new FadeInTransition());
            }
        });
        table.row();
        table.add(backBtn).padTop(5);
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
        backBtn.setChecked(false);
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
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE) {
            screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(), new FadeInTransition());
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S || keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            backBtn.setChecked(!backBtn.isChecked());
        }
        if (keycode == Input.Keys.ENTER && backBtn.isChecked()) {
            screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(), new FadeInTransition());
        }
        return false;
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
