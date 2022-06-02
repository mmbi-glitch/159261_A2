package com.mystudio.mystforest.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mystudio.mystforest.MystForest;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

import java.util.Random;

public class LoadingScreen extends BasicGameScreen {
    public static int ID = 1;

    private float loadingTime = 0f;

    private float randomLoadTime;

    private Viewport viewport;
    private Stage stage;
    private Texture texture;
    private Label loadingLabel;

    private AssetManager manager;
    public LoadingScreen(AssetManager manager) {
        this.manager = manager;
    }

    public void initialise(GameContainer gc) {
        viewport = new FitViewport(MystForest.V_WIDTH, MystForest.V_HEIGHT, new OrthographicCamera());
        viewport.update(MystForest.V_WIDTH, MystForest.V_HEIGHT);
        stage = new Stage(viewport, MystForest.batch);
        texture = new Texture("sprites/background.jpg");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quinquefive.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        BitmapFont font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Table table = new Table();
        table.center(); // align stuff to center
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
        loadingLabel = new Label("Loading..." + String.format("%.2f", loadingTime), style);
        table.add(loadingLabel).expandX().padTop(10).padBottom(20);
        table.row();
        stage.addActor(table);
        float randomFloat = new Random().nextFloat();
        randomLoadTime = 3.2f + randomFloat * (5.5f - 3.2f);
        generator.dispose();
    }

    public void update(GameContainer gc, ScreenManager screenManager, float delta) {
        if (loadingTime < randomLoadTime) { // after loading assets, wait for a few more seconds
            loadingTime+=delta;
            loadingLabel.setText("Loading..." + String.format("%.2f", loadingTime));
        }
        else {
            // Fade to MenuScreen after 4 seconds
            screenManager.enterGameScreen(MenuScreen.ID, new FadeOutTransition(),
                    new FadeInTransition());
        }
    }

    public void interpolate(GameContainer gc, float alpha) {
    }

    public void render(GameContainer gc, Graphics g) {
        // clear screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // draw stuff
        stage.draw();
    }

    public int getId() {
        return ID;
    }

    @Override
    public void onResize(int width, int height) {
        viewport.update(width, height);
    }
}
