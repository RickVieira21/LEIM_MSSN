package ecossistema;

import aa.*;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Population {
	protected List<Animal> allAnimals;
	protected Animal target;
	private double[] window;
	private boolean mutate = true;
	private Animal targetAux;
	protected List<Body> lakes = new ArrayList<>();

	float aux;
	private boolean parar = false;
	protected Animal prey, predator, superPredator;

	protected PImage imgCoelho, imgLobo, imgUrso;
	protected List<PImage> images = new ArrayList<>();

	public Population(PApplet parent, SubPlot plt, Terrain terrain){
		imgCoelho = parent.loadImage("coelho.png");
		images.add(imgCoelho);
		imgLobo = parent.loadImage("lobo.png");
		images.add(imgLobo);
		imgUrso = parent.loadImage("urso.png");
		images.add(imgUrso);

		window = plt.getWindow();
		allAnimals = new ArrayList<Animal>();

		lakes = terrain.getLakes();

						
		// Inicialização da população de presas
		for (int i = 0; i < Variables.INI_PREY_POPULATION; i++) {
		    PVector pos;
		    do {
		        pos = new PVector(parent.random((float) window[0], (float) window[1]),
		                parent.random((float) window[2], (float) window[3]));
		    } while (isInsideLake(pos, terrain));

		    int color = parent.color(Variables.PREY_COLOR[0],
		    		Variables.PREY_COLOR[1],
		    		Variables.PREY_COLOR[2]);
		    prey = new Prey(pos, Variables.PREY_VELOCITY, Variables.PREY_FORCE, Variables.PREY_MASS, Variables.PREY_SIZE, color, parent, plt, imgCoelho);
		    prey.addBehavior(new Wander(4));
		    prey.addBehavior(new Flee(0.1f));
		    Eye eye = new Eye(prey, lakes);
		    prey.setEye(eye);
		    allAnimals.add(prey);
		}

						
		// Inicialização da população de predadores
		for (int i = 0; i < Variables.INI_PREDATOR_POPULATION; i++) {
			PVector pos;
		    do {
		        pos = new PVector(parent.random((float) window[0], (float) window[1]),
		                parent.random((float) window[2], (float) window[3]));
		    } while (isInsideLake(pos, terrain));
		    
			int color = parent.color(Variables.PREDATOR_COLOR[0],
					Variables.PREDATOR_COLOR[1],
					Variables.PREDATOR_COLOR[2]);
			predator = new Predator(pos, Variables.PREDATOR_VELOCITY, Variables.PREDATOR_FORCE, Variables.PREDATOR_MASS, Variables.PREDATOR_SIZE, color, parent, plt, imgLobo);
			predator.addBehavior(new Wander(1));
			predator.addBehavior(new Pursuit(6));

			List<Body> allTrackingBodies = new ArrayList<Body>();
			for (Animal a : allAnimals){
				if (a instanceof Prey){
					allTrackingBodies.add(a);
				}
			}
			Eye eye = new Eye(predator, allTrackingBodies);
			predator.setEye(eye);
			allAnimals.add(predator);
			target = (Animal) predator.getEye().nextTarget();
			predator.setTarget(target);
		}

		
						
		// Inicialização da população de superpredadores
		for (int i = 0; i < Variables.INI_SUPERPREDATOR_POPULATION; i++) {
			PVector pos;
		    do {
		        pos = new PVector(parent.random((float) window[0], (float) window[1]),
		                parent.random((float) window[2], (float) window[3]));
		    } while (isInsideLake(pos, terrain));

			int color = parent.color(Variables.SUPERPREDATOR_COLOR[0],
					Variables.SUPERPREDATOR_COLOR[1],
					Variables.SUPERPREDATOR_COLOR[2]);
			superPredator = new SuperPredator(pos, Variables.SUPERPREDATOR_VELOCITY, Variables.SUPERPREDATOR_FORCE, Variables.SUPERPREDATOR_MASS, Variables.SUPERPREDATOR_SIZE, color, parent, plt, imgUrso);
			superPredator.addBehavior(new Hibernate(1));
			superPredator.addBehavior(new Pursuit(1));

			List<Body> allTrackingBodies = new ArrayList<Body>(allAnimals);
			Eye eye = new Eye(superPredator, allTrackingBodies);
			superPredator.setEye(eye);
			target = (Animal) superPredator.getEye().nextTarget();
			superPredator.setTarget(target);
			allAnimals.add(superPredator);
		}

		
		// adiciona lagos
		for(int i = Variables.INI_PREY_POPULATION; i < allAnimals.size(); i++){
			Body b = new Body(allAnimals.get(i).getPos());
			lakes.add(b);
		}
		Eye eye = new Eye(prey, lakes);
		prey.setEye(eye);

	}
	
	
	// verificar se uma posição está dentro de um lago
	private boolean isInsideLake(PVector pos, Terrain terrain) {
	    Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
	    return patch.getState() == Variables.PatchType.WATER.ordinal();
	}

	
	// novo alvo para um animal recém-nascido
	public void newTarget(Animal child) {
		List<Body> allTrackingBodies = new ArrayList<Body>();
		for (Animal a : allAnimals){
			if (a instanceof Prey){
				allTrackingBodies.add(a);
			}
		}
		Eye eye = new Eye(child, allTrackingBodies);
		child.setEye(eye);
		target = (Animal) child.getEye().nextTarget();
		child.setTarget(target);
	}

	
	
	// Atualiza o alvo do predador quando o anterior morre
	private void nextTargetPredator(Animal predator) {
		if (predator.getTarget().isDead()){
			targetAux = predator.getTarget();
			List<Body> allTrackingBodies = new ArrayList<Body>();
			for (Animal a : allAnimals){
				if (a instanceof Prey && a != predator.getTarget() && !a.isDead()){
					allTrackingBodies.add(a);
				}
			}
			Eye eye = new Eye(predator, allTrackingBodies);
			predator.setEye(eye);
			target = (Animal) predator.getEye().nextTarget();
			predator.setTarget(target);
		} else if(PVector.sub(predator.getPos(),predator.getTarget().getPos()).mag() > 15f){
			target = (Animal) predator.getEye().nextTarget();
			predator.setTarget(target);
		}
	}

	
	
	 // Atualiza o alvo do superpredador quando o anterior morre
	private void nextTargetSuperPredator(Animal bear) {
		if (bear.getTarget().isDead()){
			targetAux = bear.getTarget();
			List<Body> allTrackingBodies = new ArrayList<Body>();
			for (Animal a : allAnimals){
				if ((a instanceof Prey || a instanceof Predator) && a != bear.getTarget() && !a.isDead()){
					allTrackingBodies.add(a);
				}
			}
			Eye eye = new Eye(bear, allTrackingBodies);
			bear.setEye(eye);
			target = (Animal) bear.getEye().nextTarget();
			bear.setTarget(target);
		}else if(PVector.sub(bear.getPos(), bear.getTarget().getPos()).mag() > 15f){
			target = (Animal) bear.getEye().nextTarget();
			bear.setTarget(target);
		}
	}

	
		
	// Atualiza as imagens associadas aos animais
	public void updateImage(PApplet p){
		for (Animal a : allAnimals){
			if (a instanceof Prey){
				a.setImg(imgCoelho);
			}
		}
	}

	
	// Atualiza o estado da população em cada passo de simulação
	public void update(float dt, Terrain terrain){

		move(terrain, dt);
		for (Animal a : allAnimals){
			if (a instanceof Predator) {
				if (getNumPrey() > 0) {
					nextTargetPredator(a);
				} else {
					//se não houver presas vagueia
					a.applyBehavior(dt, 0);
				}
			}
			
			//ENERGIA DO SUPERPREDADOR
			if (a instanceof SuperPredator){
				if (getNumPrey() > 0 || getNumPred() > 0) {
					if (a.getEnergy() < 20f){
						//CAÇAR
						nextTargetSuperPredator(a);
						a.applyBehavior(dt, 1);
						aux = a.getEnergy();
						parar = false;
					} else if (a.getEnergy() > 50f && !parar){
						//CHEIO
						a.applyBehavior(dt, 0);
						aux = a.getEnergy();
						parar = true;
					} else if (parar) { //PARAR
					} else{
						//DE VOLTA
						nextTargetSuperPredator(a);
						a.applyBehavior(dt, 1);
						aux = a.getEnergy();
					}
				}
				else {
					System.out.println("A simulação terminou");
					a.applyBehavior(dt, 0);
				}
			}

		}
		eat(terrain);
		energy_consumtion(dt, terrain);
		reproduce(mutate);
		die();
	}

	
	
	// move para todos os animais 
	private void move(Terrain terrain, float dt) {
		for (Animal a : allAnimals){
			if(a instanceof Prey || a instanceof Predator) a.applyBehaviors(dt);
		}
	}

	
	
	// eat para todos os animais
	private void eat(Terrain terrain) {
		for (Animal a : allAnimals){
			if (a instanceof Prey){
				a.eat(terrain, null);
			}
			else if (a instanceof Predator) {
				a.eat(terrain, a.getTarget());
			}
			else if (a instanceof SuperPredator){
				a.eat(terrain, a.getTarget());
			}
		}
	}

	
	
	// consumo de energia para todos os animais
	private void energy_consumtion(float dt, Terrain terrain) {
		for (Animal a : allAnimals){
			a.energy_consumption(dt, terrain);
		}
	}

	
	
	// Método para reproduzir animais na população
	private void reproduce(boolean mutate) {
	    for (int i = allAnimals.size() - 1; i >= 0; i--) {
	        Animal a = allAnimals.get(i);
	        // Garante que apenas animais que não são superpredadores se reproduzem
	        if (!(a instanceof SuperPredator)) {
	            // Cria um novo animal como descendente
	            Animal child = a.reproduce(mutate);
	            if (child != null) {
	                // Se o descendente é um predador, define um novo alvo
	                if (child instanceof Predator) {
	                    newTarget(child);
	                }
	                // Adiciona o descendente à população
	                allAnimals.add(child);
	            }
	        }
	    }
	}

	
	
	// remover animais mortos da população
	private void die() {
	    for (int i = allAnimals.size() - 1; i >= 0; i--) {
	        Animal a = allAnimals.get(i);
	        // Remove animais mortos ou que atingiram uma condição de morte
	        if (a.isDead() || a.die()) {
	            allAnimals.remove(a);
	        }
	    }
	}

	
	
	// exibir todos os animais na população
	public void display(PApplet p, SubPlot plt) {
	    for (Animal a : allAnimals)
	        a.display(p, plt);
	}

	
	
	// lista de imagens dos animais
	public List<PImage> getImages() {
	    return images;
	}

	
	// número total de animais na população
	public int getNumAnimals() {
	    return allAnimals.size();
	}
	

	// obter o número de presas na população
	public int getNumPrey() {
	    List<Animal> prey = new ArrayList<>();
	    for (Animal a : allAnimals) {
	        if (a instanceof Prey) {
	            prey.add(a);
	        }
	    }
	    return prey.size();
	}

	
	// obter o número de predadores na população
	public int getNumPred() {
	    List<Animal> predator = new ArrayList<>();
	    for (Animal a : allAnimals) {
	        if (a instanceof Predator) {
	            predator.add(a);
	        }
	    }
	    return predator.size();
	}
	

	// calcular a velocidade máxima média da população
	public float getMeanMaxSpeed() {
	    float sum = 0;
	    for (Animal a : allAnimals) {
	        sum += a.getDna().maxSpeed;
	    }
	    return sum / allAnimals.size();
	}

	
	// calcular o desvio padrão da velocidade máxima da população
	public float getStdMaxSpeed() {
	    float mean = getMeanMaxSpeed();
	    float sum = 0;
	    for (Animal a : allAnimals) {
	        sum += Math.pow(a.getDna().maxSpeed - 2, 2);
	    }
	    return (float) Math.sqrt(sum / allAnimals.size());
	}
	

	// calcular as médias dos pesos das comportamentos na população
	public float[] getMeanWeights() {
	    float[] sums = new float[2];
	    for (Animal a : allAnimals) {
	        sums[0] += a.getBehaviors().get(0).getWeight();
	        sums[1] += a.getBehaviors().get(1).getWeight();
	    }
	    sums[0] /= allAnimals.size();
	    sums[1] /= allAnimals.size();
	    return sums;
	}
	

	// obter o superpredador na população
	public Animal getSuperPredator() {
	    for (Animal a : allAnimals) {
	        if (a instanceof SuperPredator) return a;
	    }
	    return null;
	}
}