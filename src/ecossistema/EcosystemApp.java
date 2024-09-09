package ecossistema;

import aa.*;
import physics.Body;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import setup.IProcessingApp;
import setup.SubPlot;
import tools.TimeGraph;

import java.util.ArrayList;
import java.util.List;

public class EcosystemApp implements IProcessingApp {

	private boolean start = false;
	private boolean showGraphs = true;
	private boolean paused = false;
	private float widthSize, heightSize;


	private float timeDuration = 60;
	private float refPopulation = 400;
	private float refPrey = 330;
	private float refPred = 50;

	private float[] viewportNormal = {0f, 0f, 1f, 1f};
	private float[] viewportDebug = {0f, 0f, .7f, 1f};
	private double[] winInBetween = {0, 1, 0, 1};
	private double[] winGraph1 = {0, timeDuration, 0, 2*refPopulation};
	private double[] winGraph2 = {0, timeDuration, 0, 2*refPred};
	private double[] winGraph3 = {0, timeDuration, 0, 2*refPrey};

	private float[] viewInBetween = {.7f, 0f, .03f, 1f};
	private float[] viewGraph1 = {.74f, .04f, .23f, .28f};
	private float[] viewGraph2 = {.74f, .37f, .23f, .28f};
	private float[] viewGraph3 = {.74f, .70f, .23f, .28f};

	private SubPlot plt, pltInBetween, pltGraph1, pltGraph2, pltGraph3;
	private TimeGraph tg1, tg2, tg3;
	private Terrain terrain;
	private Population population;
	private float timer, updateGraphTime;
	private float intervalUpdate = 1;
	private PFont title, textInit, text;


	
	@Override
	public void setup(PApplet p) {

		//Texto
		title = p.createFont("Arial Black", 40);
		textInit = p.createFont("Arial", 25);
		text = p.createFont("Arial", 15);	
		
		//Com gráficos
		if (showGraphs) {
			widthSize = (float) ((p.width * 0.7)/Variables.NCOLS);
			heightSize = (float) ((p.height * 0.7)/Variables.NROWS);

			plt = new SubPlot(Variables.WINDOW, viewportDebug, p.width, p.height);
			pltInBetween = new SubPlot(winInBetween, viewInBetween, p.width, p.height);
			pltGraph1 = new SubPlot(winGraph1, viewGraph1, p.width, p.height);
			pltGraph2 = new SubPlot(winGraph2, viewGraph2, p.width, p.height);
			pltGraph3 = new SubPlot(winGraph3, viewGraph3, p.width, p.height);

			tg1 = new TimeGraph(p, pltGraph1, p.color(255, 0, 0), refPopulation);
			tg2 = new TimeGraph(p, pltGraph2, p.color(255, 0, 0), refPred);
			tg3 = new TimeGraph(p, pltGraph3, p.color(255, 0, 0), refPrey);
		}
		
		//Full-Screen (sem gráficos)
		else {
			widthSize = (float) ((p.width)/Variables.NCOLS);
			heightSize = (float) ((p.height)/Variables.NROWS);
			plt = new SubPlot(Variables.WINDOW, viewportNormal, p.width, p.height);
		}

		terrain = new Terrain(p, plt);
		terrain.setStateColors(getColors(p));
		terrain.initRandomCustom(Variables.PATCH_TYPE_PROB);
		for (int i = 0; i < 5; i++) {
			terrain.majorityRule();
		}
		population = new Population(p, plt, terrain);
		timer = 0;
		updateGraphTime = timer + intervalUpdate;

	}


