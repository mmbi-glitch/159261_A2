package com.mystudio.mystforest.Tools;

/* This class handles contact between objects in the Box2D game world
*  and determines what should happen when different objects get into contact with each other */

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.*;
import com.mystudio.mystforest.MystForest;
import com.mystudio.mystforest.Scenes.HUD;
import com.mystudio.mystforest.Screens.InGameScreen;
import com.mystudio.mystforest.Sprites.Items.Coin;
import com.mystudio.mystforest.Sprites.Enemies.Enemy;
import com.mystudio.mystforest.Sprites.Player;

// A contact listener gets called when two fixtures collide in the Box2D world
public class WorldContactListener implements ContactListener {

    private AssetManager manager;

    public WorldContactListener(InGameScreen screen) {
        manager = screen.getManager();
    }

    @Override
    public void beginContact(Contact contact) { // when two fixtures start colliding
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        // handle collision between player and coin
        if (cDef == (MystForest.COIN_BIT | MystForest.PLAYER_BIT)) {
            if (fixA.getFilterData().categoryBits == MystForest.COIN_BIT) {
                ((Coin)fixA.getUserData()).collect();
            }
            else {
                ((Coin)fixB.getUserData()).collect();
            }
        }

        // handle collision between player and enemy mushroom's head
        if (cDef == (MystForest.ENEMY_HEAD_BIT | MystForest.PLAYER_BIT)) {
            if (fixA.getFilterData().categoryBits == MystForest.ENEMY_HEAD_BIT) {
                ((Enemy)fixA.getUserData()).headCollision();
                HUD.addScore(100);
            }
            else {
                ((Enemy)fixB.getUserData()).headCollision();
                HUD.addScore(100);
            }
        }

        // handle collision between player and enemy mushroom
        if (cDef == (MystForest.ENEMY_BIT | MystForest.PLAYER_BIT)) {
            if (fixA.getFilterData().categoryBits == MystForest.PLAYER_BIT) {
                ((Player) fixA.getUserData()).takeHit();
                ((Enemy) fixB.getUserData()).fatalCollision();
                HUD.addScore(-200);
            }
            else {
                ((Player) fixB.getUserData()).takeHit();
                ((Enemy) fixA.getUserData()).fatalCollision();
                HUD.addScore(-200);
            }
        }

        // handle collision between enemy mushroom and any bounding objects
        if (cDef == (MystForest.ENEMY_BIT | MystForest.GROUND_BOUNDS_BIT)) {
            if (fixA.getFilterData().categoryBits == MystForest.ENEMY_BIT) {
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
            }
            else {
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
            }
        }
        if (cDef == (MystForest.ENEMY_BIT | MystForest.DOOR_BIT)) {
            if (fixA.getFilterData().categoryBits == MystForest.ENEMY_BIT) {
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
            }
            else {
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
            }
        }

        // finally, handle collision between player and the door
        if (cDef == (MystForest.PLAYER_BIT | MystForest.DOOR_BIT)) {
            if (fixA.getFilterData().categoryBits == MystForest.PLAYER_BIT) {
                ((Player)fixA.getUserData()).reachedDoor();
            }
            else {
                ((Player)fixB.getUserData()).reachedDoor();
            }
        }
    }

    @Override
    public void endContact(Contact contact) { // when two fixtures stop colliding
          //        Gdx.app.log("End Contact: ", "");
    }

    // these are not needed
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
