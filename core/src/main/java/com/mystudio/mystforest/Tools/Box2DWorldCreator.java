package com.mystudio.mystforest.Tools;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Screens.InGameScreen;
import com.mystudio.mystforest.Sprites.Enemies.Mushroom;
import com.mystudio.mystforest.Sprites.Items.Coin;
import com.mystudio.mystforest.Sprites.TileObjects.Door;
import com.mystudio.mystforest.Sprites.TileObjects.Ground;
import com.mystudio.mystforest.Sprites.TileObjects.GroundBounds;

public class Box2DWorldCreator {

    private TiledMap map;

    private Array<Mushroom> mushrooms;

    private Array<Coin> coins;

    public Box2DWorldCreator(InGameScreen screen) {

        map = screen.getMap();

        // create ground body/fixtures

        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Ground(screen, rect);
        }

        // create ground bounds body/fixtures
        for (RectangleMapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new GroundBounds(screen, rect);
        }

        // create door body/fixtures
        for (RectangleMapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            new Door(screen, rect);
        }

        // create mushrooms
        mushrooms = new Array<Mushroom>();
        for (RectangleMapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            mushrooms.add(new Mushroom(screen, rect.getX()/ MystForest.PPM, rect.getY()/ MystForest.PPM));
        }

        // create coins
        coins = new Array<Coin>();
        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            coins.add(new Coin(screen, rect.getX()/ MystForest.PPM, rect.getY()/ MystForest.PPM));
        }
    }

    public Array<Mushroom> getMushrooms() {
        return mushrooms;
    }

    public Array<Coin> getCoins() {
        return coins;
    }
}
