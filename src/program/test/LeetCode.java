package program.test;

import java.util.Arrays;

public class LeetCode {
    static int hIndex(int[] citations) {
        //6 5 3 1 0
        Arrays.sort(citations);
        int res = 0;
        for (int i =citations.length-1 ; i >= 0;--i) {
            if (citations[i] > res) {
                ++res;
            }
        }
        return res;
    }
    static void test(){

    }
    public static void main(String[] args) {
        test();
    }
}
