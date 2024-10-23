package utilities;

import java.util.List;
import java.util.ArrayList;

public class SortUtils {
    public static <T extends Comparable<T>> List<T> mergeSort(List<T> list) {
        if (list.size() <= 1) {
            return new ArrayList<>(list); // return list copy if already sorted
        }

        int mid = list.size() / 2;
        List<T> left = new ArrayList<>(list.subList(0, mid));
        List<T> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private static <T extends Comparable<T>> List<T> merge(List<T> left, List<T> right) {
        List<T> mergedList = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                mergedList.add(left.get(i++));
            } else {
                mergedList.add(right.get(j++));
            }
        }

        // add elements from L->R
        while (i < left.size()) {
            mergedList.add(left.get(i++));
        }

        // add elements from right
        while (j < right.size()) {
            mergedList.add(right.get(j++));
        }

        return mergedList;
    }
}
