package program.AntSystem.function;

import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraph;
import program.AntSystem.graph.SubGraphs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    //权值大概率为double类型, 目前还只支持是int类型，后续再继续改进
    public static List<List<Double>> readFile(String fileName) {
        List<List<Double>> res = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(fileName)));
            String s;
            while ((s = reader.readLine()) != null) {
                List<Double> tmp = new ArrayList<>();
                String[] strings = s.split(" ");
                for (String t : strings) {
                    tmp.add(Double.valueOf(t));
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

    public static List<List<Integer>> readIntData(String fileName) {
        List<List<Double>> doubleData = readFile(fileName);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < doubleData.size(); ++i) {
            res.add(new ArrayList<>());
            for (int j = 0; j < doubleData.get(i).size(); ++j) {
                res.get(i).add((int) Math.round(doubleData.get(i).get(j)));
            }
        }
        return res;
    }

    public static Graph initialGraph(List<List<Double>> res) {//初始化没有区域的图
        Graph graph = new Graph();
        int start_node = 0;
        int end_node = 0;
        double weight = 0;
        for (int i = 0; i < res.size(); ++i) {
            start_node = (int) Math.round(res.get(i).get(0));
            end_node = (int) Math.round(res.get(i).get(1));
            weight = res.get(i).get(2);
            graph.addVertex(start_node);
            graph.addVertex(end_node);
            graph.vertex.get(start_node).addNbr(end_node, weight);
            graph.vertex.get(end_node).addNbr(start_node, weight);
        }
        return graph;
    }

    public static Graph initialSingleGraph(List<List<Double>> res) {
        Graph graph = new Graph();
        int start_node = 0;
        int end_node = 0;
        double weight = 0;
        for (int i = 0; i < res.size(); ++i) {
            start_node = (int) Math.round(res.get(i).get(0));
            end_node = (int) Math.round(res.get(i).get(1));
            weight = res.get(i).get(2);
            graph.addVertex(start_node);
            graph.addVertex(end_node);
            graph.vertex.get(start_node).addNbr(end_node, weight);
            //graph.vertex.get(end_node).addNbr(start_node,weight);
        }
        return graph;
    }

    public static void initialSubGraph(Graph graph, SubGraphs subGraphs, String fileName) {
        List<List<Double>> res = ReadFile.readFile(fileName);
        int start_node = 0;
        int start_area = 0;
        int end_node = 0;
        int end_area = 0;
        double weight = 0d;
        Graph area = new Graph();//分区图
        for (int i = 0; i < res.size(); ++i) {
            start_node = (int) Math.round(res.get(i).get(0));
            start_area = (int) Math.round(res.get(i).get(1));
            end_node = (int) Math.round(res.get(i).get(2));
            end_area = (int) Math.round(res.get(i).get(3));
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
            if (!area.vertex.containsKey(start_area)) {
                area.addVertex(start_area);
            }
            if (!area.vertex.containsKey(end_area)) {
                area.addVertex(end_area);
            }
            if (start_area != end_area) {
                area.vertex.get(start_area).addNbr(end_area, 1);
            }

        }
        //设置分区图的权值

        subGraphs.areaGraph = area;
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

    public static double distance(List<Double> x, List<Double> y) {
        double xx = x.get(0) * 100;
        double xy = x.get(1) * 100;
        double yx = y.get(0) * 100;
        double yy = y.get(1) * 100;
        return Math.sqrt((xx - yx) * (xx - yx) + (xy - yy) * (xy - yy));
    }

    //每个点的坐标都乘以100 如果坐标是0 ，那么怎么办，分区图，是采用如何以单向图导入
    public static void handleData() {
        List<List<Integer>> area = readIntData("src/program/AntSystem/friedrichshain/area.txt");
        List<List<Integer>> link = readIntData("src/program/AntSystem/friedrichshain/link.txt");
        List<List<Double>> coordinate = readFile("src/program/AntSystem/friedrichshain/coordinate.txt");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("src/program/AntSystem/friedrichshain/finalLink.txt"));
            for (int i = 0; i < link.size(); ++i) {
                StringBuilder res = new StringBuilder();
                int start_node = link.get(i).get(0) - 1;
                int end_node = link.get(i).get(1) - 1;
                int start_area = area.get(start_node).get(0);
                int end_area = area.get(end_node).get(0);
                double weight = distance(coordinate.get(start_node), coordinate.get(end_node));
                res.append(start_node).append(' ');
                res.append(start_area).append(' ');
                res.append(end_node).append(' ');
                res.append(end_area).append(' ');
                res.append(weight);
                writer.write(res.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
