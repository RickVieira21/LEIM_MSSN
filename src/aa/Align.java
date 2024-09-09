package aa;

import physics.Body;
import processing.core.PVector;

public class Align extends Behavior{
	public Align(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector vd = me.getVel().copy();
		for (Body b : me.eye.getFarSight()){
			vd.add(b.getVel());
		}
		return vd.div(me.eye.getFarSight().size()+1);
	}
}