package vidmot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
import bakendi.TourDatabase;
public class HelloController {
    //Skilgreini instance af SearchEngineController
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
    /**
     * Upphafsstillir UI
     * Tengir <enter> til að triggera searches
     * Hleður öllum tours þegar UI opnast
     * Setur focus á search glugga
     */
    @FXML
    private void initialize() {
        searchEngineController = new SearchEngineController();

        if (fxLeitarvelTexti != null) {
            // Enter takki triggerar search
            fxLeitarvelTexti.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch();
                    event.consume();
                }
            });

            // Uppfærir leitarniðurstöðum while typing
            fxLeitarvelTexti.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

            // Hleður öllum tours á startup
            Platform.runLater(() -> {
                fxLeitarvelTexti.requestFocus();
                resetTourGrid();
            });
        }
    }
    /**
     * Sér um user search queries
     * Leitar að tour út frá því sem slegið er inn og uppfærir UI skv því
     * Ef search bar er tómur, eru allir tours til sýnis
     */
    @FXML
    private void handleSearch() {
        String query = fxLeitarvelTexti.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            resetTourGrid();
            return;
        }

        List<Tour> matchingTours = searchEngineController.searchTours(query);

        fxTourGridPane.getChildren().clear();

        int row = 0, col = 0;
        for (Tour tour : matchingTours) {
            VBox tourBox = createTourBox(tour);
            fxTourGridPane.add(tourBox, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Endurraðar tourgrid til að tours séu samþjappaðir jafnóðum skv innslætti
     */

    private void resetTourGrid() {
        fxTourGridPane.getChildren().clear();

        List<Tour> allTours = TourDatabase.getAllTours();
        int row = 0, col = 0;
        for (Tour tour : allTours) {
            VBox tourBox = createTourBox(tour);
            fxTourGridPane.add(tourBox, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
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

        // Set click event til að opna rétt tour details
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




}
