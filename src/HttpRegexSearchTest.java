import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HttpRegexSearchTest {

    @Test
    void basicFind() {
        // Find the university telephone number from the front page
        HttpRegexSearch search_1 = new HttpRegexSearch(
            "https://www.southampton.ac.uk/",
            "(?<=<p>Tel: \\+)([0-9 \\(\\)]+)"
        );

        try {
            ArrayList<String> results = search_1.Execute();
            assertEquals(results.size(), 1);
            assertEquals(results.getFirst(), "44(0)23 8059 5000");
        } catch (Exception e) {
            assertEquals(1, 2);     // Not the proper way of doing this
        }
    }

    @Test
    void multipleFinds() {
        // Search for the number string "101" in the first 10000 digits of pi
        // We expect 13 results
        final String needle = "101";
        HttpRegexSearch search_1 = new HttpRegexSearch(
                "https://files.pilookup.com/pi/10000.txt",
                needle
        );

        try {
            ArrayList<String> results = search_1.Execute();
            assertEquals(results.size(), 13);

            // Ensure there is only one type of match
            assertEquals(results.getFirst(), needle);
            Set<String> collapsed_results = new HashSet<String>(results);
            assertEquals(collapsed_results.size(), 1);
        } catch (Exception e) {
            assertEquals(1, 2);     // Not the proper way of doing this
        }
    }
}