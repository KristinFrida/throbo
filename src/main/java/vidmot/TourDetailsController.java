package vidmot;
import bakendi.TourDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import bakendi.Tour;
import javafx.event.ActionEvent;
import java.io.InputStream;

/**
 * Controller til að birta upplýsingar um valinn tour (sem var settur inn í leitarvél)
 * Sér um að birta ítarlegar upplýsingar um hvern tour; nafn, lýsing, lengd, aldurstakmark og ljósmyndir.
 * Sækir uppl um tours úr TourDataBase og uppfærir UI skv þeim.
 */
public class TourDetailsController {
    @FXML private Label tourTitleLabel;
    @FXML private ImageView tourMainImage, tourImage2, tourImage3;
    @FXML private Text tourShortDescription, tourStartLocation, tourDuration, tourMinAge, tourLongDescription;

    /**
     * Hleður selected tour í UI.
     * @param tour Tour object sem inniheldur uppl um tour
     */
    public void loadTour(Tour tour) {
        if (tour == null) {
            System.out.println("❌ Error: tour is NULL!");
            return;
        }

        System.out.println("✅ Loading Tour: " + tour.getName());
        System.out.println("Short Description: " + tour.getShortDescription());
        System.out.println("Start Location: " + tour.getStartLocation());
        System.out.println("Duration: " + tour.getDuration() + " hours");
        System.out.println("Min Age: " + tour.getMinAge() + " years old");
        System.out.println("Long Description: " + tour.getLongDescription());

        // Debug: Are the FXML elements null?
        if (tourTitleLabel == null) System.out.println("❌ tourTitleLabel is NULL!");
        if (tourShortDescription == null) System.out.println("❌ tourShortDescription is NULL!");
        if (tourStartLocation == null) System.out.println("❌ tourStartLocation is NULL!");
        if (tourDuration == null) System.out.println("❌ tourDuration is NULL!");
        if (tourMinAge == null) System.out.println("❌ tourMinAge is NULL!");
        if (tourLongDescription == null) System.out.println("❌ tourLongDescription is NULL!");

        // Set values (only if they are NOT null)
        if (tourTitleLabel != null) tourTitleLabel.setText(tour.getName());
        if (tourShortDescription != null) tourShortDescription.setText(tour.getShortDescription());
        if (tourStartLocation != null) tourStartLocation.setText(tour.getStartLocation());
        if (tourDuration != null) tourDuration.setText(tour.getDuration() + " hours");
        if (tourMinAge != null) tourMinAge.setText(tour.getMinAge() + " years old");
        if (tourLongDescription != null) tourLongDescription.setText(tour.getLongDescription());

        // Load Images
        loadImage(tourMainImage, tour.getMainImage());
        loadImage(tourImage2, tour.getImage2());
        loadImage(tourImage3, tour.getImage3());
    }

    /**
     * Hleður myndum frá resources.
     * @param imageView, ImageView hluturinn sem myndin á að birtast í
     * @param imagePath nafn á file sem myndin er geymd í
     */
    private void loadImage(ImageView imageView, String imagePath) {
        InputStream stream = getClass().getResourceAsStream("/images/" + imagePath);
        if (stream != null) {
            imageView.setImage(new Image(stream));
        }
    }
    /**
     * Fer til baka í start view
     * @param event event triggered af back button
     */
    @FXML
    private void goBack(ActionEvent event) {
        ViewSwitcher.switchTo(View.START);
    }
    /**
     * Hleður og sýnir uppl um tour fyrir gefið tour name
     * @param tourName nafn toursins til að sýna(display)
     */
    public void goToTourDetails(String tourName) {
        Tour tour = TourDatabase.getTourByName(tourName);
        if (tour != null) {
            TourDetailsController detailsController = (TourDetailsController) ViewSwitcher.getController(View.TOUR_DETAILS);
            detailsController.loadTour(tour);
            ViewSwitcher.switchTo(View.TOUR_DETAILS);
        }
    }
}
