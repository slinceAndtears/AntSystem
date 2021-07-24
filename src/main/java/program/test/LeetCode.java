package program.test;

import java.util.*;

class ListNode{
    int val;
    ListNode next;
    ListNode(){}
    ListNode(int val){
        this.val=val;
    }
}
class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(){}
    TreeNode(int val){
        this(val,null,null);
    }
    TreeNode(int val,TreeNode left,TreeNode right){
        this.val=val;
        this.left=left;
        this.right=right;
    }
}
public class LeetCode {
    static ListNode createList(int[] a) {
        ListNode h = new ListNode(a[0]);
        ListNode p = h;
        for (int i = 1; i < a.length; ++i) {
            ListNode t = new ListNode(a[i]);
            p.next = t;
            p = t;
        }
        p.next = null;
        return h;
    }

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

    static void dfs(int left, int right, StringBuilder t) {
        if (right == 0) {
            res.add(t.toString());
            return;
        }
        if (left != right && right > 0) {
            StringBuilder tmp = new StringBuilder(t);
            dfs(left, right - 1, tmp.append(')'));
        }
        if (left > 0) {
            StringBuilder tmp = new StringBuilder(t);
            dfs(left - 1, right, tmp.append('('));
        }
    }

    static List<String> generateParenthesis(int n) {
        res = new ArrayList<>();
        dfs(n, n, new StringBuilder());
        return res;
    }

    static int searchInsert(int[] nums, int target) {
        if (target > nums[nums.length - 1]) {
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
            tmp[i] = max;
        }
        int res = 0;
        max = 0;
        for (int i = height.length - 1; i >= 0; --i) {
            max = Math.max(max, height[i]);
            res += Math.min(tmp[i], max) - height[i];
        }
        return res;
    }

    static int maxLen(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }

    // 1 2
    static String longestPalindrome(String s) {
        int max = 0;
        int resLeft = 0;
        int resRight = 0;
        for (int i = 0; i < s.length(); ++i) {
            int one = maxLen(s, i, i);//奇数
            if (one > max) {
                max = one;
                resLeft = i - one / 2;
                resRight = i + one / 2;
            }
            int two = maxLen(s, i, i + 1);//偶数
            if (two > max) {
                max = two;
                resLeft = i - two / 2 + 1;
                resRight = i + two / 2;
            }
        }
        return s.substring(resLeft, resRight + 1);
    }

