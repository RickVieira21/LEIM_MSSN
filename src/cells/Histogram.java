package cells;

public class Histogram {
	int[] hist;
	int nbins;

	public Histogram(int[] data, int nbins){
		this.nbins = nbins;
		hist = new int[nbins];
		for (int i = 0; i < data.length; i++) {
			hist[data[i]]++;
		}
	}

	public int[] getDistribution(){
		return hist;
	}

	public int getMode(int preference){
		int maxVal = 0;
		int mode = 0;
		for (int i = 0; i < nbins; i++) {
			if (hist[i] > maxVal){
				maxVal = hist[i];
				mode = i;
			}
		}
		if (hist[preference] == hist[mode]){
			return preference;
		}
		return mode;
	}

	public void display(){
		for (int i = 0; i < nbins; i++) {
			System.out.println(hist[i]);
		}
		System.out.println();
	}
}