package Ecosystem;

abstract class Species {
    private String name;
    private int population;
    private double minTemperatureTolerance;
    private double maxTemperatureTolerance;
    public Species(String name, int population, double minTemperatureTolerance, double maxTemperatureTolerance) {
        this.name = name;
        this.population = population;
        this.minTemperatureTolerance = minTemperatureTolerance;
        this.maxTemperatureTolerance = maxTemperatureTolerance;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getMinTemperatureTolerance() {
        return minTemperatureTolerance;
    }
    public void setMinTemperatureTolerance(double minTemperatureTolerance) {
        this.minTemperatureTolerance = minTemperatureTolerance;
    }
    public double getMaxTemperatureTolerance() {
        return maxTemperatureTolerance;
    }
    public void setMaxTemperatureTolerance(double maxTemperatureTolerance) {
        this.maxTemperatureTolerance = maxTemperatureTolerance;
    }

    public void updatePopulation(int change) {
        population += change;
    }

    public void decreasePopulation(int amount) {
        population -= amount;
        if (population <= 0) {
            population = 0;
        }
    }
}
