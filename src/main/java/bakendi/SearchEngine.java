package bakendi;

import java.util.List;
import java.util.stream.Collectors;

public class SearchEngine {
    private List<Tour> tours;

    public SearchEngine(List<Tour> tours) {
        this.tours = tours;
    }

    public List<Tour> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }

        String trimmedQuery = name.trim().toLowerCase();

        return tours.stream()
                .filter(t -> t.getName().toLowerCase().contains(trimmedQuery))
                .toList();
    }
}
