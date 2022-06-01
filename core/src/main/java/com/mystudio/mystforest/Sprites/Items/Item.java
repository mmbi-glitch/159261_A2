package com.mystudio.mystforest.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mystudio.mystforest.Screens.InGameScreen;

public abstract class Item extends Sprite {
    protected InGameScreen screen;
    protected World world;
    protected Fixture fixture;
    protected Body b2body;

    public Item(InGameScreen screen, float x, float y, TextureRegion region){
        super(region);
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        defineItem();
    }

    public abstract void defineItem();

    public abstract void update(float delta);

    public abstract void collect();
}