package cpt111.group76.recommendation;

import java.util.List;

import cpt111.group76.movie.Movie;

/**
 * A Quick sort realization to sort a movie list,
 * accepts a custom comparator abstract class,
 * which needs to be extended to implement the compare method.
 */
public class Sort {
    public static class Comparator {
        /**
         * Compare two movies. Return negative if a should come before b,
         * zero if equal, positive if a should come after b.
         * This method must be overridden by subclasses.
         *
         * @throws UnsupportedOperationException if not implemented
         */
        public int compare(Movie a, Movie b) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Comparator.compare() not implemented");
        }
    }


    public static void sort(List<Movie> list, Comparator cmp) {
        if (list == null || list.size() <= 1 || cmp == null) {
            return;
        }
        quickSort(list, 0, list.size() - 1, cmp);
    }

    private static void quickSort(List<Movie> list, int low, int high, Comparator cmp) {
        if (low < high) {
            int p = partition(list, low, high, cmp);
            quickSort(list, low, p - 1, cmp);
            quickSort(list, p + 1, high, cmp);
        }
    }


    private static int partition(List<Movie> list, int low, int high, Comparator cmp) {
        Movie pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (cmp.compare(list.get(j), pivot) < 0) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }


    private static void swap(List<Movie> list, int a, int b) {
        if (a == b) {
            return;
        }
        Movie tmp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, tmp);
    }
}
