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
    @FXML private Text tourShortDescription, tourStartLocation, tourDuration, tourMinAge, tourLongDescription, tourPriceRange;
    /**
     * Hleður selected tour í UI.
     * @param tour Tour object sem inniheldur uppl um tour
     */
    public void loadTour(Tour tour) {
        if (tour == null) {
            return;
        }

        // hreinsar eldri myndir áður en nýrri er hlaðað
        tourMainImage.setImage(null);
        tourImage2.setImage(null);
        tourImage3.setImage(null);

        // hleður nýum myndum
        loadImage(tourMainImage, tour.getMainImage());
        loadImage(tourImage2, tour.getImage2());
        loadImage(tourImage3, tour.getImage3());

        // Setur upp text fields
        if (tourTitleLabel != null) tourTitleLabel.setText(tour.getName());
        if (tourShortDescription != null) tourShortDescription.setText(tour.getShortDescription());
        if (tourStartLocation != null) tourStartLocation.setText(tour.getStartLocation());
        if (tourDuration != null) tourDuration.setText(tour.getDuration() + " hours");
        if (tourMinAge != null) tourMinAge.setText(tour.getMinAge() + " years old");
        if (tourLongDescription != null) tourLongDescription.setText(tour.getLongDescription());
        if (tourPriceRange!=null) tourPriceRange.setText(String.format("%.0f ISK", tour.getVerdBilCheck()));

    }

    /**
     * Hleður myndum frá resources.
     * @param imageView, ImageView hluturinn sem myndin á að birtast í
     * @param imagePath nafn á file sem myndin er geymd í
     */
    private void loadImage(ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        // rétt format á path
        String correctedPath = imagePath.startsWith("/") ? imagePath : "/images/" + imagePath;

        InputStream stream = getClass().getResourceAsStream(correctedPath);
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