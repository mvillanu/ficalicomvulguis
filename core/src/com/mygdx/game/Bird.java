package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Albert on 24/04/2015.
 */
public class Bird extends Personatge {


    public Bird(World world) {
        super(world);
    }

    public Bird(World world, float position, float position2) {
        super(world, position, position2);
    }

    public Bird(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
    }


    private void carregarTextures(String animatedImage, String stoppedImage){
        //super.ca

    }



}
