package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Bird;
import com.mygdx.game.Crap;
import com.mygdx.game.Enemy;
import com.mygdx.game.GestorContactes;
import com.mygdx.game.ImagesPath;
import com.mygdx.game.JocDeTrons;
import com.mygdx.game.MapBodyManager;
import com.mygdx.game.Personatge;
import com.mygdx.game.TiledMapHelper;
import com.mygdx.game.Tornado;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Una pantalla del joc
 * 
 * @author Marc
 *
 */
public class MainScreen extends AbstractScreen {

/*Arreglar sprites*/
    /**
     * Estils
     */
    private final Skin skin;
    /**
	 * Variable d'instancia que permet gestionar i pintar el mapa a partir d'un
	 * TiledMap (TMX)
	 */
	private TiledMapHelper tiledMapHelper;

	// objecte que gestiona el protagonista del joc
	// ---->private PersonatgeBackup personatge;
    private Personatge personatge;
    private Crap crap;
    private Bird bird;
    //private Enemy enemic;
    private Tornado tornado;




	/**
	 * Objecte que cont� tots els cossos del joc als quals els aplica la
	 * simulaci�
	 */
	private World world;

	/**
	 * Objecte que dibuixa elements per debugar. Dibuixa linies al voltant dels
	 * l�mits de les col�lisions. Va molt b� per comprovar que les
	 * col�lisions s�n les que desitgem. Cal tenir present, pe`ro, que �s
	 * m�s lent. Nom�s s'ha d'utitilitzar per debugar.
	 */
	private Box2DDebugRenderer debugRenderer;

	/**
	 * Musica i sons
	 */
	private Music musica;

    /**
     * Per debugar les col·lisions
     */
	private Box2DDebugRenderer box2DRenderer;

    /**
     * Per mostrar el títol
     */
    private Label title;
    private Table table = new Table();
    private GestorContactes gestorContactes;
    private ArrayList<Crap> crapList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<ImagesPath> imagesPath;

    /**
     * per indicar quins cossos s'han de destruir
     * @param joc
     */
    private ArrayList<Body> bodyDestroyList;
	

	public MainScreen(JocDeTrons joc) {
		super(joc);

        // carregar el fitxer d'skins
        skin = new Skin(Gdx.files.internal("skins/skin.json"));
        title = new Label(joc.getTitol(),skin, "groc");
		/*
		 * Crear el mon on es desenvolupa el joc. S'indica la gravetat: negativa
		 * perquè indica cap avall
		 */
		world = new World(new Vector2(0.0f, -9.8f), true);
		comprovarMidesPantalla();
		carregarMapa();
		carregarObjectes();
		carregarMusica();
        lastTime=0;
        bodyDestroyList= new ArrayList<Body>();
        gestorContactes=new GestorContactes(bodyDestroyList);
        // --- si es volen destruir objectes, descomentar ---
		world.setContactListener(gestorContactes);
		//world.setContactListener(new GestorContactes());

		// crear el personatge
        personatge = new Personatge(world,Personatge.HERO);
        bird = new Bird(world,"imatges/angrybirdSprite_sensefons.png",null,1.0f, 10.0f, 5,3, Bird.BIRD);
        //enemic = new Enemy(world,"imatges/pumaSprite.png","imatges/puma_s.png",1.0f, 3.0f, Enemy.ENEMIC1);
        tornado= new Tornado(world,"imatges/epictornadogran.png","imatges/qtornado.png",-1.0f, 4.5f, 5, 3, Tornado.TORNADO);


        enemyList=new ArrayList<Enemy>();
        crapList= new ArrayList<Crap>();
        ImagesPath crapPath = new ImagesPath("imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif");
        imagesPath= new ArrayList<ImagesPath>();
        imagesPath.add(new ImagesPath(/*"imatges/puma_s.png"*/null,"imatges/pumaSprite.png"));
        imagesPath.add(new ImagesPath(/*"imatges/avestru.png"*/null,"imatges/vestrus.png"));
        imagesPath.add(new ImagesPath(/*"imatges/shark.png"*/null,"imatges/sharkftw.png"));

        // objecte que permet debugar les col·lisions
		debugRenderer = new Box2DDebugRenderer();

	}

