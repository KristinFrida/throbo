package bakendi;

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
    private int verdBilCheck;

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
     * @param verdBilCheck
     */
    public Tour(String name, String mainImage, String image2, String image3,
                String shortDescription, String startLocation, double duration,
                int minAge, String longDescription, int verdBilCheck) {
        this.name = name;
        this.mainImage = mainImage;
        this.image2 = image2;
        this.image3 = image3;
        this.shortDescription = shortDescription;
        this.startLocation = startLocation;
        this.duration = duration;
        this.minAge = minAge;
        this.longDescription = longDescription;
        this.verdBilCheck = verdBilCheck;
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
    public double getVerdBilCheck() {return verdBilCheck;}
}