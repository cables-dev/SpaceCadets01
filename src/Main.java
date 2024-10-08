import java.util.*;

public class Main {
    private static Optional<String> FindNameForId(String user_id) {
        // Specify aforementioned target urls
        // https://www.southampton.ac.uk/ can deal with new-style ids such as "5x82lq"
        // https://www.ecs.soton.ac.uk/ can deal with old-style ids such as "dem"
        final String[] search_targets = {"https://www.ecs.soton.ac.uk/people/", "https://www.southampton.ac.uk/people/"};
        // We use the same regex expression for all url targets
        final String regex = "(?<=<h1 class=\"heading-m inline-block text-prussianDark\">)(.*)(?=</h1>)";

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
        final Set<Character> invalid_chars = new HashSet<Character>(Arrays.asList('#', '!', '/', '\\', ' ', ':', '.'));
        for (Character ch : user_id.toCharArray()) {
            if (invalid_chars.contains(ch)) {
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