    /**
     * Moure la càmera en funció de la posició del personatge
     */
	private void moureCamera() {

        // Posicionem la camera centran-la on hi hagi l'sprite del protagonista
        tiledMapHelper.getCamera().position.x = JocDeTrons.PIXELS_PER_METRE
                * tornado.getPositionBody().x+400;

        //tiledMapHelper.getCamera().position.x = 600;

            // Assegurar que la camera nomes mostra el mapa i res mes
            //Gdx.app.log("ScreenWidth",String.valueOf(joc.getScreenWidth()));
            /*if (tiledMapHelper.getCamera().position.x < joc.getScreenWidth() / 2) {
                tiledMapHelper.getCamera().position.x = joc.getScreenWidth()/ 2;
            }
            if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth()
                    -  joc.getScreenWidth()/ 2) {
                tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
                        - joc.getScreenWidth()/ 2;
            }

            if (tiledMapHelper.getCamera().position.y < joc.getScreenHeight() / 2) {
                tiledMapHelper.getCamera().position.y = joc.getScreenHeight()/ 2;
            }
            if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
                    - joc.getScreenHeight() / 2) {
                tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight()
                        - joc.getScreenHeight() / 2;
            }*/

        if (tiledMapHelper.getCamera().position.x < 600){
            tiledMapHelper.getCamera().position.x = 600;
        }

            // actualitzar els nous valors de la càmera
            tiledMapHelper.getCamera().update();



	}
/*
    @Override
    public boolean keyUp(int keycode) {
        personatge.inicialitzarMoviments();
        if(keycode == Input.Keys.DPAD_RIGHT) {
            personatge.setMoureDreta(true);
        } else if(keycode == Input.Keys.DPAD_LEFT) {
            personatge.setMoureEsquerra(true);
        } else if(keycode == Input.Keys.DPAD_UP) {
            personatge.setFerSalt(true);
        }

        //personatge.moure();
        //personatge.updatePosition();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        personatge.inicialitzarMoviments();

        if (screenX > Gdx.graphics.getWidth() * 0.80f) {
            personatge.setMoureDreta(true);
        } else if(screenX > Gdx.graphics.getWidth() * 0.20f) {
            personatge.setMoureEsquerra(true);
        } else if(screenY < Gdx.graphics.getHeight() * 0.20f) {
            personatge.setFerSalt(true);
        }
        //personatge.moure();
        //personatge.updatePosition();
        return true;
    }
    */
    /**
     * tractar els events de l'entrada
     */
	private void tractarEventsEntrada() {


		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			personatge.setMoureDreta(true);

		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() > Gdx.graphics.getWidth() * 0.80f) {
					personatge.setMoureDreta(true);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			personatge.setMoureEsquerra(true);
            //enemic.setMoureEsquerra(true);
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getX() < Gdx.graphics.getWidth() * 0.20f) {
					personatge.setMoureEsquerra(true);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
			personatge.setFerSalt(true);
		} else {
			for (int i = 0; i < 2; i++) {
				if (Gdx.input.isTouched(i)
						&& Gdx.input.getY() < Gdx.graphics.getHeight() * 0.20f) {
					personatge.setFerSalt(true);
				}
			}
		}
	}

    /**
     * comprovar les mides de la pantalla
     */
	private void comprovarMidesPantalla() {
		/**
		 * Si la mida de la finestra de dibuix no està definida, la
		 * inicialitzem
		 */
		if (joc.getScreenWidth() == -1) {
			joc.setScreenWidth(JocDeTrons.WIDTH);
			joc.setScreenHeight(JocDeTrons.HEIGHT);
		}
	}


	/**
	 * Carrega el mapa del joc a partir d'un fitxer TMX
	 */
	private void carregarMapa() {
		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.setPackerDirectory("world/level1/packer");
		tiledMapHelper.loadMap("world/level1/packer/theFinalMap.tmx");
		tiledMapHelper.prepareCamera(joc.getScreenWidth(),
                joc.getScreenHeight());

	}

	/**
	 * Carregar i reproduir l'arxiu de música de fons
	 */
	public void carregarMusica() {
		musica = Gdx.audio.newMusic(Gdx.files
				.internal("sons/gameOfThrones.mp3"));
		musica.setLooping(true);
		musica.setVolume(0.5f);
		musica.play();
	}

	/**
	 * Càrrega dels objectes que defineixen les col·lisions
	 */
	private void carregarObjectes() {
		MapBodyManager mapBodyManager = new MapBodyManager(world,
				JocDeTrons.PIXELS_PER_METRE,
				Gdx.files.internal("world/level1/materials.json"), 1);
		mapBodyManager.createPhysics(tiledMapHelper.getMap(), "Box2D");
	}
	
	// ----------------------------------------------------------------------------------
	// MÈTODES SOBREESCRITS DE AbstractScreen
	// ----------------------------------------------------------------------------------
	
	@Override
	public void render(float delta) {

		personatge.inicialitzarMoviments();
        bird.inicialitzarMoviments(personatge);
        //enemic.inicialitzarMoviments();
        tornado.inicialitzarMoviments();
		tractarEventsEntrada();
        spawnEnemy();

        //enemic.setMoureDreta(true);
        /*
        MOU L'ENEMIC SEGONS LA POSICIÓ DEL JUGADOR
         */

        //checkMovimentEnemic(enemic);

        //Gdx.app.log("ViewPort height", String.valueOf(tiledMapHelper.getCamera().viewportHeight));
        //Gdx.app.log("ViewPort width",String.valueOf(tiledMapHelper.getCamera().viewportWidth));
        //Gdx.app.log("Position x",String.valueOf(tiledMapHelper.getCamera().position.x));
        //Gdx.app.log("Position y",String.valueOf(tiledMapHelper.getCamera().position.y));

        tornado.moure();
        tornado.updatePosition(delta);
	    personatge.moure();
        personatge.updatePosition(delta);
        bird.moure();
        bird.updatePosition(delta);
        moveEnemies(delta);
        moveCraps(delta);


        String enemyJump=null;
        if ((enemyJump=gestorContactes.enemyMustJump())!=null){
            gestorContactes.resetEnemyName();
            //enemic.setFerSalt(true);
        }



//       enemic.moure();
  //     enemic.updatePosition(delta);


        /**
         * Cal actualitzar les posicions i velocitats de tots els objectes. El
         * primer paràmetre és la quanitat de frames/segon que dibuixaré
         * El segon i tercer paràmetres indiquen la quantitat d'iteracions per
         * la velocitat i per tractar la posició. Un valor alt és més
         * precís però més lent.
         */
        world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);

        //if(!world.isLocked()){
            //eliminarCossos();
        //}

        // Esborrar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Color de fons marro
        Gdx.gl.glClearColor(185f / 255f, 122f / 255f, 87f / 255f, 0);
        eliminarCossos();
		moureCamera();
        tiledMapHelper.createCameraLimit(world, tornado.getCos().getPosition().x + 0.7f);


		// pintar el mapa
		tiledMapHelper.render();
		// Preparar l'objecte SpriteBatch per dibuixar la resta d'elements
		batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		// iniciar el lot
		batch.begin();
    		personatge.dibuixar(batch);
            bird.dibuixar(batch);
            //enemic.dibuixar(batch);
            tornado.dibuixar(batch);
            //checkCrapList();
        printEnemies();
        printCraps();

	    	// finalitzar el lot: a partir d'aquest moment es dibuixa tot el que
		    // s'ha indicat entre begin i end
		batch.end();



        // dibuixar els controls de pantalla
        stage.act();
        stage.draw();
        //Gdx.app.log("Tornado position:", String.valueOf(tornado.getCos().getPosition().x));
        //Gdx.app.log("Hero position:",String.valueOf(personatge.getCos().getPosition().x));



        debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
                JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
                JocDeTrons.PIXELS_PER_METRE));


	}

    private long lastTime;



    private void printCraps(){
        for(Crap x : crapList) {
            x.dibuixar(batch);
        }
    }

    private void moveCraps(float delta){
        for(Crap x : crapList) {
            x.inicialitzarMoviments();
            x.moure();
            x.updatePosition(delta);
        }
    }

    private void printEnemies(){
        for(Enemy x : enemyList) {
            x.dibuixar(batch);
        }
    }
    private void moveEnemies(float delta){
        for(Enemy x : enemyList){
            x.inicialitzarMoviments();
            checkMovimentEnemic(x);
            //x.setMoureDreta(true);
            x.moure();
            x.updatePosition(delta);
        }
    }
    private void spawnEnemy(){
        //Gdx.app.log("time", String.valueOf(getSysTime()-lastTime));
        Collections.shuffle(imagesPath);
        int frame_rows = 4, frame_cols = 7;

        if(getSysTime()-lastTime>5){

            if(imagesPath.get(0).getAnimatedImage().toString().compareToIgnoreCase("imatges/pumaSprite.png") == 0){
                frame_cols = 5;
                frame_rows = 2;
            } else if(imagesPath.get(0).getAnimatedImage().toString().compareToIgnoreCase("imatges/vestrus.png") == 0){
                frame_cols = 7;
                frame_rows = 2;
            } else if(imagesPath.get(0).getAnimatedImage().toString().compareToIgnoreCase("imatges/sharkftw.png") == 0){
                frame_cols = 7;
                frame_rows = 2;
            }

            if(enemyList.size() < 4 ){
                Enemy e = new Enemy(world,imagesPath.get(0).getAnimatedImage(),imagesPath.get(0).getStoppedImage(),tornado.getCos().getPosition().x+2, tornado.getCos().getPosition().y, frame_cols, frame_rows, Enemy.ENEMIC1);
                enemyList.add(e);
            }



            crapList.add(new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",bird.getCos().getPosition().x,bird.getCos().getPosition().y-1,Crap.CRAP));
            lastTime=getSysTime();


        }

    }

    private long getSysTime(){

        return System.currentTimeMillis()/1000;
    }

    /***
     *  Retorna true si el cos rebut per parametre es troba dins l'array d'enemics i
     *  l'elimina
     *  en cas contrari retorna false*/
    private boolean removeThatBody(Body b){
        boolean resultat = false;
        for(Enemy e : enemyList){
            if(e.getCos().equals(b)){
                resultat = true;
                enemyList.remove(e);
                break;
            }
        }
        return resultat;
    }

    private void eliminarCossos() {
        /*
		 * per destruir cossos marcats per ser eliminats
		 */
        //bodyDestroyList = gestorContactes.getBodyDestroyList();
        if (!bodyDestroyList.isEmpty()){
            Gdx.app.log("Destroy ", String.valueOf(bodyDestroyList.size()));
            for (Body b : bodyDestroyList) {
                Gdx.app.log("Destroy", b.getUserData().toString());
                world.destroyBody(b);
                removeThatBody(b);
                crapList.clear();
                //enemyList.remove(b);
                //crapList.clear();
            }
            bodyDestroyList.clear();
            //gestorContactes.setBodyDestroyList(destroy);
        }

    }

    private void checkMovimentEnemic(Enemy enemic) {
        if(personatge.isAlive()) {
            if (enemic.getPositionBody().x < personatge.getPositionBody().x) {
                enemic.setMoureDreta(true);
            } else if (enemic.getPositionBody().x == personatge.getPositionBody().x) {
                enemic.setMoureEsquerra(true);
            } else {
                enemic.setMoureEsquerra(true);
            }
        }
    }

    @Override
	public void dispose() {
		musica.stop();
		musica.dispose();
		world.dispose();
		personatge.dispose();
        bird.dispose();
        //enemic.dispose();
        tornado.dispose();
        for(Enemy p : enemyList){
            p.dispose();
        }
        for(Crap c : crapList){
            c.dispose();
        }
	}

    public void show() {
        // Els elements es mostren en l'ordre que s'afegeixen.
        // El primer apareix a la part superior, el darrer a la part inferior.
        table.center().top();
        Cell cell = table.add(title).padTop(5);
        table.setFillParent(true);
        stage.addActor(table);
    }

}
