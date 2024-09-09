package aa;

import setup.IProcessingApp;
import setup.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class BoidApp implements IProcessingApp {

    private Boid b;
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport = {0, 0, 1, 1};
    private SubPlot plt;

    @Override
    public void setup(PApplet p) {
        plt = new SubPlot(window, viewport, p.width, p.height);
        b = new Boid(new PVector(), 1, 0.5f, p.color(0), plt, p);
        b.addBehavior(new Wander(1f));  
    }

    @Override
    public void draw(PApplet p, float dt) {
        p.background(255);
        b.applyBehaviors(dt);
        b.display(p, plt);
    }

    @Override
    public void mousePressed(PApplet p) {

    }

    @Override
    public void keyPressed(PApplet p) {

    }

}
