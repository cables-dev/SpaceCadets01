import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;

public class Main {
    private static Optional<String> FindNameForId(String user_id) {
        // We use the same regex expression for all url targets
        final String regex = "(?<=<h1 class=\"heading-m inline-block text-prussianDark\">)(.*)(?=</h1>)";
        // Specify aforementioned target urls
        final String[] search_targets = {"https://www.ecs.soton.ac.uk/people/", "https://www.southampton.ac.uk/people/"};

        for (String url_base : search_targets) {
            String full_url = url_base + user_id;                       // Construct full url with user's id
            HttpRegexSearch rx = new HttpRegexSearch(full_url, regex);  // Create search command
            try {
                ArrayList<String> particular_result = rx.Execute();
                if (!particular_result.isEmpty()) {
                    return Optional.of(particular_result.getFirst());
                }
            } catch (Exception e) {
                System.out.println("Exception encountered while searching targets!");       // lol
            }
        }

        // This is reached when all search attempts have failed
        return Optional.empty();
    }

    // Very basic input sanitation
    private static Boolean IsValidUserId(String user_id) {
        final String[] invalid_chars = {"#", "!", "/", "\\", " ", ":", "."};
        for (String ch : invalid_chars) {
            if (user_id.contains(ch)) {
                return false;
            }
        }
        return true;
    }

    // Do user id lookup with input sanitation on user id
    public static Optional<String> GetNameForId(String user_id) {
        Optional<String> result = Optional.empty();
        if (IsValidUserId(user_id)) {
            result = FindNameForId(user_id);
        }
        return result;
    }

    public static void main(String[] args) {
        // Request input from the user via stdin
        System.out.println("Enter a user's id:");
        Scanner sc = new Scanner(System.in);
        String user_input = sc.nextLine().strip();

        Optional<String> result = GetNameForId(user_input);
        if (result.isEmpty())
            System.out.println("Not found!");
        else
            System.out.println(result.get());
    }
}