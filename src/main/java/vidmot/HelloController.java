package vidmot;

import bakendi.Tour;
import bakendi.TourDatabase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

            // Uppfærir leitarniðurstöður while typing
            fxLeitarvelTexti.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

            // Hleður öllum tours á startup og setur focus á search glugga
            Platform.runLater(() -> {
                fxLeitarvelTexti.requestFocus();
                updateDisplayedTours();  // sýna alla tours í byrjun
            });
        }
    }

    /**
     * Meðhöndlar bæði texta-leit og verð-filter og birtir niðurstöðu.
     * Kallast frá handleSearch() og handleFilterChange().
     */
    private void updateDisplayedTours() {
        // 1. Byrjum með alla tours
        List<Tour> allTours = TourDatabase.getAllTours();

        // 2. Sía út frá textaleit, ef notandi hefur slegið inn eitthvað
        String query = (fxLeitarvelTexti != null) ? fxLeitarvelTexti.getText().trim().toLowerCase() : "";
        List<Tour> searchFiltered;
        if (query.isEmpty()) {
            // Ef leit er tóm, notum alla tours
            searchFiltered = allTours;
        } else {
            // Notum searchEngineController til að finna viðeigandi tours
            searchFiltered = searchEngineController.searchTours(query);
        }

        // 3. Safna saman verðbilum (checkbox filters)
        List<Predicate<Tour>> priceConditions = new ArrayList<>();
        if (fxVerdbil1.isSelected()) {
            // 0 - 5000 ISK
            priceConditions.add(t -> t.getVerdBilCheck() >= 0 && t.getVerdBilCheck() <= 5000);
        }
        if (fxVerdbil2.isSelected()) {
            // 5001 - 10000 ISK
            priceConditions.add(t -> t.getVerdBilCheck() >= 5001 && t.getVerdBilCheck() <= 10000);
        }
        if (fxVerdbil3.isSelected()) {
            // 10001 - 20000 ISK
            priceConditions.add(t -> t.getVerdBilCheck() >= 10001 && t.getVerdBilCheck() <= 20000);
        }
        if (fxVerdbil4.isSelected()) {
            // 20001+
            priceConditions.add(t -> t.getVerdBilCheck() >= 20001);
        }
        // Ef engin checkbox eru valin => leyfum öll verð
        if (priceConditions.isEmpty()) {
            priceConditions.add(t -> true);
        }

        // 4. "OR" logic fyrir verðbil, en "AND" við textaleit
        List<Tour> finalFiltered = searchFiltered.stream()
                .filter(tour -> priceConditions.stream().anyMatch(cond -> cond.test(tour)))
                .collect(Collectors.toList());

        // 5. Uppfærum GridPane með loka-niðurstöðunni
        updateGridPane(finalFiltered);
    }

    /**
     * Sér um user search queries.
     * Kallar á sameiginlegt updateDisplayedTours() svo verðcheckbox og leit séu samstillt.
     */
    @FXML
    private void handleSearch() {
        updateDisplayedTours();
    }

    /**
     * Endurraðar tourgrid til að sýna alla tours (ef þú vilt hafa þetta áfram).
     * Hér gætirðu líka einfaldlega kallað á updateDisplayedTours()
     * með tómu query og engum verðbilum.
     */
    private void resetTourGrid() {
        updateDisplayedTours();
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
     * Sýnir leitarniðurstöður í searchResultsContainer (ef þú notar þetta umfram GridPane).
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
     * Kallar á sama updateDisplayedTours() og leitin, svo þær virki saman.
     */
    @FXML
    private void handleFilterChange() {
        updateDisplayedTours();
    }
}
