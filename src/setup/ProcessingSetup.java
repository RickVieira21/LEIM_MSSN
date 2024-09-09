package setup;

import processing.core.PApplet;
import solar_system.Solar_System;
import aa.BoidApp;
//import aa.DebuggingApp;
import aa.FlockApp;
import aa.HunterApp;
import chaos_game.ChaosGameApp;
import dla.DLA;
import ecossistema.EcosystemApp;
import fractais.LsystemTestApp;
import fractais.MandelbrotApp;

public class ProcessingSetup extends PApplet {
	
	public static IProcessingApp app;
	private int lastUpdate;
	
	@Override
	public void settings() {
		size(800,600);
	}

	@Override
	public void setup() {		
		app.setup(this);	
		lastUpdate = millis();
	}
	
	public void draw() {

		int now = millis();
		float dt = (now - lastUpdate)/1000f;
		lastUpdate = now;
		app.draw(this , dt);
	}
	
	@Override
	public void mousePressed() {
		app.mousePressed(this);
	}
	
	@Override
	public void keyPressed() {
		app.keyPressed(this);
	}
	
	public static void main(String[] args) {
		
		app = new EcosystemApp(); 
		PApplet.main(ProcessingSetup.class);
	}

}
