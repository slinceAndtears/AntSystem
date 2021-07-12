package program.AntSystem.function;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<List<Integer>> path;
    public List<List<Integer>> areaPath;//区域图走的路径
    public double sumTime;

    public Solution() {
    }

    public Solution(List<List<Integer>> p, double t, List<List<Integer>> a) {
        this.sumTime = t;
        path = new ArrayList<>();
        for (List<Integer> tmp : p) {
            path.add(new ArrayList<>(tmp));
        }
        areaPath = new ArrayList<>();
        for (List<Integer> list : a) {
            areaPath.add(new ArrayList<>(list));
        }
    }

    @Override
    public String toString() {
        return String.valueOf(sumTime);
    }
}
