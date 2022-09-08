package arraypack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Arraymain {

    public static void main(String[] args) {

    }

    public static int[] convertArray(int[] arr){
        List<Integer> integers = new ArrayList<>();
        if (Arrays.stream(arr).noneMatch((i)-> i==4 )){
            throw new RuntimeException();
        }
        boolean b = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 4){
                integers.clear();
                b = true;
                continue;
            }
            if (b){
                integers.add(arr[i]);
            }
        }
        int[] a =new int[integers.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = integers.get(i);
        }
        return a;
    }

    public static boolean matchersOneAndFour(int[] arr){
        for (int i = 0; i < arr.length ; i++) {
            if (arr[i] != 4 && arr[i] != 1)
                return false;
        }
         return true;
    }
    
}
