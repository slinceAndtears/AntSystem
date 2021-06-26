package program.AntSystem.function;

import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraph;
import program.AntSystem.graph.SubGraphs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    //权值大概率为double类型, 目前还只支持是int类型，后续再继续改进
    public static List<List<Integer>> readFile(String fileName) {
        List<List<Integer>> res = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(fileName)));
            String s;
            while ((s = reader.readLine()) != null) {
                List<Integer> tmp = new ArrayList<>();
                String[] strings = s.split(" ");
                for (String t : strings) {
                    tmp.add(Integer.valueOf(t));
                }
                res.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Graph initialGraph(List<List<Integer>> res) {//初始化没有区域的图
        Graph graph = new Graph();
        int start_node = 0;
        int end_node = 0;
        int weight = 0;
        for (int i = 0; i < res.size(); ++i) {
            start_node = res.get(i).get(0);
            end_node = res.get(i).get(1);
            weight = res.get(i).get(2);
            graph.addVertex(start_node);
            graph.addVertex(end_node);
            graph.vertex.get(start_node).addNbr(end_node, weight);
            graph.vertex.get(end_node).addNbr(start_node, weight);
        }
        return graph;
    }

    public static Graph initialSingleGraph(List<List<Integer>> res) {
        Graph graph = new Graph();
        int start_node = 0;
        int end_node = 0;
        int weight = 0;
        for (int i = 0; i < res.size(); ++i) {
            start_node = res.get(i).get(0);
            end_node = res.get(i).get(1);
            weight = res.get(i).get(2);
            graph.addVertex(start_node);
            graph.addVertex(end_node);
            graph.vertex.get(start_node).addNbr(end_node, weight);
            //graph.vertex.get(end_node).addNbr(start_node,weight);
        }
        return graph;
    }

    public static void initialSubGraph(Graph graph, SubGraphs subGraphs) {
        List<List<Integer>> res = ReadFile.readFile("src/program/AntSystem/graph.txt");
        int start_node = 0;
        int start_area = 0;
        int end_node = 0;
        int end_area = 0;
        double weight = 0d;
        Graph area=new Graph();//分区图
        for (int i = 0; i < res.size(); ++i) {
            start_node = res.get(i).get(0);
            start_area = res.get(i).get(1);
            end_node = res.get(i).get(2);
            end_area = res.get(i).get(3);
            weight = res.get(i).get(4);

            graph.addVertex(start_node);
            graph.addVertex(end_node);

            graph.vertex.get(start_node).addNbr(end_node, weight);
            //graph.vertex.get(end_node).addNbr(start_node,weight);
            if (subGraphs.subGraphs.get(start_area) == null) {
                subGraphs.subGraphs.put(start_area, new SubGraph(start_area));
            }
            subGraphs.subGraphs.get(start_area).addVertex(start_node);
            if (subGraphs.subGraphs.get(end_area) == null) {
                subGraphs.subGraphs.put(end_area, new SubGraph(end_area));
            }
            subGraphs.subGraphs.get(end_area).addVertex(end_node);
            if (start_area == end_area) {
                subGraphs.subGraphs.get(start_area).vertex.get(start_node).addNbr(end_node, weight);
                //subGraphs.subGraphs.get(start_area).vertex.get(end_node).addNbr(start_node,weight);
            }
            //在此处可以导入分区图
            if (!area.vertex.containsKey(start_area)){
                area.addVertex(start_area);
            }
            if (!area.vertex.containsKey(end_area)){
                area.addVertex(end_area);
            }
            if (start_area!=end_area){
                area.vertex.get(start_area).addNbr(end_area,1);
            }

        }
        //设置分区图的权值

        subGraphs.areaGraph=area;
/*        //分区之间的联通性 ,目前建议都联通 不做特殊处理 还是手动导入？？？
        for (int i = 0; i < res.size(); ++i) {
            int start = res.get(i).get(0);
            int end = res.get(i).get(1);
            List<Integer> connect = new ArrayList<>();
            for (int j = 2; j < res.get(i).size(); ++j) {
                connect.add(res.get(i).get(j));
            }
            subGraphs.subGraphs.get(start).connect.put(end, connect);
        }*/
    }
}
