package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Classe que implementa el protagonista del joc
 */
public class Personatge {
    public static final String HERO = "hero";
    public static final int FRAME_COLS = 9;
    public static final int FRAME_ROWS = 2;
    private static final float SPEED = 10f; // 10 pixels per second
    private static final float SPEED_SLOW = 1f; // 1 pixel per second
    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;
    private boolean ferSalt;
    private boolean personatgeCaraDreta;

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Sound soSalt;               // el so que reprodueix en saltar
    private Texture animatedTexture;
    private boolean isAlive;


    public Personatge(World world, String tag) {
        moureEsquerra = moureDreta = ferSalt = false;
        this.setWorld(world);
        carregarTextures();
        carregarSons();
        crearProtagonista(tag);
        setAlive(true);
    }

    public Personatge(World world, float position, float position2, String tag) {
        moureEsquerra = moureDreta = ferSalt = false;
        this.setWorld(world);
        carregarTextures();
        carregarSons();
        crearProtagonista(position, position2, tag);
        getCos().setUserData(tag);
        setAlive(true);
    }

    public Personatge(World world, String animatedImage, String stoppedImage, String tag){
        moureEsquerra = moureDreta = ferSalt = false;
        this.setWorld(world);
        carregarTextures(animatedImage, stoppedImage);
        carregarSons();
        crearProtagonista(tag);
        setAlive(true);
    }

    public Personatge(World world, String animatedImage, String stoppedImage, float position, float position2, String tag){
        moureEsquerra = moureDreta = ferSalt = false;
        this.setWorld(world);
        carregarTextures(animatedImage, stoppedImage);
        carregarSons();
        crearProtagonista(position, position2, tag);
        getCos().setUserData(tag);
        setAlive(true);
    }

    public Personatge(World world, String animatedImage, String stoppedImage, float position1, float position2, int frame_cols, int frame_rows, String tag){
        moureEsquerra = moureDreta = ferSalt = false;
        this.setWorld(world);
        carregarTextures(animatedImage, stoppedImage);
        carregarSons();
        crearProtagonista(position1, position2, frame_cols, frame_rows, tag);

        setAlive(true);
    }

