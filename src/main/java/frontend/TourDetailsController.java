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
     * Hleður selected tour í UI.
     * @param tour Tour object sem inniheldur uppl um tour
     */
    public void loadTour(Tour tour) {
        if (tour == null) {
            return;
        }

        this.selectedTour = tour;


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
        if (tourPriceRange!=null) {
            double price = tour.getPrice();
            tourPriceRange.setText(String.format("%,.0f ISK", price));
        }
        if (bookNowButton != null) {
            bookNowButton.setDisable(false);
        }

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

        // Correct format for path
        String correctedPath = imagePath.startsWith("/") ? imagePath : "/images/" + imagePath;

        InputStream stream = getClass().getResourceAsStream(correctedPath);
        if (stream != null) {
            imageView.setImage(new Image(stream));
        }
    }

    /**
     * Kveikir á Booking Dialog þegar ýtt er á "Book Now".
     */
    @FXML
    private void onBookNowClicked() {

        // Checks if user is logged in
        if(!UserRepository.isUserLoggedIn()) {
            ViewSwitcher.switchTo(View.LOGIN);
            return;
        }

        if (selectedTour == null) {
            return;
        }

        try {
            // Hleður booking-dialog.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/booking-dialog.fxml"));
            Pane pane = loader.load();

            // Sækir controller fyrir booking-dialog
            BookingDialogController controller = loader.getController();
            controller.setTour(selectedTour);

            // Setur upp nýjan glugga
            Stage stage = new Stage();
            stage.setTitle("Book Tour");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));

            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Gat ekki hlaðið booking-dialog.fxml");
            e.printStackTrace();
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

}