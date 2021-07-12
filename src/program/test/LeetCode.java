package program.test;

import program.AntSystem.function.Solution;

import java.util.*;

class ListNode{
    int val;
    ListNode next;
    ListNode(){}
    ListNode(int val){
        this.val=val;
    }
}
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

    static int maxLen(String s,int left,int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left-1;
    }
    // 1 2
    static String longestPalindrome(String s){
        int max=0;
        int resLeft=0;
        int resRight=0;
        for (int i=0;i<s.length();++i){
            int one=maxLen(s,i,i);//奇数
            if (one>max){
                max=one;
                resLeft=i-one/2;
                resRight=i+one/2;
            }
            int two=maxLen(s,i,i+1);//偶数
            if (two>max){
                max=two;
                resLeft=i - two/2 + 1;
                resRight= i + two/2;
            }
        }
        return s.substring(resLeft,resRight+1);
    }

    static String convert(String s,int numRows) {
        if (numRows==1){
            return s;
        }
        List<StringBuilder> tmp = new ArrayList<>();
        for (int i = 0; i < numRows; ++i) {
            tmp.add(new StringBuilder());
        }
        int index = 0;
        boolean tag = true;
        for (int i = 0; i < s.length(); ++i) {
            tmp.get(index).append(s.charAt(i));
            if (tag) {
                ++index;
                if (index == numRows - 1) {
                    tag = false;
                }
            } else {
                --index;
                if (index == 0) {
                    tag = true;
                }
            }
        }
        StringBuilder res = new StringBuilder();
        for (StringBuilder t : tmp) {
            res.append(t);
        }
        return res.toString();
    }

    static List<List<Integer>> resp;
    static boolean[] tag;
    static void dfs(int[] nums,int cur,List<Integer> tmp){
        if (cur==nums.length){
            resp.add(tmp);
            return;
        }
        for (int i=0;i<nums.length;++i){
            if (!tag[i]){
                List<Integer> t=new ArrayList<>(tmp);
                t.add(nums[i]);
                tag[i]=true;
                dfs(nums,cur+1,t);
                tag[i]=false;
            }
        }
    }

    static List<List<Integer>> permute(int[] nums){
        resp=new ArrayList<>();
        tag=new boolean[nums.length];
        dfs(nums,0,new ArrayList<>());
        return resp;
    }

    static int jump(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 0; i < nums.length; ++i) {
            for (int j = i + 1; j < Math.min(nums.length, i + nums[i] + 1); ++j) {
                dp[j] = Math.min(dp[j], dp[i] + 1);
            }
        }
        System.out.println(Arrays.toString(dp));
        return dp[nums.length - 1];
    }
    static int jump1(int[] nums) {
        int length = nums.length;
        int end = 0;
        int maxPosition = 0;
        int step = 0;
        for (int i = 0; i < length - 1; ++i) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == end) {
                end = maxPosition;
                ++step;
            }
        }
        return step;
    }

    static void test() {
        List<List<Integer>> a=new ArrayList<>();
        List<Integer> tmp=new ArrayList<>();
        tmp.add(10);
        a.add(tmp);
        Solution s=new Solution(a,1,a);
        a.get(0).add(100);
        System.out.println(s.path);
    }

    public static void main(String[] args) {
        test();
    }
}