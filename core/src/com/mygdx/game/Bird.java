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
        ammo = new Crap(world);
    }

    public Bird(World world, float position, float position2) {
        super(world, position, position2);
        ammo = new Crap(world,position,position2);
    }

    public Bird(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);
        ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif");
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
        ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",position,position2);
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2, int frame_cols, int frame_rows, String tag) {
        super(world, animatedImage, stoppedImage, position, position2, frame_cols, frame_rows);
        getCos().setUserData(tag);
        ammo = new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",position,position2);
    }

    public void inicialitzarMoviments() {
        setMoureDreta(true);
        setMoureEsquerra(false);
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

        if (getCos().getPosition().y < 6.0f) {
            getCos().applyLinearImpulse(new Vector2(0.0f, 2.0f),
                    getCos().getWorldCenter(), true);
        }


        if (isMoureDreta() && xVelocity<1.0f) {
            getCos().applyLinearImpulse(new Vector2(0.4f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.RIGHT);


            setPersonatgeCaraDreta(true);
        } else if (isMoureEsquerra() && xVelocity<2.2f) {
            getCos().applyLinearImpulse(new Vector2(-0.4f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.LEFT);
            if (isPersonatgeCaraDreta()) {
                getSpritePersonatge().flip(true, false);
            }
            setPersonatgeCaraDreta(false);
        }



        if (isFerSalt() && isMoureDreta()&&Math.abs(getCos().getLinearVelocity().y) < 1e-9 && yVelocity<5f) {
            getCos().applyLinearImpulse(new Vector2(2.0f, 5.0f),
                    getCos().getWorldCenter(), true);
            setFerSalt(false);
            long id = getSoSalt().play();
        }

        if (isFerSalt() && isMoureEsquerra()&&Math.abs(getCos().getLinearVelocity().y) < 1e-9 && yVelocity<5f) {
            getCos().applyLinearImpulse(new Vector2(-2.0f, 5.0f),
                    getCos().getWorldCenter(), true);
            setFerSalt(false);
            long id = getSoSalt().play();
        }

        if (isFerSalt() && Math.abs(getCos().getLinearVelocity().y) < 1e-9 && yVelocity<5f) {
            getCos().applyLinearImpulse(new Vector2(0.0f, 5.0f),
                    getCos().getWorldCenter(), true);
            setFerSalt(false);
            long id = getSoSalt().play();
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
