package aa;

import aa.*;
import setup.IProcessingApp;
import setup.SubPlot;
import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class HunterApp implements IProcessingApp {

    private Flock flock;
    private Boid hunter;
    private Body target;
    private float[] sacWeights = {0.33f, 0.33f, 0.33f};
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport = {0, 0, 1, 1};
    private SubPlot plt;

    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);
        flock = new Flock(20, 0.1f, 0.3f, p.color(50,50,200), sacWeights, p, plt);
        hunter = new Boid(new PVector(p.random((float)window[0],(float)window[1]), (float)window[2],(float)window[3]),
                0.1f, 0.5f, p.color(200,50,50), plt, p);
        hunter.addBehavior(new Pursuit(20f));
        hunter.addBehavior(new Wander(1f));

        List<Body> allTrackingBodies = new ArrayList<Body>();
        for(Boid b : flock.getBoids()){
            allTrackingBodies.add(b);
        }
        hunter.setEye(new Eye(hunter, allTrackingBodies));
        target = hunter.getEye().nextTarget();
    }

    private void newState(PApplet p, Body target){
        Boid aux = (Boid) target;
        if (!(aux.isDead())){
            float dist = PVector.dist(hunter.getPos(), aux.getPos());
            if (dist < 3*aux.getRadius()){
                aux.setDead(true);
                hunter.getEye().removeTarget(aux);
            }
        }
    }

    @Override
    public void mousePressed(PApplet p) {

    }

    @Override
    public void keyPressed(PApplet p) {

    }

    @Override
    public void draw(PApplet p, float dt) {
        p.background(195,195,195);
        flock.applyBehavior(dt);
        flock.display(p, plt);
        if (target == null){
            hunter.applyBehavior(dt, 1);
        }
        else {
            newState(p, target);
            if (((Boid)target).isDead()){
                target = hunter.getEye().nextTarget();
            }
            hunter.applyBehavior(dt, 0);

        }
        hunter.display(p, plt);
    }
}