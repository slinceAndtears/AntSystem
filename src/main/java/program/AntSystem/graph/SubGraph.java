package program.AntSystem.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubGraph extends Graph {
    public int area;

    public SubGraph(int a) {
        super();
        this.area = a;
        connect = new HashMap<>();
    }

    //分区之间联通的点
    public Map<Integer, List<Integer>> connect;
}
