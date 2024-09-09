package ecossistema;

import cells.MajorityCA;
import physics.Body;
import processing.core.PApplet;
import setup.SubPlot;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends MajorityCA {

    public Terrain(PApplet p, SubPlot plt) {
        super(p, plt, Variables.NROWS, Variables.NCOLS, Variables.NSTATES, 1);
    }

    // Método para criar as células do terreno
    @Override
    protected void createCells() {
        int minRT = (int) (Variables.REGENERATION_TIME[0] * 1000);
        int maxRT = (int) (Variables.REGENERATION_TIME[1] * 1000);
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                int timeToGrow = (int) (minRT + (maxRT - minRT) * Math.random());
                cells[i][j] = new Patch(this, i, j, timeToGrow);
            }
        }
        setMooreNeighbors();
    }

    
    // Método para obter as posições da relva no terreno
    public List<Body> getGrass() {
        List<Body> bodies = new ArrayList<>();
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (cells[i][j].getState() == Variables.PatchType.GRASS.ordinal()) {
                    Body b = new Body(this.getCenterCell(i, j));
                    bodies.add(b);
                }
            }
        }
        return bodies;
    }

    
    // Método para regenerar as células do terreno
    public void regenerate() {
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                ((Patch) cells[i][j]).regenerate();
            }
        }
    }

    
    // Método para obter as posições da água no terreno
    public List<Body> getLakes() {
        List<Body> bodies = new ArrayList<>();
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (cells[i][j].getState() == Variables.PatchType.WATER.ordinal()) {
                    Body b = new Body(this.getCenterCell(i, j));
                    bodies.add(b);
                }
            }
        }
        return bodies;
    }
}
