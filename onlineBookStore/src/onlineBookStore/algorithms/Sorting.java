// File: /OnlineBookstore/src/onlineBookStore/algorithms/Sorting.java
package onlineBookStore.algorithms;

import onlineBookStore.models.Book;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Sorting utilities: insertion sort and merge sort for Book arrays/lists.
 */
public class Sorting {

    // Insertion sort by title (in-place)
    public static void insertionSortByTitle(Book[] arr) {
        for (int i = 1; i < arr.length; i++) {
            Book key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].getTitle().compareToIgnoreCase(key.getTitle()) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Merge sort by author (returns new sorted array)
    public static Book[] mergeSortByAuthor(Book[] arr) {
        if (arr.length <= 1) return arr;
        int mid = arr.length / 2;
        Book[] left = Arrays.copyOfRange(arr, 0, mid);
        Book[] right = Arrays.copyOfRange(arr, mid, arr.length);
        left = mergeSortByAuthor(left);
        right = mergeSortByAuthor(right);
        return merge(left, right, Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER));
    }

    // Merge two sorted arrays into one sorted array using the provided comparator
    private static Book[] merge(Book[] a, Book[] b, Comparator<Book> cmp) {
        Book[] res = new Book[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) {
            if (cmp.compare(a[i], b[j]) <= 0) res[k++] = a[i++];
            else res[k++] = b[j++];
        }
        while (i < a.length) res[k++] = a[i++];
        while (j < b.length) res[k++] = b[j++];
        return res;
    }
}
