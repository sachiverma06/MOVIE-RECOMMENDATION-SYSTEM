import java.util.*;
import java.util.stream.Collectors;

class Film {
    String name;
    String summary;
    String genre;
    String educationNote;

    public Film(String name, String summary, String genre, String educationNote) {
        this.name = name;
        this.summary = summary.toLowerCase();
        this.genre = genre.toLowerCase();
        this.educationNote = educationNote.trim();
    }

    public Film(String name, String summary) {
        this(name, summary, "general", "");
    }
}

public class MovieRecommender {

    public static double findSimilarity(String text1, String text2) {
        String[] wordsA = text1.split(" ");
        String[] wordsB = text2.split(" ");

        Set<String> allWords = new HashSet<>();
        Collections.addAll(allWords, wordsA);
        Collections.addAll(allWords, wordsB);

        Map<String, Integer> freq1 = new HashMap<>();
        Map<String, Integer> freq2 = new HashMap<>();

        for (String word : allWords) {
            freq1.put(word, 0);
            freq2.put(word, 0);
        }

        for (String word : wordsA) {
            freq1.put(word, freq1.get(word) + 1);
        }

        for (String word : wordsB) {
            freq2.put(word, freq2.get(word) + 1);
        }

        double dot = 0, norm1 = 0, norm2 = 0;
        for (String word : allWords) {
            int f1 = freq1.get(word);
            int f2 = freq2.get(word);
            dot += f1 * f2;
            norm1 += f1 * f1;
            norm2 += f2 * f2;
        }

        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public static void suggestMovies(List<Film> films, String inputTitle) {
        String inputLower = inputTitle.toLowerCase();

        if (inputLower.contains("bollywood")) {
            List<Film> bollywood = films.stream()
                .filter(f -> f.genre.equals("bollywood"))
                .collect(Collectors.toList());

            System.out.println("\n🎬 Recommended Bollywood Movies:");
            for (Film f : bollywood) {
                System.out.println("👉 " + f.name);
            }
            return;
        }

        if (inputLower.contains("hollywood")) {
            List<Film> hollywood = films.stream()
                .filter(f -> f.genre.equals("hollywood"))
                .collect(Collectors.toList());

            System.out.println("\n🎬 Recommended Hollywood Movies (Educational):");
            for (Film f : hollywood) {
                System.out.println("\n👉 " + f.name + "\n📚 " + f.educationNote);
            }
            return;
        }

        List<Film> matched = films.stream()
            .filter(f -> f.name.toLowerCase().contains(inputLower))
            .collect(Collectors.toList());

        if (!matched.isEmpty()) {
            System.out.println("\n📽 You searched for: " + inputTitle);
            System.out.println("🎬 Matching movies:");
            for (Film f : matched) {
                System.out.println("👉 " + f.name);
            }
            return;
        }

        Film selected = null;
        for (Film f : films) {
            if (f.name.toLowerCase().equals(inputLower)) {
                selected = f;
                break;
            }
        }

        if (selected == null) {
            System.out.println("❌ Movie not found.");
            return;
        }

        Map<String, Double> scores = new HashMap<>();
        for (Film f : films) {
            if (!f.name.toLowerCase().equals(inputLower)) {
                double sim = findSimilarity(selected.summary, f.summary);
                scores.put(f.name, sim);
            }
        }

        List<Map.Entry<String, Double>> top3 = scores.entrySet()
            .stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(3)
            .collect(Collectors.toList());

        if (!top3.isEmpty()) {
            System.out.println("\n📽 You might also enjoy:");
            for (Map.Entry<String, Double> entry : top3) {
                System.out.println("👉 " + entry.getKey());
            }
        }
    }

    public static void main(String[] args) {
        List<Film> movieList = new ArrayList<>(Arrays.asList(
                        
            // Bollywood Educational
            new Film("Hichki", "A teacher with Tourette syndrome turns her weakness into strength", "bollywood", ""),
            new Film("Nil Battey Sannata", "A mother joins school to inspire her daughter’s education", "bollywood", ""),
            new Film("Dangal", "A father trains his daughters to become world-class wrestlers", "bollywood", ""),
            new Film("Gunjan Saxena: The Kargil Girl", "India's first female combat pilot", "bollywood", ""),
            new Film("Chhapaak", "The real-life story of acid attack survivor Laxmi Agarwal", "bollywood", ""),
            new Film("English Vinglish", "A homemaker regains confidence by learning English", "bollywood", ""),
            new Film("Secret Superstar", "A young girl fights for her dream to become a singer", "bollywood", ""),
            new Film("I Am Kalam", "A poor boy dreams of becoming like Dr. A.P.J. Abdul Kalam", "bollywood", ""),

            // Hollywood Educational
            new Film("Hidden Figures", "African-American women mathematicians at NASA", "hollywood",
                     "Shows how math and perseverance helped send astronauts into orbit."),
            new Film("The Pursuit of Happyness", "Perseverance and self-belief in financial struggle", "hollywood",
                     "Teaches resilience and hard work."),
            new Film("Mona Lisa Smile", "An art history professor inspires her female students", "hollywood",
                     "Challenges gender roles and promotes intellectual freedom."),
            new Film("Akeelah and the Bee", "A girl from South LA competes in the National Spelling Bee", "hollywood",
                     "Highlights education, confidence, and community support."),
            new Film("The Theory of Everything", "The life of Stephen Hawking", "hollywood",
                     "Depicts scientific pursuit despite disability."),
            new Film("Legally Blonde", "A fashionista proves she can succeed at Harvard Law", "hollywood",
                     "Promotes self-worth and breaking stereotypes."),
            new Film("Queen of Katwe", "A Ugandan girl becomes a chess champion", "hollywood",
                     "Inspires through poverty, talent, and determination."),
            new Film("Freedom Writers", "A teacher inspires at-risk students with writing", "hollywood",
                     "Demonstrates the power of storytelling and education.")
        ));

        Scanner sc = new Scanner(System.in);
        System.out.print("🎬 Enter a movie name or genre (e.g., Bollywood, Hollywood): ");
        String fav = sc.nextLine();

        suggestMovies(movieList, fav);
    }
}
