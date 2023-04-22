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
     */
    public static int climbStairs(int n) {
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
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
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
                    while (left < right && nums[left] == nums[left - 1]) {
                        ++left;
                    }
                    --right;
                    while (left < right && nums[right] == nums[right + 1]) {
                        --right;
                    }
                }
            }
        }
        return res;
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode head = new ListNode();
        ListNode p = head;
        while (list1 != null || list2 != null) {
            if (list2 == null || (list1 != null && list1.val < list2.val)) {
                p.next = list1;
                p = list1;
                list1 = list1.next;
            } else {
                p.next = list2;
                p = list2;
                list2 = list2.next;
            }
        }
        p.next = null;
        return head.next;
    }

    public int hammingDistance(int x, int y) {
        int res = 0;
        while (x != 0 || y != 0) {
            if (x % 2 == y % 2) {
                ++res;
            }
            x = x / 2;
            y = y / 2;
        }
        return res;
    }

    public int maxArea(int[] height) {
        int max = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            max = Math.max(max, (right - left) * Math.min(height[left], height[right]));
            if (height[left] > height[right]) {
                --right;
            } else {
                ++left;
            }
        }
        return max;
    }

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSymmetricDfs(root.left, root.right);
    }

    public boolean isSymmetricDfs(TreeNode left, TreeNode right) {
        if (left == null || right == null) {
            return false;
        }
        if (left.val != right.val) {
            return false;
        }
        return isSymmetricDfs(left.left, right.right) && isSymmetricDfs(left.right, right.left);
    }

    public int maxProfit(int[] prices) {
        int res = 0;
        if (prices.length < 2) {
            return 0;
        }
        int minPrice = prices[0];
        for (int i = 1; i < prices.length; ++i) {
            res = Math.max(res, prices[i] - minPrice);
            minPrice = Math.min(minPrice, prices[i]);
        }
        return res;
    }

    public int singleNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; ++i) {
            res = res ^ nums[i];
        }
        return res;
    }

    public static String expand(String s, int left, int right) {
        if (left >= 0 && right < s.length() && s.charAt(left) != s.charAt(right)) {
            return "";
        }
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return s.substring(left + 1, right);
    }

    public static String longestPalindrome(String s) {
        String res = "";
        for (int i = 0; i < s.length(); ++i) {
            String one = expand(s, i, i);
            String two = expand(s, i, i + 1);
            String max = one.length() > two.length() ? one : two;
            res = res.length() > max.length() ? res : max;
        }
        return res;
    }

    //二维数组
    public static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        int res = 1;
        Arrays.fill(dp, 1);
        for (int i = 1; i < nums.length; ++i) {
            int max = 1;
            for (int j = 0; j < i; ++j) {
                if (nums[i] > nums[j]) {
                    max = Math.max(max, dp[j] + 1);
                }
            }
            dp[i] = max;
            res = Math.max(res, dp[i]);
        }
        System.out.println(Arrays.toString(dp));
        return res;
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode first = head;
        ListNode next = head;
        for (int i = 0; i < n; ++i) {
            first = first.next;
        }
        //排除删除第一个节点的特殊情况
        if (first == null) {
            return head.next;
        }
        while (first.next != null) {
            first = first.next;
            next = next.next;
        }
        next.next = next.next.next;
        return head;
    }

    public List<String> res = new ArrayList<>();

    public void dfs(String digits, StringBuilder tmp, int index) {
        if (index == digits.length()) {
            res.add(tmp.toString());
            return;
        }
        if (digits.charAt(index) == '2') {
            dfs(digits, new StringBuilder(tmp).append('a'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('b'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('c'), index + 1);
        } else if (digits.charAt(index) == '3') {
            dfs(digits, new StringBuilder(tmp).append('d'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('e'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('f'), index + 1);
        } else if (digits.charAt(index) == '4') {
            dfs(digits, new StringBuilder(tmp).append('g'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('h'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('i'), index + 1);
        } else if (digits.charAt(index) == '5') {
            dfs(digits, new StringBuilder(tmp).append('j'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('k'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('l'), index + 1);
        } else if (digits.charAt(index) == '6') {
            dfs(digits, new StringBuilder(tmp).append('m'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('n'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('o'), index + 1);
        } else if (digits.charAt(index) == '7') {
            dfs(digits, new StringBuilder(tmp).append('p'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('q'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('r'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('s'), index + 1);
        } else if (digits.charAt(index) == '8') {
            dfs(digits, new StringBuilder(tmp).append('t'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('u'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('v'), index + 1);
        } else if (digits.charAt(index) == '9') {
            dfs(digits, new StringBuilder(tmp).append('w'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('x'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('y'), index + 1);
            dfs(digits, new StringBuilder(tmp).append('z'), index + 1);
        }
    }

    public List<String> letterCombinations(String digits) {
        dfs(digits, new StringBuilder(), 0);
        return res;
    }

    public int maxSubArray(int[] nums) {
        int sum = 0;
        int res = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; ++i) {
            sum += nums[i];
            res = Math.max(res, sum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return res;
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<List<Integer>> res = new ArrayList<>();
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; ++i) {
                root = queue.poll();
                level.add(root.val);
                if (root.left!=null) {
                    queue.offer(root.left);
                }
                if (root.right!=null) {
                    queue.offer(root.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    public List<String> generateParenthesisRes = new ArrayList<>();

    public void dfs(int left, int right, StringBuilder tmp) {
        if (left == 0 && right == 0) {
            generateParenthesisRes.add(tmp.toString());
            return;
        }
        if (left == 0) {
            dfs(left, right - 1, new StringBuilder(tmp).append(')'));
        } else if (left == right) {
            dfs(left - 1, right, new StringBuilder(tmp).append('('));
        } else {
            dfs(left - 1, right, new StringBuilder(tmp).append('('));
            dfs(left, right - 1, new StringBuilder(tmp).append(')'));
        }
    }

    public List<String> generateParenthesis(int n) {
        dfs(n,n,new StringBuilder());
        return generateParenthesisRes;
    }
    //6 7 1 2 3 4  1
    public static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[low] <= nums[mid]) {//左边有序
                if (nums[mid] > target && nums[low] <= target) {
                    high = mid - 1;
                } else {
                    low = mid +1;
                }
            } else {//右边有序
                if (nums[mid] < target && nums[high] >= target) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
        }
        return nums[low] == target ? low : -1;
    }

    public int minPathSum(int[][] grid) {
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                if (i == 0 && j == 0) {
                } else if (i == 0) {
                    grid[i][j] += grid[i][j - 1];
                } else if (j == 0) {
                    grid[i][j] += grid[i - 1][j];
                } else {
                    grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
                }
            }
        }
        return grid[grid.length - 1][grid[0].length - 1];
    }

    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        if (slow == null) {
            return false;
        }
        ListNode fast = slow.next;
        while (slow != null && fast != null && slow!=fast) {
            slow = slow.next;
            fast = fast.next == null ? null : fast.next.next;
        }
        return slow != null;
    }

    public int majorityElement(int[] nums) {
        int count = 1;
        int res = nums[0];
        for (int i = 1; i < nums.length; ++i) {
            if (res == nums[i]) {
                ++count;
            } else {
                --count;
                if (count == 0) {
                    count = 1;
                    res = nums[i];
                }
            }
        }
        return res;
    }

    public List<List<Integer>> permuteRes = new ArrayList<>();

    public void dfs(int[] nums, List<Integer> num, int count,int[] flag) {
        if (count == nums.length) {
            permuteRes.add(new ArrayList<>(num));
            return;
        }
        for (int i = 0; i < nums.length; ++i) {
            if (flag[i] == 0) {
                flag[i] = 1;
                num.add(nums[i]);
                dfs(nums, num, count + 1, flag);
                num.remove(num.size()-1);
                flag[i] = 0;
            }
        }
    }

    public boolean canJump(int[] nums) {
        int max = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (i > max) {
                return false;
            }
            max = Math.max(max, i + nums[i]);
        }
        return true;
    }

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = new ListNode();
        ListNode p;
        while (head != null) {
            p = head;
            head = head.next;
            p.next = tail.next;
            tail.next = p;
        }
        return tail.next;
    }

    public List<List<Integer>> permute(int[] nums) {
        int[] flag =new int[nums.length];
        dfs(nums,new ArrayList<>(), 0, flag);
        return permuteRes;
    }

    public static List<Integer> findDisappearedNumber(int[] nums) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; ++i) {
            int t = nums[i] - 1;
            nums[t] = - Math.abs(nums[t]);
        }
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] > 0) {
                res.add(i + 1);
            }
        }
        return res;
    }

    public void moveZeros(int[] nums) {
        int sum = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] == 0) {
                ++sum;
            }
        }
        if (sum == 0) {
            return;
        }
        int index = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != 0) {
                nums[index++] = nums[i];
            }
        }
        for (int i = index; i < nums.length; ++i) {
            nums[i] = 0;
        }
    }

    public static int searchInsert(int[] nums, int target) {
        if (nums[nums.length-1] < target) {
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

    public static void main(String[] args) {
        System.out.println(findDisappearedNumber(new int[]{4,3,2,7,8,2,3,1}));
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
class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

