package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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


    public final static short WORLD_ENTITY = 2;
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
    private Enemy enemic;
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
    //private ArrayList<Body> bodyDestroyList;
	

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
        gestorContactes=new GestorContactes();
        // --- si es volen destruir objectes, descomentar ---
		ArrayList<Body> bodyDestroyList= new ArrayList<Body>();
		world.setContactListener(new GestorContactes(bodyDestroyList));
		world.setContactListener(gestorContactes);

		// crear el personatge
        personatge = new Personatge(world,Personatge.HERO);
        bird = new Bird(world,"imatges/angrybirdSprite_sensefons.png",null,1.0f, 8.0f, 5,3, Bird.BIRD);
        enemic = new Enemy(world,"imatges/pumaSprite.png","imatges/puma_s.png",1.0f, 3.0f, Enemy.ENEMIC1);
        tornado= new Tornado(world,"imatges/epictornadogran.png","imatges/qtornado.png",2.0f, 5.0f, 5, 3, Tornado.TORNADO);


        enemyList=new ArrayList<Enemy>();
        crapList= new ArrayList<Crap>();
        ImagesPath crapPath = new ImagesPath("imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif");
        imagesPath= new ArrayList<ImagesPath>();
        imagesPath.add(new ImagesPath(/*"imatges/puma_s.png"*/null,"imatges/pumaSprite.png"));
        imagesPath.add(new ImagesPath(/*"imatges/avestru.png"*/null,"imatges/vestruç.png"));
        imagesPath.add(new ImagesPath(/*"imatges/shark.png"*/null,"imatges/sharkftw.png"));

        // objecte que permet debugar les col·lisions
		//debugRenderer = new Box2DDebugRenderer();
	}

    /**
     * Moure la càmera en funció de la posició del personatge
     */
	private void moureCamera() {
		// Posicionem la camera centran-la on hi hagi l'sprite del protagonista
		tiledMapHelper.getCamera().position.x = JocDeTrons.PIXELS_PER_METRE
				* personatge.getPositionBody().x;

		// Assegurar que la camera nomes mostra el mapa i res mes
		if (tiledMapHelper.getCamera().position.x <  joc.getScreenWidth() / 2) {
			tiledMapHelper.getCamera().position.x =  joc.getScreenWidth()/ 2;
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
         bird.inicialitzarMoviments();
         //enemic.inicialitzarMoviments();
        tornado.inicialitzarMoviments();
		 tractarEventsEntrada();
        spawnEnemy();

        //enemic.setMoureDreta(true);
        /*
        MOU L'ENEMIC SEGONS LA POSICIÓ DEL JUGADOR
         */

        checkMovimentEnemic(enemic);

        tornado.moure();
        tornado.updatePosition();
	     personatge.moure();
         personatge.updatePosition();
        bird.moure();
        bird.updatePosition();
        moveEnemies();
        moveCraps();


        String enemyJump=null;
        if ((enemyJump=gestorContactes.enemyMustJump())!=null){
            gestorContactes.resetEnemyName();
        //    enemic.setFerSalt(true);
        }



       //enemic.moure();
        //enemic.updatePosition();

        eliminarCossos();
        /**
         * Cal actualitzar les posicions i velocitats de tots els objectes. El
         * primer paràmetre és la quanitat de frames/segon que dibuixaré
         * El segon i tercer paràmetres indiquen la quantitat d'iteracions per
         * la velocitat i per tractar la posició. Un valor alt és més
         * precís però més lent.
         */
		world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);






		// Esborrar la pantalla
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Color de fons marro
		Gdx.gl.glClearColor(185f / 255f, 122f / 255f, 87f / 255f, 0);

		moureCamera();
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
        Gdx.app.log("Tornado position:",String.valueOf(tornado.getCos().getPosition().x));
        Gdx.app.log("Tornado position:",String.valueOf(tornado.getCos().getPosition().y));


        //debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
		//		JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
		//		JocDeTrons.PIXELS_PER_METRE));
	}

    private long lastTime;



    private void printCraps(){
        for(Crap x : crapList) {
            x.dibuixar(batch);
        }
    }

    private void moveCraps(){
        for(Crap x : crapList) {
            x.inicialitzarMoviments();
            x.moure();
            x.updatePosition();
        }
    }

    private void printEnemies(){
        for(Enemy x : enemyList) {
            x.dibuixar(batch);
        }
    }
    private void moveEnemies(){
        for(Enemy x : enemyList){
            x.inicialitzarMoviments();
            checkMovimentEnemic(x);
            //x.setMoureDreta(true);
            x.moure();
            x.updatePosition();
        }
    }
    private void spawnEnemy(){
        //Gdx.app.log("time", String.valueOf(getSysTime()-lastTime));
        Collections.shuffle(imagesPath);
        int frame_rows = 2, frame_cols = 6;

        if(getSysTime()-lastTime>10 &&enemyList.size() < 1){

            if(imagesPath.get(0).getAnimatedImage().toString().compareToIgnoreCase("imatges/pumaSprite.png") == 0){
                frame_cols = 5;
                frame_rows = 2;
            } else if(imagesPath.get(0).getAnimatedImage().toString().compareToIgnoreCase("imatges/vestruç.png") == 0){
                frame_cols = 7;
                frame_rows = 4;
            } else if(imagesPath.get(0).getAnimatedImage().toString().compareToIgnoreCase("imatges/sharkftw.png") == 0){
                frame_cols = 7;
                frame_rows = 2;
            }

            Enemy e = new Enemy(world,imagesPath.get(0).getAnimatedImage(),imagesPath.get(0).getStoppedImage(),tornado.getCos().getPosition().x+2, tornado.getCos().getPosition().y, frame_cols, frame_rows, Enemy.ENEMIC1);
            enemyList.add(e);
            crapList.add(new Crap(world,"imatges/animated-bullet-21.gif","imatges/animated-bullet-21.gif",bird.getCos().getPosition().x-0.5f,bird.getCos().getPosition().y-0.5f,Crap.CRAP));
            lastTime=getSysTime();
        }
    }

    private long getSysTime(){

        return System.currentTimeMillis()/1000;
    }




    private void eliminarCossos(){
        /*
		 * per destruir cossos marcats per ser eliminats
		 */
        if(!gestorContactes.getBodyDestroyList().isEmpty()){
            for(int i = gestorContactes.getBodyDestroyList().size()-1; i >=0; i-- ) {
                Body cos = gestorContactes.getBodyDestroyList().get(i);
                if(cos.equals(Crap.CRAP)){

                    personatge.setAlive(false);
                }
                world.destroyBody(cos);
            }
            gestorContactes.getBodyDestroyList().clear();
        }
    }

    private void checkMovimentEnemic(Personatge enemic) {
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
        enemic.dispose();
        tornado.dispose();
        for(Personatge p : enemyList){
            p.dispose();
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
