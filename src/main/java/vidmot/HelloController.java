package vidmot;

import bakendi.Tour;
import bakendi.TourDatabase;
import bakendi.TourFilter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;

public class HelloController {

    // Skilgreini instance af SearchEngineController
    private SearchEngineController searchEngineController;

    @FXML
    private TextField fxLeitarvelTexti;
    @FXML
    private Button fxLeitarvelTakki;
    @FXML
    private GridPane fxTourGridPane;
    @FXML
    private DatePicker fxDagatal;
    @FXML
    private VBox searchResultsContainer;
    @FXML
    private Label outputUsername;

    // Verðbil checkboxes
    @FXML
    private CheckBox fxVerdbil1; // "0 - 5000 ISK"
    @FXML
    private CheckBox fxVerdbil2; // "5001 - 10000 ISK"
    @FXML
    private CheckBox fxVerdbil3; // "10001 - 20000 ISK"
    @FXML
    private CheckBox fxVerdbil4; // "20001+ ISK"

    @FXML
    private void initialize() {
        searchEngineController = new SearchEngineController();

        if (fxLeitarvelTexti != null) {
            // ENTER takki triggerar leit
            fxLeitarvelTexti.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch();
                    event.consume();
                }
            });

            // Uppfærir leit-niðurstöður meðan notandi skrifar
            fxLeitarvelTexti.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

            // Hleður öllum tours á startup
            Platform.runLater(() -> {
                fxLeitarvelTexti.requestFocus();
                updateDisplayedTours();
            });
        }
    }

    /**
     * Kallast þegar notandi breytir leitartexta
     * eða þegar ýtt er á Search takka.
     */
    @FXML
    private void handleSearch() {
        updateDisplayedTours();
    }

    /**
     * Kallast þegar notandi breytir verðbil-checkboxes.
     */
    @FXML
    private void handleFilterChange() {
        updateDisplayedTours();
    }

    /**
     * Kallar á TourFilter til að sía út frá texta og verðbilum og birtir niðurstöður.
     */
    private void updateDisplayedTours() {
        List<Tour> allTours = TourDatabase.getAllTours();

        String query = fxLeitarvelTexti.getText().trim().toLowerCase();

        List<Tour> searchFiltered = query.isEmpty()
                ? allTours
                : searchEngineController.searchTours(query);

        List<java.util.function.Predicate<Tour>> priceConditions = TourFilter.buildPriceConditions(
                fxVerdbil1.isSelected(),
                fxVerdbil2.isSelected(),
                fxVerdbil3.isSelected(),
                fxVerdbil4.isSelected()
        );

        List<Tour> finalFiltered = TourFilter.filterByPrice(searchFiltered, priceConditions);
        updateGridPane(finalFiltered);
    }

    /**
     * Uppfærir GridPane miðað við gefinn lista af Tours.
     */
    private void updateGridPane(List<Tour> tours) {
        fxTourGridPane.getChildren().clear();
        int row = 0, col = 0;

        for (Tour tour : tours) {
            VBox tourBox = createTourBox(tour);
            fxTourGridPane.add(tourBox, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createTourBox(Tour tour) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("grid-cell");

        vbox.setOnMouseClicked(event -> goToTourDetails(tour));

        String imagePath = tour.getMainImage();
        ImageView imageView = new ImageView();
        try {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                imageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setFitHeight(100);
        imageView.setFitWidth(160);
        imageView.setPreserveRatio(true);

        Label label = new Label(tour.getName());
        label.getStyleClass().add("cell-title");

        vbox.getChildren().addAll(imageView, label);
        return vbox;
    }

    private void goToTourDetails(Tour tour) {
        ViewSwitcher.switchTo(View.TOUR_DETAILS);
        TourDetailsController detailsController =
                (TourDetailsController) ViewSwitcher.getController(View.TOUR_DETAILS);
        if (detailsController != null) {
            detailsController.loadTour(tour);
        }
    }

    @FXML
    private void handleCellClick(MouseEvent event) {
        VBox cell = (VBox) event.getSource();
        Integer row = GridPane.getRowIndex(cell);
        Integer col = GridPane.getColumnIndex(cell);
        if (row == null) row = 0;
        if (col == null) col = 0;
        int cellIndex = row * 3 + col + 1;
        System.out.println("Cell clicked: " + cellIndex);
        try {
            View view = View.valueOf("GRID" + cellIndex);
            ViewSwitcher.switchTo(view);
        } catch (IllegalArgumentException e) {
            System.err.println("No view available for cell: " + cellIndex);
        }
    }

    @FXML
    private void goToHome(ActionEvent event) {
        ViewSwitcher.switchTo(View.START);
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        ViewSwitcher.switchTo(View.LOGIN);
    }

    public void updateLabel(String text) {
        outputUsername.setText("Welcome " + text);
    }
}
