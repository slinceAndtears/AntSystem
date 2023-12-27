package program.work;


import program.test.LeetCode;

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

    public static void main(String[] args) {

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