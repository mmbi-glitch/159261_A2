package com.mystudio.mystforest.Sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Screens.InGameScreen;

public class Door extends NonInteractiveTile {
    public Door(InGameScreen screen, Rectangle bounds) {
        super(screen, bounds);
        setCategoryFilter(MystForest.DOOR_BIT);
        fixture.setUserData(this);
        fixture.setSensor(true);
    }
}
