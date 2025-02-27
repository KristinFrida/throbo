package bakendi;

import java.util.ArrayList;
import java.util.List;

public class TourDatabase {
    private static final List<Tour> tours = new ArrayList<>();

    static {
        // Add all 12 tours
        tours.add(new Tour("Northern Lights", "/images/tour1.png", "/images/tour1-2.png", "/images/tour1-3.png",
                "Experience the magic of the Northern Lights with our guided tour from Reykjavik.", "Reykjavik", 3.5, 6,
                "Jump aboard this affordable bus tour for your chance to see one of nature’s greatest marvels, the Northern Lights. This tour is for those travelling outside of the summer months, who seek to tick ‘seeing the aurora borealis’ off of their bucket list."));

        tours.add(new Tour("Whale Watching", "/images/tour2.png", "/images/tour2-2.png", "/images/tour2-3.png",
                "Embark on an unforgettable journey across the ocean and witness the majestic whales of Iceland up close with our expert-guided tour from Reykjavik.", "Reykjavik", 3.5, 0,
                "Join this whale watching tour to see the incredible wealth of marine wildlife just off Reykjavík’s shores. You’ll journey into the famous Faxafloi Bay in search of these amazing creatures of the deep. This tour is a must for nature-lovers and fits perfectly into a busy holiday - a 3-3.5 hour trip departing straight from the capital city’s Old Harbour."));

        tours.add(new Tour("Blue Lagoon", "/images/tour3.png", "/images/tour3-2.png", "/images/tour3-3.png",
                "Unwind in the Blue Lagoon’s soothing, mineral-rich waters for the ultimate Icelandic spa experience.", "Blue Lagoon", 3.0, 2,
                "Experience the best geothermal bath in Iceland by purchasing this ticket to the Blue Lagoon. Travelers on a budget who want to soak in the relaxing waters of the lagoon should book this affordable Blue Lagoon entrance ticket today, which includes our best-price guarantee!"));

        tours.add(new Tour("Katla Ice Cave", "/images/tour4.png", "/images/tour4-2.png", "/images/tour4-3.png",
                "Explore the stunning Katla Ice Cave, where blue ice and volcanic ash create a breathtaking natural wonder.", "Vík", 3.0, 6,
                "Join the original Katla Ice Cave tour and experience the magic of ice caving in Iceland like never before. If you’re looking for a hassle-free and thrilling way to explore Iceland’s incredible glaciers, this tour is designed just for you. Unlike Ice Cave tours from the glacier lagoon, this one departs from Vík and doesn’t require traveling to Jökulsárlon, making it a quicker and more accessible option."));

        tours.add(new Tour("South Coast of Iceland", "/images/tour5.png", "/images/tour5-2.png", "/images/tour5-3.png",
                "Discover Iceland’s breathtaking South Coast, where waterfalls, black sand beaches, and glaciers create an unforgettable adventure.", "Reykjavik", 10.0, 6,
                "Join this small group minibus day tour of Iceland’s South Coast. Witness glaciers, volcanoes, waterfalls and the famous Reynisfjara black sand beach on this trip. This tour is perfect for those wanting the best value tour focused on exploring the best spots along Iceland’s magnificent South Coast."));

        tours.add(new Tour("Golden Circle", "/images/tour6.png", "/images/tour6-2.png", "/images/tour6-3.png",
                "Journey through Iceland’s iconic Golden Circle, where geysers, waterfalls, and historic landscapes showcase nature’s raw beauty.", "Reykjavik", 8.0, 6,
                "Hop onboard this small group minibus tour of the Golden Circle - Iceland's most popular travel route. This highly recommended tour starts with a pickup from your hotel in Reykjavik and takes you to see Iceland's most famous attractions, without the rush and hassle of the big buses."));

        tours.add(new Tour("Highland Snowmobiling", "/images/tour7.png", "/images/tour7-2.png", "/images/tour7-3.png",
                "Race across the icy expanse of Eyjafjallajökull on a thrilling snowmobiling adventure through Iceland’s breathtaking highlands.", "Hvolsvöllur", 5.0, 17,
                "Get ready for an exhilarating adventure through Iceland’s winter wilderness on this five-hour snowmobiling tour on the Eyjafjallajokull glacier. This exclusive day tour in Iceland offers a thrilling way to explore the Highlands, complete with a single-rider snowmobile, safety gear, and an expert English-speaking guide."));

        tours.add(new Tour("2-Day Buggy Tour", "/images/tour8.png", "/images/tour8-2.png", "/images/tour8-3.png",
                "Experience the thrill of a two-day buggy adventure through Iceland’s rugged highlands", "Hvolsvöllur", 48.0, 6,
                "Embark on a thrilling buggy tour in the Icelandic Highlands by booking this incredible two-day guided excursion with buggy gear, meals, and one-night accommodation. Those eager to have an epic adventure and discover the stunning scenery of the Highlands should book this tour now."));

        tours.add(new Tour("Dog Sledding", "/images/tour9.png", "/images/tour9-2.png", "/images/tour9-3.png",
                "Glide through the snowy landscapes of Akureyri on a private 3-hour dog sledding adventure, led by a team of energetic huskies.", "Akureyri", 3.0, 0,
                "Explore the breathtaking around Akureyri as huskies pull you along by booking this incredible guided excursion. Those who want to see beautiful surroundings during winter in Iceland and get to know these beautiful dogs should book this private tour now."));

        tours.add(new Tour("Hiking with Huskies Dogs", "/images/tour10.png", "/images/tour10-2.png", "/images/tour10-3.png",
                "Explore the scenic trails of Akureyri on a private 2-hour hiking tour, accompanied by friendly and energetic husky dogs.", "Akureyri", 2.0, 0,
                "Experience the beauty of North Iceland on a private husky hiking tour in Akureyri. This two-hour experience through a stunning woodland combines the joy of being pulled by friendly huskies with breathtaking views of Eyjafjörður, private guides, free photos, and refreshments."));

        tours.add(new Tour("Skaftafell Glacier Hiking", "/images/tour11.png", "/images/tour11-2.png", "/images/tour11-3.png",
                "Hike across the breathtaking ice formations of Skaftafell Glacier on this guided adventure, perfect for nature and thrill seekers alike.", "Skaftafell", 3.0, 8,
                "Join a glacier hike along Iceland’s awe-inspiring South Coast at Skaftafell. This is the perfect way to add excitement to your holiday by hiking on top of an outlet glacier of Vatnajokull, Europe's largest glacier. This exciting tour is perfect for those traveling the South Coast of Iceland who want to combine incredible landscapes with a thrilling adventure."));

        tours.add(new Tour("Jökulsárlón Boat Tour", "/images/tour12.png", "/images/tour12-2.png", "/images/tour12-3.png",
                "Sail among towering icebergs on the Jökulsárlón Glacier Lagoon Boat Tour, a stunning journey through Iceland’s most famous glacier lagoon.", "Jökulsárlón", 0.58, 0,
                "Explore one of the world’s most beautiful natural wonders, Jökulsárlón Glacier Lagoon, on this unique boat tour. If you want to get close to mesmerizing icebergs and glide through serene waters as they head to the ocean, this tour is an ideal choice. Book now to secure your spot at an affordable price."));
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
