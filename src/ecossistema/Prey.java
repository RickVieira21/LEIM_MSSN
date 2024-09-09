package ecossistema;

import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.SubPlot;

import java.util.List;

public class Prey extends Animal {

	private PApplet parent;
	private SubPlot plt;
	protected PImage img;

	public Prey(PVector pos, float preyVel, float preyForce, float preyMass, float preySize, int color, PApplet parent, SubPlot plt, PImage img) {
		super(pos, preyVel, preyForce, preyMass, preySize, color, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = Variables.INI_PREY_ENERGY;
		this.img = img;
	}

	public Prey(Prey prey, boolean mutate, PApplet parent, SubPlot plt, PImage img) {
		super(prey, mutate, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = Variables.INI_PREY_ENERGY;
		this.img = img;
	}

	//Quando a presa come -> setFertile
	@Override
	public void eat(Terrain terrain, Animal target){
		Patch patch = (Patch) terrain.world2Cell(this.pos.x, this.pos.y);
		if (patch.getState() == Variables.PatchType.GRASS.ordinal()){
			energy += Variables.ENERGY_FROM_PLANT;
			patch.setFertile();
		}
	}

	@Override
	public Animal reproduce(boolean mutate){
		Animal child = null;
		if (energy > Variables.PREY_ENERGY_TO_REPRODUCE){
			energy -= Variables.INI_PREY_ENERGY;
			child = new Prey(this, mutate, parent, plt, img);
			if (mutate) child.mutateBehaviors();
		}
		return child;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		p.image(img, pp[0]-10f, pp[1]-10f, 20f, 20f);
	}

	@Override
	public void setImg(PImage img) {
		this.img = img;
	}
}