package program.AntSystem.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    public Map<Integer, Vertex> vertex;
    public int nodeNum;

    public Graph() {
        nodeNum = 0;
        vertex = new HashMap<>();
    }

    public void addVertex(int id) {//添加一个顶点
        if (!vertex.containsKey(id)) {
            vertex.put(id, new Vertex(id));
            ++nodeNum;
        }
    }

    public List<Integer> getAllVertex() {//获取所有的节点
        List<Integer> res = new ArrayList<>();
        for (Map.Entry<Integer, Vertex> t : vertex.entrySet()) {
            res.add(t.getKey());
        }
        return res;
    }
}
