package aa;

import setup.SubPlot;
import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Flock {

	private List<Boid> boids;

	public Flock(int nBoids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt) {
		boids = new ArrayList<Boid>();
		double[] w = plt.getWindow();
		for(int i=0; i<nBoids;i++) {
			float x = p.random((float)w[0], (float)w[1]);
			float y = p.random((float)w[2], (float)w[3]);
			 Boid b = new Boid(new PVector(x, y), mass, radius, color, plt, p);
			// Boid b = new Boid(new PVector(x, y), new PVector(0, 0), mass, radius, color, p, plt);
			b.addBehavior(new Separate(sacWeights[0]));
			b.addBehavior(new Align(sacWeights[1]));
			b.addBehavior(new Cohesion(sacWeights[2]));
			boids.add(b);
		}

		List<Body> bodies = boid2Body(boids);
		for(Boid b : boids) {
			b.setEye(new Eye(b, bodies));
		}
	}

	public List<Boid> getBoids(){
		return boids;
	}

	public void removeBoid(Boid b){
		boids.remove(b);
	}

	private List<Body> boid2Body(List<Boid> boids){
		List<Body> bodies = new ArrayList<Body>();
		for(Boid b : boids) {
			bodies.add(b);
		}
		return bodies;
	}

	public void applyBehavior(float dt) {
		for (Boid b : boids) {
			b.applyBehaviors(dt);
		}
	}

	public Boid getBoid(int i) {
		return boids.get(i);
	}

	public void display(PApplet p, SubPlot plt) {
		for(Boid b : boids) {
			b.display(p, plt);
		}
	}

}