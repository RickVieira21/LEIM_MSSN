package aa;

import processing.core.PVector;

public class Hibernate extends Behavior{
    public Hibernate(float weight) {
        super(weight);
    }

    @Override
    public PVector getDesiredVelocity(Boid me) {

        return PVector.mult(me.vel,-1);
    }
}