    static String convert(String s, int numRows) {
        if (numRows == 1) {
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

    static void dfs(int[] nums, int cur, List<Integer> tmp) {
        if (cur == nums.length) {
            resp.add(tmp);
            return;
        }
        for (int i = 0; i < nums.length; ++i) {
            if (!tag[i]) {
                List<Integer> t = new ArrayList<>(tmp);
                t.add(nums[i]);
                tag[i] = true;
                dfs(nums, cur + 1, t);
                tag[i] = false;
            }
        }
    }

    static List<List<Integer>> permute(int[] nums) {
        resp = new ArrayList<>();
        tag = new boolean[nums.length];
        dfs(nums, 0, new ArrayList<>());
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

    static int reverse(int x) {
        if (x == 0 || x == Integer.MIN_VALUE) {
            return 0;
        }
        int tag = x > 0 ? 1 : -1;
        x = Math.abs(x);
        int boundary = Integer.MAX_VALUE / 10;
        int res = 0;
        while (x != 0) {
            int t = x % 10;
            if (res > boundary) {
                return 0;
            } else if (res == boundary) {
                if (t >= 7) {
                    return 0;
                } else {
                    res = res * 10 + t;
                }
            } else {
                res = res * 10 + t;
            }
            x = x / 10;
        }
        return res * tag;
    }

    static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < dp.length; ++i) {
            for (int j = 0; j < dp[i].length; ++j) {
                if (i == 0 && j == 0) {
                    dp[i][j] = 0;
                } else if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    static int rob(int[] nums) {
        int yes = nums[0];
        int no = 0;
        int t;
        for (int i = 1; i < nums.length; ++i) {
            t = nums[i] + no;
            no = Math.max(no, yes);
            yes = t;

        }
        return Math.max(yes, no);
    }

    static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        //左右两边一定有一个是有序的数组
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[low] <= nums[mid]) {
                if (target >= nums[low] && target <= nums[mid]) {
                    high = mid;
                } else {
                    low = mid + 1;
                }
            } else {
                if (target >= nums[mid] && target <= nums[high]) {
                    low = mid;
                } else {
                    high = mid - 1;
                }
            }
        }
        return nums[low] == target ? low : -1;
    }

    static int[] searchRange(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] > target) {
                high = mid - 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                low = mid;
                break;
            }
        }
        if (nums[low] != target) {
            return new int[]{-1, -1};
        }
        int left = low;
        int right = low;
        while (left >= 0 && nums[left] == target) {
            --left;
        }
        while (right < nums.length && nums[right] == target) {
            ++right;
        }
        return new int[]{left + 1, right - 1};
    }

    static int firstMissing(int[] nums) {
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] <= 0) {
                nums[i] = nums.length + 1;
            }
        }
        for (int i = 0; i < nums.length; ++i) {
            int t = nums[i];
            if (Math.abs(t) <= nums.length) {
                t = Math.abs(t) - 1;
                nums[t] = -Math.abs(nums[t]);
            }
        }
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] >= 0) {
                return i + 1;
            }
        }
        return nums.length;
    }

    static StringBuilder mul(String num1, int num2) {
        if (num2 == 0) {
            return new StringBuilder("0");
        }
        StringBuilder res = new StringBuilder();
        int i = num1.length() - 1;
        int jump = 0;
        while (i >= 0) {
            int r = num2 * (num1.charAt(i) - '0') + jump;
            jump = r / 10;
            r = r % 10;
            res.insert(0, r);
            --i;
        }
        if (jump != 0) {
            res.insert(0, jump);
        }
        return res;
    }

    static StringBuilder add(String num1, String num2) {
        StringBuilder res = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int jump = 0;
        while (i >= 0 || j >= 0) {
            int numA = i >= 0 ? num1.charAt(i) - '0' : 0;
            int numB = j >= 0 ? num2.charAt(j) - '0' : 0;
            int r = numA + numB + jump;
            if (r > 9) {
                r -= 10;
                jump = 1;
            } else {
                jump = 0;
            }
            res.insert(0, r);
            --i;
            --j;
        }
        if (jump != 0) {
            res.insert(0, jump);
        }
        return res;
    }

    static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        StringBuilder res = new StringBuilder('0');
        int a = 1;
        for (int i = num2.length() - 1; i >= 0; --i) {
            StringBuilder t = mul(num1, num2.charAt(i) - '0');
            if (!t.equals("0")) {
                for (int j = num2.length() - 1; j > i; --j) {
                    t.append('0');
                }
            }
            res = add(res.toString(), t.toString());
        }
        return res.toString();
    }

    static List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<List<Integer>> res = new ArrayList<>();
        while (!queue.isEmpty()) {
            List<Integer> t = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; ++i) {
                root = queue.poll();
                t.add(root.val);
                if (root.left != null) {
                    queue.offer(root.left);
                }
                if (root.right != null) {
                    queue.offer(root.right);
                }
            }
            res.add(t);
        }
        return res;
    }

    static String countArray(int n) {
        if (n < 1) {
            return "";
        }
        StringBuilder res = new StringBuilder("1");
        for (int i = 2; i <= n; ++i) {
            StringBuilder t = new StringBuilder();
            int j = 0;
            while (j < res.length()) {
                int num = res.charAt(j) - '0';
                int index = j;
                while (j < res.length() - 1 && res.charAt(j) == res.charAt(j + 1)) {
                    ++j;
                }
                ++j;
                t.append(j - index);
                t.append(num);
            }
            res = new StringBuilder(t);
        }
        return res.toString();
    }

    static void numReverse(int[] nums, int low, int high) {
        while (low < high) {
            int t = nums[low];
            nums[low] = nums[high];
            nums[high] = t;
            ++low;
            --high;
        }
    }

    static void rotate(int[] nums, int k) {
        numReverse(nums, nums.length - k, nums.length - 1);
        numReverse(nums, 0, nums.length - k - 1);
        numReverse(nums, 0, nums.length - 1);
    }

    static void test() {
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        rotate(a, 3);
        System.out.println(Arrays.toString(a));
        //StringBuilder r=new StringBuilder();
    }

    public static void main(String[] args) {
        test();
    }
}