package vidmot;

import bakendi.Tour;
import bakendi.TourDatabase;
import bakendi.TourFilter;
import bakendi.UserRepository;

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
import javafx.scene.text.Text;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {

    private SearchEngineController searchEngineController = new SearchEngineController();

    @FXML private TextField fxLeitarvelTexti;
    @FXML private GridPane fxTourGridPane;
    @FXML private DatePicker datePicker;
    @FXML private VBox searchResultsContainer;
    @FXML private Text outputUsername;
    @FXML private CheckBox fxVerdbil1;
    @FXML private CheckBox fxVerdbil2;
    @FXML private CheckBox fxVerdbil3;
    @FXML private CheckBox fxVerdbil4;
    @FXML private Button fxLoginTakki;
    @FXML private Label fxNoResultsLabel;

    //Fyrir location checkbox
    @FXML private CheckBox fxLocationReykjavik;
    @FXML private CheckBox fxLocationVik;
    @FXML private CheckBox fxLocationAkureyri;
    @FXML private CheckBox fxLocationHvolsvollur;
    @FXML private CheckBox fxLocationSkaftafell;
    @FXML private CheckBox fxLocationJokulsarlon;
    @FXML private CheckBox fxLocationBlueLagoon;


    @FXML
    private void initialize() {
        assert datePicker != null : "Datepicker is not injected";
        assert fxLeitarvelTexti != null : "LeitarvelTexti is not injected";
        assert fxLoginTakki != null : "LoginTakki is not injected";
        refreshLoginState();

        searchEngineController = new SearchEngineController();
        fxLeitarvelTexti.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSearchClicked();
                event.consume();
            }
        });

        Platform.runLater(() -> {
            updateDisplayedTours(null);
            refreshLoginState();
        });
    }

    public void refreshLoginState() {
        System.out.println("Checking login state...");
        System.out.println("User logged in? " + UserRepository.isUserLoggedIn());

        if (UserRepository.isUserLoggedIn()) {
            fxLoginTakki.setText("My Page");
            fxLoginTakki.setOnAction(e -> ViewSwitcher.switchTo(View.MYPAGE));
            Platform.runLater(() -> {
                MyPageController myPage = (MyPageController) ViewSwitcher.lookup(View.MYPAGE);
                if (myPage != null) {
                    myPage.refreshPage();
                }
            });
        } else {
            fxLoginTakki.setText("Login");
            fxLoginTakki.setOnAction(e -> ViewSwitcher.switchTo(View.LOGIN));

            // 👇 Reset welcome message on logout
            outputUsername.setText("Welcome");
        }
    }


    @FXML
    private void onSearchClicked() {
        LocalDate selectedDate = datePicker.getValue();
        updateDisplayedTours(selectedDate);
    }

    @FXML
    private void handleFilterChange() {
        LocalDate selectedDate = datePicker.getValue();
        updateDisplayedTours(selectedDate);
    }

    private void updateDisplayedTours(LocalDate selectedDate) {
        List<Tour> allTours = TourDatabase.getAllTours();

        List<Tour> dateFiltered = (selectedDate == null)
                ? allTours
                : allTours.stream()
                .filter(tour -> tour.isAvailableOn(selectedDate))
                .collect(Collectors.toList());

        String query = fxLeitarvelTexti.getText().trim().toLowerCase();
        List<Tour> searchFiltered = query.isEmpty()
                ? dateFiltered
                : searchEngineController.searchTours(query).stream()
                .filter(dateFiltered::contains)
                .collect(Collectors.toList());

        List<java.util.function.Predicate<Tour>> priceConditions = TourFilter.buildPriceConditions(
                fxVerdbil1.isSelected(),
                fxVerdbil2.isSelected(),
                fxVerdbil3.isSelected(),
                fxVerdbil4.isSelected()
        );

        List<java.util.function.Predicate<Tour>>  locationConditions = TourFilter.buildLocationConditions(
                fxLocationReykjavik.isSelected(),
                fxLocationVik.isSelected(),
                fxLocationAkureyri.isSelected(),
                fxLocationHvolsvollur.isSelected(),
                fxLocationSkaftafell.isSelected(),
                fxLocationJokulsarlon.isSelected(),
                fxLocationBlueLagoon.isSelected()
        );


        List<Tour> finalFiltered = TourFilter.filterByLocation(TourFilter.filterByPrice(searchFiltered, priceConditions), locationConditions);
        updateGridPane(finalFiltered);
    }

    private void updateGridPane(List<Tour> tours) {
        fxTourGridPane.getChildren().clear();

        if (tours.isEmpty()) {
            fxTourGridPane.setVisible(false);
            fxTourGridPane.setManaged(false);

            fxNoResultsLabel.setVisible(true);
            fxNoResultsLabel.setManaged(true);
            return;
        }

        fxTourGridPane.setVisible(true);
        fxTourGridPane.setManaged(true);
        fxNoResultsLabel.setVisible(false);
        fxNoResultsLabel.setManaged(false);

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
        try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                imageView.setImage(new Image(imageStream));
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

    public void updateLabel(String text) {
        outputUsername.setText("Welcome " + text);
    }

    public void clearLabel() {
        outputUsername.setText("Welcome");
    }
}