package com.mygdx.game;

/**
 *   Copyright 2011 David Kirchner 
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 * TiledMapHelper can simplify your game's tiled map operations. You can find
 * some sample code using this class at my blog:
 * 
 * http://dpk.net/2011/05/08/libgdx-box2d-tiled-maps-full-working-example-part-2/
 * 
 * Note: This code does have some limitations. It only supports single-layered
 * maps.
 * 
 * This code is based on TiledMapTest.java found at:
 * http://code.google.com/p/libgdx/
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class TiledMapHelper {
	private FileHandle packFileDirectory;
	private OrthographicCamera camera;
	private TextureAtlas tileAtlas;
	private TiledMapRenderer tileMapRenderer;
	private Array<Body> screenLimits = new Array<Body>();
	private TiledMap map;
	private float limitCameraLeft = 1.7f;
	private float limitCameraRight = 20.7f;

	/**
	 * Renders the part of the map that should be visible to the user.
	 */
	public void render() {
		tileMapRenderer.setView(camera);
		tileMapRenderer.render();
	}

	public float getLimitCameraLeft(){
		return limitCameraLeft;
	}
	public void setLimitCameraLeft(float limitCameraLeft){
		this.limitCameraLeft = limitCameraLeft;
	}
	public float getLimitCameraRight(){
		return limitCameraRight;
	}
	public void setLimitCameraRight(float limitCameraRight){
		this.limitCameraLeft = limitCameraRight;
	}
	
	public int getMapHeight() {		
		return map.getProperties().get("height", Integer.class);
	}
	
	public int getMapWidth() {
		return map.getProperties().get("width", Integer.class);
	}

	public int getTileHeight() {
		return map.getProperties().get("tileheight", Integer.class);
	}
	
	public int getTileWidth() {
		return map.getProperties().get("tilewidth", Integer.class);
	}
	
	/**
	 * Get the height of the map in pixels
	 * 
	 * @return y
	 */
	public int getHeight() {
		return getMapHeight() * getTileHeight();
	}

	/**
	 * Get the width of the map in pixels
	 * 
	 * @return x
	 */
	public int getWidth() {
		return getMapWidth() * getTileWidth();
	}

	/**
	 * Get the map, useful for iterating over the set of tiles found within
	 * 
	 * @return TiledMap
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Calls dispose on all disposable resources held by this object.
	 */
	public void dispose() {
		tileAtlas.dispose();
	}

	/**
	 * Sets the directory that holds the game's pack files and tile sets.
	 * 
	 * @param packDirectory
	 */
	public void setPackerDirectory(String packDirectory) {
		packFileDirectory = Gdx.files.internal(packDirectory);
	}

	/**
	 * Loads the requested tmx map file in to the helper.
	 * 
	 * @param tmxFile
	 */
	public void loadMap(String tmxFile) {
		if (packFileDirectory == null) {
			throw new IllegalStateException("loadMap() called out of sequence");
		}

		map = new TmxMapLoader().load(tmxFile);
		tileMapRenderer = new OrthogonalTiledMapRenderer(map);
	}


	/**
	 * Prepares the helper's camera object for use.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void prepareCamera(int screenWidth, int screenHeight) {
		camera = new OrthographicCamera(screenWidth, screenHeight);
		//camera.setToOrtho(false, 600 / getMapWidth(), 320 / getMapHeight());
        camera.zoom += 0.5;
		camera.position.set(600, screenHeight, 0);

	}

    public void setScreenLimit(OrthographicCamera camera){
        //camera.position.x;
    }

	/**
	 * Returns the camera object created for viewing the loaded map.
	 * 
	 * @return OrthographicCamera
	 */
	public OrthographicCamera getCamera() {
		if (camera == null) {
			throw new IllegalStateException(
					"getCamera() called out of sequence");
		}
		return camera;
	}

	public void moveCameraLimits(float a, float b){
		limitCameraLeft = a;
		limitCameraRight = b;
	}

	public void createCameraLimit(World world, float a){

		limitCameraLeft = a;
		limitCameraRight = limitCameraLeft+8.8f;

		if(screenLimits.size != 0) {
			for (Body screen : screenLimits) {
				world.destroyBody(screen);
				screen.setUserData(null);
			}
			screenLimits.clear();
		}

		//Colisions limits esquerra camera
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.StaticBody;
		bodyDef2.position.set(0,0);
		FixtureDef fixtureDef2 = new FixtureDef();
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(limitCameraLeft,0,limitCameraLeft,camera.position.y);

		/*Gdx.app.log("ViewPort height", String.valueOf(camera.viewportHeight));
		Gdx.app.log("ViewPort width",String.valueOf(camera.viewportWidth));
		Gdx.app.log("Position x",String.valueOf(camera.position.x));
		Gdx.app.log("Position y", String.valueOf(camera.position.y));*/

		fixtureDef2.shape = edgeShape;
		fixtureDef2.filter.categoryBits = ColisionsGroups.MAP_ENTITY;
		fixtureDef2.filter.maskBits = ColisionsGroups.HERO_ENTITY | ColisionsGroups.ENEMIC_ENTITY;
		Body bodyEdgeScreenLeft = world.createBody(bodyDef2);
		bodyEdgeScreenLeft.createFixture(fixtureDef2);
		bodyEdgeScreenLeft.setUserData("ScreenLeft");
		screenLimits.add(bodyEdgeScreenLeft);
		fixtureDef2.shape = null;
		edgeShape.dispose();

		//Colisions limit dret camera
		BodyDef bodyDef4 = new BodyDef();
		bodyDef4.type = BodyDef.BodyType.StaticBody;
		bodyDef4.position.set(0,0);
		FixtureDef fixtureDef4 = new FixtureDef();
		EdgeShape edgeShapeRight = new EdgeShape();
		edgeShapeRight.set(limitCameraRight,0,limitCameraRight,camera.position.y);
		fixtureDef4.shape = edgeShapeRight;
		fixtureDef4.filter.categoryBits = ColisionsGroups.MAP_ENTITY;
		fixtureDef4.filter.maskBits = ColisionsGroups.HERO_ENTITY | ColisionsGroups.ENEMIC_ENTITY;;
		Body bodyEdgeScreenRight = world.createBody(bodyDef4);
		bodyEdgeScreenRight.createFixture(fixtureDef4);
		bodyEdgeScreenRight.setUserData("ScreenRight");
		screenLimits.add(bodyEdgeScreenRight);
		fixtureDef4.shape = null;
		edgeShapeRight.dispose();

	}

}
