package physics;

import setup.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class RigidBody {
	private PVector pos;
	private PVector vel;
	private PVector acc;
	private float mass, radius;
	private int[] colour;

	private static double G = 6.67e-11;

	public RigidBody(PVector pos, PVector vel, float mass, float radius, int[] colour){
		this.pos = pos.copy();
		this.vel = vel;
		this.acc = new PVector();
		this.mass = mass;
		this.radius = radius;
		this.colour = colour;
	}
	public PVector getPos() {
		return pos;
	}
	public void setPos(PVector pos){
		this.pos = pos;
	}

	public void setVel(PVector vel){
		this.vel = vel;
	}

	public int[] getColour() {
		return colour;
	}

	public float getRadius() {
		return radius;
	}

	public void applyForce(PVector force){
		acc.add(PVector.div(force,  mass));
	}

	public PVector getAttraction(RigidBody rb){
		PVector r = PVector.sub(this.pos, rb.pos);
		float dist = r.mag();
		float strenght = (float)(this.G*this.mass*rb.mass / Math.pow(dist,2));
		return r.normalize().mult(strenght);
	}

	public void move(float dt){
		vel.add(acc.mult(dt));
		pos.add(PVector.mult(vel, dt));
		acc.mult(0);
	}

	public void display(PApplet p, SubPlot plt){
		p.pushStyle();
		float[] coord = plt.getPixelCoord(pos.x, pos.y);
		float[] dim = plt.getDimInPixel(radius*60,radius*60);
		p.noStroke();
		p.fill(colour[0], colour[1], colour[2]);
		p.circle(coord[0], coord[1],2+dim[0]);
		p.popStyle();
	}
}