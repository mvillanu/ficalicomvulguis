package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;
/**
 * Classe que implementa la interface de gesti� de contactes
 * 
 * @author Marc
 *
 */
public class GestorContactes implements ContactListener {
	// de moment, no implementat
	private ArrayList<Body> bodyDestroyList;
    private String enemyName;

	public GestorContactes() {
		enemyName=null;
        bodyDestroyList = new ArrayList<Body>();

	}

    public String enemyMustJump(){
        return enemyName;
    }
    public void resetEnemyName(){enemyName=null;}

    public ArrayList<Body> getBodyDestroyList(){
        return bodyDestroyList;
    }
	public GestorContactes(ArrayList<Body> bodyDestroyList) {
		this.bodyDestroyList = bodyDestroyList;
	}
	@Override
	public void beginContact(Contact contact) {

		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Gdx.app.log("beginContact", "entre " + fixtureA.getBody().getUserData() + " i "
				+ fixtureB.getBody().getUserData().toString());

		if (fixtureA.getBody().getUserData() == null
				|| fixtureB.getBody().getUserData() == null) {
			return;
		}

        checkCrapContact(fixtureA,fixtureB);

        /*if(fixtureA.getBody().getUserData().equals(Crap.CRAP)&&!fixtureB.getBody().getUserData().equals(Crap.CRAP)){
            bodyDestroyList.add(fixtureA.getBody());
            Gdx.app.log("Shit on the ground!","pringue");
        } else {
            bodyDestroyList.add(fixtureB.getBody());
            Gdx.app.log("Shit on the ground!","pringue");
        }*/


        //checkEsborrar(fixtureA, fixtureB);

        chechEnemyContact(fixtureA, fixtureB);

		if (fixtureA.getBody().getUserData().equals("Personatge")
				&& fixtureB.getBody().getUserData().equals("primerObjecte")
				|| fixtureA.getBody().getUserData().equals("primerObjecte")
				&& fixtureB.getBody().getUserData().equals("Personatge")) {

			Gdx.app.log("HIT", "stark ha topat amb el primer objecte");
			/*
			 * Afegir cos a destruir
			 *
			 * if(!fixtureA.getBody().getUserData().equals("stark")) {
				bodyDestroyList.add(fixtureA.getBody());
			} else {
				bodyDestroyList.add(fixtureB.getBody());
			}*/
		}


	}

    private void chechEnemyContact(Fixture fixtureA, Fixture fixtureB) {
        if ((fixtureA.getBody().getUserData().equals(Enemy.ENEMIC1)
                && fixtureB.getBody().getUserData().toString().startsWith("enemy"))
                || fixtureA.getBody().getUserData().toString().startsWith("enemy")){
            enemyName=Enemy.ENEMIC1;

        }
    }

    private void checkEsborrar(Fixture fixtureA, Fixture fixtureB) {
        if((fixtureA.getBody().getUserData().equals(Enemy.ENEMIC1)||fixtureB.getBody().getUserData().equals(Enemy.ENEMIC1))&&
                (fixtureA.getBody().getUserData().equals("Personatge")||fixtureB.getBody().getUserData().equals("Personatge"))){

            if(fixtureA.getBody().getUserData().equals("Personatge")){
                bodyDestroyList.add(fixtureA.getBody());
            }else{
                bodyDestroyList.add(fixtureB.getBody());
            }

        }

        if((fixtureA.getBody().getUserData().equals(Tornado.TORNADO)||fixtureB.getBody().getUserData().equals(Tornado.TORNADO))&&
                (fixtureA.getBody().getUserData().equals("Personatge")||fixtureB.getBody().getUserData().equals("Personatge"))){

            if(fixtureA.getBody().getUserData().equals("Personatge")){
                bodyDestroyList.add(fixtureA.getBody());
            }else{
                bodyDestroyList.add(fixtureB.getBody());
            }

        }
    }



    private void checkCrapContact(Fixture fixtureA, Fixture fixtureB){
        if (fixtureA.getBody().getUserData().equals(Enemy.ENEMIC1)
                && fixtureB.getBody().getUserData().equals(Crap.CRAP)
                || fixtureA.getBody().getUserData().equals(Crap.CRAP)
                && fixtureB.getBody().getUserData().equals(Enemy.ENEMIC1)) {
                bodyDestroyList.add(fixtureA.getBody());
            Gdx.app.log("Shit on the enemy!","pringue");
                bodyDestroyList.add(fixtureB.getBody());

        }

        if(fixtureA.getBody().getUserData().equals(Crap.CRAP)&&fixtureB.getBody().getUserData().equals("terra1")||
                fixtureA.getBody().getUserData().equals("terra1")&&fixtureB.getBody().getUserData().equals(Crap.CRAP) ){
            if(fixtureA.getBody().getUserData().equals(Crap.CRAP)){
                bodyDestroyList.add(fixtureA.getBody());
                Gdx.app.log("Shit on the ground!","pringue");
            }else{
                bodyDestroyList.add(fixtureB.getBody());
                Gdx.app.log("Shit on the ground!","pringue");
            }
        }
    }


    @Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
