package org.example;

import Analysis.PredictionEcosystem;
import Analysis.SimulationEcosystem;
import Ecosystem.Ecosystem;
import Ecosystem.Animal;
import Ecosystem.Plant;
import File.FileManager;
import java.io.File;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private String filename;
    public Menu() {
        this.scanner = new Scanner(System.in);
        this.filename = null;
    }
    public void displayMenu() {
        Ecosystem ecosystem = null;

        while(true) {
            System.out.println("Select an action: ");
            System.out.println("1. Upload saved content");
            System.out.println("2. Create a new ecosystem");
            System.out.println("0. Exit");
            int choice = getValidIntInput();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ecosystem = uploadContent();
                    if (ecosystem != null) ecosystemMenu(ecosystem);
                    break;
                case 2:
                    ecosystem = createContent();
                    if (ecosystem != null) ecosystemMenu(ecosystem);
                    break;
                case 0:
                    scanner.close();
                    return;
                default:
                    System.out.println("Incorrect choice. Try again.");
            }
        }
    }

    private Ecosystem uploadContent() {
        Ecosystem ecosystem = new Ecosystem();
        File folder = new File("simulations");

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Directory 'simulations' not found. Create the directory and add simulation files.");
            return null;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("No simulations available to upload.");
            return null;
        }

        System.out.println("Available simulations:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ": " + files[i].getName());
        }

        while (true) {
            System.out.print("Choose a simulation to upload: ");

                int choice = getValidIntInput();

                if (choice > 0 && choice <= files.length) {
                    filename = files[choice - 1].getName();
                    ecosystem = FileManager.loadEcosystemData(filename);
                    return ecosystem;
                } else {
                    System.out.println("Invalid choice.");
                }
        }
    }


    private Ecosystem createContent() {
        Ecosystem ecosystem = new Ecosystem();

        while (true) {
            System.out.print("Enter a name for your save: ");
            filename = scanner.nextLine().trim();
            if (!filename.isEmpty()) {
                break;
            }
            System.out.println("Filename cannot be empty. Please try again.");
        }

        initializeDefaultEcosystem(ecosystem);

        FileManager.saveEcosystemData(filename, ecosystem);
        System.out.println("New ecosystem created and saved as " + filename + ".");
        return ecosystem;
    }

    private void initializeDefaultEcosystem(Ecosystem ecosystem) {
        Animal bear = new Animal("Bear", 5, -10, 20, 10, "carnivore", 8, 5, 7);
        Animal deer = new Animal("Deer", 20, 0, 25, 5, "herbivore", 3, 6, 4);
        ecosystem.addAnimal(bear);
        ecosystem.addAnimal(deer);

        Plant oakTree = new Plant("Oak Tree", 50, -5, 30, 100, 8, 6);
        Plant grass = new Plant("Grass", 100, -10, 35, 1, 5, 8);
        ecosystem.addPlant(oakTree);
        ecosystem.addPlant(grass);

        ecosystem.getEnvironment().setTemperature(15);
        ecosystem.getEnvironment().setHumidity(70);
        ecosystem.getEnvironment().setWater(500);
        ecosystem.getEnvironment().setSunlight(40);

        System.out.println("Default ecosystem initialized with animals, plants, and environment settings.");
    }


    private void ecosystemMenu(Ecosystem ecosystem) {
        while(true) {
            System.out.println("Welcome to your ecosystem: ");
            System.out.println("1. Display ecosystem details");
            System.out.println("2. Changing ecosystem parameters");
            System.out.println("3. Save file");
            System.out.println("4. Make a forecast");
            System.out.println("5. Simulate one day");
            System.out.println("6. Automatic simulate");
            System.out.println("0. Return to main menu");

            int choice = getValidIntInput();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayEcosystem(ecosystem);
                    break;
                case 2:
                    changingEcosystem(ecosystem);
                    break;
                case 3:
                    FileManager.saveEcosystemData(filename, ecosystem);
                    System.out.println("The save was successful");
                    break;
                case 4:
                    PredictionEcosystem predictionEcosystem = new PredictionEcosystem();
                    predictionEcosystem.makeForecast(ecosystem);
                    break;
                case 5:
                    SimulationEcosystem simulationEcosystem = new SimulationEcosystem(ecosystem, "log");
                    simulationEcosystem.simulateDay();
                    break;
                case 6:
                    System.out.println("Enter how many days of stimulation there will be: ");
                    int days = getValidIntInput();
                    scanner.nextLine();
                    SimulationEcosystem autoSimulationEcosystem = new SimulationEcosystem(ecosystem, "log");
                    for (int i = 0; i < days; i++)
                        autoSimulationEcosystem.simulateDay();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Incorrect choice. Try again.");
            }
        }
    }

    private void displayEcosystem(Ecosystem ecosystem) {
            System.out.println("===== Information about the Ecosystem =====");
            System.out.println();

            System.out.println("== Animals ==");
            if (ecosystem.getAnimalList().isEmpty()) {
                System.out.println("No animals in the ecosystem.");
            } else {
                ecosystem.getAnimalList().forEach(animal -> System.out.println("- " + animal));
            }
            System.out.println();

            System.out.println("== Plants ==");
            if (ecosystem.getPlantList().isEmpty()) {
                System.out.println("No plants in the ecosystem.");
            } else {
                ecosystem.getPlantList().forEach(plant -> System.out.println("- " + plant));
            }
            System.out.println();

            System.out.println("== Environment ==");
            System.out.println(ecosystem.getEnvironment());

            System.out.println("===========================================");
    }

    private void changingEcosystem(Ecosystem ecosystem)
    {
        while(true) {
            System.out.println("Select an action");
            System.out.println("1. Add animal");
            System.out.println("2. Update animal");
            System.out.println("3. Delete animal");
            System.out.println("4. Add plant");
            System.out.println("5. Update plant");
            System.out.println("6. Delete plant");
            System.out.println("7. Update environment");
            System.out.println("0. Exit");

            int choice = getValidIntInput();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addAnimal(ecosystem);
                    break;
                case 2:
                    updateAnimal(ecosystem);
                    break;
                case 3:
                    deleteAnimal(ecosystem);
                    break;
                case 4:
                    addPlant(ecosystem);
                    break;
                case 5:
                    updatePlant(ecosystem);
                    break;
                case 6:
                    deletePlant(ecosystem);
                    break;
                case 7:
                    updateEnvironment(ecosystem);
                case 0:
                    return;
                default:
                    System.out.println("Incorrect choice. Try again.");
            }
        }
    }
    private Animal inputNewAnimal(String name) {
        System.out.println("Enter the population (non-negative): ");
        int population = getValidNonNegativeIntInput();

        System.out.println("Enter the minimum temperature tolerance: ");
        int mintemp = getValidIntInput();

        System.out.println("Enter the maximum temperature tolerance: ");
        int maxtemp = getValidIntInput();

        while (maxtemp < mintemp) {
            System.out.println("Max temperature cannot be lower than min temperature. Enter the maximum temperature tolerance again:");
            maxtemp = getValidIntInput();
        }

        System.out.println("Enter the age (non-negative): ");
        int age = getValidNonNegativeIntInput();

        System.out.println("Choose type of food: ");
        String typefood = getValidFoodType();

        System.out.println("Enter the power (positive): ");
        int power = getValidPositiveIntInput();

        System.out.println("Enter the hunger level (1-99): ");
        int hungry = getValidIntInRange(1, 99);

        System.out.println("Enter the thirst level (1-99): ");
        int thirst = getValidIntInRange(1, 99);

        return new Animal(name, population, mintemp, maxtemp, age, typefood, power, hungry, thirst);
    }
    private int getValidIntInRange(int min, int max) {
        int value;
        do {
            value = getValidIntInput();
            if (value < min || value > max) {
                System.out.println("Invalid input. Please enter a value between " + min + " and " + max + ".");
            }
        } while (value < min || value > max);
        return value;
    }
    private int getValidPositiveIntInput() {
        int value;
        do {
            value = getValidIntInput();
            if (value <= 0) {
                System.out.println("Invalid input. Please enter a positive integer.");
            }
        } while (value <= 0);
        return value;
    }

    private int getValidNonNegativeIntInput() {
        int value;
        do {
            value = getValidIntInput();
            if (value < 0) {
                System.out.println("Invalid input. Please enter a non-negative integer.");
            }
        } while (value < 0);
        return value;
    }

    private String getValidFoodType() {
        while (true) {
            System.out.println("1. Carnivore");
            System.out.println("2. Herbivore");
            int choice = getValidIntInput();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    return "carnivore";
                case 2:
                    return "herbivore";
                default:
                    System.out.println("Incorrect choice. Try again.");
            }
        }
    }
    private int getValidIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.nextLine();
            }
        }
    }
    private double getValidDoubleInput() {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a decimal number.");
                scanner.nextLine();
            }
        }
    }

    private void addAnimal(Ecosystem ecosystem)
    {
        String name;
        while (true) {
            System.out.println("Enter the name: ");
            name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                break;
            } else {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            }
        }

        ecosystem.addAnimal(inputNewAnimal(name));
    }

    private void updateAnimal(Ecosystem ecosystem) {
        if (ecosystem.getAnimalList().isEmpty()) {
            System.out.println("No animals available to update.");
            return;
        }

        System.out.println("Available animals:");
        for (int i = 0; i < ecosystem.getAnimalList().size(); i++) {
            System.out.println((i + 1) + ": " + ecosystem.getAnimalList().get(i));
        }

        while (true)
        {
            System.out.print("Choose an animal to update: ");
            int choice = getValidIntInput();

            if (choice > 0 && choice <= ecosystem.getAnimalList().size()) {
                Animal selectedAnimal = ecosystem.getAnimalList().get(choice - 1);
                Animal updatedAnimal = inputNewAnimal(selectedAnimal.getName());
                ecosystem.updateAnimal(updatedAnimal);
                return;
            } else {
                System.out.println("Invalid choice. Please select a valid animal.");
            }
        }
    }

    private void deleteAnimal(Ecosystem ecosystem) {
        System.out.println("Available animals:");
        for (int i = 0; i < ecosystem.getAnimalList().size(); i++) {
            System.out.println((i + 1) + ": " + ecosystem.getAnimalList().get(i));
        }

        while (true)
        {
            System.out.print("Choose an animals to delete: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= ecosystem.getAnimalList().size()) {
                Animal selectedAnimal = ecosystem.getAnimalList().get(choice - 1);
                ecosystem.deleteAnimal(selectedAnimal);
                return;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private Plant inputNewPlant(String name) {
        System.out.println("Enter the population (non-negative): ");
        int population = getValidNonNegativeIntInput();

        System.out.println("Enter the minimum temperature tolerance: ");
        int mintemp = getValidIntInput();

        System.out.println("Enter the maximum temperature tolerance: ");
        int maxtemp = getValidIntInput();

        while (maxtemp < mintemp) {
            System.out.println("Max temperature cannot be lower than min temperature. Enter the maximum temperature tolerance again:");
            maxtemp = getValidIntInput();
        }

        System.out.println("Enter the age (non-negative): ");
        int age = getValidNonNegativeIntInput();

        System.out.println("Enter the water need level (1-99): ");
        int waterNeed = getValidIntInRange(1, 99);

        System.out.println("Enter the light need level (1-99): ");
        int lightNeed = getValidIntInRange(1, 99);

        return new Plant(name, population, mintemp, maxtemp, age, waterNeed, lightNeed);
    }
    private void addPlant(Ecosystem ecosystem) {
        String name;
        while (true) {
            System.out.println("Enter the name of the plant: ");
            name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                break;
            } else {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            }
        }

        ecosystem.addPlant(inputNewPlant(name));
    }

    private void updatePlant(Ecosystem ecosystem) {
        if (ecosystem.getPlantList().isEmpty()) {
            System.out.println("No plants available to update.");
            return;
        }

        System.out.println("Available plants:");
        for (int i = 0; i < ecosystem.getPlantList().size(); i++) {
            System.out.println((i + 1) + ": " + ecosystem.getPlantList().get(i));
        }

        while (true) {
            System.out.print("Choose a plant to update: ");
            int choice = getValidIntInput();

            if (choice > 0 && choice <= ecosystem.getPlantList().size()) {
                Plant selectedPlant = ecosystem.getPlantList().get(choice - 1);
                Plant updatedPlant = inputNewPlant(selectedPlant.getName());
                ecosystem.updatePlant(updatedPlant);
                System.out.println("Plant updated successfully.");
                return;
            } else {
                System.out.println("Invalid choice. Please select a valid plant.");
            }
        }
    }

    private void deletePlant(Ecosystem ecosystem) {
        if (ecosystem.getPlantList().isEmpty()) {
            System.out.println("No plants available to delete.");
            return;
        }

        System.out.println("Available plants:");
        for (int i = 0; i < ecosystem.getPlantList().size(); i++) {
            System.out.println((i + 1) + ": " + ecosystem.getPlantList().get(i));
        }

        while (true) {
            System.out.print("Choose a plant to delete: ");
            int choice = getValidIntInput();
            scanner.nextLine();

            if (choice > 0 && choice <= ecosystem.getPlantList().size()) {
                Plant selectedPlant = ecosystem.getPlantList().get(choice - 1);
                ecosystem.deletePlant(selectedPlant);
                System.out.println("Plant deleted successfully.");
                return;
            } else {
                System.out.println("Invalid choice. Please select a valid plant.");
            }
        }
    }
    private void updateEnvironment(Ecosystem ecosystem) {
        System.out.print("Enter new temperature: ");
        double newTemperature = getValidDoubleInput();
        ecosystem.getEnvironment().setTemperature(newTemperature);

        System.out.print("Enter new humidity (0-100): ");
        int newHumidity = getValidIntInRange(0, 100);
        ecosystem.getEnvironment().setHumidity(newHumidity);

        System.out.print("Enter new water level: ");
        int newWater = getValidIntInput();
        ecosystem.getEnvironment().setWater(newWater);

        System.out.print("Enter new sunlight level (0-100): ");
        int newSunlight = getValidIntInRange(0, 100);
        ecosystem.getEnvironment().setSunlight(newSunlight);

    }
}

