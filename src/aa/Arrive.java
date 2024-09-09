package aa;

import processing.core.PVector;

public class Arrive extends Behavior{
	public Arrive(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector vd = PVector.sub(me.eye.target.getPos(), me.getPos());
		float d = vd.mag();
		float R = me.dna.radiusArrive;
		if(d < R){
			vd.mult(d/R);
		}
		return vd;
	}
}
