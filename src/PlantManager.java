import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlantManager {
    private List<Plant> plants;

    public PlantManager() {
        this.plants = new ArrayList<>();
    }

    // Přidání nové rostliny
    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    // Získání rostliny na zadaném indexu
    public Plant getPlant(int index) {
        return plants.get(index);
    }

    // Odebrání rostliny ze seznamu
    public void removePlant(int index) {
        plants.remove(index);
    }

    // Získání kopie seznamu rostlin
    public List<Plant> getPlants() {
        return new ArrayList<>(plants);
    }

    // Seznam rostlin, které je třeba zalít
    public List<Plant> getPlantsNeedingWater() {
        List<Plant> needWater = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Plant plant : plants) {
            LocalDate nextWatering = plant.getWatering().plusDays(plant.getFrequencyOfWatering());
            if (nextWatering.isBefore(today) || nextWatering.isEqual(today)) {
                needWater.add(plant);
            }
        }

        return needWater;
    }

    // Seřazení podle názvu (výchozí)
    public void sortByName() {
        plants.sort(Comparator.naturalOrder());
    }

    // Seřazení podle data poslední zálivky
    public void sortByWateringDate() {
        plants.sort(Comparator.comparing(Plant::getWatering));
    }

    // Načtení rostlin ze souboru
    public void loadFromFile(String filename) throws PlantException {
        plants.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Přeskočit prázdné řádky
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split("\t");

                    if (parts.length != 5) {
                        throw new PlantException("Chybný formát na řádku " + lineNumber +
                                ": očekáváno 5 položek oddělených tabulátorem, nalezeno " + parts.length);
                    }

                    String name = parts[0].trim();
                    String notes = parts[1].trim();
                    int frequency = Integer.parseInt(parts[2].trim());
                    LocalDate watering = LocalDate.parse(parts[3].trim());
                    LocalDate planted = LocalDate.parse(parts[4].trim());

                    Plant plant = new Plant(name, notes, planted, watering, frequency);
                    plants.add(plant);

                } catch (NumberFormatException e) {
                    throw new PlantException("Chybná frekvence zálivky na řádku " + lineNumber +
                            ": " + e.getMessage(), e);
                } catch (DateTimeParseException e) {
                    throw new PlantException("Chybný formát data na řádku " + lineNumber +
                            ": " + e.getMessage(), e);
                } catch (PlantException e) {
                    throw new PlantException("Chyba na řádku " + lineNumber + ": " + e.getMessage(), e);
                }
            }

        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor " + filename + " nebyl nalezen!", e);
        } catch (IOException e) {
            throw new PlantException("Chyba při čtení souboru " + filename + ": " + e.getMessage(), e);
        }
    }

    // Uložení rostlin do souboru
    public void saveToFile(String filename) throws PlantException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Plant plant : plants) {
                writer.write(plant.getName() + "\t" +
                        plant.getNotes() + "\t" +
                        plant.getFrequencyOfWatering() + "\t" +
                        plant.getWatering() + "\t" +
                        plant.getPlanted());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PlantException("Chyba při zápisu do souboru " + filename + ": " + e.getMessage(), e);
        }
    }

    // Počet rostlin v seznamu
    public int size() {
        return plants.size();
    }
}