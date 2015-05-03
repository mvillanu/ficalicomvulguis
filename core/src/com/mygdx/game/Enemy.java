package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Maxi on 17/04/2015.
 */
public class Enemy extends Personatge {
    public final static String ENEMIC1= "enemic1";
    public final static String ENEMIC2= "enemic2";
    public Enemy(World world,String tag) {
        super(world,tag);
    }

    public Enemy(World world, String animatedImage, String stoppedImage,String tag) {
        super(world, animatedImage, stoppedImage, tag);

    }

    public Enemy(World world, String animatedImage, String stoppedImage, float position, float position2, String tag) {
        super(world, animatedImage, stoppedImage, position, position2,tag);
        //getCos().setUserData(tag);
    }
    public Enemy(World world, String animatedImage, String stoppedImage, float position1, float position2, int frame_cols, int frame_rows, String tag){
        super(world,animatedImage,stoppedImage,position1,position2,frame_cols,frame_rows,tag);
        //getCos().setUserData(tag);
    }

    @Override
    public void inicialitzarMoviments() {
        setMoureDreta(false);
        setMoureEsquerra(false);
        setFerSalt(false);
        //setPersonatgeCaraDreta(true);
        super.getSpriteAnimat().setDirection(AnimatedSprite.Direction.RIGHT);
    }

    @Override
    public void moure() {
        //Gdx.app.log("Moves like jagger","hue");
        float xVelocity=getCos().getLinearVelocity().x;
        float yVelocity=getCos().getLinearVelocity().y;

        if (isMoureDreta() && xVelocity<2.2f) {
            getCos().applyLinearImpulse(new Vector2(0.4f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.RIGHT);

            if (!isPersonatgeCaraDreta()) {
                getSpritePersonatge().flip(true, false);
            }
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
}