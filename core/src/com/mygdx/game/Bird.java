package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by Albert on 24/04/2015.
 */
public class Bird extends Personatge {

    private Crap ammo;
    private int lastTime;
    private int shitNumber = 0;

    public Bird(World world) {
        super(world);
        setPersonatgeCaraDreta(true);
        ammo = new Crap(world);
    }

    public Bird(World world, float position, float position2) {
        super(world, position, position2);
        setPersonatgeCaraDreta(true);
        ammo = new Crap(world,position,position2);
    }

    public Bird(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);
        setPersonatgeCaraDreta(true);
        ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif");
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
        setPersonatgeCaraDreta(true);
        ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",position,position2);
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2, int frame_cols, int frame_rows, String tag) {
        super(world, animatedImage, stoppedImage, position, position2, frame_cols, frame_rows);
        getCos().setUserData(tag);
        setPersonatgeCaraDreta(true);
        ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",position,position2);
    }

    public void inicialitzarMoviments() {

        if(getCos().getPosition().x > 10){
            //Gdx.app.log("mou esquerra","puto");
            setMoureDreta(false);
            setMoureEsquerra(true);
        } else if(getCos().getPosition().x <= 2){
            setMoureDreta(true);
            setMoureEsquerra(false);
            //Gdx.app.log("mou dreta","puto");
        }

        //setMoureDreta(true);
        //setMoureEsquerra(false);
        setFerSalt(false);
        //super.getSpriteAnimat().setDirection(AnimatedSprite.Direction.STOPPED);
    }

    /**
     * Carregar els arxius de so
     */
    private void carregarSons() {
        super.setSoSalt(Gdx.audio.newSound(Gdx.files.internal("sons/salt.mp3")));
    }

    @Override
    public void moure() {

        float xVelocity=getCos().getLinearVelocity().x;
        float yVelocity=getCos().getLinearVelocity().y;


        if (getCos().getPosition().y <= 6.0f && yVelocity < 2.0f) {
            getCos().applyLinearImpulse(new Vector2(0.0f, 1.0f),
                    getCos().getWorldCenter(), true);
            //getSpriteAnimat().setDirection(AnimatedSprite.Direction.RIGHT);
        }

        if (isMoureDreta() && xVelocity < 1.0f /*&& getCos().getPosition().y < 9.0f*/) {
            getCos().applyLinearImpulse(new Vector2(0.1f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.RIGHT);

            if (!isPersonatgeCaraDreta()) {
                getSpritePersonatge().flip(true, false);
            }
            setPersonatgeCaraDreta(true);
        } else if (isMoureEsquerra() && xVelocity > -1.0f) {
            getCos().applyLinearImpulse(new Vector2(-0.1f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.LEFT);
            if (isPersonatgeCaraDreta()) {
                getSpritePersonatge().flip(true, false);
            }
            setPersonatgeCaraDreta(false);
        }

    }


    private int getSysTime(){
        return (int) System.nanoTime()/1000000000;
    }

    public Crap getCrap(){
        return ammo;
    }

    public Crap spawnShit(World world){
        if(getSysTime()-lastTime>5){
            ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",getCos().getPosition().x,getCos().getPosition().y);
        }
        return ammo;
    }


    public boolean isReadyToShit(){

        return true;
    }

}
