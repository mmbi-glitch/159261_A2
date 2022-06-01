package com.mystudio.mystforest.Sprites.TileObjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Screens.InGameScreen;

public abstract class InteractiveTile {
    protected World world;
    protected TiledMap map;
    protected AssetManager manager;
    protected com.badlogic.gdx.math.Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTile(InGameScreen screen, Rectangle bounds) {
        this.manager = screen.getManager();
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.type = BodyDef.BodyType.StaticBody; // body types like static (not affected by forces) and dynamic (affected by forces)
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2f) / MystForest.PPM, (bounds.getY() + bounds.getHeight() / 2f) / MystForest.PPM);
        body = world.createBody(bdef);
        shape.setAsBox((bounds.getWidth() / 2f) / MystForest.PPM, (bounds.getHeight() / 2) / MystForest.PPM); // set at center
        fdef.isSensor = true;
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }

    public abstract void onCollision();
    public void setCategoryFilter(short FILTER_BIT) {
        Filter filter = new Filter();
        filter.categoryBits = FILTER_BIT;
        fixture.setFilterData(filter);
    }
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(3);
        return layer.getCell((int) ((body.getPosition().x * MystForest.PPM) / 16), (int) ((body.getPosition().y * MystForest.PPM)/16));
    }
}
