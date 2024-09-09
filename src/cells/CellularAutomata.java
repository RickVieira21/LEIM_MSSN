package cells;

import processing.core.PVector;
import tools.CustomRandomGenerator;
import setup.IProcessingApp;
import processing.core.PApplet;
import setup.SubPlot;

import java.util.Random;

import static java.lang.Math.round;

public class CellularAutomata implements IProcessingApp {
	private SubPlot plt;
	private int radiusNeigh;
	protected int ncols, nrows;
	protected int nStates;
	protected float cellWidth, cellHeight; //pixels
	protected float xmin, ymin;
	protected Cell[][] cells;
	private int[]colors;
	private boolean start = false;

	public CellularAutomata(PApplet p, SubPlot plt, int nrows, int ncols, int nStates, int radiusNeigh) {
		this.nrows = nrows;
		this.ncols = ncols;
		this.nStates = nStates;
		this.radiusNeigh = radiusNeigh;
		cells = new Cell[nrows][ncols];
		colors = new int[nStates];
		float[] bb = plt.getBoundingBox();
		xmin = bb[0];
		ymin = bb[1];
		cellWidth = bb[2] / ncols;
		cellHeight = bb[3] / nrows;
		this.plt = plt;
		createCells();
		setStateColors(p);
	}
	protected void createCells(){
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				cells[i][j] = new Cell(this,i,j);
			}
		}
		setMooreNeighbors();
	}

	public void initRandom(){
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				cells[i][j].setState((int) (nStates*Math.random()));
			}
		}
	}

	public void initRandomCustom(double[] pmf){
		CustomRandomGenerator crg = new CustomRandomGenerator(pmf);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				cells[i][j].setState(crg.getRandomClass());
			}
		}
	}

	public void setStateColors(PApplet p) {
		colors[0]=p.color(0, 0, 0);
		colors[1]=p.color(10, 240, 10);
		colors[2]=p.color(220, 220, 40);
		colors[3]=p.color(95, 179, 209);
	}

	public void setStateColors(int[]colors) {
		this.colors = colors;
	}

	public int[] getStateColors() {
		return colors;
	}

	public float getCellWidth(){
		return cellWidth;
	}

	public float getCellHeight(){
		return cellHeight;
	}

	public void setRandomStates(){
		Random r = new Random();
		for (int i = 0; i < ncols; i++) {
			for (int j = 0; j < nrows; j++) {
				cells[i][j].setState(r.nextInt(0, 2));
			}
		}
	}

	public void setStateDead(){
		for (int i = 0; i < ncols; i++) {
			for (int j = 0; j < nrows; j++) {
				cells[i][j].setState(0);
			}
		}
	}

	public void display(){
		createCells();
	}

	@Override
	public void setup(PApplet p) {
		display();
		setRandomStates();
		//setStateDead();
		setStateColors(p);
		for (int i = 0; i < ncols; i++) {
			for (int j = 0; j < nrows; j++) {
				if(cells[i][j].getState() == 0) {
					p.fill(getStateColors()[0]);
					cells[i][j].display(p);
				}else{
					p.fill(getStateColors()[3]);
					cells[i][j].display(p);
				}
			}
		}
	}

	public PVector getCenterCell(int row, int col) {
		float x = (col + 0.5f) * cellWidth;
		float y = (row + 0.5f) * cellHeight;
		double[] w = plt.getWorldCoord(x, y);
		return new PVector ((float)w[0], (float)w[1]);
	}

	public Cell world2Cell(double x, double y){
		float[] xy = plt.getPixelCoord(x, y);
		return pixel2Cell(xy[0], xy[1]);
	}

	public Cell pixel2Cell(float x, float y) {
		int row = (int)((y- ymin) / cellHeight);
		int col = (int)((x- xmin) / cellWidth);

		if(row >= nrows) row = nrows -1;
		if(col >= ncols) col = ncols -1;

		if (row < 0) row = 0;
		if (col < 0) col = 0;

		return cells[row][col];
	}

	/**
	 * @param p PApplet
	 * The function mousePressed will always change the state of the "selected "cell
	 * to 1.
	 */
	@Override
	public void mousePressed(PApplet p) {
		Cell cell = pixel2Cell(p.mouseX, p.mouseY);
		p.fill(getStateColors()[1]);
		cell.display(p);
		cell.setState(1);
	}


	/**
	 * @param p PApplet
	 * The function keyPressed will pause/resume the process.
	 */
	@Override
	public void keyPressed(PApplet p){
		start= !start;
	}

	@Override
	public void draw(PApplet p, float dt) {

	}

	/**
	 * The function setMooreNeighbors will set the neighbors of the cells that
	 * are in the corners or the edges.
	 */
	protected void setMooreNeighbors() {
		int NN = (int)Math.pow(2*radiusNeigh+1, 2);
		for(int i=0;i<nrows;i++) {
			for(int j=0;j<ncols;j++) {
				Cell[] neigh = new Cell[NN];
				int n = 0;
				for(int ii=-radiusNeigh;ii<=radiusNeigh;ii++) {
					int row = (i+ii+nrows)%nrows;
					for(int jj=-radiusNeigh;jj<=radiusNeigh;jj++) {
						int col = (j+jj+ncols)%ncols;
						neigh[n++] = cells[row][col];
					}
				}
				cells[i][j].setNeighbors(neigh);
			}
		}
	}

	public void display(PApplet p){
		for(int i=0;i<nrows;i++) {
			for(int j=0;j<ncols;j++) {
				cells[i][j].display(p);
			}
		}
	}
}