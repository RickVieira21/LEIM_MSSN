package cells;

public class MajorityCell extends Cell{
	private Histogram hist;

	public MajorityCell(CellularAutomata ca, int row, int col) {
		super(ca, row, col);
	}

	public void computeHistogram() {
		Cell[] neighbors = getNeighbors();
		int[] data = new int[neighbors.length];
		for(int i=0; i<neighbors.length; i++) {
			data[i] = neighbors[i].getState();
		}
		hist = new Histogram(data, ca.nStates);
	}

	public boolean applyMajorityRule() {
		int mode = hist.getMode(0);
		boolean changed = false;
		if(getState() != mode) {
			setState(mode);
			changed = true;
		}
		return changed;
	}
}