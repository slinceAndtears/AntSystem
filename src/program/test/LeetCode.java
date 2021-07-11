package program.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode {
    static int hIndex(int[] citations) {
        //6 5 3 1 0
        Arrays.sort(citations);
        int res = 0;
        for (int i = citations.length - 1; i >= 0; --i) {
            if (citations[i] > res) {
                ++res;
            }
        }
        return res;
    }

    static List<String> res;
    static void dfs(int left,int right,StringBuilder t){
        if (right==0){
            res.add(t.toString());
            return;
        }
        if (left!=right&&right>0){
            StringBuilder tmp=new StringBuilder(t);
            dfs(left,right-1,tmp.append(')'));
        }
        if (left>0) {
            StringBuilder tmp=new StringBuilder(t);
            dfs(left - 1, right, tmp.append('('));
        }
    }
    static List<String> generateParenthesis(int n){
        res=new ArrayList<>();
        dfs(n,n,new StringBuilder());
        return res;
    }

    static int searchInsert(int[] nums,int target) {
        if (target>nums[nums.length-1]){
            return nums.length;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    static int trap(int[] height) {
        int[] tmp = new int[height.length];
        int max = 0;
        for (int i = 0; i < height.length; ++i) {
            max = Math.max(max, height[i]);
            tmp[i]=max;
        }
        int res = 0;
        max = 0;
        for (int i = height.length - 1; i >= 0; --i) {
            max = Math.max(max, height[i]);
            res += Math.min(tmp[i], max) - height[i];
        }
        return res;
    }

    static void test() {
        int[] a={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(a));
    }

    public static void main(String[] args) {
        test();
    }
}