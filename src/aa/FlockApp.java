package aa;

import aa.Flock;
import setup.IProcessingApp;
import setup.SubPlot;
import processing.core.PApplet;

//Só para desenhar Flocks
public class FlockApp implements IProcessingApp {
	private Flock flock;
	private float[] sacWeights = {0.33f, 0.33f, 0.33f};
	private double[] window = {-10, 10, -10, 10};
	private float[] viewport = {0, 0, 1, 1};
	private SubPlot plt;


	@Override
	public void setup(PApplet p) {
		plt = new SubPlot(window, viewport, p.width, p.height);
		flock = new Flock(150, 0.1f, 0.3f, p.color(0, 0, 0), sacWeights,p, plt);
	}

	@Override
	public void mousePressed(PApplet p) {

	}

	@Override
	public void keyPressed(PApplet p) {

	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(255,255,255);
		flock.applyBehavior(dt);
		flock.display(p,plt);
	}
}