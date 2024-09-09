package physics;

import processing.core.PApplet;
import processing.core.PVector;
import setup.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem extends Body{

    private List<Particle> particles;
    private PSControl psc;
    private float life = 0;
    private PVector particleSpeed;

    public ParticleSystem(PVector pos, PVector vel, float mass, float radius, PSControl psc) {
        super(pos, vel, mass, radius,0);
        this.particles = new ArrayList<Particle>();
        this.psc = psc;
    }

    public PSControl getPSControl() {
        return psc;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public void move(float dt, float speed){
        super.move(dt*speed);
        addParticles(dt);
        for(int i= particles.size()-1; i>=0; i--) {
            Particle p = particles.get(i);
            p.move(dt*speed);
            if(p.isDead()) {
                particles.remove(i);
            }
        }
    }

    private void addParticles(float dt) {
        float particlesPerFrame = psc.getFlow()*dt;
        int n = (int) particlesPerFrame;
        float f = particlesPerFrame-n;
        for(int i=0; i<n;i++) {
            addOneParticle();
        }
        if(Math.random()<f) {
            addOneParticle();
        }
    }

    public void addOneParticle() {
        Particle particle = new Particle(pos, psc.getRndVel(), psc.getRndRadius(), psc.getColor(), psc.getRndLifetime());
        particles.add(particle);
    }

    public void display(PApplet p, SubPlot plt){
        for (Particle particle : particles){
            particle.display(p,plt);
        }
    }
}