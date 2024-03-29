package com.scut.lzx;


import java.util.*;

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
class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(){}
    TreeNode(int val){
        this(val,null,null);
    }
    TreeNode(int val, TreeNode left, TreeNode right){
        this.val=val;
        this.left=left;
        this.right=right;
    }
}
public class Test {
    static void quickSort(int[] a, int low, int high) {
        if (low >= high) {
            return;
        }
        int i = low;
        int j = high;
        int temp = a[low];
        while (i < j) {
            while (i < j && a[j] >= temp) {
                --j;
            }
            a[i] = a[j];
            while (i < j && a[i] <= temp) {
                ++i;
            }
            a[j] = a[i];
        }
        a[i] = temp;
        quickSort(a, low, i - 1);
        quickSort(a, i + 1, high);
    }

    // 0 1 3
    static void merge(int[] a, int low, int mid, int high) {
        int[] temp1 = new int[mid - low + 1];
        int[] temp2 = new int[high - mid];
        for (int i = low; i <= high; ++i) {
            if (i <= mid) {
                temp1[i - low] = a[i];
            } else {
                temp2[i - mid - 1] = a[i];
            }
        }
        int index = low;
        int index1 = 0;
        int index2 = 0;
        while (index1 < temp1.length || index2 < temp2.length) {
            if (index2 == temp2.length || (index1 < temp1.length && temp1[index1] < temp2[index2])) {
                a[index++] = temp1[index1++];
            } else {
                a[index++] = temp2[index2++];
            }
        }
    }

    static void mergeSort(int[] a, int low, int high) {
        if (low < high) {
            int mid = low + (high - low) / 2;
            mergeSort(a, low, mid);
            mergeSort(a, mid + 1, high);
            merge(a, low, mid, high);
        }
    }

    static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static void adjustHeap(int[] a, int i, int length) {
        int temp = a[i];
        for (int k = i * 2 + 1; k < length; ++k) {
            if (k + 1 < length && a[k] < a[k + 1]) {
                ++k;
            }
            if (a[k] > temp) {
                a[i] = a[k];
                i = k;
            }
        }
        a[i] = temp;
    }

    static void heapSort(int[] a) {
        for (int i = (a.length - 1) / 2; i >= 0; --i) {
            adjustHeap(a, i, a.length);
        }
        for (int i = a.length - 1; i >= 0; --i) {
            swap(a, 0, i);
            adjustHeap(a, 0, i);
        }
    }

