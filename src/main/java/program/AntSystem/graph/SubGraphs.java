package program.AntSystem.graph;

import java.util.HashMap;
import java.util.Map;

public class SubGraphs {
    public Map<Integer, SubGraph> subGraphs;//key为区域id value为对应的子图
    public Graph areaGraph;

    public SubGraphs() {
        subGraphs = new HashMap<>();
        areaGraph=new Graph();
    }

    public void addSubGraph(int area) {
        if (!subGraphs.containsKey(area)) {
            subGraphs.put(area, new SubGraph(area));
            areaGraph.addVertex(area);
        }
    }
}
