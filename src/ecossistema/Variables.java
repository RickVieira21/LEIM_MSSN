package ecossistema;

public class Variables {

	public final static double[] WINDOW = {-10, 10, -10, 10};
	
	//Dim Terreno
	public final static int NROWS = 45;
	public final static int NCOLS = 60;

	public static enum PatchType{
		INFERTILE, WATER, FERTILE, GRASS
	}
	
	public final static double[] PATCH_TYPE_PROB = {0.2f, 0.2f, 0.2f, 0.4f};
	public final static int NSTATES = PatchType.values().length;
	public static int[][] TERRAIN_COLORS = {
			{139, 69, 19}, {0, 128, 255}, {0, 128, 0}, {40, 200, 20}
	};
	public final static float[] REGENERATION_TIME = {10.f, 20.f};


	//PRESAS
	public final static int INI_PREY_POPULATION = 15;
	
	public final static float PREY_SIZE = 0.2f;
	public final static float PREY_VELOCITY = 2f;
	public final static float PREY_FORCE = 7f;
	public final static float PREY_MASS = 0.8f;
	public final static float INI_PREY_ENERGY = 10f;
	public final static float ENERGY_FROM_PLANT = 4f;
	public final static float PREY_ENERGY_TO_REPRODUCE = 25f;
	public static int[] PREY_COLOR = {80, 100, 255};

	
	
	//PREDADORES
	public final static int INI_PREDATOR_POPULATION = 2;
	
	public final static float PREDATOR_SIZE = 0.5f;
	public final static float PREDATOR_VELOCITY = 4f;
	public final static float PREDATOR_FORCE = 20f;
	public final static float PREDATOR_MASS = 1.5f;
	public final static float INI_PREDATOR_ENERGY = 20f;
	public final static float ENERGY_FROM_PREY = 5.5f;
	public final static float PREDATOR_ENERGY_TO_REPRODUCE = 35f;
	public static int[] PREDATOR_COLOR = {200, 100, 100};


	
	//SUPERPREDADOR
	public final static int INI_SUPERPREDATOR_POPULATION = 1;
	
	public final static float SUPERPREDATOR_SIZE = 1;
	public final static float SUPERPREDATOR_VELOCITY = 6f;
	public final static float SUPERPREDATOR_FORCE = 20f;
	public final static float SUPERPREDATOR_MASS = 1.3f;
	public final static float INI_SUPERPREDATOR_ENERGY = 30f;
	public final static float ENERGY_FROM_PREDATOR = 8f;
	public static int[] SUPERPREDATOR_COLOR = {255, 0, 0};


}