package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
//hingað
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import vidmot.SearchEngineController;
import bakendi.Tour;
import java.io.InputStream;
import javafx.application.Platform;
import javafx.scene.Node;
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
    private CheckBox fxVerdbil1, fxVerdbil2, fxVerdbil3, fxVerdbil4;
    @FXML
    /**
     * Upphafsstillir controllerinn
     * Tengir <enter> til að triggera searches og að search bar fá focus þegar UI opnast, cursorinn er þar
     */
    private void initialize() {
        searchEngineController = new SearchEngineController();

        if (fxLeitarvelTexti != null) {
            fxLeitarvelTexti.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch();
                    event.consume();
                }
            });
            //setur cursorinn í leitarvélina
            Platform.runLater(() -> fxLeitarvelTexti.requestFocus());
        }
    }
    /**
     * Sér um user search queries
     * Leitar að tour út frá því sem slegið er inn og uppfærir UI skv því
     */
    @FXML
    private void handleSearch() {
        String query = fxLeitarvelTexti.getText().trim();
        if (query.isEmpty()) return;

        List<Tour> results = searchEngineController.searchToursByName(query);

        if (results.size() == 1) {
            goToTourDetails(results.get(0));
        } else {
            displaySearchResults(results);
        }
    }

    /**
     * Nálgast details view fyrir valinn tour
     * @param tour Valinn tour
     */
    private void goToTourDetails(Tour tour) {
        ViewSwitcher.switchTo(View.TOUR_DETAILS);

        TourDetailsController detailsController = (TourDetailsController) ViewSwitcher.getController(View.TOUR_DETAILS);

        if (detailsController != null) {
            detailsController.loadTour(tour);
        }
    }
    /**
     * Sýnir leitarniðurstöður í searchResultsContainer
     * @param results Listinn af tours sem matcha valinn tour (sem slegið var inn í leitarvél)
     */
    private void displaySearchResults(List<Tour> results) {
        Platform.runLater(() -> {
            if (searchResultsContainer == null) {
                return;
            }
            // hreinsar previous results
            searchResultsContainer.getChildren().clear();

            // Setur upp VBox hlutföll og style
            searchResultsContainer.setMinHeight(100);
            searchResultsContainer.setPrefHeight(200);
            searchResultsContainer.setMaxHeight(400);

            if (results.isEmpty()) {
                searchResultsContainer.getChildren().add(new Label("No tours found."));
                return;
            }

            // sýna Display search results í VBox
            for (Tour tour : results) {
                VBox tourBox = createTourBox(tour);
                searchResultsContainer.getChildren().add(tourBox);
            }
        });
    }
    /**
     * Býr til VBox element fyrir single tour result
     * Inniheldur ljósmynd og nafn á tour
     * @param tour Tour to dislplay
     * @return VBox sem inniheldur tour details
     */
    private VBox createTourBox(Tour tour) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 5px;");

        String imagePath = tour.getMainImage();
        ImageView imageView = new ImageView();

        try {
            InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);

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
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        vbox.getChildren().addAll(imageView, label);
        return vbox;
    }
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
    @FXML
    private void filterTours() {
        // Check which price-range checkboxes are selected:
        boolean range1 = fxVerdbil1.isSelected();  // 0–5000
        boolean range2 = fxVerdbil2.isSelected();  // 5001–10000
        boolean range3 = fxVerdbil3.isSelected();  // 10001–20000
        boolean range4 = fxVerdbil4.isSelected();  // 20001+

        // Go through each cell in the GridPane:
        for (Node node : fxTourGridPane.getChildren()) {
            if (node instanceof VBox cell) {
                // Suppose you've stored the Tour data on the VBox via setUserData() earlier:
                Tour tour = (Tour) cell.getUserData();

                // Get the price in ISK (make sure your Tour object has such a field or method)
                double priceISK = tour.getVerdATour();

                // Does this price fall within any of the *selected* ranges?
                boolean matches = matchesSelectedRange(priceISK, range1, range2, range3, range4);

                // Show/hide the cell depending on whether it matches:
                cell.setVisible(matches);
                cell.setManaged(matches);
                // setManaged(false) removes the node from layout calculations, so no empty space is left.
            }
        }
    }

    private boolean matchesSelectedRange(double priceISK,
                                         boolean r1, boolean r2, boolean r3, boolean r4) {
        // If NONE of the checkboxes are selected, you might choose to show everything or hide everything.
        // For demonstration, let's show all if none are selected:
        if (!r1 && !r2 && !r3 && !r4) {
            return true;  // Show all tours if no filter is selected
        }

        // 0–5000
        if (r1 && priceISK >= 0 && priceISK <= 5000) {
            return true;
        }
        // 5001–10000
        if (r2 && priceISK > 5000 && priceISK <= 10000) {
            return true;
        }
        // 10001–20000
        if (r3 && priceISK > 10000 && priceISK <= 20000) {
            return true;
        }
        // 20001+
        if (r4 && priceISK > 20000) {
            return true;
        }

        // If it didn’t match any *selected* range, return false
        return false;
    }




}
