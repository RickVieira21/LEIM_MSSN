package cells;

import processing.core.PApplet;
import setup.IProcessingApp;
import setup.SubPlot;

public class TestMajorityCA implements IProcessingApp {
	private double[] window = {0, 10, 0, 10};
	private float[] viewport = {0.3f, 0.3f, 0.5f, 0.6f};
	private SubPlot plt;
	private MajorityCA ca;
	@Override
	public void setup(PApplet p) {
		plt = new SubPlot(window, viewport, p.width, p.height);
		ca = new MajorityCA(p, plt, 50, 60, 4, 1);
		ca.initRandom();
	}

	@Override
	public void mousePressed(PApplet p) {
		ca.majorityRule();
	}

	@Override
	public void keyPressed(PApplet p) {

	}

	@Override
	public void draw(PApplet p, float dt) {
		ca.display(p);
	}
}