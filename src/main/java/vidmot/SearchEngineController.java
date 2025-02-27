package vidmot;
import bakendi.Tour;
import bakendi.TourDatabase;
import java.util.List;
import java.util.stream.Collectors;

public class SearchEngineController {

    public List<Tour> searchToursByName(String query) {
        if (query == null || query.trim().isEmpty()) return List.of();

        // Gera síu ónæma fyrir há-lágstöfum
        return TourDatabase.getAllTours().stream()
                .filter(tour -> tour.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
