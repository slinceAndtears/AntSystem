package program.work;

import java.util.*;

public class LeetCodeNew {
    //如果前面的累加和小于0，就抛弃掉，累加和赋值为当前下标
    public static int maxSubArray(int[] nums) {
        int res = nums[0];
        int add = nums[0];
        for (int i = 1; i < nums.length; ++i) {
            if (add < 0) {
                add = nums[i];
            } else {
                add += nums[i];
            }
            res = Math.max(add, res);
        }
        return res;
    }

    public static int getAddSum(int i) {
        int sum = 0;
        while (i != 0) {
            sum += i% 10;
            i = i/10;
        }
        return sum;
    }

    public static int maximumSum(int[] nums) {
        int res = -1;
        int[] addSum = new int[nums.length];
        for (int i = 0; i < nums.length; ++i) {
            addSum[i] = getAddSum(nums[i]);
        }
        Map<Integer, Integer> tmp = new HashMap<>();
        for (int i = 0; i < addSum.length; ++i) {
            if (tmp.containsKey(addSum[i])) {
                res = Math.max(tmp.get(addSum[i]) + nums[i], res);
                tmp.put(addSum[i], Math.max(tmp.get(addSum[i]), nums[i]));
            } else {
                tmp.put(addSum[i], nums[i]);
            }
        }
        return res;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        ListNode p = head;
        int jump = 0;
        while (l1 != null || l2 != null) {
            int a = l1 == null ? 0 : l1.val;
            int b = l2 == null ? 0 : l2.val;
            int res = a + b + jump;
            jump = 0;
            if (res > 9) {
                jump = 1;
                res -= 10;
            }
            if (l1 != null ) {
                l1.val = res;
                p.next = l1;
                p = l1;
            } else {
                l2.val = res;
                p.next = l2;
                p = l2;
            }
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
        }
        if (jump != 0 ) {
            ListNode t = new ListNode(1);
            p.next = t;
            p = t;
        }
        p.next = null;
        return head.next;
    }

    public int lengthOfLongestSubstring(String s) {
        int res = 0;
        Set<Character> tmp = new HashSet<>();
        int low = 0;
        int high = 0;
        while (high < s.length()) {
            while (high < s.length() &&!tmp.contains(s.charAt(high))) {
                tmp.add(s.charAt(high++));
            }
            res = Math.max(res, high - low);
            if (high >= s.length()) {
                return res;
            }
            while (low < high && s.charAt(low) != s.charAt(high)) {
                tmp.remove(s.charAt(low++));
            }
            tmp.remove(s.charAt(low++));
        }
        return res;
    }

    public static int removeDuplicates(int[] nums) {
        int index = 0;
        for (int i = index + 1; i < nums.length; ++i) {
            if (nums[i] != nums[index]) {
                nums[++index] = nums[i];
            }
        }
        return index + 1;
    }

    public static int removeDuplicatesNew(int[] nums) {
        int index = 0;
        int sum = 1;
        for (int i = index + 1; i < nums.length; ++i) {
            if (nums[i] != nums[index]) {
                nums[++index] = nums[i];
                sum = 1;
            } else {
                if (sum == 1) {
                    ++sum;
                    nums[++index] = nums[i];
                } else {
                    ++sum;
                }
            }
        }
        return index + 1;
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int index = nums1.length - 1;
        int i = m - 1;
        int j = n - 1;
        while (index >= 0) {
            if (j < 0 || (i >= 0 && nums1[i] > nums2[j])) {
                nums1[index--] = nums1[i--];
            } else {
                nums1[index--] = nums2[j--];
            }
        }
    }

