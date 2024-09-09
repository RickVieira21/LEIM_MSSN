package physics;
import processing.core.PVector;

public class Mover {
    public PVector pos, vel, acc;
    private float mass, radius;

    public Mover(PVector pos, PVector vel, float mass){
        this.vel = vel;
        this.mass = mass;
        this.radius = radius;
        this.pos = pos.copy();
        this.acc = new PVector();
    }

    public void applyForce(PVector f){
        f.div(this.mass);
        this.acc.add(f);
    }

    public void move(float dt){
        vel.add(PVector.mult(acc,dt));
        pos.add(PVector.mult(vel,dt));
        acc.mult(0);
    }
    public void setPos(PVector pos) {
        this.pos = pos;
    }
    public PVector getPos() {
        return this.pos;
    }

    public PVector getVel() {
        return this.vel;
    }

    public void setVel(PVector vel) {
        this.vel = vel;
    }

    public float getMass() {
        return this.mass;
    }

    public float getRadius() {
        return this.radius;
    }
}