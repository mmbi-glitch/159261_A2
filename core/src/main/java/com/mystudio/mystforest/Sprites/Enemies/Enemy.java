package com.mystudio.mystforest.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mystudio.mystforest.Screens.InGameScreen;

public abstract class Enemy extends Sprite {

    protected World world;
    protected InGameScreen screen;
    public Body b2body;
    public Vector2 velocity;
    protected Fixture fixture;

    public Enemy(InGameScreen screen, float x, float y, TextureRegion region) {
        super(region);
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0.5f, 0);
    }

    protected abstract void defineEnemy();

    public abstract void update(float delta);
    public abstract void headCollision();

    public abstract void fatalCollision();

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = velocity.x*-1;
        }
        if (y) {
            velocity.y = velocity.y*-1;
        }
    }
    public void setCategoryFilter(short FILTER_BIT) {
        Filter filter = new Filter();
        filter.categoryBits = FILTER_BIT;
        fixture.setFilterData(filter);
    }

}
