package bakendi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TourFilter {

    /**
     * Býr til lista af Predicate<Tour> út frá verðbili.
     */
    public static List<Predicate<Tour>> buildPriceConditions(boolean v1, boolean v2, boolean v3, boolean v4) {
        List<Predicate<Tour>> conditions = new ArrayList<>();

        if (v1) conditions.add(t -> t.getVerdBilCheck() == 1);
        if (v2) conditions.add(t -> t.getVerdBilCheck() == 2);
        if (v3) conditions.add(t -> t.getVerdBilCheck() == 3);
        if (v4) conditions.add(t -> t.getVerdBilCheck() == 4);

        if (conditions.isEmpty()) {
            conditions.add(t -> true); // Ef ekkert er valið, sýna allt
        }

        return conditions;
    }

    public static List<Tour> filterByPrice(List<Tour> tours, List<Predicate<Tour>> priceConditions) {
        return tours.stream()
                .filter(tour -> priceConditions.isEmpty() || priceConditions.stream().anyMatch(cond -> cond.test(tour)))
                .collect(Collectors.toList());
    }
}
