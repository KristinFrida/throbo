package bakendi;

import java.util.ArrayList;
import java.util.List;

public class TourDatabase {
    private static final List<Tour> tours = new ArrayList<>();

    static {
        tours.add(new Tour(
                "Northern Lights",
                "/images/tour1.png",
                "/images/tour1-2.png",
                "Experience the magic of the Northern Lights with our guided tour from Reykjavik.",
                "/images/tour1-3.png",
                "Reykjavik",
                15900.0,   // 7th: price
                3.5,       // 8th: duration in hours (example)
                6,         // 9th: minAge
                "Jump aboard this affordable bus tour for your chance to see one of nature’s greatest marvels, the Northern Lights. This tour is for those travelling outside of the summer months, who seek to tick ‘seeing the aurora borealis’ off of their bucket list."
        ));

        tours.add(new Tour(
                "Whale Watching",
                "/images/tour2.png",
                "/images/tour2-2.png",
                "/images/tour2-3.png",
                "Embark on an unforgettable journey across the ocean and witness the majestic whales of Iceland up close with our expert-guided tour from Reykjavik.",
                "Reykjavik",
                19900.0,   // price
                3.5,       // duration
                0,         // minAge
                "Join this whale watching tour to see the incredible wealth of marine wildlife just off Reykjavík’s shores. You’ll journey into the famous Faxafloi Bay in search of these amazing creatures of the deep..."
        ));

        tours.add(new Tour(
                "Blue Lagoon",
                "/images/tour3.png",
                "/images/tour3-2.png",
                "/images/tour3-3.png",
                "Unwind in the Blue Lagoon’s soothing, mineral-rich waters for the ultimate Icelandic spa experience.",
                "Blue Lagoon",
                12900.0,   // price
                2.0,       // duration
                2,         // minAge
                "Experience the best geothermal bath in Iceland by purchasing this ticket to the Blue Lagoon..."
        ));

        tours.add(new Tour(
                "Katla Ice Cave",
                "/images/tour4.png",
                "/images/tour4-2.png",
                "/images/tour4-3.png",
                "Explore the stunning Katla Ice Cave, where blue ice and volcanic ash create a breathtaking natural wonder.",
                "Vík",
                13900.0,   // price
                3.0,       // duration
                6,         // minAge
                "Join the original Katla Ice Cave tour and experience the magic of ice caving in Iceland like never before..."
        ));

        tours.add(new Tour(
                "South Coast Tour",
                "/images/tour5.png",
                "/images/tour5-2.png",
                "/images/tour5-3.png",
                "Discover Iceland’s breathtaking South Coast, where waterfalls, black sand beaches, and glaciers create an unforgettable adventure.",
                "Reykjavik",
                18900.0,   // price
                10.0,      // duration
                6,         // minAge
                "Join this small group minibus day tour of Iceland’s South Coast..."
        ));

        tours.add(new Tour(
                "Golden Circle Tour",
                "/images/tour6.png",
                "/images/tour6-2.png",
                "/images/tour6-3.png",
                "Journey through Iceland’s iconic Golden Circle, where geysers, waterfalls, and historic landscapes showcase nature’s raw beauty.",
                "Reykjavik",
                15900.0,   // price
                8.0,       // duration
                6,         // minAge
                "Hop onboard this small group minibus tour of the Golden Circle..."
        ));

        tours.add(new Tour(
                "Highland Snowmobiling",
                "/images/tour7.png",
                "/images/tour7-2.png",
                "/images/tour7-3.png",
                "Race across the icy expanse of Eyjafjallajökull on a thrilling snowmobiling adventure through Iceland’s breathtaking highlands.",
                "Hvolsvöllur",
                29900.0,   // price
                5.0,       // duration
                17,        // minAge
                "Get ready for an exhilarating adventure through Iceland’s winter wilderness..."
        ));

        tours.add(new Tour(
                "2-Day Buggy Tour",
                "/images/tour8.png",
                "/images/tour8-2.png",
                "/images/tour8-3.png",
                "Experience the thrill of a two-day buggy adventure through Iceland’s rugged highlands",
                "Hvolsvöllur",
                49900.0,   // price
                48.0,      // duration (2 days in hours)
                6,         // minAge
                "Embark on a thrilling buggy tour in the Icelandic Highlands..."
        ));

        tours.add(new Tour(
                "Dog Sledding",
                "/images/tour9.png",
                "/images/tour9-2.png",
                "/images/tour9-3.png",
                "Glide through the snowy landscapes of Akureyri on a private 3-hour dog sledding adventure...",
                "Akureyri",
                17900.0,   // price
                3.0,       // duration
                0,         // minAge
                "Explore the breathtaking surroundings around Akureyri as huskies pull you along..."
        ));

        tours.add(new Tour(
                "Hiking with Huskies",
                "/images/tour10.png",
                "/images/tour10-2.png",
                "/images/tour10-3.png",
                "Explore the scenic trails of Akureyri on a private 2-hour hiking tour, accompanied by friendly and energetic husky dogs.",
                "Akureyri",
                12900.0,   // price
                2.0,       // duration
                0,         // minAge
                "Experience the beauty of North Iceland on a private husky hiking tour in Akureyri..."
        ));

        tours.add(new Tour(
                "Skaftafell Glacier Hiking",
                "/images/tour11.png",
                "/images/tour11-2.png",
                "/images/tour11-3.png",
                "Hike across the breathtaking ice formations of Skaftafell Glacier on this guided adventure...",
                "Skaftafell",
                15900.0,   // price
                3.0,       // duration
                8,         // minAge
                "Join a glacier hike along Iceland’s awe-inspiring South Coast at Skaftafell..."
        ));

        tours.add(new Tour(
                "Jökulsárlón Boat Tour",
                "/images/tour12.png",
                "/images/tour12-2.png",
                "/images/tour12-3.png",
                "Sail among towering icebergs on the Jökulsárlón Glacier Lagoon Boat Tour...",
                "Jökulsárlón",
                9900.0,    // price
                0.5,       // duration (30 minutes)
                0,         // minAge
                "Explore one of the world’s most beautiful natural wonders, Jökulsárlón Glacier Lagoon..."
        ));
    }

    public static List<Tour> getAllTours() {
        return tours;
    }

    public static Tour getTourByName(String name) {
        return tours.stream()
                .filter(tour -> tour.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
