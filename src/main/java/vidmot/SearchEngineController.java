package vidmot;
import bakendi.Tour;
import bakendi.TourDatabase;
import java.util.List;
import java.util.stream.Collectors;
import java.text.Normalizer;
import java.time.LocalDate;

public class SearchEngineController {

    public List<Tour> searchTours(String query) {
        if (query == null || query.trim().isEmpty()) return List.of();

        // leyfir að skrifa íslenska stafi
        String normalizedQuery = removeAccents(query.toLowerCase());

        return TourDatabase.getAllTours().stream()
                .filter(tour ->
                        removeAccents(tour.getName().toLowerCase()).contains(normalizedQuery) ||
                                removeAccents(tour.getStartLocation().toLowerCase()).contains(normalizedQuery)
                )
                .collect(Collectors.toList());
    }

    public List<Tour> filterToursByDate(LocalDate selectedDate) {
        if (selectedDate == null) return TourDatabase.getAllTours();

        return TourDatabase.getAllTours().stream()
                .filter(tour -> tour.isAvailableOn(selectedDate))
                .collect(Collectors.toList());
    }

    private String removeAccents(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", ""); // Remove diacritic marks (accents)
    }
}