    public boolean canJump(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; ++i) {
            if (max < i) {
                return false;
            }
            max = Math.max(max, i + nums[i]);
        }
        return true;
    }

    public static int hIndex(int[] citations) {
        int[] tmp = new int[1001];
        for (int i = 0; i < citations.length; ++i) {
            ++tmp[citations[i]];
        }
        int sum = 0;//论文数量
        for (int i = tmp.length - 1; i >= 0; --i) {
            for (int j = 0; j < tmp[i]; ++j) {
                sum += 1;
                if (sum > i) {
                    return sum - 1;
                }
            }
        }
        return citations.length;
    }

    public static int maxProfit(int[] prices) {
        int res = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; ++i) {
            res = Math.max(res, prices[i] - min);
            min = Math.min(min, prices[i]);
        }
        return res;
    }

    public static int maxProfit2(int[] prices) {
        int res = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; ++i) {
            if (min < prices[i]) {//
                res += prices[i] - min;
            }
            min = prices[i];
        }
        return res;
    }

    public int jump2(int[] nums) {
        int[] dp = new int[nums.length];
        for (int i = 1; i < nums.length; ++i) {
            int m = Integer.MAX_VALUE;
            for (int j = 0; j < i; ++j) {
                if (nums[j] + j >= i) {
                    m = Math.min(m, dp[j]);
                }
            }
            dp[i] = m + 1;
        }
        return dp[nums.length - 1];
    }

    public static int lengthOfLastWord(String s) {
        int last = s.length() - 1;
        int start = 0;
        while (last >= 0) {
            while (last >= 0 && s.charAt(last) == ' ') {
                --last;
            }
            start = last;
            while (last >= 0 && s.charAt(last) != ' ') {
                --last;
            }
            return start - last;
        }
        return 0;
    }

    public static boolean canConstruct(String ransomNode, String magazine) {
        if (ransomNode.length() > magazine.length()) {
            return false;
        }
        Map<Character, Integer> tmp = new HashMap<>();
        for (int i = 0; i < magazine.length(); ++i) {
            tmp.put(magazine.charAt(i), tmp.getOrDefault(magazine.charAt(i), 0) + 1);
        }
        for (int i = 0; i < ransomNode.length(); ++i) {
            if (!tmp.containsKey(ransomNode.charAt(i)) || tmp.get(ransomNode.charAt(i)) < 1) {
                return false;
            }
            tmp.put(ransomNode.charAt(i), tmp.get(ransomNode.charAt(i)) - 1);
        }
        return true;
    }

    public static int removeElement(int[] nums, int val) {
        int index = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != val) {
                nums[index++] = nums[i];
            }
        }
        return index;
    }

    public static int majorityElement(int[] nums) {
        int sum = 1;
        int res = nums[0];
        for (int i = 1; i < nums.length; ++i) {
            if (res == nums[i]) {
                ++sum;
            } else {
                --sum;
                if (sum == 0) {
                    sum = 1;
                    res = nums[i];
                }
            }
        }
        return res;
    }

    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 1) {
            return strs[0];
        }
        StringBuilder res = new StringBuilder();
        int index = 0;
        while (true) {
            for (int i = 0; i < strs.length - 1; ++i) {
                if (strs[i].length() <= index || strs[i + 1].length() <= index ||
                        strs[i].charAt(index) != strs[i + 1].charAt(index)) {
                    return res.toString();
                }
            }
            res.append(strs[0].charAt(index++));
        }
    }

    public static int minSubArray(int target, int[] nums) {
        int low = 0;
        int high = 0;
        int sum = 0;
        int res = Integer.MAX_VALUE;
        while (high < nums.length) {
            while (sum < target && high < nums.length) {
                sum += nums[high++];
            }
            if (sum >= target) {
                res = Math.min(res, high - low);
            }
            while (sum >= target && low < high) {
                sum -= nums[low++];
            }
            if (low > 0 && sum + nums[low - 1] >= target) {
                res = Math.min(res, high - low + 1);
            }
        }
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    public static boolean isPalindrome(String s) {
        int low = 0;
        int high = s.length() - 1;
        while (low < high) {
            while (low < high && !(s.charAt(low) >= 'a' && s.charAt(low) <= 'z')
                    && !(s.charAt(low) >= 'A' && s.charAt(low) <= 'Z') &&
                    !(s.charAt(low) >= '0' && s.charAt(low) <= '9')) {
                ++low;
            }
            while (low < high && !(s.charAt(high) >= 'a' && s.charAt(high) <= 'z')
                    && !(s.charAt(high) >= 'A' && s.charAt(high) <= 'Z') &&
                    !(s.charAt(high) >= '0' && s.charAt(high) <= '9')) {
                --high;
            }
            if (!(s.charAt(low) >= '0' && s.charAt(low) <= '9') && !(s.charAt(high) >= '0' && s.charAt(high) <= '9')) {
                if (s.charAt(low) == s.charAt(high) || s.charAt(low) + 32 == s.charAt(high) ||
                        s.charAt(low) - 32 == s.charAt(high)){
                    ++low;
                    --high;
                } else {
                    return false;
                }
            } else if (s.charAt(low) == s.charAt(high)) {
                ++low;
                --high;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean isValidSodoku(char[][] board) {
        boolean[][] tmp1 = new boolean[9][10];
        boolean[][] tmp2 = new boolean[9][10];
        boolean[][][] tmp3 = new boolean[3][3][10];
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] >= '0' && board[i][j] <= '9') {
                    int num = board[i][j] - '0';
                    if (tmp1[i][num] || tmp2[j][num] || tmp3[i / 3][j / 3][num]) {
                        return false;
                    }
                    tmp1[i][num] = true;
                    tmp2[j][num] = true;
                    tmp3[i / 3][j / 3][num] = true;
                }
            }
        }
        return true;
    }

    public int longestConsecutive(int[] nums) {
        Set<Integer> tmp = new HashSet<>();
        for (int num : nums) {
            tmp.add(num);
        }
        int res = 0;
        for (int num : tmp) {
            if (!tmp.contains(num - 1)) {
                int current = num;
                int length = 1;
                while (tmp.contains(current + 1)) {
                    current += 1;
                    length += 1;
                }
                res = Math.max(res, length);
            }
        }
        return res;
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> tmp = new HashMap<>();
        for (int i = 0; i < nums.length; ++i) {
            if (tmp.containsKey(target - nums[i])) {
                return new int[]{i, tmp.get(target - nums[i])};
            }
            tmp.put(nums[i], i);
        }
        return new int[]{};
    }

    public Node copyRandomList(Node head) {
        Map<Node, Node> tmp = new HashMap<>();
        Node p = head;
        Node tail = new Node(0);
        Node q = tail;
        while (p != null) {
            Node t = new Node(p.val);
            q.next = t;
            q = t;
            tmp.put(p, t);
            p = p.next;
        }
        q.next = null;
        p = head;
        q = tail.next;
        while (p != null) {
            q.random = tmp.get(p.random);
            p = p.next;
            q = q.next;
        }
        return tail.next;
    }

    public int climbStairs(int n) {
        int p = 0;
        int q = 1;
        for (int i = 2; i <= n; ++i) {
            int sum = p + q;
            p = q;
            q = sum;
        }
        return q;
    }

    public static int minimumTotal(List<List<Integer>> triangle) {
        for (int i = 0; i < triangle.size(); ++i) {
            for (int j = 0; j < triangle.get(i).size(); ++j) {
                //能够有两个备选路径的
                if (i != 0 && j != 0 && j != triangle.get(i - 1).size()) {
                    triangle.get(i).set(j, Math.min(triangle.get(i - 1).get(j - 1), triangle.get(i - 1).get(j)) + triangle.get(i).get(j));
                } else if (i != 0 && j == 0) {//只能从上到下
                    triangle.get(i).set(j, triangle.get(i - 1).get(j) + triangle.get(i).get(j));
                } else if (i != 0 && j == triangle.get(i - 1).size()) {//只能走对角
                    triangle.get(i).set(j, triangle.get(i).get(j) + triangle.get(i - 1).get(j - 1));
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < triangle.get(triangle.size() - 1).size(); ++i) {
            min = Math.min(min, triangle.get(triangle.size() - 1).get(i));
        }
        return min;
    }

    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        Map<Character, Character> tmp1 = new HashMap<>();
        Map<Character, Character> tmp2 = new HashMap<>();
        for (int i = 0; i < s.length(); ++i) {
            if (tmp1.containsKey(s.charAt(i)) && tmp1.get(s.charAt(i)) != t.charAt(i)) {
                return false;
            } else {
                tmp1.put(s.charAt(i), t.charAt(i));
            }
            if (tmp2.containsKey(t.charAt(i)) && tmp2.get(t.charAt(i)) != s.charAt(i)) {
                return false;
            } else {
                tmp2.put(t.charAt(i), s.charAt(i));
            }
        }
        return true;
    }

    public int searchInsert(int[] nums, int target) {
        if (nums[nums.length-1]<target){
            return nums.length;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = (high - low) / 2 + low;
            if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }


    public static void main(String[] args) {
        isPalindrome("ab2a");
    }
}
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}