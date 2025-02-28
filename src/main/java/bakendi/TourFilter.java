package bakendi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TourFilter {

    /**
     * Tekur lista af tours og texta-leit (query),
     * skilar niðurstöðum sem innihalda query í nafninu eða textanum.
     */
    public static List<Tour> filterBySearch(List<Tour> allTours, String query) {
        if (query == null || query.trim().isEmpty()) {
            return allTours;
        }
        String lowerQuery = query.trim().toLowerCase();

        return allTours.stream()
                .filter(tour ->
                        tour.getName().toLowerCase().contains(lowerQuery) ||
                                tour.getShortDescription().toLowerCase().contains(lowerQuery) ||
                                tour.getLongDescription().toLowerCase().contains(lowerQuery)
                )
                .collect(Collectors.toList());
    }

    /**
     * Býr til lista af Predicate<Tour> út frá checkbox-value (verðbil).
     * Ef ekkert er valið, skilar alltaf sönnu (t -> true).
     */
    public static List<Predicate<Tour>> buildPriceConditions(boolean v1, boolean v2, boolean v3, boolean v4) {
        List<Predicate<Tour>> conditions = new ArrayList<>();

        if (v1) {
            conditions.add(t -> t.getVerdBilCheck() >= 0 && t.getVerdBilCheck() <= 5000);
        }
        if (v2) {
            conditions.add(t -> t.getVerdBilCheck() >= 5001 && t.getVerdBilCheck() <= 10000);
        }
        if (v3) {
            conditions.add(t -> t.getVerdBilCheck() >= 10001 && t.getVerdBilCheck() <= 20000);
        }
        if (v4) {
            conditions.add(t -> t.getVerdBilCheck() >= 20001);
        }

        if (conditions.isEmpty()) {
            conditions.add(t -> true);
        }

        return conditions;
    }

    /**
     * Tekur listann sem var síaður af leit, og sía hann svo af verðbilum.
     * Notar OR logic milli allra valdra predicata.
     */
    public static List<Tour> filterByPrice(List<Tour> tours, List<Predicate<Tour>> priceConditions) {
        return tours.stream()
                .filter(tour -> priceConditions.stream().anyMatch(cond -> cond.test(tour)))
                .collect(Collectors.toList());
    }
}