    static boolean isValid(String s) {
        Deque<Character> stack = new LinkedList<>();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(' || s.charAt(i) == '{' || s.charAt(i) == '[') {
                stack.push(s.charAt(i));
            } else if (s.charAt(i) == ')') {
                if (stack.size() != 0 && stack.peek() == '(') {
                    stack.pop();
                } else {
                    return false;
                }
            } else if (s.charAt(i) == ']') {
                if (stack.size() != 0 && stack.peek() == '[') {
                    stack.pop();
                } else {
                    return false;
                }
            } else {
                if (stack.size() != 0 && stack.peek() == '{') {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode h = new ListNode();
        h.next = head;
        ListNode p = h.next;
        head = head.next;
        while (head != null) {
            if (head.val != p.val) {
                p.next = head;
                p = head;
            }
            head = head.next;
        }
        p.next = null;
        return h.next;
    }

    static int searchInsert(int[] nums, int target) {
        if (nums[nums.length - 1] < target) {
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

    static int findPeakElement(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (mid == 0 || mid == nums.length - 1) {
                return mid;
            } else if (nums[mid] < nums[mid + 1]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int low = 0;
        int high = m * n - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (matrix[mid / n][mid % n] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return matrix[low / n][low % n] == target;
    }

    static int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[low] < nums[mid]) {
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

    static int findMin(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
        }
        return 0;
    }

    static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int left = 0;
        int right = matrix[0].length - 1;
        int top = 0;
        int down = matrix.length - 1;
        int sum = matrix.length * matrix[0].length;
        while (res.size() < sum) {
            for (int i = left; i <= right && res.size() < sum; ++i) {
                res.add(matrix[top][i]);
            }
            ++top;
            for (int i = top; i <= down && res.size() < sum; ++i) {
                res.add(matrix[i][right]);
            }
            --right;
            for (int i = right; i >= left && res.size() < sum; --i) {
                res.add(matrix[down][i]);
            }
            --down;
            for (int i = down; i >= top && res.size() < sum; --i) {
                res.add(matrix[i][left]);
            }
            ++left;
        }
        return res;
    }

    static int SEG_COUNT = 3;
    static List<String> res;

    static void dfs(String s, int cur, int seg, StringBuilder t) {
        if (cur == s.length() && seg <= 3) {
            return;
        }
        if (seg == SEG_COUNT) {
            if ((s.length() - cur < 4 && Integer.valueOf(s.substring(cur)) <= 255) || (cur == s.length() - 1)) {
                t.append(s.substring(cur));
                res.add(t.toString());
            }
            return;
        } else if (s.charAt(cur) == '0') {
            StringBuilder tmp = new StringBuilder(t);
            tmp.append("0.");
            dfs(s, cur + 1, seg + 1, tmp);
        } else {
            int i = 1;
            while (i <= 3 && cur + i < s.length() && Integer.valueOf(s.substring(cur, cur + i)) <= 255) {
                StringBuilder tmp = new StringBuilder(t);
                tmp.append(Integer.valueOf(s.substring(cur, cur + i)));
                tmp.append('.');
                dfs(s, cur + i, seg + 1, tmp);
                ++i;
            }
        }
    }

    static List<String> restoreIpAddress(String s) {
        res = new ArrayList<>();
        dfs(s, 0, 0, new StringBuilder());
        return res;
    }

    static void dfs(char[][] grid, int x, int y) {
        if (x < 0 || x == grid[0].length || y < 0 || y == grid.length || grid[x][y] != '1') {
            return;
        }
        grid[x][y] = '0';
        dfs(grid, x + 1, y);
        dfs(grid, x - 1, y);
        dfs(grid, x, y + 1);
        dfs(grid, x, y - 1);
    }

    static int numsIslands(char[][] grid) {
        int res = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                if (grid[i][j] == '1') {
                    ++res;
                    dfs(grid, i, j);
                }
            }
        }
        return res;
    }

    static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return res;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; ++i) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum > 0) {
                    --k;
                } else if (sum < 0) {
                    ++j;
                } else {
                    res.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    while (j < k && nums[j] == nums[j + 1]) {
                        ++j;
                    }
                    ++j;
                    while (j < k && nums[k] == nums[k - 1]) {
                        --k;
                    }
                    --k;
                }
            }
        }
        return res;
    }

    static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (int[] o1, int[] o2) -> o1[0] - o2[0]);
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < intervals.length; ++i) {
            int l = intervals[i][0];
            int r = intervals[i][1];
            if (res.size() == 0 || res.get(res.size() - 1)[1] <= l) {
                res.add(new int[]{l, r});
            } else {
                res.get(res.size() - 1)[1] = Math.max(r, res.get(res.size() - 1)[1]);
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    static ListNode rotateRight(ListNode head, int k) {
        int len = 0;
        ListNode p = head;
        while (p.next != null) {
            p = p.next;
            ++len;
        }
        p.next = head;
        k = k % (len + 1);
        k = len - k;
        for (int i = 0; i < k; ++i) {
            p = p.next;
        }
        head = p.next;
        p.next = null;
        return head;
    }

    static int[][] generateMatrix(int n) {
        int[][] res = new int[n][n];
        int i = 1;
        int x = 0;
        int y = 0;
        char direction = 'r';
        while (i <= n * n) {
            if (direction == 'r') {//向右
                res[x][y] = i++;
                if (y == n - 1 || res[x][y + 1] != 0) {
                    direction = 'd';
                    ++x;
                } else {
                    ++y;
                }
            } else if (direction == 'd') {//向下
                res[x][y] = i++;
                if (x == n - 1 || res[x + 1][y] != 0) {
                    direction = 'l';
                    --y;
                } else {
                    ++x;
                }
            } else if (direction == 'l') {//想左
                res[x][y] = i++;
                if (y == 0 || res[x][y - 1] != 0) {
                    direction = 't';
                    --x;
                } else {
                    --y;
                }
            } else {//向上
                res[x][y] = i++;
                if (x == 0 || res[x - 1][y] != 0) {
                    direction = 'r';
                    ++y;
                } else {
                    --x;
                }
            }
        }
        return res;
    }

    static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode h = new ListNode();
        ListNode p = h;
        int jump = 0;
        while (l1 != null || l2 != null) {
            int num1 = l1 == null ? 0 : l1.val;
            int num2 = l2 == null ? 0 : l2.val;
            int res = num1 + num2 + jump;
            if (res > 9) {
                jump = 1;
                res -= 10;
            } else {
                jump = 0;
            }
            if (l1 != null) {
                l1.val = res;
                p.next = l1;
                p = l1;
            } else {
                l2.val = res;
                p.next = l2;
                p = l2;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (jump != 0) {
            ListNode s = new ListNode(jump);
            p.next = s;
            p = s;
        }
        p.next = null;
        return h.next;
    }

    static int minDistance(String word1, String word2) {
        if (word1.length() * word2.length() == 0) {
            return word1.length() + word2.length();
        }
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i < word1.length(); ++i) {
            dp[i][0] = i;
        }
        for (int i = 0; i < word2.length(); ++i) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= word1.length(); ++i) {
            for (int j = 1; j <= word2.length(); ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

    static class UnionFind {
        int count;
        int[] parent;
        int[] rank;

        public UnionFind(char[][] grid) {
            count = 0;
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        ++count;
                    }
                    rank[i * n + j] = 0;
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx] += 1;
                }
                --count;
            }
        }

        public int getCount() {
            return this.count;
        }
    }

    static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        Test.UnionFind unionFind = new Test.UnionFind(grid);
        for (int r = 0; r < m; ++r) {
            for (int c = 0; c < n; ++c) {
                if (grid[r][c] == '1') {
                    grid[r][c] = '0';
                    if (r - 1 >= 0 && grid[r - 1][c] == '1') {
                        unionFind.union(r * n + c, (r - 1) * n + c);
                    }
                    if (r + 1 < m && grid[r + 1][c] == '1') {
                        unionFind.union(r * n + c, (r + 1) * n + c);
                    }
                    if (c - 1 >= 0 && grid[r][c - 1] == '1') {
                        unionFind.union(r * n + c, r * n + c - 1);
                    }
                    if (c + 1 < n && grid[r][c + 1] == '1') {
                        unionFind.union(r * n + c, r * n + c + 1);
                    }
                }
            }
        }
        return unionFind.getCount();
    }

    static int maxSubArray(int[] nums) {
        int res = nums[0];
        int max = 0;
        for (int i = 1; i < nums.length; ++i) {
            max = Math.max(max + nums[i], max);
            res = Math.max(res, max);
        }
        return res;
    }

    static List<List<Integer>> permuteResult;
    static boolean[] tag;

    static void dfs(int[] nums, int cur, List<Integer> t) {
        if (cur == nums.length) {
            permuteResult.add(new ArrayList<>(t));
            return;
        }
        for (int i = 0; i < nums.length; ++i) {
            if (tag[i]) {
                continue;
            }
            t.add(nums[i]);
            tag[i] = true;
            dfs(nums, cur + 1, t);
            tag[i] = false;
            t.remove(t.size() - 1);
        }
    }

    static List<List<Integer>> permute(int[] nums) {
        permuteResult = new ArrayList<>();
        tag = new boolean[nums.length];
        dfs(nums, 0, new ArrayList<>());
        return permuteResult;
    }

    static String reverseWords(String s) {
        StringBuilder res = new StringBuilder();
        int i = s.length() - 1;
        while (i >= 0) {
            if (s.charAt(i) != ' ') {
                StringBuilder t = new StringBuilder();
                while (i >= 0 && s.charAt(i) != ' ') {
                    t.insert(0, s.charAt(i));
                    --i;
                }
                res.append(t);
                res.append(' ');
            } else {
                --i;
            }
        }
        res.deleteCharAt(res.length() - 1);
        return res.toString();
    }

    static int rob(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int yes = nums[0];
        int no = 0;
        int t = 0;
        for (int i = 1; i < nums.length; ++i) {
            t = no;
            no = Math.max(yes, no);
            yes = t + nums[i];
        }
        return Math.max(yes, no);
    }

    static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        int res = 0;
        for (int i = 0; i < nums.length; ++i) {
            dp[i] = 1;
        }
        for (int i = 1; i < nums.length; ++i) {
            for (int j = 0; j < i; ++j) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            res = Math.max(dp[i], res);
        }
        return res;
    }

    static int findNthDigit(int n) {
        long num = n;
        long size = 1;
        long max = 9;
        while (n > 0) {
            if (n - max * size > 0) {
                num -= max * size;
                max *= 10;
                ++size;
            } else {
                long count = num / size;
                long left = num % size;
                if (left == 0) {
                    return (int) (((long) Math.pow(10, size - 1) + count - 1) % 10);
                } else {
                    return (int) ((((long) Math.pow(10, size - 1) + count) / ((long) Math.pow(10, (size - left)))) % 10);
                }
            }
        }
        return 0;
    }

    static ListNode[] myReverse(ListNode head, ListNode tail) {
        ListNode h = new ListNode();
        ListNode t = head;
        ListNode tai = tail.next;
        while (head != tai) {
            ListNode p = head;
            head = head.next;
            p.next = h.next;
            h.next = p;
        }
        return new ListNode[]{h.next, t};
    }

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

    static void show(ListNode h) {
        while (h != null) {
            System.out.print(h.val + " ");
            h = h.next;
        }
        System.out.println();
    }

    static ListNode reverseKGroup(ListNode head, int k) {
        ListNode h = new ListNode();
        h.next = head;
        ListNode p = h;
        while (head != null) {
            ListNode tail = head;
            for (int i = 0; i < k - 1; ++i) {
                tail = tail.next;
                if (tail == null) {
                    return h.next;
                }
            }
            ListNode tmp = tail.next;
            ListNode[] pairs = myReverse(head, tail);
            p.next = pairs[0];
            pairs[1].next = tmp;
            head = tmp;
            p = pairs[1];
        }
        return h.next;
    }

    static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == root || q == root) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        return root;
    }

    static ListNode partition(ListNode head, int x) {
        ListNode large = new ListNode();
        ListNode small = new ListNode();
        ListNode l = large;
        ListNode s = small;
        while (head != null) {
            if (head.val >= x) {
                l.next = head;
                l = head;
            } else {
                s.next = head;
                s = head;
            }
            head = head.next;
        }
        l.next = null;
        s.next = large.next;
        return small.next;
    }

    static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int min = Integer.MAX_VALUE;
        int res = 0;
        for (int i = 0; i < nums.length - 2; ++i) {
            int low = i + 1;
            int high = nums.length - 1;
            while (low < high) {
                int sum = nums[i] + nums[low] + nums[high];
                if (min > Math.abs(sum - target)) {
                    min = Math.abs(sum - target);
                    res = sum;
                }
                if (sum > target) {
                    --high;
                } else if (sum < target) {
                    ++low;
                } else {
                    return 0;
                }
            }
        }
        return res;
    }

    static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 3; ++i) {
            if (i > 0 && nums[i] == nums[i + 1]) {
                continue;
            }
            for (int j = i + 1; j < nums.length - 2; ++j) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                int low = j + 1;
                int high = nums.length - 1;
                while (low < high) {
                    int sum = nums[i] + nums[j] + nums[low] + nums[high];
                    if (sum > target) {
                        --high;
                    } else if (sum < target) {
                        ++low;
                    } else {
                        res.add(Arrays.asList(nums[i], nums[j], nums[low], nums[high]));
                        int l = nums[low];
                        int h = nums[high];
                        while (low < high && nums[low] == l) {
                            ++low;
                        }
                        while (low < high && nums[high] == h) {
                            --high;
                        }
                    }
                }
            }
        }
        return res;
    }

    static boolean existRes;

    static void dfs(char[][] board, String word, int x, int y, int cur) {
        if (cur == word.length()) {
            existRes = true;
            return;
        }
        if (x < 0 || x > board.length || y < 0 || y < board[x].length || existRes || board[x][y] != word.charAt(cur)) {
            return;
        }
        char t = board[x][y];
        board[x][y] = '.';
        dfs(board, word, x + 1, y, cur + 1);
        dfs(board, word, x - 1, y, cur + 1);
        dfs(board, word, x, y + 1, cur + 1);
        dfs(board, word, x, y - 1, cur + 1);
    }

    static boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == word.charAt(0)) {
                    dfs(board, word, i, j, 1);
                }
            }
        }
        return existRes;
    }

    static int compareVersion(String version1, String version2) {
        String[] versions1 = version1.split("\\.");
        String[] versions2 = version2.split("\\.");
        int v1 = 0;
        int v2 = 0;
        while (v1 < versions1.length || v2 < versions2.length) {
            int t1 = v1 == versions1.length ? 0 : Integer.valueOf(versions1[v1]);
            int t2 = v2 == versions2.length ? 0 : Integer.valueOf(versions2[v2]);
            if (t1 == t2) {
                ++v1;
                ++v2;
            } else if (t1 > t2) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }

    static int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] entry = new int[numCourses];
        int[] res = new int[numCourses];
        boolean[] tag = new boolean[numCourses];
        for (int i = 0; i < prerequisites.length; ++i) {
            ++entry[prerequisites[i][0]];
        }
        //System.out.println(Arrays.toString(entry));
        int sum = 0;
        while (sum < numCourses) {
            int node = -1;
            for (int i = 0; i < entry.length; ++i) {
                if (tag[i] == false && entry[i] == 0) {
                    node = i;
                    break;
                }
            }
            if (node == -1) {
                return new int[]{};
            }
            tag[node] = true;
            res[sum++] = node;
            for (int i = 0; i < prerequisites.length; ++i) {
                if (prerequisites[i][1] == node) {
                    --entry[prerequisites[i][0]];
                }
            }
        }
        return res;
    }

    static ListNode insertionSortList(ListNode head) {
        ListNode h = new ListNode();
        h.next = head;
        head = head.next;
        h.next.next = null;
        while (head != null) {
            ListNode t = head;
            head = head.next;
            ListNode p = h;
            while (p.next != null && p.next.val < t.val) {
                p = p.next;
            }
            t.next = p.next;
            p.next = t;
        }
        return h.next;
    }

    static ListNode reverseList(ListNode head){
        if (head==null||head.next==null){
            return head;
        }
        ListNode h=new ListNode();
        while (head!=null){
            ListNode t=head;
            head=head.next;
            t.next=h.next;
            h.next=t;
        }
        return h.next;
    }

    static void reorderList(ListNode head) {
        if (head==null||head.next==null){
            return;
        }
        ListNode h = new ListNode();
        h.next = head;
        ListNode slow = h;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        show(slow);
        ListNode p = slow;
        slow = slow.next;
        show(slow);
        p.next = null;
        slow = reverseList(slow);
        h.next = null;
        ListNode s = h;
        while (head != null || slow != null) {
            if (head!=null){
                s.next = head;
                s = head;
                head = head.next;
            }
            if (slow!=null) {
                s.next = slow;
                s = slow;
                slow = slow.next;
            }
        }
        s.next = null;
        head = h.next;
    }

    static String largestNumber(int[] nums) {
        String[] numbers = new String[nums.length];
        for (int i = 0; i < nums.length; ++i) {
            numbers[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(numbers, (o1, o2) -> (o2 + o1).compareTo(o1 + o2));
        StringBuilder number = new StringBuilder();
        for (String s : numbers) {
            if (s.equals("0") && number.toString().equals("0")) {
                continue;
            }
            number.append(s);
        }
        return number.toString();
    }

    static int search1(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            System.out.println(mid);
            if (nums[mid] > target) {
                high = mid - 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return nums[low] == target ? low : -1;
    }

    static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode A = headA;
        ListNode B = headB;
        while (A != B) {
            A = A.next == null ? headB : A.next;
            B = B.next == null ? headA : B.next;
        }
        return A;
    }

    static ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    @org.junit.Test
    public void test1() {
        int[] a = {1};
        ListNode l=createList(a);
        reorderList(l);
    }
}
