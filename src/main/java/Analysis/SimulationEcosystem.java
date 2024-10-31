package Analysis;

import Ecosystem.Ecosystem;
import Ecosystem.Animal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import java.util.stream.Collectors;
import Ecosystem.Plant;

public class SimulationEcosystem {
    private Ecosystem ecosystem;
    private PredictionEcosystem predictionEcosystem;
    public SimulationEcosystem (Ecosystem ecosystem)
    {
        this.ecosystem = ecosystem;
        predictionEcosystem = new PredictionEcosystem();
    }

    public void simulateDay()
    {
        updateEnvironment();
        ecosystem.getAnimalList().forEach(this::processAnimalActions);
        ecosystem.getPlantList().forEach(this::processPlantActions);
        checkWhoIsDeadAnimal(ecosystem.getAnimalList());
        checkWhoIsDeadPlant(ecosystem.getPlantList());
    }

    private void updateEnvironment() {
        double temperatureRandom = ThreadLocalRandom.current().nextDouble(-0.5, 0.5) + ecosystem.getEnvironment().getTemperature();
        ecosystem.getEnvironment().setTemperature(temperatureRandom);

        int humidityRandom = ThreadLocalRandom.current().nextInt(-1, 2) + ecosystem.getEnvironment().getHumidity();
        ecosystem.getEnvironment().setHumidity(Math.max(0, Math.min(100, humidityRandom)));

        int waterRandom = ThreadLocalRandom.current().nextInt(-1, 2) + ecosystem.getEnvironment().getWater();
        ecosystem.getEnvironment().setWater(Math.max(0, Math.min(100, waterRandom)));

        int sunlightRandom = ThreadLocalRandom.current().nextInt(-1, 2) + ecosystem.getEnvironment().getSunlight();
        ecosystem.getEnvironment().setSunlight(Math.max(0, Math.min(100, sunlightRandom)));
    }

    private void processAnimalActions(Animal animal) {
        double survivalRate = predictionEcosystem.calculateAnimalSurvivalRate(animal, ecosystem.getEnvironment(), ecosystem.getAnimalList(), ecosystem.getPlantList());
        int amount = (int) Math.min(0.25 * animal.getPopulation(), 10);

        if (survivalRate == 0.0)
            animal.setPopulation(0);

        if (survivalRate < 1.0) {
            amount = (int) (5 - (0.2 * animal.getPopulation()));
            animal.decreasePopulation(amount);
        }

        else {
            if (animal.getTypeFood().equals("carnivore"))
            {
                Animal prey = getRandomPrey(animal, ecosystem.getAnimalList());
                if (prey != null)
                {
                    prey.decreasePopulation(amount);
                    int change = (int) ((amount * 0.15) + (prey.getPopulation() * 0.2));
                    animal.updatePopulation(change);
                    animal.setHungry(0);
                }
                else
                    animal.setHungry(animal.getHungry() + 10);
            }
            else
            {
                if (!ecosystem.getPlantList().isEmpty()) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(ecosystem.getPlantList().size());
                    Plant randomPlant = ecosystem.getPlantList().get(randomIndex);
                    amount = (int) Math.min(0.1 * animal.getPopulation(), 5);
                    randomPlant.decreasePopulation(amount);

                    int change = (int) Math.min(0.25 * animal.getPopulation(), 10);
                    animal.updatePopulation(change);
                } else {
                    animal.setHungry(animal.getHungry() + 10);
                }
            }
            drink(animal);
        }
    }

    private void checkWhoIsDeadAnimal(List<Animal> animalList) {
        List<Animal> deadAnimals = new ArrayList<>();

        for (Animal animal : animalList) {
            if (animal.getPopulation() == 0 || animal.getThirst() > 100 || animal.getHungry() > 100) {
                System.out.println("The population is extinct: " + animal.getName());
                deadAnimals.add(animal);
            }
        }

        for (Animal deadAnimal : deadAnimals) {
            ecosystem.deleteAnimal(deadAnimal);
        }
    }
    private void drink(Animal animal) {
        if (ecosystem.getEnvironment().getWater() > 0 && ecosystem.getEnvironment().getWater() > animal.getThirst() * animal.getPopulation()) {
            animal.setThirst(0);
            int newWaterLevel = ecosystem.getEnvironment().getWater() - (10 * animal.getPopulation());
            ecosystem.getEnvironment().setWater(Math.max(newWaterLevel, 0));
        } else {
            animal.setThirst(animal.getThirst() + 5);
        }
    }
    private void processPlantActions(Plant plant) {
        double survivalRate = predictionEcosystem.calculatePlantSurvivalRate(plant, ecosystem.getEnvironment(), ecosystem.getAnimalList(), ecosystem.getPlantList());

        if (survivalRate == 0.0)
            plant.setPopulation(0);
        if (survivalRate < 1.0) {
            int amount = (int) Math.min(0.1 * plant.getPopulation(), 5);
            plant.decreasePopulation(amount);
        }

        else {
            if (ecosystem.getEnvironment().getWater() > 0 && ecosystem.getEnvironment().getWater() > plant.getwaterNeed() * plant.getPopulation()) {
                plant.setwaterNeed(0);
                int newWaterLevel = ecosystem.getEnvironment().getWater() - (10 * plant.getPopulation());
                ecosystem.getEnvironment().setWater(Math.max(newWaterLevel, 0));
                int regrowthAmount = (int) (0.1 * plant.getPopulation());
                plant.updatePopulation(regrowthAmount);
            } else {
                plant.setwaterNeed(plant.getwaterNeed() + 5);
            }
        }
    }
    private void checkWhoIsDeadPlant(List<Plant> plantList) {
        List<Plant> deadPlants = new ArrayList<>();

        for (Plant plant : plantList) {
            if (plant.getPopulation() == 0 || plant.getwaterNeed() > 80) {
                System.out.println("The population is extinct: " + plant.getName());
                deadPlants.add(plant);
            }
        }

        for (Plant deadPlant : deadPlants) {
            ecosystem.deletePlant(deadPlant);
        }
    }
    private Animal getRandomPrey(Animal predator, List<Animal> allAnimals) {
        List<Animal> preyList = allAnimals.stream()
                .filter(predator::canEat)
                .collect(Collectors.toList());

        if (!preyList.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(preyList.size());
            return preyList.get(randomIndex);
        }

        return null;
    }
}
