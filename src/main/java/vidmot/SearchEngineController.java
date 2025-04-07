package vidmot;
import bakendi.Tour;
import bakendi.TourDatabase;
import java.util.List;
import java.util.stream.Collectors;
import java.text.Normalizer;

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

    private String removeAccents(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
}