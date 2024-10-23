package utilities;
import java.util.List;

// Generic searching utility class
public class SearchUtils {
    // Generic Binary Search
    public static <T extends Comparable<T>> int binarySearch(List<T> list, T target) {
        return binarySearch(list, target, 0, list.size() - 1);
    }

    private static <T extends Comparable<T>> int binarySearch(List<T> list, T target, int low, int high) {
        if (low > high) {
            return -1; // Target not found
        }

        int mid = (low + high) / 2;
        T midElement = list.get(mid);

        if (midElement.compareTo(target) == 0) {
            return mid; // Target found
        } else if (midElement.compareTo(target) > 0) {
            return binarySearch(list, target, low, mid - 1);
        } else {
            return binarySearch(list, target, mid + 1, high);
        }
    }
}