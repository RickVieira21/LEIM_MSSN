package ecossistema;

import aa.Boid;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.SubPlot;

import java.util.List;

public class Predator extends Animal {

	private PApplet parent;
	private SubPlot plt;
	protected PImage img;

	protected Predator(PVector pos, float predatorVel, float predatorForce, float predatormass, float predatorradius, int color, PApplet parent, SubPlot plt, PImage img) {
		super(pos, predatorVel, predatorForce, predatormass, predatorradius, color, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = Variables.INI_PREDATOR_ENERGY;
		this.img = img;
	}

	protected Predator(Predator predator, boolean mutate, PApplet parent, SubPlot plt, PImage img) {
		super(predator, mutate, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = Variables.INI_PREDATOR_ENERGY;
		this.img = img;
	}

	@Override
	public void eat(Terrain terrain, Animal target){
		if (target != null) {
			if (!(target.isDead())) {
				float dist = PVector.dist(pos, target.getPos());
				if (dist < 3 * target.getRadius()) {
					energy += Variables.ENERGY_FROM_PREY;
					target.setDead(true);
				}
			}
		}
	}

	@Override
	public Animal reproduce(boolean mutate) {
		Animal child = null;
		if (energy > Variables.PREDATOR_ENERGY_TO_REPRODUCE){
			energy -= Variables.INI_PREDATOR_ENERGY;
			child = new Predator(this, mutate, parent, plt, img);
			if (mutate) child.mutateBehaviors();
		}
		return child;
	}

	
	@Override
	public void display(PApplet p, SubPlot plt) {
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		//a,b coordenadas da imagem x,y  ;   c,d largura e altura de cada imagem
		p.image(img, pp[0]-15f, pp[1]-15f, 30f, 30f);
	}

	@Override
	public void setImg(PImage img) {
		this.img = img;
	}
}