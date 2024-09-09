package ecossistema;

public interface IAnimal {
	public Animal reproduce(boolean mutate);
	public void eat(Terrain terrain, Animal target);
	public void energy_consumption(float dt, Terrain terrain);
	public boolean die();
}