package ecossistema;

import aa.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.SubPlot;

public abstract class Animal extends Boid implements IAnimal {
	protected float energy;
	private Animal target;
	protected PImage img;


	protected Animal(PVector pos, float vel, float mForce, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, vel, mForce, mass, radius, color, plt, p);
	}

	
	protected Animal(Animal a, boolean mutate, PApplet p, SubPlot plt) {
		super(a.pos, a.getMass(), a.radius, a.color, plt, p);
		for (Behavior b : a.behaviors) {
			this.addBehavior(b);
		}
		if (a.eye != null) {
			eye = new Eye(this, a.eye);
		}
		dna = new DNA(a.dna, mutate);
	}
	

    // Método para simular o consumo de energia pelo animal
	@Override
	public void energy_consumption(float dt, Terrain terrain) {
        // Consumo de energia ao longo do tempo
		energy -= dt;
        // Consumo de energia com base na velocidade 
		energy -= getMass() * Math.pow(vel.mag(), 2) * 0.3 * dt;
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
        // Energia ganha pela água
		if (patch.getState() == Variables.PatchType.WATER.ordinal()) {         
				energy += dt;
				// só as presas são afetadas pelos lagos
		        if (this instanceof Prey) {
		            vel.mult(-1);  
		        }
		}
	}

    // energia menor que 0 -> morre
	@Override
	public boolean die() {
		return (energy < 0);
	}

	public void setTarget(Animal target) {
		this.target = target;
	}

	public Animal getTarget() {
		return target;
	}

	public float getEnergy() {
		return energy;
	}

	public void setImg(PImage img) {
		this.img = img;
	}
}
