package bakendi;


public class Booking {
    private int id;
    private String tourName;
    private int people;
    private String date;
    private String pickup;

    public Booking(int id, String tourName, int people, String date, boolean pickup) {
        this.id = id;
        this.tourName = tourName;
        this.people = people;
        this.date = date;
        this.pickup = pickup ? "Yes" : "No";
    }

    public int getId() {
        return id;
    }

    public String getTourName() {
        return tourName;
    }

    public int getPeople() {
        return people;
    }

    public String getDate() {
        return date;
    }

    public String getPickup() {
        return pickup;
    }
}
