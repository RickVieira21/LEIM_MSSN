package cells;

import processing.core.PApplet;

public class Cell {
	protected int row,col, state;
	protected CellularAutomata ca;
	private Cell[] neighbors;

	/**
	 * @param ca CellularAutomata
	 * @param col cell's collumn
	 * @param row cell's row
	 *
	 * This is the constructor of the class Cell.
	 */
	public Cell(CellularAutomata ca, int row, int col){
		this.ca = ca;
		this.row = row;
		this.col = col;
		this.state = 0;
		this.neighbors = null;
	}

	public void setNeighbors(Cell[] neigh) {
		this.neighbors = neigh;
	}

	/**
	 * The function getNeighbors will return the array of neighbors.
	 * @return Cell array of neightbors
	 */
	public Cell[] getNeighbors() {
		return neighbors;
	}

	/**
	 * @param state Either dead of alive (0 or 1)
	 * The function setState will change the state of the cell.
	 */
	public void setState(int state){
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public float getX() {
		return (ca.xmin + this.col*ca.cellWidth);
	}

	public float getY() {
		return (ca.ymin + this.row*ca.cellHeight);
	}
	public void display(PApplet p){
		p.pushStyle();
		p.noStroke();
		p.fill(ca.getStateColors()[state]);
		p.rect(ca.xmin + col*ca.cellWidth, ca.ymin + row*ca.cellHeight, ca.getCellWidth(), ca.getCellHeight());
		p.popStyle();
	}

}