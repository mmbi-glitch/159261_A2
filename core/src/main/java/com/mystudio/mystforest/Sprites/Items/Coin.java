package com.mystudio.mystforest.Sprites.Items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Scenes.HUD;
import com.mystudio.mystforest.Screens.InGameScreen;

public class Coin extends Item {

    private Array<TextureRegion> frames;
    private AssetManager manager;
    private Animation<TextureRegion> coinSpin;
    private float stateTimer;
    private boolean setToDestroy;
    private boolean destroyed;

    public Coin(InGameScreen screen, float x, float y) {
        super(screen, x, y, screen.getCoinAtlas().findRegion("coin"));
        manager = screen.getManager();
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i*8, 0, 8,8));
        }
        coinSpin = new Animation<TextureRegion>(0.1f, frames);
        setBounds(getX(), getY(), 8/ MystForest.PPM, 8/ MystForest.PPM);
        stateTimer = 0;
        setToDestroy = false;
        destroyed = false;
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + (8/ MystForest.PPM)/2,getY() + (8/ MystForest.PPM)/2);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(4f/ MystForest.PPM);
        fdef.filter.categoryBits = MystForest.COIN_BIT;
        fdef.isSensor = true;
        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    @Override
    public void update(float delta) {
        stateTimer += delta;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        else {
            TextureRegion region = coinSpin.getKeyFrame(stateTimer, true);
            setRegion(region);
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }


    @Override
    public void collect() {
        setToDestroy = true;
        HUD.addScore(50);
        manager.get("audio/sounds/coin_collect.wav", Sound.class).play();
    }

}
