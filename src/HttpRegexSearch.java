import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRegexSearch {
    private final String m_url;
    private final Pattern m_regex_pattern;
    private Pattern GetRegex() {return m_regex_pattern;}

    private HttpClient GetHttpClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)    // Important: set redirection policy
                .build();
    }

    // Create java-specific URI instance given an url
    // May fail if input formatting is incorrect
    private Optional<URI> CreateURI(String url_string) {
        Optional<URI> result = Optional.empty();
        try {
            result = Optional.of(new URI(url_string));
            return result;
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    // Will return all regex matches as strings
    private ArrayList<String> ApplyRegex(String web_content) {
        Pattern pattern = GetRegex();
        Matcher matcher = pattern.matcher(web_content);

        ArrayList<String> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(web_content.substring(matcher.start(), matcher.end()));
        }
        return results;
    }

    public HttpRegexSearch(String url, String regex_pattern) {
        m_url = url;
        m_regex_pattern = Pattern.compile(regex_pattern);
    }

    public Optional<HttpRequest> CreateRequest() {
        Optional<URI> uri = CreateURI(m_url);
        if (uri.isEmpty()) {
            System.out.println("Error creating URI.");
            return Optional.empty();
        }

        return Optional.of(HttpRequest.newBuilder()
                .uri(uri.get())
                .GET()
                .build());
    }

    public ArrayList<String> Execute() throws IOException, InterruptedException {
        Optional<HttpRequest> req = CreateRequest();
        if (req.isEmpty())
            return new ArrayList<>();

        HttpClient client = GetHttpClient();
        HttpResponse<String> response = client.send(req.get(), HttpResponse.BodyHandlers.ofString());
        client.close();
        return ApplyRegex(response.body());
    }
}
