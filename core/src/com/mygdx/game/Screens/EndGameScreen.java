package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.AnimatedSprite;
import com.mygdx.game.JocDeTrons;

/**
 * Created by Albert on 27/04/2015.
 */
public class EndGameScreen extends AbstractScreen {

    private Music musica;
    private SpriteBatch batch;

    private Texture warrior;
    private Sprite warriorSprite;
    private AnimatedSprite animation;

    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    private float height = Gdx.graphics.getHeight();
    private float width = Gdx.graphics.getWidth();

    private Camera camera;

    public EndGameScreen(JocDeTrons joc, Camera camera) {
        super(joc);
        this.camera = camera;
        batch = new SpriteBatch();

        //warrior = new Texture(Gdx.files.internal("imatges/mario.png"));
        //warriorSprite = new Sprite(warrior);

        backgroundTexture = new Texture(Gdx.files.internal("world/level1/packer/Cloudy-sky.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        //carregarMusica();
    }

    private void createSpriteAnimat(){

        animation = new AnimatedSprite(warriorSprite,5,1,null);



    }

    /**
     * Carregar i reproduir l'arxiu de música de fons
     */
    public void carregarMusica() {
        musica = Gdx.audio.newMusic(Gdx.files
                .internal("sons/battlesong.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);
        musica.play();
    }

    @Override
    public void render(float delta){

        // Esborrar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Color de fons marro
        Gdx.gl.glClearColor(185f / 255f, 122f / 255f, 87f / 255f, 0);
        batch.begin();
        batch.draw(backgroundSprite, 0, 0, width, height);
        //warriorSprite.draw(batch);
        //animation.draw(batch);
        batch.end();



    }

  /*  @Override
    public void show() {
        super.show();
        carregarMusica();
        carregarFons();
        stage.addActor(splashImage);
        stage.draw();

        //debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
        //		JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
        //		JocDeTrons.PIXELS_PER_METRE));
    }*/




















}

