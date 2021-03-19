import java.util.Arrays;

public class SatMergeSortIntArray {

    private static int[] merge(int[] arr1, int[] arr2) {
        int[] newArr = new int[arr1.length + arr2.length];

        int j = 0;
        int k = 0;

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

    private static void splitArray(int[] arr, int[] arr1, int[] arr2,
        int arr1len, int arr2len) {

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
