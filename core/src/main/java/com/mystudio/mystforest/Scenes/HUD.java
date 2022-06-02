package com.mystudio.mystforest.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mystudio.mystforest.MystForest;

// This is the class that deals with creating the heads-up display
// Basically, the HUD to a fixed viewport, so that it doesn't move with the gamCam

public class HUD {
    public Stage stage;
    private Viewport hudPort;
    private static Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private static Integer lives;

    Label livesLabel;
    Label scoreLabel;
    Label worldLabel;
    Label timeLabel;

    static Label scoreTrackLabel;
    static Label livesTrackLabel;
    Label levelsTrackLabel;
    Label timeTrackLabel;

    public HUD(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        lives = 3;
        hudPort = new FitViewport(MystForest.V_WIDTH, MystForest.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(hudPort, sb);

        Table table = new Table();
        table.top(); // align stuff to top
        table.setFillParent(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quinquefive.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        parameter.hinting = FreeTypeFontGenerator.Hinting.AutoFull;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        livesLabel = new Label("LIVES", style);
        scoreLabel = new Label("SCORE", style);
        worldLabel = new Label("LEVEL", style);
        timeLabel = new Label("TIME", style);
        parameter.size = 8;
        font = generator.generateFont(parameter);
        style = new Label.LabelStyle(font, Color.WHITE);
        livesTrackLabel = new Label(String.format("%03d", lives), style);
        scoreTrackLabel = new Label(String.format("%05d", score), style);
        levelsTrackLabel = new Label("1-1", style);
        timeTrackLabel = new Label(String.format("%03d", worldTimer), style);


        table.add(livesLabel).expandX().padTop(5);
        table.add(scoreLabel).expandX().padTop(5);
        table.add(worldLabel).expandX().padTop(5);
        table.add(timeLabel).expandX().padTop(5);
        table.row(); // add a new row
        table.add(livesTrackLabel).expandX();
        table.add(scoreTrackLabel).expandX();
        table.add(levelsTrackLabel).expandX();
        table.add(timeTrackLabel).expandX();

        stage.addActor(table);
    }

    public void update(float delta) {
        timeCount += delta;
        if (timeCount >= 1) { // greater than 1 second
            worldTimer--;
            timeTrackLabel.setText(String.format("%03d", worldTimer));
            if (worldTimer < 100) {
                timeTrackLabel.setColor(Color.RED);
            }
            timeCount = 0;
        }
    }

    public static void addScore(int value) {
        score += value;
        scoreTrackLabel.setText(String.format("%05d", score));
    }

    public static boolean isTimeUp() {
        return worldTimer <= 0;
    }

    public static void removeLife() {
        lives--;
        livesTrackLabel.setText(String.format("%03d", lives));
        if (lives <= 1) {
            livesTrackLabel.setColor(Color.RED);
        }
    }

    public static void removeAllLives() {
        livesTrackLabel.setText(String.format("%03d", 0));
        livesTrackLabel.setColor(Color.RED);
    }

    public static Integer getScore() {
        return score;
    }
}
