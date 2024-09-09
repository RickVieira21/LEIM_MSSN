package tools;

public class CustomRandomGenerator {

	private double[] cdf;

	public CustomRandomGenerator(double[] pmf){
		cdf = new double[pmf.length];
		cdf[0] = pmf[0];
		for (int i = 1; i < pmf.length; i++) {
			cdf[i] = cdf[i-1] + pmf[i];
		}
	}

	public int getRandomClass(){
		double r = Math.random();
		int val = 0;
		for (int i = 0; i < cdf.length; i++) {
			if (r > cdf[i]) val++;
			else break;
		}
		return val;
	}
}