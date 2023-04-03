package program.work;

import java.util.*;

public class leetcode {
    public static boolean isValid(String s) {
        Deque<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') {
                stack.push(s.charAt(i));
            } else if (s.charAt(i) == ')' && !stack.isEmpty() && stack.peek() == '(') {
                stack.pop();
            } else if (s.charAt(i) == ']' && !stack.isEmpty() && stack.peek() == '[') {
                stack.pop();
            } else if (s.charAt(i) == '}' && !stack.isEmpty() && stack.peek() == '{') {
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    /**
     * n = (n-1) + (n-2)
     * 0 1
     * 1 1
     * 2 2
     * */
    public static int climbStairs (int n) {
        int pre = 1;
        int now = 1;
        int next = 1;
        for (int i = 2; i <= n; ++i) {
            pre = now;
            now = next;
            next = pre + now;
        }
        return next;
    }

    public static List<Integer> inorderTravsersal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new LinkedList<>();
        while (!stack.isEmpty() && root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }
        return res;
    }

    public static int maxDepth(TreeNode root) {
        if (root==null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) +1;
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length < 3) {
            return res;
        }
        //排序+双指针
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; ++i) {
            int k = nums[i];
            if (k > 0) {
                continue;
            }
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            //-4 -1 -1 0 1 2
            while (left < right) {
                int sum = k + nums[left] + nums[right];
                if (sum > 0) {
                    --right;
                } else if (sum < 0) {
                    ++left;
                } else {
                    res.add(Arrays.asList(k, nums[left], nums[right]));
                    ++left;
                    while (left<right &&nums[left]==nums[left-1]){
                        ++left;
                    }
                    --right;
                    while (left<right && nums[right]==nums[right+1]){
                        --right;
                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(threeSum(new int[]{-2,0,0,2,2}));
    }
}
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

