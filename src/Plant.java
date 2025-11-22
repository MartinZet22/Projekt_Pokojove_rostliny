import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Plant implements Comparable<Plant> {
    private String name;
    private String notes;
    private LocalDate planted;
    private LocalDate watering;
    private int frequencyOfWatering;

    // Konstruktor pro nastavení všech atributů
    public Plant(String name, String notes, LocalDate planted, LocalDate watering, int frequencyOfWatering) throws PlantException {
        this.name = name;
        this.notes = notes;
        this.planted = planted;
        setWatering(watering);
        setFrequencyOfWatering(frequencyOfWatering);
    }

    // Konstruktor s prázdnou poznámkou a dnešním datem
    public Plant(String name, LocalDate planted, int frequencyOfWatering) throws PlantException {
        this(name, "", planted, LocalDate.now(), frequencyOfWatering);
    }

    // Konstruktor pouze s názvem, výchozí frekvence 7 dnů
    public Plant(String name) throws PlantException {
        this(name, "", LocalDate.now(), LocalDate.now(), 7);
    }

    // Gettery
    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getPlanted() {
        return planted;
    }

    public LocalDate getWatering() {
        return watering;
    }

    public int getFrequencyOfWatering() {
        return frequencyOfWatering;
    }

    // Settery
    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPlanted(LocalDate planted) {
        this.planted = planted;
    }

    public void setWatering(LocalDate watering) throws PlantException {
        if (watering.isBefore(planted)) {
            throw new PlantException("Datum poslední zálivky (" + watering +
                    ") nemůže být starší než datum zasazení (" + planted + ")!");
        }
        this.watering = watering;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) throws PlantException {
        if (frequencyOfWatering <= 0) {
            throw new PlantException("Frekvence zálivky musí být kladné číslo! Zadáno: " + frequencyOfWatering);
        }
        this.frequencyOfWatering = frequencyOfWatering;
    }

    // Metoda pro informaci o zálivce
    public String getWateringInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate nextWatering = watering.plusDays(frequencyOfWatering);

        return "Rostlina: " + name +
                ", poslední zálivka: " + watering.format(formatter) +
                ", doporučená další zálivka: " + nextWatering.format(formatter);
    }

    // Metoda pro zalití rostliny dnes
    public void doWateringNow() throws PlantException {
        setWatering(LocalDate.now());
    }

    // Výchozí řazení podle názvu
    @Override
    public int compareTo(Plant other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "Plant{" +
                "name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", planted=" + planted +
                ", watering=" + watering +
                ", frequencyOfWatering=" + frequencyOfWatering +
                '}';
    }
}