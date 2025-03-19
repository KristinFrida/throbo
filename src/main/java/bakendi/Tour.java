package bakendi;

import java.time.LocalDate;
import java.util.List;

/**
 * Heldur utan um upplýsingar um tours
 */
public class Tour {
    private String name;
    private String mainImage, image2, image3;
    private String shortDescription, longDescription;
    private String startLocation;
    private double duration;
    private int minAge;
    private double price;
    private List<LocalDate> availableDates;

    /**
     * Smíðir tour hluti
     * @param name
     * @param mainImage
     * @param image2
     * @param image3
     * @param shortDescription
     * @param startLocation
     * @param duration
     * @param minAge
     * @param longDescription
     */
    public Tour(String name, String mainImage, String image2, String image3,
                String shortDescription, String startLocation, double duration,
                int minAge, String longDescription, double price, List<LocalDate> availableDates) {
        this.name = name;
        this.mainImage = mainImage;
        this.image2 = image2;
        this.image3 = image3;
        this.shortDescription = shortDescription;
        this.startLocation = startLocation;
        this.duration = duration;
        this.minAge = minAge;
        this.longDescription = longDescription;
        this.price = price;
        this.availableDates = availableDates;
    }

    public String getName() { return name; }
    public String getMainImage() { return mainImage; }
    public String getImage2() { return image2; }
    public String getImage3() { return image3; }
    public String getShortDescription() { return shortDescription; }
    public String getStartLocation() { return startLocation; }
    public double getDuration() { return duration; }
    public int getMinAge() { return minAge; }
    public String getLongDescription() { return longDescription; }
    public double getPrice() { return this.price; }
    public List<LocalDate> getAvailableDates() { return availableDates; }
    public boolean isAvailableOn(LocalDate date) { return availableDates.contains(date);}

    // Útreikna verðbil út frá price
    public int getVerdBilCheck() {
        if (price >= 0 && price <= 5000) return 1;
        if (price >= 5001 && price <= 10000) return 2;
        if (price >= 10001 && price <= 20000) return 3;
        if (price >= 20001) return 4;
        return 0; // Óþekkt verð
    }
}
