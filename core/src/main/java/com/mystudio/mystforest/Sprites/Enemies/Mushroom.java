package com.mystudio.mystforest.Sprites.Enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Screens.InGameScreen;

public class Mushroom extends Enemy {

    private float stateTimer;
    private Animation<TextureRegion> mushroomWalk;
    private Animation<TextureRegion> mushroomDie;
    private Array<TextureRegion> frames;
    private boolean rightDirection;

    // need these flags to tell Box2D world to destroy bodies at NEXT step
    // destroying stuff in the same step leads to undefined behavior
    private boolean setToDestroy;
    private boolean destroyed;
    private AssetManager manager;

    public Mushroom(InGameScreen screen, float x, float y) {
        super(screen, x, y, screen.getMushroomAtlas().findRegion("mushroom"));
        manager = screen.getManager();
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16,16));
        }
        mushroomWalk = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        for (int i = 7; i < 12; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16,16));
        }
        mushroomDie = new Animation<TextureRegion>(0.1f, frames);
        stateTimer = 0;
        setBounds(getX(), getY(), 16/ MystForest.PPM, 16/ MystForest.PPM);
        setToDestroy = false;
        destroyed = false;
        rightDirection = true;
    }

    public void update(float delta) {
        stateTimer += delta;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
        }
        else {
            TextureRegion region;
            if (!destroyed) {
                setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
                region = mushroomWalk.getKeyFrame(stateTimer, true);
                b2body.setLinearVelocity(velocity);
            }
            else {
                region = mushroomDie.getKeyFrame(stateTimer);
            }
            if (((b2body.getLinearVelocity().x < 0 || !rightDirection) && !region.isFlipX())) {
                region.flip(true, false);
                rightDirection = false;
            }
            else if (((b2body.getLinearVelocity().x > 0 || rightDirection) && region.isFlipX())) {
                region.flip(true, false);
                rightDirection = true;
            }
            setRegion(region);
        }
    }

    @Override
    public void draw(Batch batch) {
        // as long as sprite not destroyed, draw the sprite
        // 2nd clause stops drawing after destroyed (after 0.6 sec, we stop
        // drawing the sprite if it's destroyed)
        if (!destroyed || stateTimer < 0.6f) {
            super.draw(batch);
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.5f/ MystForest.PPM);

        fdef.filter.categoryBits = MystForest.ENEMY_BIT;
        fdef.filter.maskBits = (short) (MystForest.GROUND_BIT | MystForest.GROUND_BOUNDS_BIT | MystForest.DOOR_BIT | MystForest.PLAYER_BIT);

        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);

        // Create the mushroom's head here
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-4, 9).scl(1/ MystForest.PPM);
        vertices[1] = new Vector2(4, 9).scl(1/ MystForest.PPM);
        vertices[2] = new Vector2(-1, 5).scl(1/ MystForest.PPM);
        vertices[3] = new Vector2(1, 5).scl(1/ MystForest.PPM);
        head.set(vertices);
        fdef.shape = head;
        fdef.restitution = 0.7f; // add bounciness
        fdef.filter.categoryBits = MystForest.ENEMY_HEAD_BIT;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    @Override
    public void headCollision() {
        setToDestroy = true;
        manager.get("audio/sounds/stomp_enemy.wav", Sound.class).play();
    }

    @Override
    public void fatalCollision() {
        setToDestroy = true;
    }

}
