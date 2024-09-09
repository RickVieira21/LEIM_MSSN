package aa;

public class DNA {
	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionSafeDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;

	public DNA(float vel, float mForce){
		//Physics
		maxSpeed = random(vel-1, vel);
		maxForce = random(mForce-3, mForce);
		//Vision
		visionDistance = random(1.5f, 2f);
		visionSafeDistance = 0.25f * visionDistance;
		visionAngle = (float)Math.PI* 0.3f;
		//Pursuit
		deltaTPursuit = random(0.5f, 1f);
		//Arrive
		radiusArrive = random(3, 5);
		//Wander
		deltaTWander = random(0.3f, 0.6f);
		radiusWander = random(1f, 3f);
		deltaPhiWander = (float) (Math.PI/4);
	}

	public DNA(DNA dna, boolean mutate){
		maxSpeed = dna.maxSpeed;
		maxForce = dna.maxForce;
		//Vision
		visionDistance = dna.visionDistance;
		visionSafeDistance = dna.visionSafeDistance;
		visionAngle = dna.visionAngle;
		//Pursuit
		deltaTPursuit = dna.deltaTPursuit;
		//Arrive
		radiusArrive = dna.radiusArrive;
		//Wander
		deltaTWander = dna.deltaTWander;
		radiusWander = dna.radiusWander;
		deltaPhiWander = dna.deltaPhiWander;

		if (mutate) mutate();
	}

	private void mutate() {
		maxSpeed += random(-0.2f, 0.2f);
		maxSpeed = Math.max(0, maxSpeed);
	}

	public static float random(float min, float max){
		return (float)(min + (max-min)*Math.random());
	}
}