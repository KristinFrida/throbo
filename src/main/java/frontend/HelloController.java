package frontend;

import backend.Tour;
import backend.TourDatabase;
import backend.TourFilter;
import backend.UserRepository;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import backend.BookingManager;


public class HelloController {

    private SearchEngineController searchEngineController = new SearchEngineController();

    @FXML private TextField fxSearchEngineText;
    @FXML private GridPane fxTourGridPane;
    @FXML private DatePicker datePicker;
    @FXML private Text outputUsername;
    @FXML private CheckBox fxPriceRange1;
    @FXML private CheckBox fxPriceRange2;
    @FXML private CheckBox fxPriceRange3;
    @FXML private CheckBox fxPriceRange4;
    @FXML private Button fxLoginButton;
    @FXML private Label fxNoResultsLabel;

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
        assert fxSearchEngineText != null : "LeitarvelTexti is not injected";
        assert fxLoginButton != null : "LoginButton is not injected";
        refreshLoginState();

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                getStyleClass().removeAll("past-date", "available-date");

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    getStyleClass().add("past-date");
                } else {
                    getStyleClass().add("available-date");
                }
            }
        });

        searchEngineController = new SearchEngineController();
        fxSearchEngineText.setOnKeyPressed(event -> {
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
            fxLoginButton.setText("My Page");
            fxLoginButton.setOnAction(e -> ViewSwitcher.switchTo(View.MYPAGE));
            Platform.runLater(() -> {
                MyPageController myPage = (MyPageController) ViewSwitcher.lookup(View.MYPAGE);
                if (myPage != null) {
                    myPage.refreshPage();
                }
            });
        } else {
            fxLoginButton.setText("Login");
            fxLoginButton.setOnAction(e -> ViewSwitcher.switchTo(View.LOGIN));

            outputUsername.setText("Welcome");
            clearAllFilters();
            updateDisplayedTours(null);

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
                .filter(tour -> BookingManager.getTotalPeopleForTourOnDate(tour.getName(), selectedDate) < 20)
                .collect(Collectors.toList());

        String query = fxSearchEngineText.getText().trim().toLowerCase();
        List<Tour> searchFiltered = query.isEmpty()
                ? dateFiltered
                : searchEngineController.searchTours(query).stream()
                .filter(dateFiltered::contains)
                .collect(Collectors.toList());

        List<java.util.function.Predicate<Tour>> priceConditions = TourFilter.buildPriceConditions(
                fxPriceRange1.isSelected(),
                fxPriceRange2.isSelected(),
                fxPriceRange3.isSelected(),
                fxPriceRange4.isSelected()
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

    public void clearAllFilters() {
        fxSearchEngineText.clear();
        datePicker.setValue(null);

        fxPriceRange1.setSelected(false);
        fxPriceRange2.setSelected(false);
        fxPriceRange3.setSelected(false);
        fxPriceRange4.setSelected(false);

        fxLocationReykjavik.setSelected(false);
        fxLocationVik.setSelected(false);
        fxLocationAkureyri.setSelected(false);
        fxLocationHvolsvollur.setSelected(false);
        fxLocationSkaftafell.setSelected(false);
        fxLocationJokulsarlon.setSelected(false);
        fxLocationBlueLagoon.setSelected(false);
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

    public void refreshFilteredTours() {
        updateDisplayedTours(datePicker.getValue());
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