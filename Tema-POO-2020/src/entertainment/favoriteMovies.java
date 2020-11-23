package entertainment;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class favoriteMovies {
    private final List<Movie> movies;
    private final int id;
    private final int number;
    private final List<List<String>> filters;
    private final String sortType;
    private final List<User> users;

    public favoriteMovies(List<Movie> movie, int id, int number, List<List<String>> filters, String sortType, List<User> users) {
        this.movies = new ArrayList<>();
        this.movies.addAll(movie);
        this.id = id;
        this.number = number;
        this.filters = filters;
        this.sortType = sortType;
        this.users = users;
    }

    public JSONObject execute(Writer fileWriter) throws IOException {
        addToFavorite();
        if (sortType.equals("asc")) {
            ascsort();
        } else {
            descsort();
        }
        return fileWriter.writeFile(id, null, "Query result: " + filter());
    }

    private void ascsort() {
        Comparator<Movie> comparator = new Comparator<Movie>() {
            @Override
            public int compare(final Movie m1, final Movie m2) {
                if (m1.getFavorite() != m2.getFavorite()) {
                    return Integer.compare(m1.getFavorite(), m2.getFavorite());
                } else {
                    return m1.getTitle().compareTo(m2.getTitle());
                }
            }
        };

        Collections.sort(movies, comparator);
    }

    private void descsort() {
        Comparator<Movie> comparator = new Comparator<Movie>() {
            @Override
            public int compare(final Movie m1, final Movie m2) {
                if (m1.getFavorite() != m2.getFavorite()) {
                    return Integer.compare(m2.getFavorite(), m1.getFavorite());
                } else {
                    return m2.getTitle().compareTo(m1.getTitle());
                }
            }
        };

        Collections.sort(movies, comparator);
    }

    private List<String> filter() {
        int limit = 1;

        List<String> filteredMovies = new ArrayList<String>();
        for (Movie m : movies) {
            boolean found = true;
            if (filters.get(0).get(0) != null) {
                if (!String.valueOf(m.getYear()).equals(filters.get(0).get(0))) {
                    found = false;
                }
            }
            if (filters.get(1).get(0) != null) {
                for (String genre : filters.get(1)) {
                    if (!m.getGenres().contains(genre)) {
                        found = false;
                    }
                }
            }
            if (limit <= number && m.getFavorite() != 0 && found == true) {
                filteredMovies.add(m.getTitle());
                limit++;
            }
        }
        return filteredMovies;
    }
    void addToFavorite() {
        for(User u : users) {
            for (String movie : u.getFavoriteMovies()) {
                Movie m = searchMovie(movie);
                if(m != null) {
                    m.setFavorite(1);
                }
            }
        }
    }
    public Movie searchMovie(String title) {
        for(Movie m : movies) {
            if(m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }
}
