package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Maxi on 24/04/2015.
 */
public class Crap extends Personatge {

    public final static String CRAP= "crap";
    public Crap(World world) {
        super(world);
    }

    public Crap(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);

    }

    public Crap(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
    }

    public Crap(World world, String animatedImage, String stoppedImage, float position, float position2, String tag) {
        super(world, animatedImage, stoppedImage, position, position2);
        getCos().setUserData(tag);
    }

    @Override
    public void inicialitzarMoviments() {
        super.getSpriteAnimat().setDirection(AnimatedSprite.Direction.STOPPED);
    }

    @Override
    public void moure() {


    }
}
