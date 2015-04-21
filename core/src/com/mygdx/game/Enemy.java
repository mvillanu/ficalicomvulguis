package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Maxi on 17/04/2015.
 */
public class Enemy extends Personatge {
    public Enemy(World world) {
        super(world);
    }

    public Enemy(World world, String animatedImage, String stoppedImage){
        super(world,animatedImage, stoppedImage);
    }
}
