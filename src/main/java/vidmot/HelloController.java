package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
//hingað
import javafx.application.Platform;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import vidmot.SearchEngineController;
import bakendi.Tour;
import java.io.InputStream;
import javafx.application.Platform;


public class HelloController {
    //Skilgreini instance af SearchEngineController
    private SearchEngineController searchEngineController;

    //Tengi við fx:id í FXML
    @FXML
    private TextField fxLeitarvelTexti;
    @FXML
    private Button fxLeitarvelTakki;


    //Tengi við tour grid í FXML
    @FXML
    private GridPane fxTourGridPane;

    @FXML
    private DatePicker fxDagatal;
    @FXML
    private VBox searchResultsContainer;
    @FXML
    private void initialize() {
        searchEngineController = new SearchEngineController();

        if (fxLeitarvelTexti != null) {
            fxLeitarvelTexti.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch();
                    event.consume();
                }
            });

            Platform.runLater(() -> fxLeitarvelTexti.requestFocus());
        }
    }

    @FXML
    private void handleSearch() {
        String query = fxLeitarvelTexti.getText().trim();
        if (query.isEmpty()) return;

        List<Tour> results = searchEngineController.searchToursByName(query);

        if (results.size() == 1) {
            // 🔥 If there's exactly one result, go straight to the details page
            goToTourDetails(results.get(0));
        } else {
            // Otherwise, update the search results in VBox
            displaySearchResults(results);
        }
    }


    private void goToTourDetails(Tour tour) {
        System.out.println("🔥 Navigating to tour details: " + tour.getName());

        // Switch to details view
        ViewSwitcher.switchTo(View.TOUR_DETAILS);

        // Load the tour details into the controller
        TourDetailsController detailsController = (TourDetailsController) ViewSwitcher.getController(View.TOUR_DETAILS);

        if (detailsController != null) {
            detailsController.loadTour(tour);
        } else {
            System.out.println("❌ Error: TourDetailsController is NULL");
        }
    }


    private void displaySearchResults(List<Tour> results) {
        System.out.println("📌 searchResultsContainer: " + searchResultsContainer);
        System.out.println("⚡ Updating UI with " + results.size() + " search results.");

        Platform.runLater(() -> {
            if (searchResultsContainer == null) {
                System.out.println("❌ searchResultsContainer is NULL! Check FXML bindings.");
                return;
            }

            // 🔹 Clear previous results
            searchResultsContainer.getChildren().clear();

            // 🔹 Ensure VBox visibility
            searchResultsContainer.setMinHeight(100);
            searchResultsContainer.setPrefHeight(200);
            searchResultsContainer.setMaxHeight(400);
            searchResultsContainer.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-background-color: lightgrey;");

            if (results.isEmpty()) {
                System.out.println("❌ No search results found.");
                searchResultsContainer.getChildren().add(new Label("No tours found."));
                return;
            }

            // 🔹 Display search results in VBox
            for (Tour tour : results) {
                VBox tourBox = createTourBox(tour);
                searchResultsContainer.getChildren().add(tourBox);
                System.out.println("✅ Added tour to UI: " + tour.getName());
            }

            System.out.println("🎯 UI update completed.");
        });
    }




    private VBox createTourBox(Tour tour) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        String imagePath = tour.getMainImage();
        ImageView imageView = new ImageView(); // Create an empty ImageView by default

        try {
            // Attempt to load the image
            InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);

            if (imageStream != null) {
                Image image = new Image(imageStream);
                imageView.setImage(image);
            } else {
                System.out.println("❌ Image not found: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("🚨 Error loading image: " + imagePath);
            e.printStackTrace();
        }

        imageView.setFitHeight(100);
        imageView.setFitWidth(160);
        imageView.setPreserveRatio(true);

        Label label = new Label(tour.getName());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        vbox.getChildren().addAll(imageView, label);
        return vbox;
    }





    //hingað
    @FXML
    private Label outputUsername;
    /**
     * Called when any grid cell is clicked.
     * Computes which cell was clicked and switches view accordingly.
     */
    @FXML
    private void handleCellClick(MouseEvent event) {
        // The clicked node is a VBox (the grid cell in hello-view.fxml)
        VBox cell = (VBox) event.getSource();

        // Get the row and column indices from the GridPane layout.
        // (Note: These might be null if not explicitly set, so we default them to 0)
        Integer row = GridPane.getRowIndex(cell);
        Integer col = GridPane.getColumnIndex(cell);
        if (row == null) row = 0;
        if (col == null) col = 0;

        // Calculate a 1-based index for the cell.
        // Assuming 3 columns in the GridPane:
        // index = (row * number_of_columns) + col + 1.
        int cellIndex = row * 3 + col + 1;
        System.out.println("Cell clicked: " + cellIndex);

        try {
            // Use the cell index to get the corresponding View enum constant.
            // Example: If cellIndex is 1, then we get View.GRID1, etc.
            View view = View.valueOf("GRID" + cellIndex);

            // Switch to the view using your ViewSwitcher class.
            ViewSwitcher.switchTo(view);

        } catch (IllegalArgumentException e) {
            // If there is no matching enum constant, handle it gracefully.
            System.err.println("No view available for cell: " + cellIndex);
        }
    }

    @FXML
    private void goToHome(ActionEvent event) {
        ViewSwitcher.switchTo(View.START);
    }

    @FXML
    private void goToLogin(ActionEvent event){ViewSwitcher.switchTo(View.LOGIN);
    }


    // Þessi tekur username sem er sláð inn og birtist það á forsíðunni
    public void updateLabel(String text){
        outputUsername.setText("Welcome " + text);
    }




}
