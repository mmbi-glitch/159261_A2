package com.mystudio.mystforest.Sprites.TileObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Screens.InGameScreen;

// This is the abstract superclass for initializing static, largely non-interactive tiles,
// like the ground, ground bounds, and door
public abstract class NonInteractiveTile {

    protected World world;
    protected TiledMap map;
    protected AssetManager manager;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public NonInteractiveTile(InGameScreen screen, Rectangle bounds) {
        this.manager = screen.getManager();
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.type = BodyDef.BodyType.StaticBody; // static body is not affected by forces
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2f) / MystForest.PPM, (bounds.getY() + bounds.getHeight() / 2f) / MystForest.PPM);

        body = world.createBody(bdef);

        // set at center
        shape.setAsBox((bounds.getWidth() / 2f) / MystForest.PPM, (bounds.getHeight() / 2) / MystForest.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public void setCategoryFilter(short FILTER_BIT) {
        Filter filter = new Filter();
        filter.categoryBits = FILTER_BIT;
        fixture.setFilterData(filter);
    }
}
