package ecossistema;

import cells.MajorityCell;

public class Patch extends MajorityCell {

	private long eatenTime;
	private int timeToGrow;

	public Patch(Terrain terrain, int row, int col, int timeToGrow) {
		super(terrain, row, col);
		this.timeToGrow = timeToGrow;
		eatenTime = System.currentTimeMillis();
	}

	// Marca o patch como fértil e atualiza o tempo 
	public void setFertile(){
		state = Variables.PatchType.FERTILE.ordinal();
		eatenTime = System.currentTimeMillis();
	}

	// Regenera o patch se estiver no estado FERTILE e tempo de regeneração expirou
	public void regenerate(){
		if (state == Variables.PatchType.FERTILE.ordinal()
				&& System.currentTimeMillis() > (eatenTime + timeToGrow)){
			// Altera o estado para GRASS indicando que está regenerado e contém comida
			state = Variables.PatchType.GRASS.ordinal();
		}
	}
}