    private void carregarTextures() {
        setAnimatedTexture(new Texture(Gdx.files.internal("imatges/warriorSpriteSheet.png")));
        getAnimatedTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setStoppedTexture(new Texture(Gdx.files.internal("imatges/warrior.png")));
        getStoppedTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    private void carregarTextures(String animatedImage, String stoppedImage) {
        if(animatedImage != null){
            setAnimatedTexture(new Texture(Gdx.files.internal(animatedImage)));
            getAnimatedTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        if(stoppedImage != null) {
            setStoppedTexture(new Texture(Gdx.files.internal(stoppedImage)));
            getStoppedTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    /**
     * Carregar els arxius de so
     */
    private void carregarSons() {
        soSalt = Gdx.audio.newSound(Gdx.files.internal("sons/salt.mp3"));
    }


    private void setFilterColisions(FixtureDef propietats){
        if(getCos().getUserData().toString().equals(Enemy.ENEMIC1) || getCos().getUserData().toString().equals(Enemy.ENEMIC2)){
            propietats.filter.categoryBits = ColisionsGroups.ENEMIC_ENTITY;
            propietats.filter.maskBits = ColisionsGroups.HERO_ENTITY | ColisionsGroups.MAP_ENTITY;

        } else if (getCos().getUserData().toString().equals(Tornado.TORNADO)){
            propietats.filter.categoryBits = ColisionsGroups.TORNADO_ENTITY;
            propietats.filter.maskBits = 0;

        } else if (getCos().getUserData().toString().equals(Bird.BIRD)){
            propietats.filter.categoryBits = ColisionsGroups.BIRD_ENTITY;
            propietats.filter.maskBits = 0;
        } else if(getCos().getUserData().toString().equals(HERO)){
            propietats.filter.categoryBits = ColisionsGroups.HERO_ENTITY;
            propietats.filter.maskBits = ColisionsGroups.MAP_ENTITY | ColisionsGroups.ENEMIC_ENTITY;
        } else if(getCos().getUserData().toString().equals((Crap.CRAP))){
            propietats.filter.categoryBits = ColisionsGroups.CRAP_ENTITY;
            propietats.filter.maskBits = ColisionsGroups.MAP_ENTITY | ColisionsGroups.ENEMIC_ENTITY;
        }
        String tipus = "nothing for me";
        if(propietats.filter.categoryBits == ColisionsGroups.BIRD_ENTITY){
            tipus = "bird";
        } else if(propietats.filter.categoryBits == ColisionsGroups.ENEMIC_ENTITY){
            tipus = "enemic";
        }else if(propietats.filter.categoryBits == ColisionsGroups.TORNADO_ENTITY){
            tipus = "Tornado";
        } else if(propietats.filter.categoryBits == ColisionsGroups.HERO_ENTITY) {
            tipus = "Hero";
        } else if(propietats.filter.categoryBits == ColisionsGroups.CRAP_ENTITY){
            tipus = "crap";
        }
        Gdx.app.log("GRUP: ",tipus);
        Gdx.app.log("GRUP: ",getCos().getUserData().toString());
    }


    private void crearProtagonista(float position1, float position2, String tag) {
        setSpritePersonatge(new Sprite(getAnimatedTexture()));
        setSpriteAnimat(new AnimatedSprite(getSpritePersonatge(), FRAME_COLS, FRAME_ROWS, getStoppedTexture()));

        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(position1, position2);

        setCos(getWorld().createBody(defCos));
        getCos().setUserData(tag);
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((getSpritePersonatge().getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (getSpritePersonatge().getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricció del protagonista. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 3.0f;

        setFilterColisions(propietats);

        getCos().setFixedRotation(true);
        getCos().createFixture(propietats);

        requadre.dispose();
    }

    public void applyImpulse(){

        getCos().applyLinearImpulse(new Vector2(5.0f, 0.0f),
                getCos().getWorldCenter(), true);



    }


    private void crearProtagonista(String tag) {
        setSpritePersonatge(new Sprite(getAnimatedTexture()));
        setSpriteAnimat(new AnimatedSprite(getSpritePersonatge(), FRAME_COLS, FRAME_ROWS, getStoppedTexture()));

        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(8.0f, 5.0f);

        setCos(getWorld().createBody(defCos));
        getCos().setUserData(tag);
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((getSpritePersonatge().getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (getSpritePersonatge().getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricció del protagonista. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 3.0f;

        setFilterColisions(propietats);

        getCos().setFixedRotation(true);
        getCos().createFixture(propietats);
        requadre.dispose();
    }

    private void crearProtagonista(float position1, float position2, int frame_cols, int frame_rows, String tag) {
        setSpritePersonatge(new Sprite(getAnimatedTexture()));
        setSpriteAnimat(new AnimatedSprite(getSpritePersonatge(), frame_cols, frame_rows, getStoppedTexture()));

        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(position1, position2);

        setCos(getWorld().createBody(defCos));
        getCos().setUserData(tag);
        /**
         * Definir les vores de l'sprite
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((getSpritePersonatge().getWidth() / frame_cols) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (getSpritePersonatge().getHeight() / frame_rows) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricció del protagonista. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 3.0f;

        setFilterColisions(propietats);

        getCos().setFixedRotation(true);
        getCos().createFixture(propietats);
        requadre.dispose();
    }


    public AnimatedSprite getSpriteAnimat(){
        return this.spriteAnimat;
    }

    public void inicialitzarMoviments() {
        setMoureDreta(false);
        setMoureEsquerra(false);
        setFerSalt(false);
        getSpriteAnimat().setDirection(AnimatedSprite.Direction.STOPPED);
    }

    /**
     * Actualitza la posició de l'sprite
     */
    public void updatePosition(float dt) {
        getSpritePersonatge().setPosition(
                JocDeTrons.PIXELS_PER_METRE * getCos().getPosition().x
                        - getSpritePersonatge().getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * getCos().getPosition().y
                        - getSpritePersonatge().getHeight() / FRAME_ROWS / 2);
        getSpriteAnimat().setPosition(getSpritePersonatge().getX(), getSpritePersonatge().getY());

    }

    public void dibuixar(SpriteBatch batch) {
        getSpriteAnimat().draw(batch);
    }

    /**
     * Fer que el personatge es mogui
     * <p/>
     * Canvia la posició del protagonista
     * Es tracta de forma separada el salt perquè es vol que es pugui moure si salta
     * al mateix temps..
     * <p/>
     * Els impulsos s'apliquen des del centre del protagonista
     */
    public void moure() {

        float xVelocity=getCos().getLinearVelocity().x;
        float yVelocity=getCos().getLinearVelocity().y;
        if (moureDreta) {
            getCos().applyLinearImpulse(new Vector2(0.2f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.RIGHT);

            if (!isPersonatgeCaraDreta()) {
                getSpritePersonatge().flip(true, false);
            }
            setPersonatgeCaraDreta(true);
        } else if (moureEsquerra) {
            getCos().applyLinearImpulse(new Vector2(-0.2f, 0.0f),
                    getCos().getWorldCenter(), true);
            getSpriteAnimat().setDirection(AnimatedSprite.Direction.LEFT);
            if (isPersonatgeCaraDreta()) {
                getSpritePersonatge().flip(true, false);
            }
            setPersonatgeCaraDreta(false);
        }

        if (ferSalt && Math.abs(getCos().getLinearVelocity().y) < 1e-9) {
            getCos().applyLinearImpulse(new Vector2(0.0f, 6.0f),
                    getCos().getWorldCenter(), true);
            long id = soSalt.play();
        }
    }

    public boolean isMoureEsquerra() {
        return moureEsquerra;
    }

    public void setMoureEsquerra(boolean moureEsquerra) {
        this.moureEsquerra = moureEsquerra;
    }

    public boolean isMoureDreta() {
        return moureDreta;
    }

    public void setMoureDreta(boolean moureDreta) {
        this.moureDreta = moureDreta;
    }

    public boolean isFerSalt() {
        return ferSalt;
    }

    public void setFerSalt(boolean ferSalt) {
        this.ferSalt = ferSalt;
    }

    public boolean isCaraDreta() {
        return this.isPersonatgeCaraDreta();
    }

    public void setCaraDreta(boolean caraDreta) {
        this.setPersonatgeCaraDreta(caraDreta);

    }

    public Sound getSoSalt() {
        return soSalt;
    }

    public void setSoSalt(Sound soSalt) {
        this.soSalt = soSalt;
    }

    public Vector2 getPositionBody() {
        return this.getCos().getPosition();
    }

    public Vector2 getPositionSprite() {
        return new Vector2().set(this.getSpritePersonatge().getX(), this.getSpritePersonatge().getY());
    }


    public Texture getTextura() {
        return getStoppedTexture();
    }

    public void setTextura(Texture textura) {
        this.setStoppedTexture(textura);
    }


    public void dispose() {
        getAnimatedTexture().dispose();
        getStoppedTexture().dispose();
        soSalt.dispose();
    }

    public boolean isPersonatgeCaraDreta() {
        return personatgeCaraDreta;
    }

    public void setPersonatgeCaraDreta(boolean personatgeCaraDreta) {
        this.personatgeCaraDreta = personatgeCaraDreta;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Body getCos() {
        return cos;
    }

    public void setCos(Body cos) {
        this.cos = cos;
    }

    public Sprite getSpritePersonatge() {
        return spritePersonatge;
    }

    public void setSpritePersonatge(Sprite spritePersonatge) {
        this.spritePersonatge = spritePersonatge;
    }

    public Texture getStoppedTexture() {
        return stoppedTexture;
    }

    public void setStoppedTexture(Texture stoppedTexture) {
        this.stoppedTexture = stoppedTexture;
    }

    public Texture getAnimatedTexture() {
        return animatedTexture;
    }

    public void setAnimatedTexture(Texture animatedTexture) {
        this.animatedTexture = animatedTexture;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setSpriteAnimat(AnimatedSprite spriteAnimat) {
        this.spriteAnimat = spriteAnimat;
    }
}
