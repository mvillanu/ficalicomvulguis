package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Maxi on 28/04/2015.
 */
public class Tornado extends Enemy {
    public final static String TORNADO="Tornado";
    public Tornado(World world) {
        super(world);
    }

    public Tornado(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);
    }

    public Tornado(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
    }

    public Tornado(World world, String animatedImage, String stoppedImage, float position, float position2, String tag) {
        super(world, animatedImage, stoppedImage, position, position2, tag);
        getCos().setGravityScale(0f);
        getCos().getFixtureList().get(0).setDensity(0);
        getCos().getFixtureList().get(0).setFriction(0);
    }

    @Override
    public void inicialitzarMoviments() {
        setMoureDreta(true);
        setMoureEsquerra(false);
        setFerSalt(false);
        super.getSpriteAnimat().setDirection(AnimatedSprite.Direction.STOPPED);
    }

    @Override
    public void moure() {

        float xVelocity=getCos().getLinearVelocity().x;
        float yVelocity=getCos().getLinearVelocity().y;

        if (isMoureDreta() && xVelocity<1.0f) {
            getCos().applyLinearImpulse(new Vector2(0.5f, 0.0f),
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