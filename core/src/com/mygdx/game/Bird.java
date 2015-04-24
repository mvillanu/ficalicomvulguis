package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Albert on 24/04/2015.
 */
public class Bird extends Personatge {

    //ArrayList<Crap> ammo;

    public Bird(World world) {
        super(world);
        //ammo.add(new Crap());
    }

    public Bird(World world, float position, float position2) {
        super(world, position, position2);
        //ammo.add(new Crap());
    }

    public Bird(World world, String animatedImage, String stoppedImage) {
        super(world, animatedImage, stoppedImage);
        //ammo.add(new Crap());
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2) {
        super(world, animatedImage, stoppedImage, position, position2);
        //ammo.add(new Crap());
    }

    public Bird(World world, String animatedImage, String stoppedImage, float position, float position2, int frame_cols, int frame_rows, String tag) {
        super(world, animatedImage, stoppedImage, position, position2, frame_cols, frame_rows);
        getCos().setUserData(tag);
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

        if (getCos().getPosition().y < 5.0f) {
            getCos().applyLinearImpulse(new Vector2(0.4f, 1.0f),
                    getCos().getWorldCenter(), true);
        }


        if (isMoureDreta() && xVelocity<2.2f) {
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


    private void spawnShit(){



    }

    public boolean isReadyToShit(){

        return true;
    }

}
