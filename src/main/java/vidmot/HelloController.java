package vidmot;

import bakendi.Tour;
import bakendi.TourDatabase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    // FXML reitir fyrir verðbil checkboxes
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
            // Enter takki triggerar search
            fxLeitarvelTexti.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearch();
                    event.consume();
                }
            });

            // Uppfærir leitarniðurstöðum while typing
            fxLeitarvelTexti.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

            // Hleður öllum tours á startup og setur focus á search glugga
            Platform.runLater(() -> {
                fxLeitarvelTexti.requestFocus();
                resetTourGrid();
            });
        }
    }

    /**
     * Sér um user search queries.
     * Ef search bar er tómur, eru allir tours til sýnis.
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
     * Endurraðar tourgrid til að sýna alla tours.
     */
    private void resetTourGrid() {
        List<Tour> allTours = TourDatabase.getAllTours();
        updateGridPane(allTours);
    }

    /**
     * Uppfærir GridPane til að sýna tiltekna lista af tours.
     *
     * @param tours Listi af tours sem eiga að birtast.
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

    /**
     * Nálgast details view fyrir valinn tour.
     *
     * @param tour Valinn tour.
     */
    private void goToTourDetails(Tour tour) {
        ViewSwitcher.switchTo(View.TOUR_DETAILS);
        TourDetailsController detailsController = (TourDetailsController) ViewSwitcher.getController(View.TOUR_DETAILS);
        if (detailsController != null) {
            detailsController.loadTour(tour);
        }
    }

    /**
     * Sýnir leitarniðurstöður í searchResultsContainer.
     *
     * @param results Listi af tours sem passa við leitina.
     */
    private void displaySearchResults(List<Tour> results) {
        Platform.runLater(() -> {
            if (searchResultsContainer == null) {
                return;
            }
            // Hreinsar fyrri niðurstöður.
            searchResultsContainer.getChildren().clear();
            searchResultsContainer.setMinHeight(100);
            searchResultsContainer.setPrefHeight(200);
            searchResultsContainer.setMaxHeight(400);

            if (results.isEmpty()) {
                searchResultsContainer.getChildren().add(new Label("No tours found."));
                return;
            }

            // Býr til og bætir við VBox fyrir hvern tour.
            for (Tour tour : results) {
                VBox tourBox = createTourBox(tour);
                searchResultsContainer.getChildren().add(tourBox);
            }
        });
    }

    /**
     * Býr til VBox element fyrir einn tour.
     * Inniheldur ljósmynd og nafn á tour.
     *
     * @param tour Tour til að birta.
     * @return VBox sem inniheldur tour upplýsingar.
     */
    private VBox createTourBox(Tour tour) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        // Notum CSS klasa úr styles.css í stað þess að setja stíla beint
        vbox.getStyleClass().add("grid-cell");

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
        // Bætum við CSS klasa fyrir titil texta
        label.getStyleClass().add("cell-title");

        vbox.getChildren().addAll(imageView, label);
        return vbox;
    }

    /**
     * Called when any grid cell is clicked.
     * Reiknar út hvaða reit var smellt á og skiptar um view.
     */
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

    /**
     * Þessi tekur username sem er slegið inn og birtir það á forsíðunni.
     *
     * @param text Username til að sýna.
     */
    public void updateLabel(String text) {
        outputUsername.setText("Welcome " + text);
    }

    /**
     * Sér um breytingar á verð-bilum.
     * Ef checkboxes eru valin, eru aðeins tours sem passa við þau sýnd.
     */
    @FXML
    private void handleFilterChange() {
        // 1. Safna notendavalnum verðbilum
        List<Predicate<Tour>> priceConditions = new ArrayList<>();
        if (fxVerdbil1.isSelected()) {
            // 0 - 5000
            priceConditions.add(t -> t.getVerdBilCheck() >= 0 && t.getVerdBilCheck() <= 5000);
        }
        if (fxVerdbil2.isSelected()) {
            // 5001 - 10000
            priceConditions.add(t -> t.getVerdBilCheck() >= 5001 && t.getVerdBilCheck() <= 10000);
        }
        if (fxVerdbil3.isSelected()) {
            // 10001 - 20000
            priceConditions.add(t -> t.getVerdBilCheck() >= 10001 && t.getVerdBilCheck() <= 20000);
        }
        if (fxVerdbil4.isSelected()) {
            // 20001+
            priceConditions.add(t -> t.getVerdBilCheck() >= 20001);
        }

        // Ef engin checkboxes eru valin, birtum við ALLA tours
        if (priceConditions.isEmpty()) {
            priceConditions.add(t -> true);
        }

        // 2. Sía tours út frá vali (OR logic)
        List<Tour> allTours = TourDatabase.getAllTours();
        List<Tour> filteredTours = allTours.stream()
                .filter(tour -> priceConditions.stream().anyMatch(cond -> cond.test(tour)))
                .collect(Collectors.toList());

        // 3. Uppfærum GridPane með síaðri niðurstöðu
        updateGridPane(filteredTours);
    }
}
