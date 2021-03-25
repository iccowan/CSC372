import java.util.Arrays;

/**
 *  Static class to merge sort an int array for displaying output
 *  Ian Cowan
 *  CSC 372
 *  Spring 2021
 *
 *  @class SatMergeSortIntArray
 */
public class SatMergeSortIntArray {

    /**
     *  Merges 2 arrays together in a sorted fashion
     *
     *  @param int[] arr1 the first array to merge
     *  @param int[] arr2 the second array to merge
     *  @return int[]     the merged array
     */
    private static int[] merge(int[] arr1, int[] arr2) {
        // Create a new array for the merging
        int[] newArr = new int[arr1.length + arr2.length];

        int j = 0;
        int k = 0;

        // Loop through both arrays and merge each element
        // We know each of the arrays are already sorted, so this works
        for (int i = 0; i < newArr.length; i++) {
            if (j >= arr1.length) {
                newArr[i] = arr2[k];
                k++;
            } else if (k >= arr2.length) {
                newArr[i] = arr1[j];
                j++;
            } else if (Math.abs(arr1[j]) < Math.abs(arr2[k])) {
                newArr[i] = arr1[j];
                j++;
            } else {
                newArr[i] = arr2[k];
                k++;
            }
        }

        return newArr;
    }

    /**
     *  Splits an array into 2 smaller arrays.
     *  This will update the 2 array parameters that are passed to this
     *  method for the 2 split arrays
     *
     *  @param int[] arr     the array to split
     *  @param int[] arr1    the first half of the array
     *  @param int[] arr2    the second half of the array
     *  @param int[] arr1len the length of the first half of the array
     *  @param int[] arr2len the length of the second half of the array
     */
    private static void splitArray(int[] arr, int[] arr1, int[] arr2,
        int arr1len, int arr2len) {

        // Start at 0 and split into the 2 lengths
        int i = 0;
        for (int j = 0; j < arr1len; j++) {
            arr1[j] = arr[i];
            i++;
        }

        for (int k = 0; k < arr2len; k++) {
            arr2[k] = arr[i];
            i++;
        }
    }

    /**
     *  Sorts an array via merge sort
     *
     *  @param int[] arr the array to sort
     *  @return int[]    the sorted array
     */
    public static int[] sort(int[] arr) {
        // If the length of the array is 0 or 1, return
        if (arr.length == 0 || arr.length == 1)
            return arr;

        // Split the array into 2 pieces
        int arr1len = arr.length / 2;
        int arr2len = arr.length - (arr.length / 2);
        int[] arr1 = new int[arr1len];
        int[] arr2 = new int[arr2len];
        splitArray(arr, arr1, arr2, arr1len, arr2len);

        // Sort the subarrays
        int[] mergeArr1 = sort(arr1);
        int[] mergeArr2 = sort(arr2);

        // Merge the subarrays
        int[] mergeArray = merge(mergeArr1, mergeArr2);
        return mergeArray;
    }

}
