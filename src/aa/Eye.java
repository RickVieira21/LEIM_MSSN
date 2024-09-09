package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import setup.SubPlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Eye {
	private List<Body> allTrackingBodies;
	private List<Body> farSight;
	private List<Body> nearSight;
	private Boid me;
	protected Body target;

	public Eye(Boid me, List<Body> allTrackingBodies) {
		this.me = me;
		this.allTrackingBodies = allTrackingBodies;
		if(allTrackingBodies.size() > 0) target = this.getRandomTarget();
	}

	public Eye(Boid me, Eye eye) {
		allTrackingBodies = eye.allTrackingBodies;
		this.me = me;
		target = eye.getRandomTarget();
	}

	public Body getNear() {
		if(allTrackingBodies.size() == 0) {
			return null;
		}
		Body t = allTrackingBodies.get(0);
		float dist = (PVector.sub(me.getPos(), t.getPos())).mag();
		for (Body b : allTrackingBodies) {
			float dif =(PVector.sub(me.getPos(), b.getPos())).mag() ;
			if(dif < dist) {
				dist = dif;
				t = b;
			}
		}
		target = t;
		return t;
	}

	public List<Body> getFarSight(){
		return farSight;
	}

	public List<Body> getNearSight(){
		return nearSight;
	}

	public Body nextTarget(){
		if (allTrackingBodies.size() == 0){
			return null;
		}
		//começamos por ver a distância entre primeiro o primeiro body e o hunter (no caso do hunter)
		Body body = allTrackingBodies.get(0);
		//vemos a distancia de referencia
		float dist = PVector.sub(me.getPos(), body.getPos()).mag();
		for (Body aux : allTrackingBodies){
			float newdist =  PVector.sub(me.getPos(),aux.getPos()).mag();
			//se a distacia for menor que a anterior atualizamos a menor distancia e atualizamos o body certo
			if (newdist < dist){
				dist =  newdist;
				body = aux;
			}
		}
		target = body;
		return body;
	}

	public void look() {
		farSight = new ArrayList<Body>();
		nearSight = new ArrayList<Body>();
		for(Body b : allTrackingBodies) {
			if(farSight(b.getPos())) {
				farSight.add(b);
			}
			if(nearSight(b.getPos())) {
				nearSight.add(b);
			}
		}
	}

	private boolean inSight(PVector t, float maxDistance, float maxAngle) {
		PVector r = PVector.sub(t,  me.getPos());
		float d = r.mag();
		float angle = PVector.angleBetween(r, me.getVel());
		return ((d>0) && (d< maxDistance) && (angle < maxAngle));
	}

	public boolean farSight(PVector t) {
		return inSight(t, me.dna.visionDistance, me.dna.visionAngle);
	}

	public boolean nearSight(PVector t) {
		return inSight(t, me.dna.visionSafeDistance, (float)Math.PI);
	}

	public void setTarget(Body t) {
		target = t;
	}

	public void removeTarget(Body t) {
		allTrackingBodies.remove(t);
	}

	public Body getRandomTarget() {
		int rnd = new Random().nextInt(allTrackingBodies.size());
		return allTrackingBodies.get(rnd);
	}

	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		p.pushMatrix();
		float[] pp = plt.getPixelCoord(me.getPos().x, me.getPos().y);
		p.translate(pp[0],  pp[1]);
		p.rotate(-me.getVel().heading());
		p.noFill();
		p.stroke(255,0,0);
		p.strokeWeight(3);
		float[] dd1 = plt.getVectorCoord(me.dna.visionDistance, me.dna.visionDistance);
		float[] dd2 = plt.getVectorCoord(me.dna.visionSafeDistance, me.dna.visionSafeDistance);
		p.rotate(me.dna.visionAngle);
		p.line(0,  0,  dd1[0], 0);
		p.rotate(-2*me.dna.visionAngle);
		p.line(0,  0,  dd1[0], 0);
		p.rotate(me.dna.visionAngle);
		p.arc(0,  0,  2*dd1[0], 2*dd1[0], -me.dna.visionAngle, me.dna.visionAngle);
		p.stroke(255, 0, 255);
		p.circle(0, 0, 2*dd2[0]);
		p.popMatrix();
		p.popStyle();
	}
}