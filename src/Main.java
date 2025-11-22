import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Main {
    public static void main(String[] args) {
        PlantManager manager = new PlantManager();

        try {
            System.out.println("=== 1. Načtení rostlin ze souboru kvetiny.txt ===");
            manager.loadFromFile("kvetiny.txt");
            System.out.println("Načteno " + manager.size() + " rostlin.");
            System.out.println();

            System.out.println("=== 2. Informace o zálivce pro všechny rostliny ===");
            for (Plant plant : manager.getPlants()) {
                System.out.println(plant.getWateringInfo());
            }
            System.out.println();

            System.out.println("=== 3. Přidání nové rostliny ===");
            Plant newPlant = new Plant("Fíkus", "Velká rostlina v obýváku",
                    LocalDate.of(2023, 5, 15),
                    LocalDate.of(2024, 11, 20),
                    5);
            manager.addPlant(newPlant);
            System.out.println("Přidána rostlina: " + newPlant.getName());
            System.out.println();

            System.out.println("=== 4. Přidání 10 tulipánů na prodej ===");
            for (int i = 1; i <= 10; i++) {
                Plant tulip = new Plant("Tulipán na prodej " + i,
                        "Tulipán určený k prodeji",
                        LocalDate.now(),
                        LocalDate.now(),
                        14);
                manager.addPlant(tulip);
            }
            System.out.println("Přidáno 10 tulipánů na prodej.");
            System.out.println("Celkový počet rostlin: " + manager.size());
            System.out.println();

            System.out.println("=== 5. Odebrání rostliny na třetí pozici (index 2) ===");
            Plant removed = manager.getPlant(2);
            System.out.println("Odebírám rostlinu: " + removed.getName());
            manager.removePlant(2);
            System.out.println("Rostlina odebrána. Aktuální počet: " + manager.size());
            System.out.println();

            System.out.println("=== 6. Uložení do nového souboru ===");
            manager.saveToFile("kvetiny-export.txt");
            System.out.println("Seznam rostlin uložen do souboru kvetiny-export.txt");
            System.out.println();

            System.out.println("=== 7. Opětovné načtení vygenerovaného souboru ===");
            PlantManager manager2 = new PlantManager();
            manager2.loadFromFile("kvetiny-export.txt");
            System.out.println("Načteno " + manager2.size() + " rostlin z exportovaného souboru.");
            System.out.println();

            System.out.println("=== 8. Seřazení podle názvu ===");
            manager.sortByName();
            System.out.println("První tři rostliny po seřazení podle názvu:");
            for (int i = 0; i < Math.min(3, manager.size()); i++) {
                System.out.println("  " + manager.getPlant(i).getName());
            }
            System.out.println();

            System.out.println("=== 9. Seřazení podle data zálivky ===");
            manager.sortByWateringDate();
            System.out.println("První tři rostliny po seřazení podle data zálivky:");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            for (int i = 0; i < Math.min(3, manager.size()); i++) {
                Plant p = manager.getPlant(i);
                System.out.println("  " + p.getName() + " - " + p.getWatering().format(formatter));
            }
            System.out.println();

            System.out.println("=== 10. Rostliny vyžadující zálivku ===");
            for (Plant plant : manager.getPlantsNeedingWater()) {
                System.out.println(plant.getWateringInfo());
            }
            System.out.println();

        } catch (PlantException e) {
            System.err.println("Chyba: " + e.getMessage());
        }

        // Test vadných souborů
        System.out.println("=== Test chybových stavů ===");

        System.out.println("\nTest 1: Pokus o načtení souboru se špatným datem");
        PlantManager testManager1 = new PlantManager();
        try {
            testManager1.loadFromFile("kvetiny-spatne-datum.txt");
        } catch (PlantException e) {
            System.err.println("Očekávaná chyba: " + e.getMessage());
        }

        System.out.println("\nTest 2: Pokus o načtení souboru se špatnou frekvencí");
        PlantManager testManager2 = new PlantManager();
        try {
            testManager2.loadFromFile("kvetiny-spatne-frekvence.txt");
        } catch (PlantException e) {
            System.err.println("Očekávaná chyba: " + e.getMessage());
        }

        System.out.println("\n=== Aplikace ukončena úspěšně ===");
    }
}