	@Override
	public void mousePressed(PApplet p) {
		if(!start) {
			p.background(0);
			start = true;
		}
		if (showGraphs) {
			winGraph1[0] = timer;
			winGraph1[1] = timer + timeDuration;
			pltGraph1 = new SubPlot(winGraph1, viewGraph1, p.width, p.height);
			tg1 = new TimeGraph(p, pltGraph1, p.color(255, 0, 0), refPopulation);
			winGraph2[0] = timer;
			winGraph2[1] = timer + timeDuration;
			tg2 = new TimeGraph(p, pltGraph2, p.color(255, 0, 0), refPred);
			winGraph3[0] = timer;
			winGraph3[1] = timer + timeDuration;
			tg3 = new TimeGraph(p, pltGraph3, p.color(255, 0, 0), refPrey);
		}
	}


	
	// Adiciona presas (C) ou predadores (L) ou pausa simulação
	@Override
	public void keyPressed(PApplet p) {
		
		if (p.key == ' ') {
            paused = !paused;
        }
		
		if (p.key == 'c' || p.key == 'C'){
			PVector pos = new PVector(p.random((float) plt.getWindow()[0], (float) plt.getWindow()[1]),
					p.random((float) plt.getWindow()[2], (float) plt.getWindow()[3]));
			int color = p.color(Variables.PREY_COLOR[0],
					Variables.PREY_COLOR[1],
					Variables.PREY_COLOR[2]);
			Animal prey = new Prey(pos, Variables.PREY_VELOCITY, Variables.PREY_FORCE, Variables.PREY_MASS, Variables.PREY_SIZE, color, p, plt, population.getImages().get(0));
			prey.addBehavior(new Wander(4));
			prey.addBehavior(new Flee(0.1f));
			Eye eye = new Eye(prey, population.lakes);
			prey.setEye(eye);
			population.allAnimals.add(prey);
		}
		
		
		if ((p.key == 'l' || p.key == 'L') && population.getNumPrey() > 0){
			PVector pos = new PVector(p.random((float) plt.getWindow()[0], (float) plt.getWindow()[1]),
					p.random((float) plt.getWindow()[2], (float) plt.getWindow()[3]));
			int color = p.color(Variables.PREDATOR_COLOR[0],
					Variables.PREDATOR_COLOR[1],
					Variables.PREDATOR_COLOR[2]);
			Animal predator = new Predator(pos, Variables.PREDATOR_VELOCITY, Variables.PREDATOR_FORCE, Variables.PREDATOR_MASS, Variables.PREDATOR_SIZE, color, p, plt, population.getImages().get(1));
			predator.addBehavior(new Wander(1));
			predator.addBehavior(new Pursuit(6));

			List<Body> allTrackingBodies = new ArrayList<Body>();
			for (Animal a : population.allAnimals){
				if (a instanceof Prey){
					allTrackingBodies.add(a);
				}
			}
			Eye eye = new Eye(predator, allTrackingBodies);
			predator.setEye(eye);
			population.allAnimals.add(predator);
			population.target = (Animal) predator.getEye().nextTarget();
			predator.setTarget(population.target);
		}
	}

	
	// START SCREEN
	@Override
	public void draw(PApplet p, float dt) {
		if (!start){
			p.background(0);
			p.textAlign(PApplet.CENTER);
			p.fill(112, 146, 190);
			p.textFont(title);
			

			p.fill(255);
			p.textFont(textInit);
			p.text("\n \n Comandos: \n \n"
					+ "L para adicionar predadores (Lobos) \n \n"
					+ "C para adicionar presas (Coelhos) \n \n"
					+ "Space para pausar a simulação \n \n",p.width/2,130);
			p.text("Click para começar a simulação",p.width/2,570);
		}
		
		if (!paused && start){
			timer += dt;

			terrain.regenerate();
			population.update(dt, terrain);

			terrain.display(p);
			population.display(p, plt);

			if (showGraphs ){
				plotGraph(p, dt);
			}
			
		}
	}

	
	// Atualização dos gráficos
	public void plotGraph(PApplet p, float dt){
		p.fill(0);
		p.noStroke();
		float[] bb = pltInBetween.getBoundingBox();
		p.rect(bb[0], bb[1], bb[2], bb[3]);

		if (timer > updateGraphTime) {
			tg1.plot(timer, population.getNumAnimals());
			tg2.plot(timer, population.getNumPred());
			tg3.plot(timer, population.getNumPrey());
			updateGraphTime = timer + intervalUpdate;
		}
	}
	
	
	
	// Obter as cores a partir das constantes do mundo
	private int[] getColors(PApplet p){
		int colors[] = new int[Variables.NSTATES];
		for (int i = 0; i < Variables.NSTATES; i++) {
			colors[i] = p.color(Variables.TERRAIN_COLORS[i][0],
					Variables.TERRAIN_COLORS[i][1],
					Variables.TERRAIN_COLORS[i][2]);
		}
		return colors;
	}
}