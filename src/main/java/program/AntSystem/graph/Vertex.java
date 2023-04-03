package program.AntSystem.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex {
    public int id;//当前顶点的id值
    Map<Integer, Double> neighbors;//该点的邻居以及对应的权值

    private static final Logger logger = LoggerFactory.getLogger(Vertex.class);

    public Vertex(int i) {
        id = i;
        neighbors = new HashMap<>();
    }

    public void addNbr(int id, double weight) {//添加一个邻居
        if (neighbors.containsKey(id)) {
            //logger.error("边已经存在start node is {},end node is {}", this.id, id);
        }
        neighbors.put(id, weight);
    }

    public double getWeight(int id) {//根据邻居的id获得权值
        if (neighbors.containsKey(id)) {//如果存在这个邻居 那么返回它对应的权值
            return neighbors.get(id);
        }
        return Integer.MAX_VALUE;//不存在这个邻居 那么直接返回int的最大值
    }

    public List<Integer> getAllNbr() {//获取所有邻居
        List<Integer> res = new ArrayList<>();
        for (Map.Entry<Integer, Double> x : neighbors.entrySet()) {
            res.add(x.getKey());
        }
        return res;
    }

    public boolean removeNbr(int nbr){
        neighbors.remove(nbr);
        return true;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                '}';
    }
}
