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

public class TourDetailsController {

    @FXML private Label tourTitleLabel;
    @FXML private ImageView tourMainImage, tourImage2, tourImage3;
    @FXML private Text tourShortDescription, tourStartLocation, tourDuration, tourMinAge, tourLongDescription;

    /**
     * Loads the selected tour into the UI dynamically.
     */
    public void loadTour(Tour tour) {
        if (tour == null) {
            System.out.println("❌ Error: Tour is NULL!");
            return;
        }

        System.out.println("✅ Loading tour: " + tour.getName());

        // Set text fields
        tourTitleLabel.setText(tour.getName());
        tourShortDescription.setText(tour.getShortDescription());
        tourStartLocation.setText(tour.getStartLocation());
        tourDuration.setText(tour.getDuration() + " hours");
        tourMinAge.setText(tour.getMinAge() + " years old");
        tourLongDescription.setText(tour.getLongDescription());

        // Load images safely
        loadImage(tourMainImage, tour.getMainImage());
        loadImage(tourImage2, tour.getImage2());
        loadImage(tourImage3, tour.getImage3());
    }

    /**
     * Loads an image from resources safely.
     */
    private void loadImage(ImageView imageView, String imagePath) {
        InputStream stream = getClass().getResourceAsStream("/images/" + imagePath);
        if (stream != null) {
            imageView.setImage(new Image(stream));
        } else {
            System.out.println("❌ Image not found: " + imagePath);
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        ViewSwitcher.switchTo(View.START);
    }

    public void goToTourDetails(String tourName) {
        Tour tour = TourDatabase.getTourByName(tourName);
        if (tour != null) {
            TourDetailsController detailsController = (TourDetailsController) ViewSwitcher.getController(View.TOUR_DETAILS);
            detailsController.loadTour(tour);
            ViewSwitcher.switchTo(View.TOUR_DETAILS);
        } else {
            System.out.println("❌ Error: No tour found with name " + tourName);
        }
    }

}
