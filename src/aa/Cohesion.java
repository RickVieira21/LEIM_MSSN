package aa;

import physics.Body;
import processing.core.PVector;

public class Cohesion extends Behavior{
	public Cohesion(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector taregt = me.getPos().copy();
		for (Body b : me.eye.getFarSight()){
			taregt.add(b.getPos());
		}
		taregt.div(me.eye.getFarSight().size()+1);
		return PVector.sub(taregt, me.getPos());
	}
}