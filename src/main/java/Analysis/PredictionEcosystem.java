package Analysis;

import Ecosystem.Animal;
import Ecosystem.Ecosystem;
import Ecosystem.Environment;
import Ecosystem.Plant;

import java.util.*;

public class PredictionEcosystem {
    public PredictionEcosystem()
    {

    }

    public void makeForecast(Ecosystem ecosystem)
    {
        for (Animal animal : ecosystem.getAnimalList())
        {
            System.out.println("Forecast for " + animal.getName());
            double survivalRate = calculateAnimalSurvivalRate(animal, ecosystem.getEnvironment(), ecosystem.getAnimalList(), ecosystem.getPlantList());
            System.out.println(analyzeSurvivalRate(survivalRate));
        }
        for (Plant plant : ecosystem.getPlantList())
        {
            System.out.println("Forecast fot " + plant.getName());
            double survivalRate = calculatePlantSurvivalRate(plant, ecosystem.getEnvironment(), ecosystem.getAnimalList(), ecosystem.getPlantList());
            System.out.println(analyzeSurvivalRate(survivalRate));
        }
    }
    private String analyzeSurvivalRate(double survivalRate) {
        if (survivalRate > 1.2) {
            return "Populations are likely to grow.";
        } else if (survivalRate >= 1.0 && survivalRate <= 1.2) {
            return "Populations are likely to remain stable.";
        } else {
            return "Populations are likely to decline.";
        }
    }

    public double calculateAnimalSurvivalRate(Animal animal, Environment environment, List<Animal> listAnimal, List<Plant> listPlant) {
        double survivalRate = 0.0;
        double foodHerbivoreFactor = 1.0;
        double temperatureSurvivalFactor = (environment.getTemperature() >= animal.getMinTemperatureTolerance()
                && environment.getTemperature() <= animal.getMaxTemperatureTolerance()) ? 1.0 : 0.0;
        double temperatureIdealFactor = (environment.getTemperature() >= animal.getMinTemperatureTolerance() - 10.0
                && environment.getTemperature() <= animal.getMaxTemperatureTolerance() + 10.0) ? 1.1 : 0.9;
        double humidityFactor = (environment.getHumidity() > 0 && environment.getHumidity() < 100) ? 1.0 : 0;
        double humidityIdealFactor = (environment.getHumidity() >= 30 && environment.getHumidity() <= 70) ? 1.1 : 0.9;
        double waterFactor = (environment.getWater() >= animal.getThirst() * animal.getPopulation()) ? 1.0 : 0;
        double foodFactor = calculateAnimalFoodFactor(animal, listAnimal, listPlant);
        if (animal.getTypeFood().equals("herbivore")) {
            foodHerbivoreFactor = calculateAnimalHerbivoreFactor(listAnimal, listPlant);
        }
        survivalRate = temperatureSurvivalFactor * temperatureIdealFactor *
                humidityFactor * humidityIdealFactor
                * foodFactor * waterFactor * foodHerbivoreFactor;
        return survivalRate;
    }

    public double calculatePlantSurvivalRate(Plant plant, Environment environment, List<Animal> listAnimal, List<Plant> listPlant) {
        double survivalRate = 0.0;
        double temperatureSurvivalFactor = (environment.getTemperature() >= plant.getMinTemperatureTolerance()
                && environment.getTemperature() <= plant.getMaxTemperatureTolerance()) ? 1.0 : 0.0;
        double temperatureIdealFactor = (environment.getTemperature() >= plant.getMinTemperatureTolerance() - 6.0
                && environment.getTemperature() <= plant.getMaxTemperatureTolerance() + 6.0) ? 1.1 : 0.9;
        double humidityFactor = (environment.getHumidity() > 0 && environment.getHumidity() < 100) ? 1.0 : 0.0;
        double humidityIdealFactor = (environment.getHumidity() >= 30 && environment.getHumidity() <= 70) ? 1.1 : 0.9;
        double waterFactor = (environment.getWater() > 0 && environment.getWater() >= plant.getwaterNeed() * plant.getPopulation()) ? 1.0 : 0;
        double sunlightFactor = (environment.getSunlight() >= plant.getlightNeed() && environment.getSunlight() > 0 && environment.getSunlight() < 100) ? 1.0 : 0.0;
        double sunlightIdealFactor = (environment.getSunlight() >= 20 && environment.getSunlight() <= 70) ? 1.1 : 0.9;
        survivalRate = temperatureSurvivalFactor * temperatureIdealFactor *
                humidityFactor * humidityIdealFactor *
                waterFactor * sunlightFactor * sunlightIdealFactor;
        return survivalRate;
    }

    private double calculateAnimalHerbivoreFactor(List<Animal> listAnimal, List<Plant> listPlant)
    {
        double foodFactor = 0.0;
        int allPopulationHerbivore = listAnimal.stream()
                .filter(animal -> animal.getTypeFood().equals("herbivore"))
                .mapToInt(Animal::getPopulation)
                .sum();
        int allPopulationPlant = listPlant.stream()
                .mapToInt(Plant::getPopulation)
                .sum();
        foodFactor = (allPopulationPlant - allPopulationHerbivore >= 0) ? 1.0 : 0.0;
        return foodFactor;
    }
    private double calculateAnimalFoodFactor(Animal animal, List<Animal> listAnimal, List<Plant> listPlant)
    {
        double foodFactor = 0.0;
        int populationFood = 0;
        if (animal.getTypeFood().equals("carnivore"))
        {
            for (Animal otheranimal : listAnimal)
            {
                if (animal.canEat(otheranimal) == true)
                {
                    populationFood += otheranimal.getPopulation();
                }
            }
        }

        if (animal.getTypeFood().equals("herbivore"))
        {
            for (Plant otherplant : listPlant)
            {
                populationFood += otherplant.getPopulation();
            }
        }

        if (populationFood != 0)
            foodFactor = ((animal.getPopulation() / populationFood) <= 0.65) ? 1.2 : 0.8;
        return foodFactor;
    }

}
