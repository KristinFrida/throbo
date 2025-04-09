package frontend;
import backend.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import backend.Tour;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;


public class TourDetailsController {
    @FXML private Label tourTitleLabel;
    @FXML private Button bookNowButton;
    @FXML private ImageView tourMainImage, tourImage2, tourImage3;
    @FXML private Text tourShortDescription, tourStartLocation, tourDuration, tourMinAge, tourLongDescription, tourPriceRange;

    private Tour selectedTour;

    /**
     * Load selected tour in UI
     * @param tour Tour object that stored information about a tour
     */
    public void loadTour(Tour tour) {
        if (tour == null) {
            System.out.println("loadTour() calles with null tour");
            return;
        }

        this.selectedTour = tour;

        // clears older pics before new ones load
        tourMainImage.setImage(null);
        tourImage2.setImage(null);
        tourImage3.setImage(null);

        // load new pics
        loadImage(tourMainImage, tour.getMainImage());
        loadImage(tourImage2, tour.getImage2());
        loadImage(tourImage3, tour.getImage3());

        if (tourTitleLabel != null) tourTitleLabel.setText(tour.getName());
        if (tourShortDescription != null) tourShortDescription.setText(tour.getShortDescription());
        if (tourStartLocation != null) tourStartLocation.setText(tour.getStartLocation());
        if (tourDuration != null) tourDuration.setText(tour.getDuration() + " hours");
        if (tourMinAge != null) tourMinAge.setText(tour.getMinAge() + " years old");
        if (tourLongDescription != null) tourLongDescription.setText(tour.getLongDescription());
        if (tourPriceRange!=null) {
            double price = tour.getPrice();
            tourPriceRange.setText(String.format("%,.0f ISK", price));
        }
        if (bookNowButton != null) {
            bookNowButton.setDisable(false);
        }

    }

    /**
     * Load pics from resources
     * @param imageView, ImageView where the pics appear in
     * @param imagePath name of the file that the pics are stored in
     */
    private void loadImage(ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        // correct format for path
        String correctedPath = imagePath.startsWith("/") ? imagePath : "/images/" + imagePath;

        InputStream stream = getClass().getResourceAsStream(correctedPath);
        if (stream != null) {
            imageView.setImage(new Image(stream));
        }
    }

    /**
     * Activate Booking Dialog when "Book Now" button is pressed
     */
    @FXML
    private void onBookNowClicked() {

        // check if user is logged in
        if(!UserRepository.isUserLoggedIn()) {
            System.out.println("User not logged in, direct to login page.");
            ViewSwitcher.switchTo(View.LOGIN);
            return;
        }

        if (selectedTour == null) {
            System.out.println("Error: No tour picked in onBookNowClicked()");
            return;
        }

        try {
            // load booking-dialog.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/booking-dialog.fxml"));
            Pane pane = loader.load();

            // fetch controller for booking-dialog
            BookingDialogController controller = loader.getController();
            controller.setTour(selectedTour);

            // set up a new window
            Stage stage = new Stage();
            stage.setTitle("Book Tour");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));

            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("could not load booking-dialog.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        ViewSwitcher.switchTo(View.START);
    }
}