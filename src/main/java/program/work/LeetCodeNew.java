package program.work;


import program.test.LeetCode;

import javax.swing.text.Highlighter;
import java.util.*;
import java.util.stream.Collectors;

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

    public int lengthOfLIS(int[] nums) {
        int res = 0;
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        for (int i = 0; i < nums.length; ++i) {
            int max = 0;
            for (int j = 0; j < i; ++j) {
                if (nums[i] > nums[j]) {
                    max = Math.max(max, dp[j]);
                }
            }
            dp[i] = max + 1;
            res = Math.max(dp[i], res);
        }
        return res;
    }

    public static boolean wordPattern(String pattern, String s) {
        String[] strs = s.split(" ");
        if (strs.length != pattern.length()) {
            return false;
        }
        Map<Character, String> tmp = new HashMap<>();
        for (int i = 0; i < pattern.length(); ++i) {
            if (tmp.containsKey(pattern.charAt(i))) {
                if (!tmp.get(pattern.charAt(i)).equals(strs[i])) {
                    return false;
                }
            } else {
                if (tmp.containsValue(strs[i])) {
                    return false;
                }
                tmp.put(pattern.charAt(i), strs[i]);
            }
        }
        return true;
    }

    public boolean isValid(String s) {
        Deque<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == ')') {
                if (stack.size() == 0 || stack.peek() != '(') {
                    return false;
                }
                stack.pop();
            } else if (s.charAt(i) == ']') {
                if (stack.size() == 0 || stack.peek() != '[') {
                    return false;
                }
                stack.pop();
            } else if (s.charAt(i) == '}') {
                if (stack.size() == 0 || stack.peek() != '{') {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(s.charAt(i));
            }
        }
        return stack.size() == 0;
    }

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null || p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    public void rotateWithIndex(int[] nums, int low, int high) {
        while (low < high) {
            int tmp = nums[low];
            nums[low] = nums[high];
            nums[high] = tmp;
            ++low;
            --high;
        }
    }
    public void rotate(int[] nums, int k) {
        if (k != 0) {
            k = k % nums.length;
            rotateWithIndex(nums, 0, nums.length - k - 1);
            rotateWithIndex(nums, nums.length - k, nums.length - 1);
            rotateWithIndex(nums, 0, nums.length - 1);
        }
    }

    /**
     * 1 1 1*2 1*2*3
     * 4*3*2  4*3 4 1
     *
     */
    public int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];
        int[] left = new int[nums.length];
        int[] right = new int[nums.length];
        Arrays.fill(left, 1);
        Arrays.fill(right, 1);
        for (int i = 1; i < left.length; ++i) {
            left[i] = left[i - 1] * nums[i - 1];
        }
        for (int i = right.length - 2; i >= 0; --i) {
            right[i] = right[i + 1] * nums[i + 1];
        }
        for (int i = 0; i < res.length; ++i) {
            res[i] = left[i] * right[i];
        }
        return res;
    }

    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int n = gas.length;
        int i = 0;
        while (i < n) {
            int sumOfGas = 0;
            int sumOfCost = 0;
            int cnt = 0;
            while (cnt < n) {
                int j = (i + cnt) % n;
                sumOfGas += gas[j];
                sumOfCost += cost[j];
                if (sumOfCost > sumOfGas) {
                    break;
                }
                ++cnt;
            }
            if (cnt == n) {
                return i;
            } else {
                i += cnt + i;
            }
        }
        return -1;
    }

    public int[] twoSum2(int[] numbers, int target) {
        int low = 0;
        int high = numbers.length - 1;
        while (low < high) {
            int sum = numbers[low] + numbers[high];
            if (sum > target) {
                --high;
            } else if (sum < target) {
                ++low;
            } else {
                return new int[]{low + 1, high + 1};
            }
        }
        return new int[]{};
    }

    public int maxArea(int[] height) {
        int low = 0;
        int high = height.length - 1;
        int res = 0;
        while (low < high) {
            res = Math.max(res, (high - low) * Math.min(height[low], height[high]));
            if (height[low] > height[high]) {
                --high;
            } else {
                ++low;
            }
        }
        return res;
    }

    public static int trap(int[] height) {
        int res = 0;
        int maxIndex = 0;
        int max = 0;
        for (int i = 0; i < height.length; ++i) {
            if (max < height[i]) {
                max = height[i];
                maxIndex = i;
            }
        }
        max = 0;
        for (int i = 0; i < maxIndex; ++i) {
            max = Math.max(max, height[i]);
            res += max - height[i];
        }
        max = 0;
        for (int i = height.length - 1; i > maxIndex; --i) {
            max = Math.max(max, height[i]);
            res += max - height[i];
        }
        return res;
    }

    /**
     * 13  841
     * 1011
     * 1101
     */
    public static double myPow(double x, int n) {
        if (x == 1d || x == 0d) {
            return x;
        }
        double res = 1d;
        boolean t = n < 0;
        long tmp = n;
        tmp = Math.abs(tmp);

        while (tmp != 0) {
            long r = tmp % 2;
            if (r == 1) {
                res *= x;
            }
            x *= x;
            tmp = tmp / 2;
        }
        if (t) {
            return 1 / res;
        }
        return res;
    }

    public static String reverseWords(String s) {
        StringBuilder res = new StringBuilder();
        int index = 0;
        while (index<s.length()){
            while (index<s.length()&&s.charAt(index)==' '){
                ++index;
            }
            StringBuilder t =new StringBuilder(" ");
            while (index<s.length()&&s.charAt(index)!=' '){
                t.append(s.charAt(index++));
            }
            res.insert(0, t);
        }
        return res.toString().trim();
    }

    public boolean isAnagram(String s, String t) {
        int[] tmp = new int[32];
        for (int i = 0; i < s.length(); ++i) {
            ++tmp[s.charAt(i) - 'a'];
        }
        for (int i = 0; i < t.length(); ++i) {
            --tmp[t.charAt(i) - 'a'];
        }
        for (int i = 0; i < tmp.length; ++i) {
            if (tmp[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> tmp = new HashMap<>();
        for (int i = 0; i < strs.length; ++i) {
            int[] count =new int[26];
            for (int j = 0; j < strs[i].length(); j++) {
                count[strs[i].charAt(j) - 'a']++;
            }
            StringBuilder sb=new StringBuilder();
            for (int j = 0; j < 26; j++) {
                if (count[j] != 0) {
                    sb.append((char) ('a' + j));
                    sb.append(count[j]);
                }
            }
            String key = sb.toString();
            List<String> list = tmp.getOrDefault(key, new ArrayList<String>());
            list.add(strs[i]);
            tmp.put(key, list);

        }
        return new ArrayList<>(tmp.values());
    }

    public String addBinary(String a,String b) {
        StringBuilder res = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int jump = 0;
        while (i >= 0 || j >= 0) {
            int t1 = i >= 0 ? a.charAt(i) - '0' : 0;
            int t2 = j >= 0 ? b.charAt(j) - '0' : 0;
            int r = t1 + t2 + jump;
            res.insert(0, r % 2);
            jump = 0;
            if (r > 1) {
                jump = 1;
            }
            --i;
            --j;
        }
        if (jump == 1) {
            res.insert(0, 1);
        }
        return res.toString();
    }

    public boolean isPalindrome(int x) {
        if (x<0){
            return false;
        }
        List<Integer> t = new ArrayList<>();
        while (x != 0) {
            t.add(x % 10);
            x = x / 10;
        }
        int low = 0;
        int high = t.size()-1;
        while (low < high) {
            if (t.get(low) != t.get(high)) {
                return false;
            }
            ++low;
            --high;
        }
        return true;
    }

    public ListNode deleteDuplicates(ListNode head) {
        ListNode tail = new ListNode();
        tail.next = head;
        ListNode p = head;
        ListNode t = tail;
        while (p != null) {
            ListNode q = p;
            int sum = 0;
            while (q != null && p.val == q.val) {
                ++sum;
                q = q.next;
            }
            if (sum == 1) {
                t.next = p;
                t = p;
            }
            p = q;
        }
        t.next = null;
        return tail.next;
    }

    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }
        if (root.val == targetSum && root.left == null && root.right == null) {
            return true;
        }
        return hasPathSum(root.left, targetSum - root.val) ||
                hasPathSum(root.right, targetSum - root.val);
    }

    public boolean[][] tmp;
    public void deepSearch(char[][] grid,int i,int j) {
        if (i < 0 || j < 0 || i == grid.length || j == grid[i].length || grid[i][j] == '0'||tmp[i][j]) {
            return;
        }
        tmp[i][j] = true;
        deepSearch(grid, i - 1, j);
        deepSearch(grid, i + 1, j);
        deepSearch(grid, i, j - 1);
        deepSearch(grid, i, j + 1);
    }

    public int numIslands(char[][] grid) {
        tmp = new boolean[grid.length][grid[0].length];
        int sum = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                if (!tmp[i][j] && grid[i][j] == '1') {
                    ++sum;
                    deepSearch(grid, i, j);
                }
            }
        }
        return sum;
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode head = new ListNode();
        ListNode p = head;
        while (list1 != null || list2 != null) {
            if (list1 == null || (list2 != null && list1.val > list2.val)) {
                p.next = list2;
                p = list2;
                list2 = list2.next;
            } else {
                p.next = list1;
                p = list1;
                list1 = list1.next;
            }
        }
        p.next = null;
        return head.next;
    }

    public static ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode tail = new ListNode();
        tail.next = head;
        ListNode beforeStart = tail;
        for (int i = 0; i < left - 1; ++i) {
            beforeStart = beforeStart.next;
        }
        ListNode afterEnd = beforeStart.next;
        for (int i = left; i <= right; ++i) {
            afterEnd = afterEnd.next;
        }
        ListNode t = beforeStart.next;
        ListNode start = beforeStart.next;
        beforeStart.next = null;
        for (int i = left; i <= right; ++i) {
            ListNode q = start;
            start = start.next;
            q.next = beforeStart.next;
            beforeStart.next = q;
        }
        t.next = afterEnd;
        return tail.next;
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head.next == null && n == 1) {
            return null;
        }
        ListNode t = head;
        for (int i = 0; i < n; ++i) {
            t = t.next;
        }
        if (t == null) {
            return head.next;
        }
        ListNode q = head;
        while (t.next != null) {
            t = t.next;
            q = q.next;
        }
        if (q.next != null) {
            q.next = q.next.next;
        } else {
            q.next = null;
        }
        return head;
    }

    public int getSum(int n) {
        int res = 0;
        while (n != 0) {
            res += (n % 10) * (n % 10);
            n = n / 10;
        }
        return res;
    }
    public boolean isHappy(int n) {
        Map<Integer, Integer> tmp = new HashMap<>();
        while (n != 1) {
            int res = 0;
            if (tmp.containsKey(n)) {
                res = tmp.get(n);
            } else {
                res = getSum(n);
            }
            if (tmp.containsKey(res)) {
                return false;
            }
            tmp.put(n, res);
        }
        return true;
    }

    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> tmp = new HashMap<>();
        for (int i = 0; i < nums.length; ++i) {
            if (tmp.containsKey(nums[i]) && Math.abs(i - tmp.get(nums[i])) <= k) {
                return true;
            }
            tmp.put(nums[i], i);
        }
        return false;
    }

    public List<Double> averageOfLevels(TreeNode root) {
        Queue<TreeNode> tmp = new LinkedList<>();
        List<Double> res = new ArrayList<>();
        tmp.offer(root);
        while (!tmp.isEmpty()) {
            int size = tmp.size();
            double sum = 0d;
            for (int i = 0; i < size; ++i) {
                TreeNode poll = tmp.poll();
                sum += poll.val;
                if (poll.left != null) {
                    tmp.offer(poll.left);
                }
                if (poll.right != null) {
                    tmp.offer(poll.right);
                }

            }
            res.add(sum / size);
        }
        return res;
    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (int[] a, int[] b) -> a[0] - b[0]);
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < intervals.length; ++i) {
            if (res.size() == 0 || res.get(res.size() - 1)[1] < intervals[i][0]) {
                res.add(intervals[i]);
            } else {//有重合
                res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], intervals[i][1]);
            }
        }
        int[][] result = new int[res.size()][2];
        for (int i = 0; i < res.size(); ++i) {
            result[i] = res.get(i);
        }
        return result;
    }

    public List<List<Integer>> rightSideView(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<Integer>> res = new ArrayList<>();
        if (root != null) {
            queue.offer(root);
        }
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                root = queue.poll();
                level.add(root.val);
                if (root.left != null) {
                    queue.offer(root.left);
                }
                if (root.right != null) {
                    queue.offer(root.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null) {
            return head;
        }
        ListNode p = head;
        int size = 1;
        while (p.next != null) {
            size += 1;
            p = p.next;
        }
        k = k % size;
        if (k == 0) {
            return head;
        }
        p = head;
        ListNode last = head;
        while (last.next != null) {
            last = last.next;
        }
        last.next = head;
        for (int i = 0; i < size - k; ++i) {
            p = p.next;
        }
        ListNode res = p.next;
        p.next = null;
        return res;
    }

    public List<String> summaryRanges(int[] nums) {
        List<String> res = new ArrayList<>();
        if (nums.length == 0) {
            return res;
        }
        int pre = nums[0];
        int min = nums[0];
        for (int i = 0; i < nums.length; ++i) {
            if (i < nums.length - 1 && nums[i + 1] - nums[i] == 1) {
                pre = nums[i];
            } else {
                StringBuilder t = new StringBuilder();
                if (min == nums[i]) {
                    t.append(min);
                } else {
                    t.append(min).append("->").append(nums[i]);
                }
                res.add(t.toString());
                if (i < nums.length - 1) {
                    pre = nums[i + 1];
                    min = pre;
                }
            }
        }
        return res;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; ++i) {
            if (nums[i] > 0) {
                return res;
            }
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum > 0) {
                    --right;
                } else if (sum < 0) {
                    ++left;
                } else {
                    List<Integer> integers = Arrays.asList(nums[i], nums[left], nums[right]);
                    ++left;
                    --right;
                    res.add(integers);
                    //跳过重复的
                    while (left < right && nums[left] == nums[left - 1]) {
                        ++left;
                    }
                    while (left < right && nums[right] == nums[right + 1]) {
                        --right;
                    }
                }
            }
        }
        return res;
    }

    public static String simplifyPath(String path) {
        Deque<String> stack = new LinkedList<>();
        StringBuilder res = new StringBuilder();
        int index = 0;
        while (index < path.length()) {
            if (path.charAt(index) == '.') {
                StringBuilder t = new StringBuilder();
                int sum = 0;
                while (index < path.length() && path.charAt(index) != '/') {
                    if (path.charAt(index) == '.') {
                        ++sum;
                    }
                    t.append(path.charAt(index++));
                }
                if (sum == 2 && t.length() == 2 ) {
                    if (stack.size() > 2){
                        stack.pop();
                        stack.pop();
                    }
                } else if (t.length() > 1){
                    stack.push(t.toString());
                }
            } else if (path.charAt(index) == '/') {
                if (stack.isEmpty() || (!stack.peek().equals("/") && index != path.length() - 1)) {
                    stack.push(String.valueOf(path.charAt(index)));
                }
                ++index;
            } else {
                StringBuilder name = new StringBuilder();
                while (index < path.length() && path.charAt(index) != '/') {
                    name.append(path.charAt(index++));
                }
                stack.push(name.toString());
            }
        }
        if (stack.size() == 0) {
            return "/";
        } else {
            if (stack.peek().equals("/")){
                stack.pop();
            }
            while (!stack.isEmpty()) {
                res.insert(0, stack.poll());
            }
            return res.toString();
        }
    }

    public int findPeakElement(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (mid == 0) {
                if (nums[mid] > nums[mid + 1]) {
                    return mid;
                } else {
                    low = mid + 1;
                }
            } else if (mid == nums.length - 1) {
                if (nums[mid] > nums[mid - 1]) {
                    return mid;
                } else {
                    high = mid - 1;
                }
            } else {
                if (nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                    return mid;
                } else if (nums[mid] > nums[mid - 1] && nums[mid] < nums[mid + 1]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return low;
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }

    public boolean isSymmetric(TreeNode root) {
        return check(root, root);
    }

    public boolean check(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.val == right.val && check(left.left, right.right) && check(left.right, right.left);
    }

    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (inorder.length == 0 || preorder.length == 0) {
            return null;
        }
        int val = preorder[0];
        TreeNode root = new TreeNode(val);
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        int index = 0;
        while (index < inorder.length && inorder[index] != val) {
            left.add(inorder[index++]);
        }
        ++index;
        while (index < inorder.length) {
            right.add(inorder[index++]);
        }
        if (preorder.length > left.size()) {
            int[] newRightPreorder = new int[preorder.length - 1 - left.size()];
            for (int i = 1 + left.size(); i < preorder.length; ++i) {
                newRightPreorder[i - 1 - left.size()] = preorder[i];
            }
            int[] newLeftPreOrder = new int[left.size()];
            for (int i = 1; i <= left.size(); ++i) {
                newLeftPreOrder[i - 1] = preorder[i];
            }
            root.left = buildTree(newLeftPreOrder, left.stream().mapToInt(Integer::valueOf).toArray());
            root.right = buildTree(newRightPreorder, right.stream().mapToInt(Integer::valueOf).toArray());
        }
        return root;
    }

    int sum = 0;
    public void getSumNumbers(TreeNode root, int val) {
        if (root == null) {
            return;
        }
        int v = val * 10 + root.val;
        if (root.left == null && root.right == null) {
            sum += v;
        } else {
            getSumNumbers(root.left, v);
            getSumNumbers(root.right, v);
        }
    }

    public int sumNumbers(TreeNode root){
        getSumNumbers(root, 0);
        return sum;
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
                    grid[i][j] += Math.min(grid[i][j - 1], grid[i - 1][j]);
                }
            }
        }
        return grid[grid.length - 1][grid[grid.length - 1].length - 1];
    }

    public static TreeNode sortedArrayToBST(int[] nums) {
        if (nums.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode();
        int index = nums.length / 2;
        root.val = nums[index];
        TreeNode leftNode = null;
        TreeNode rightNode = null;
        if (index >= 0) {
            int[] left = new int[index];
            for (int i = 0; i < left.length; ++i) {
                left[i] = nums[i];
            }
            leftNode = sortedArrayToBST(left);
        }
        if (nums.length - index - 1 >= 0) {
            int[] right = new int[nums.length - index - 1];
            for (int i = index + 1; i < nums.length; ++i) {
                right[i - index - 1] = nums[i];
            }
            rightNode = sortedArrayToBST(right);
        }
        root.left = leftNode;
        root.right = rightNode;
        return root;
    }

    List<String> letterRes = new ArrayList<>();
    public String getStrByNum(int num) {
        if (num == 2) {
            return "abc";
        } else if (num == 3) {
            return "def";
        } else if (num == 4) {
            return "ghi";
        } else if (num == 5) {
            return "jkl";
        } else if (num == 6) {
            return "mno";
        } else if (num == 7) {
            return "pqrs";
        } else if (num == 8) {
            return "tuv";
        } else {
            return "wxyz";
        }
    }
    public void dfs(int index, String digits, StringBuilder pre) {
        if (index == digits.length()) {
            letterRes.add(pre.toString());
            return;
        }
        int num = digits.charAt(index) - '0';
        String str = getStrByNum(num);
        for (int i = 0; i < str.length(); ++i) {
            StringBuilder t = new StringBuilder(pre);
            t.append(str.charAt(i));
            dfs(index + 1, digits, t);
        }
    }

    public List<String> letterCombinations(String digits) {
        if (digits.length() > 0) {
            dfs(0, digits, new StringBuilder());
        }
        return letterRes;
    }

    List<List<Integer>> combineRes = new ArrayList<>();
    public void dfs(int n, int current, int k, List<Integer> t) {
        if (t.size() == k) {
            combineRes.add(t);
            return;
        }
        for (int i = current + 1; i <= n; ++i) {
            List<Integer> tmp = new ArrayList<>(t);
            tmp.add(i);
            dfs(n,i,k,tmp);
        }
    }
    public List<List<Integer>> combine(int n, int k) {
        dfs(n, 0, k, new ArrayList<>());
        return combineRes;
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int x = 0;
        int y = matrix[0].length - 1;
        while (x < matrix.length && y >= 0) {
            int num = matrix[x][y];
            if (num > target) {
                --y;
            } else if (num < target) {
                ++x;
            } else {
                return true;
            }
        }
        return false;
    }

    public static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        // 左右肯定有一边是有序的 左边有序且一定
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if ((nums[low] <= target && target < nums[mid]) || (nums[mid] < nums[high] && (target < nums[mid] || target > nums[high]))) {
                // 左边有序且target在左边，或者右边有序但不在右边
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    public int[] plusOne(int[] digits) {
        int res = digits[digits.length - 1] + 1;
        if (res < 10) {
            digits[digits.length - 1] = res;
            return digits;
        } else {
            int jump = 1;
            digits[digits.length - 1] = res - 10;
            for (int i = digits.length - 2; i >= 0; --i) {
                res = jump + digits[i];
                jump = 0;
                if (res < 10) {
                    digits[i] = res;
                    return digits;
                } else {
                    digits[i] = res - 10;
                    jump = 1;
                }
            }
            if (jump != 0) {
                int[] newDigits = new int[digits.length + 1];
                newDigits[0] = 1;
                for (int i = 0; i < digits.length; ++i) {
                    newDigits[i + 1] = digits[i];
                }
                return newDigits;
            }
            return digits;
        }
    }

    public static int hammingWeight(int n) {
        int res = 0;
        int t = 1;
        for (int i = 0; i < 32; ++i) {
            if ((t & n) != 0) {
                ++res;
            }
            t = t << 1;
        }
        return res;
    }

    public static int trailingZeroes(int n) {
        int res = 0;
        for (int i = 1; i <= n; ++i) {
            int t = i;
            while (t % 10 == 0) {
                ++res;
                t = t / 10;
            }
            while (t % 5 == 0) {
                ++res;
                t = t / 5;
            }
        }
        return res;
    }

    int pre = -1;
    int minimumDiff = Integer.MAX_VALUE;
    public void midOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            midOrder(root.left);
        }
        if (pre == -1) {
            pre = root.val;
        } else {
            minimumDiff = Math.min(minimumDiff, Math.abs(pre - root.val));
            pre = root.val;
        }
        if (root.right != null) {
            midOrder(root.right);
        }
    }

    public int getMinimumDifference(TreeNode root) {
        midOrder(root);
        return minimumDiff;
    }

    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int m = matrix.length;
        int n = matrix[0].length;
        boolean[][] tag = new boolean[m][n];
        int x = 0;
        int y = 0;
        String direct = "right";
        while (res.size() < m * n) {
            res.add(matrix[x][y]);
            tag[x][y] = true;
            if (direct.equals("right")) {
                if (y == n - 1 || tag[x][y + 1]) {
                    direct = "down";
                    ++x;
                } else {
                    ++y;
                }
            } else if (direct.equals("down")) {
                if (x == m - 1 || tag[x + 1][y]) {
                    direct = "left";
                    --y;
                } else {
                    ++x;
                }
            } else if (direct.equals("left")) {
                if (y == 0 || tag[x][y - 1]) {
                    direct = "up";
                    --x;
                } else {
                    --y;
                }
            } else {
                if (x == 0 || tag[x - 1][y]) {
                    direct = "right";
                    ++y;
                } else {
                    --x;
                }
            }
        }
        return res;
    }

    public void setZeroes(int[][] matrix) {
        int[] x = new int[matrix.length];
        int[] y = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (matrix[i][j] == 0) {
                    x[i] = 1;
                    y[j] = 1;
                }
            }
        }
        for (int i = 0; i < x.length; ++i) {
            if (x[i] == 1) {
                for (int j = 0; j < matrix[i].length; ++j) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int j = 0; j < y.length; ++j) {
            if (y[j] == 1) {
                for (int i = 0; i < matrix.length; ++i) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public ListNode partition(ListNode head, int x) {
        ListNode l1 = new ListNode();
        ListNode l2 = new ListNode();
        ListNode p = l1;
        ListNode q = l2;
        while (head != null) {
            if (head.val < x) {
                p.next = head;
                p = head;
            } else {
                q.next = head;
                q = head;
            }
            head = head.next;
        }
        q.next = null;
        p.next = l2.next;
        return l1.next;
    }

    public List<List<Integer>> res = new ArrayList<>();
    public void dfs(int[] nums, boolean[] tag, List<Integer> tmp) {
        if (tmp.size() == nums.length) {
            res.add(tmp);
            return;
        }
        for (int i = 0; i < nums.length; ++i) {
            if (!tag[i]) {
                List<Integer> t = new ArrayList<>(tmp);
                t.add(nums[i]);
                tag[i] = true;
                dfs(nums, tag, t);
                tag[i] = false;
            }
        }
    }

    public List<List<Integer>> permute(int[] nums) {
        dfs(nums, new boolean[nums.length], new ArrayList<>());
        return res;
    }

    public static int mySqrt(int x) {
        for (int i = 1; i <= x / 2 + 1; ++i) {
            long p = i * i;
            long q = (i + 1) * (i + 1);
            if (p <= x && (q > x || q < 0)) {
                return i;
            }
        }
        return 0;
    }

    /**
     *   1 2 3 1
     * y 1 2 4 3
     * n 0 1 2 4
     */
    public int rob(int[] nums) {
        int yes = nums[0];
        int no = 0;
        for (int i=1;i<nums.length;++i) {
            int newYes = no + nums[i];
            int newNo = Math.max(yes, no);
            yes = newYes;
            no = newNo;
        }
        return Math.max(yes, no);
    }

    /**
     * n = min()
     */
    public static int coinChange(int[] coins, int amount) {
        int[] tmp = new int[amount + 1];
        for (int i = 0; i < coins.length; ++i) {
            if (coins[i] <= amount) {
                tmp[coins[i]] = 1;
            }
        }
        for (int i = 1; i <= amount; ++i) {
            if (tmp[i] != 0) {
                continue;
            }
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < coins.length; ++j) {
                if (i > coins[j] && tmp[i - coins[j]] != 0 && tmp[i - coins[j]] != Integer.MAX_VALUE) {
                    min = Math.min(min, tmp[i - coins[j]] + 1);
                }
            }
            tmp[i] = min;
        }
        return tmp[amount] == Integer.MAX_VALUE ? -1 : tmp[amount];
    }

    public ListNode mergeTwoList(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        ListNode p = head;
        while (l1 != null || l2 != null) {
            if (l2 == null || (l1 != null && l1.val <= l2.val)) {
                p.next = l1;
                p = l1;
                l1 = l1.next;
            } else {
                p.next = l2;
                p = l2;
                l2 = l2.next;
            }
        }
        p.next = null;
        return head.next;
    }

    public ListNode sortList(ListNode head) {
        int len = 0;
        ListNode p = head;
        while (p != null) {
            p = p.next;
            ++len;
        }
        if (len < 2) {
            return head;
        }
        p = head;
        int half = len / 2;
        for (int i = 1; i < half; ++i) {
            p = p.next;
        }
        ListNode q = p.next;
        p.next = null;
        ListNode before = sortList(head);
        ListNode after = sortList(q);
        head = mergeTwoList(before, after);
        return head;
    }

    public void flatten(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return;
        }
        flatten(root.left);//左子树转化为链表
        flatten(root.right);//右子树转化为链表
        //左子树的根节点赋值到右子树然后 原左子树赋值为空
        TreeNode tmp = root.right;
        root.right = root.left;
        root.left = null;
        TreeNode t = root;
        //找到最右边赋值为原右子树
        while (t.right != null) {
            t = t.right;
        }
        t.right = tmp;
    }


    public int charToInt(char s) {
        switch (s){
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

    public int romanToInt(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); ++i) {
            int t = charToInt(s.charAt(i));
            if (i == s.length() - 1 || t <= charToInt(s.charAt(i + 1))) {
                res += t;
            } else {
                res -= t;
            }
        }
        return res;
    }

    public int singleNumber(int[] nums) {
        int res = nums[0];
        for (int i = 1; i < nums.length; ++i) {
            res = res ^ nums[i];
        }
        return res;
    }

    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        boolean tag = false;
        for (int i = 0; i < m; ++i) {
            if (obstacleGrid[i][0] == 1) {
                tag = true;
            }
            if (tag) {
                dp[i][0] = 0;
            } else {
                dp[i][0] = 1;
            }
        }
        tag = false;
        for (int i = 0; i < n; ++i) {
            if (obstacleGrid[0][i] == 1) {
                tag = true;
            }
            if (tag) {
                dp[0][i] = 0;
            } else {
                dp[0][i] = 1;
            }
        }
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public int findMin(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] < nums[high]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return nums[low];
    }

    public int quickSort(int[] nums, int low, int high) {
        int i = low;
        int j = high;
        int tmp = nums[low];
        while (low < high) {
            while (low < high && nums[high] > tmp) {
                --high;
            }
            while (low < high && nums[low] <= tmp) {
                ++low;
            }
            if (low<high){
                int t = nums[low];
                nums[low]=nums[high];
                nums[high] = t;
            }
        }
        nums[i] = nums[low];
        nums[low] = tmp;
        return low;
    }

    public int findKthLargest(int[] nums, int k) {
        int low = 0;
        int high = nums.length - 1;
        k = nums.length - k;
        while (low < high) {
            int mid = quickSort(nums, low, high);
            if (mid == k) {
                return nums[mid];
            } else if (mid > k) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return 0;
    }

    public void change(char[][] board, int i, int j) {
        if (i < 0 || j < 0 || i == board.length || j == board[i].length || board[i][j] != 'O') {
            return;
        }
        board[i][j] = 'A';
        change(board, i + 1, j);
        change(board, i - 1, j);
        change(board, i, j + 1);
        change(board, i, j - 1);
    }

    public void solve(char[][] board) {
        for (int i = 0; i < board.length; ++i) {
            change(board, i, 0);
            change(board, i, board[i].length - 1);
        }
        for (int i = 0; i < board[0].length; ++i) {
            change(board, 0, i);
            change(board, board.length - 1, i);
        }
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                } else if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    /**
     * ho -> ro = h r
     *  min ([i-1,j-1],[i-1,j],[i,j-1]) + 1
     *    h o r s e
     * r  1 2 2 3 4
     * o  2 1 2
     * s  3
     * */
    public int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        // 有一个字符串为空串
        if (n * m == 0) {
            return n + m;
        }

        // DP 数组
        int[][] D = new int[n + 1][m + 1];

        // 边界状态初始化
        for (int i = 0; i < n + 1; i++) {
            D[i][0] = i;
        }
        for (int j = 0; j < m + 1; j++) {
            D[0][j] = j;
        }

        // 计算所有 DP 值
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int left = D[i - 1][j] + 1;
                int down = D[i][j - 1] + 1;
                int left_down = D[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                    left_down += 1;
                }
                D[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return D[n][m];

    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Deque<TreeNode> stack = new LinkedList<>();
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                res.add(root.val);
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            root = root.right;
        }
        return res;
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new LinkedList<>();
        while (!stack.isEmpty() || root != null) {
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

    int BSTSum = 0;

    public void  dfs(TreeNode root, int low, int high) {
        if (root==null){
            return;
        }
        if (root.val>=low &&root.val<=high){
            BSTSum+=root.val;
        }
        dfs(root.left,low,high);
        dfs(root.right, low, high);
    }
    public int rangeSumBST(TreeNode root, int low, int high) {
        dfs(root, low, high);
        return BSTSum;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root.val == p.val) {
            return p;
        }
        if (root.val == q.val) {
            return q;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        } else {
            return root;
        }
    }

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean tag = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> data = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                root = queue.poll();
                if (tag) {
                    data.add(root.val);
                } else {
                    data.add(0, root.val);
                }
                if (root.left != null) {
                    queue.offer(root.left);
                }
                if (root.right != null) {
                    queue.offer(root.right);
                }

            }
            tag = tag ? false : true;
            res.add(data);
        }
        return res;
    }

    public int maximalSquare(char[][] matrix) {
        int[][] dp = new int[matrix.length][matrix[0].length];
        int max = 0;
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (matrix[i][j] == '1') {
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    }
                }
                max = Math.max(max, dp[i][j]);
            }
        }
        return max * max;
    }

    public int countSquares(int[][] matrix) {
        int[][] dp = new int[matrix.length][matrix[0].length];
        int sum = 0;
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (matrix[i][j] == 1) {
                    if (i == 0 || j == 0) {
                        dp[i][j]=1;
                    } else {
                        dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    }
                }
                sum += dp[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        coinChange(new int[]{1}, 1);
    }

    public static ListNode createList(int[] a) {
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
}

class RandomizedSet {

    Map<Integer, Integer> value;
    Random r;
    List<Integer> nums;

    public RandomizedSet() {
        r=new Random();
        nums = new ArrayList<>();
        value = new HashMap<>();
    }

    public boolean insert(int val) {
        if (value.containsKey(val)) {
            return false;
        }
        nums.add(val);
        value.put(val,nums.size() - 1);
        return true;
    }

    public boolean remove(int val) {
        if (value.containsKey(val)) {
            int idx = value.get(val);
            int last = nums.size() -1;
            int lastVal = nums.get(last);
            nums.set(idx, lastVal);
            nums.remove(last);
            value.put(lastVal, idx);
            value.remove(val);
            return true;
        }
        return false;
    }

    public int getRandom() {
        int x = r.nextInt(nums.size());
        return nums.get(x);
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

class MinStackNew {
    private Deque<Integer> stack;
    private Deque<Integer> min;

    public MinStackNew() {
        stack = new LinkedList<>();
        min = new LinkedList<>();
    }

    public void push(int val) {
        stack.push(val);
        if (min.isEmpty() || val <= min.peek()) {
            min.push(val);
        } else {
            min.push(min.peek());
        }
    }

    public void pop() {
        stack.pop();
        min.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return min.peek();
    }
}