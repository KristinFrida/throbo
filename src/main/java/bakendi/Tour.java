package bakendi;

public class Tour {
    private String name;
    private String mainImage, image2, image3;
    private String shortDescription, longDescription;
    private String startLocation;
    private double duration;
    private int minAge;
    private double verdATour;
    public Tour(String name,
                String mainImage,
                String image2,
                String image3,
                String shortDescription,
                String startLocation,
                double verdATour,
                double duration,
                int minAge,
                String longDescription) {

        this.name = name;
        this.mainImage = mainImage;
        this.image2 = image2;
        this.image3 = image3;
        this.shortDescription = shortDescription;
        this.startLocation = startLocation;
        this.verdATour = verdATour;
        this.duration = duration;
        this.minAge = minAge;
        this.longDescription = longDescription;
    }

    // Getters
    public String getName() { return name; }
    public String getMainImage() { return mainImage; }
    public String getImage2() { return image2; }
    public String getImage3() { return image3; }
    public String getShortDescription() { return shortDescription; }
    public String getStartLocation() { return startLocation; }
    public double getDuration() { return duration; }
    public int getMinAge() { return minAge; }
    public String getLongDescription() { return longDescription; }
    public double getVerdATour() { return verdATour; }
}