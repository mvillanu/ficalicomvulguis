package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Maxi on 24/04/2015.
 */
public class Crap extends Personatge {

    public final static String CRAP= "crap";
    private boolean isAlive;
    public Crap(World world) {
        super(world);
    }

    public Crap(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);
        isAlive=true;
    }

    public Crap(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
        isAlive=true;
    }

    public Crap(World world, String animatedImage, String stoppedImage, float position, float position2, String tag) {
        super(world, animatedImage, stoppedImage, position, position2);
        getCos().setUserData(tag);
        isAlive=true;
    }

    @Override
    public void inicialitzarMoviments() {
        super.getSpriteAnimat().setDirection(AnimatedSprite.Direction.STOPPED);
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void setAlive(boolean alive){
        this.isAlive=alive;
    }

    @Override
    public void moure() {


    }
}
