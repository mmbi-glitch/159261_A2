package com.mystudio.mystforest.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Scenes.HUD;
import com.mystudio.mystforest.Screens.GameEndScreen;
import com.mystudio.mystforest.Screens.InGameScreen;

public class Player extends Sprite {
    public World world;
    public Body b2body;
    private static final int MAXIMUM_HITS = 3;
    private int hitsTaken;

    // state enum
    public enum PlayerState {
        STANDING, RUNNING, JUMPING, FALLING, DEAD,
    }

    // vars for tracking states
    public PlayerState currentState;
    public PlayerState previousState;

    // vars for animation
    private Animation<TextureRegion> playerStanding;
    private Animation<TextureRegion> playerRunning;
    private Animation<TextureRegion> playerJumping;
    private Animation<TextureRegion> playerFalling;
    private Animation<TextureRegion> playerDying;

    private boolean rightDirection;
    private boolean playerIsDead;
    private boolean setToDie;
    private boolean reachedDoor;

    // how much time spent in each state
    private float stateTimer;

    private AssetManager manager;

    public static int MAXIMUM_JUMPS = 2;
    private int jumpsPerformed;

    public Player(InGameScreen screen, AssetManager manager) {
        super(screen.getPlayerAtlas().findRegion("hero"));
        this.manager = manager;
        world = screen.getWorld();
        currentState = PlayerState.STANDING;
        previousState = PlayerState.STANDING;
        stateTimer = 0;
        rightDirection = true;

        // array for holding animation frames
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // loading idle animation frames into the frames array
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        playerStanding = new Animation<TextureRegion>(0.2f, frames);

        frames.clear();

        // loading running animation frames into the frames array
        for (int i = 4; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        playerRunning = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        // loading jump animation frames
        for (int i = 10; i < 14; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16,16));
        }
        playerJumping = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        // loading falling animation frames
        for (int i = 14; i < 17; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16,16));
        }
        playerFalling = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();
        for (int i = 17; i < 25; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0,16,16));
        }
        playerDying = new Animation<TextureRegion>(0.1f, frames);

        definePlayer();
        playerIsDead = false;
        setToDie = false;
        hitsTaken = 0;
        setBounds(16/ MystForest.PPM,16/ MystForest.PPM,16/ MystForest.PPM, 16/ MystForest.PPM);
        jumpsPerformed = 0;
    }

    public void update(float delta) {
        if (HUD.isTimeUp()) {
            GameEndScreen.endMessage = "Time Ticked Away";
            setToDie = true;
        }
        if (b2body.getPosition().y - getHeight()/2 < 0) {
            GameEndScreen.endMessage = "Fell Off World";
            setToDie = true;
            b2body.setLinearVelocity(0,0);
        }
        if (b2body.getPosition().y + getHeight()/2 > MystForest.V_HEIGHT/ MystForest.PPM) {
            b2body.setLinearVelocity(0, -10f*delta);
        }
        if (reachedDoor && !playerIsDead) {
            manager.get("audio/music/game_music.ogg", Music.class).stop();
            manager.get("audio/sounds/player_level_up.wav", Sound.class).play();
//            Gdx.app.log("Player", "Won");
            world.destroyBody(b2body);
            playerIsDead = true;
            stateTimer = 0;
        }
        // if the player is set to die, do this for the next physics simulation step
        else if (setToDie && !playerIsDead) {
            HUD.removeAllLives();
            manager.get("audio/music/game_music.ogg", Music.class).stop();
            manager.get("audio/sounds/player_die.wav", Sound.class).play();
//            Gdx.app.log("Player", "Died");
            world.destroyBody(b2body);
            playerIsDead = true;
            stateTimer = 0;
        }
        else {
            setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y - getHeight()/2); // set sprite to position of box2d body
            setRegion(getFrame(delta));
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!playerIsDead || stateTimer < 0.8f) { // wait for death animation to play, then stop drawing sprite
            super.draw(batch);
        }
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = playerDying.getKeyFrame(stateTimer);
                break;
            case JUMPING:
                region = playerJumping.getKeyFrame(stateTimer);
                break;
            case FALLING:
                region = playerFalling.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRunning.getKeyFrame(stateTimer, true);
                break;
            default:
                region = playerStanding.getKeyFrame(stateTimer, true);
                break;
        }
        if (((b2body.getLinearVelocity().x < 0 || !rightDirection) && !region.isFlipX())) {
            region.flip(true, false);
            rightDirection = false;
        }
        else if (((b2body.getLinearVelocity().x > 0 || rightDirection) && region.isFlipX())) {
            region.flip(true, false);
            rightDirection = true;
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        if (stateTimer == 0 && currentState == PlayerState.JUMPING) {
            manager.get("audio/sounds/jump.mp3", Sound.class).play();
        }
        previousState = currentState;
        return region;
    }



    public PlayerState getState() {
        if (setToDie || reachedDoor) {
            return PlayerState.DEAD;
        }
        else if (b2body.getLinearVelocity().y > 0) {
            return PlayerState.JUMPING;
        }
        else if (b2body.getLinearVelocity().y < 0) {
            return PlayerState.FALLING;
        }
        else if (b2body.getLinearVelocity().x != 0) {
            jumpsPerformed = 0;
            return PlayerState.RUNNING;
        }
        jumpsPerformed = 0;
        return PlayerState.STANDING;

    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MystForest.PPM,32 / MystForest.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6f/ MystForest.PPM);

        fdef.filter.categoryBits = MystForest.PLAYER_BIT;
        fdef.filter.maskBits = (short) (MystForest.GROUND_BIT | MystForest.GROUND_BOUNDS_BIT | MystForest.DOOR_BIT
                | MystForest.ENEMY_BIT | MystForest.COIN_BIT | MystForest.ENEMY_HEAD_BIT);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean hasDied() {
        return playerIsDead;
    }

    public void takeHit() {
        if (hitsTaken < MAXIMUM_HITS - 1) {
            hitsTaken++;
            HUD.removeLife();
            manager.get("audio/sounds/take_hit.wav", Sound.class).play(0.5f);
//            Gdx.app.log("Player", "Hit");
        }
        else {
            GameEndScreen.endMessage = "Squished by Mushroom";
            setToDie = true;
        }
    }

    public void reachedDoor() {
        reachedDoor = true;
    }

    public boolean hasReachedDoor() {
        return reachedDoor;
    }

    public void incrementJumps() {
        jumpsPerformed++;
    }

    public int jumps() {
        return jumpsPerformed;
    }
}
