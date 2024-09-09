package ecossistema;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import setup.SubPlot;

public class SuperPredator extends Animal {
    protected PImage img, hibernateImage;

    protected SuperPredator(PVector pos, float vel, float mForce, float mass, float radius, int color, PApplet p, SubPlot plt, PImage img) {
        super(pos, vel, mForce, mass, radius, color, p, plt);
        energy = Variables.INI_SUPERPREDATOR_ENERGY;
        this.img = img;
    }

    // não se reproduz 
    @Override
    public Animal reproduce(boolean mutate) {
        return null;
    }

    // método de alimentação do superpredador
    @Override
    public void eat(Terrain terrain, Animal target) {
        if (target != null) {
            if (!(target.isDead())) {
                float dist = PVector.dist(pos, target.getPos());
                // Se o alvo estiver próximo, consome energia com base no tipo de presa (presa ou predador)
                if (dist < 3 * target.getRadius()) {
                    if (target instanceof Prey) {
                        energy += Variables.ENERGY_FROM_PREY;
                    } else if (target instanceof Predator) {
                        energy += Variables.ENERGY_FROM_PREDATOR;
                    }
                    target.setDead(true);
                }
            }
        }
    }
    


    @Override
    public void display(PApplet p, SubPlot plt) {
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.image(img, pp[0] - 30f, pp[1] - 30f, 60f, 60f);
    }

    @Override
    public void setImg(PImage img) {
        this.img = img;
    }
}
