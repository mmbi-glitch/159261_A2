package com.mystudio.mystforest.Sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Screens.InGameScreen;

public class GroundBounds extends NonInteractiveTile {

    public GroundBounds(InGameScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this); // now we can access this fixture through its class
        setCategoryFilter(MystForest.GROUND_BOUNDS_BIT);
    }
}
