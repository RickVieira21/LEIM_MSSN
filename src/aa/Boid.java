package aa;

import processing.core.*;
import setup.SubPlot;
import physics.Body;

import java.util.ArrayList;
import java.util.List;

public class Boid extends Body {
    private SubPlot plt;
    private PShape shape;
    public DNA dna;
    protected Eye eye;
    protected float velocity, mForce;
    private double[] window;
    protected List<Behavior> behaviors;
    protected Behavior currentBehaviour;
    protected float phiWander;
    private float sumWeights;
    private boolean dead = false;

    public Boid(PVector pos, float velocity, float mForce, float mass, float radius, int color, SubPlot plt, PApplet p) {
        super(pos, new PVector(), mass, radius, color);
        behaviors = new ArrayList<Behavior>();
        this.plt = plt;
        this.velocity = velocity;
        this.mForce = mForce;
        dna = new DNA(this.velocity, this.mForce);
        window = plt.getWindow();
        setShape(p, plt);
    }

    public Boid(PVector pos, float mass, float radius, int color, SubPlot plt, PApplet p) {
        super(pos, new PVector(), mass, radius, color);
        behaviors = new ArrayList<Behavior>();
        this.plt = plt;
        dna = new DNA(velocity, mForce);
        window = plt.getWindow();
        setShape(p, plt);
    }

    public void mutateBehaviors(){
        updateSumWeights();
    }

    public DNA getDna(){
        return dna;
    }

    private void updateSumWeights() {
        sumWeights = 0;
        for(Behavior beh : behaviors) {
            sumWeights += beh.getWeight();
        }
    }

    public void setEye(Eye eye) {
        this.eye = eye;
    }

    public Eye getEye() {
        return eye;
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
        updateSumWeights();
    }

    public List<Behavior> getBehaviors(){
        return behaviors;
    }

    public void removeBehavior(Behavior behavior) {
        if(behaviors.contains(behavior)) {
            behaviors.remove(behavior);
        }
        updateSumWeights();
    }

    public void applyBehavior(float dt, int i) {
        eye.look();
        Behavior behavior = behaviors.get(i);
        this.currentBehaviour = behavior;
        PVector vd = behavior.getDesiredVelocity(this);
        move(dt, vd);
    }


    public void applyBehaviors(float dt) {
        if(eye != null) {
            eye.look();
        }
        PVector vd = new PVector();
        for(Behavior behavior : behaviors) {
            PVector vdd = behavior.getDesiredVelocity(this);
            vdd.mult(behavior.getWeight()/sumWeights);
            vd.add(vdd);
        }
        move(dt, vd);
    }

    public Behavior getCurrentBehaviour(){
        return this.currentBehaviour;
    }

    private void move(float dt, PVector vd) {
        vd.normalize().mult(dna.maxSpeed);
        PVector fs = PVector.sub(vd, vel);
        applyForce(fs.limit(dna.maxForce));
        super.move(dt);

        if(pos.x < window[0])
            pos.x += window[1] - window[0];
        if(pos.y < window[2])
            pos.y += window[3] - window[2];
        if(pos.x >= window[1])
            pos.x -= window[1] - window[0];
        if(pos.y >= window[3])
            pos.y -= window[3] - window[2];

    }

    public void setShape(PApplet p, SubPlot plt) {
        float[] rr = plt.getVectorCoord(radius, radius);
        shape = p.createShape();
        shape.beginShape();
        shape.fill(color);
        shape.noStroke();
        shape.vertex(-rr[0], rr[0]/2);
        shape.vertex(rr[0], 0);
        shape.vertex(-rr[0], -rr[0]/2);
        shape.vertex(-rr[0]/2, 0);
        shape.endShape(PConstants.CLOSE);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean b) {
        dead = b;
    }

    public float getRadius() {
        return radius;
    }

    public void display(PApplet p, SubPlot plt) {
        if(!dead) {
                p.pushMatrix();
                float[] pp = plt.getPixelCoord(pos.x, pos.y);
                p.translate(pp[0], pp[1]);
                p.rotate(-vel.heading());
                p.shape(shape);
                p.popMatrix();
            }
        }
    }
