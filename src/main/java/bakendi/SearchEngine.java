package bakendi;

import java.util.List;
import java.util.stream.Collectors;

public class SearchEngine {
    private List<Tour> tours; // Assume this is initialized elsewhere

    public SearchEngine(List<Tour> tours) {
        this.tours = tours;
    }

    public List<Tour> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("⚠️ Empty search query! Returning no results.");
            return List.of(); // Safe empty list
        }

        String trimmedQuery = name.trim().toLowerCase();
        System.out.println("🔍 Searching in SearchEngine for: " + trimmedQuery);

        List<Tour> results = tours.stream()
                .filter(t -> {
                    System.out.println("🔍 Checking: " + t.getName()); // Debugging
                    return t.getName().toLowerCase().contains(trimmedQuery);
                })
                .toList(); // Use Collectors.toList() if on Java 15 or lower

        System.out.println("✅ Search results found: " + results.size());
        return results;
    }
}
