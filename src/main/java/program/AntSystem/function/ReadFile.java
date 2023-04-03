package program.AntSystem.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.AntSystem.graph.Graph;
import program.AntSystem.graph.SubGraph;
import program.AntSystem.graph.SubGraphs;

import java.io.*;
import java.util.*;

public class ReadFile {

    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);

    //权值为double类型,由一开始的int改的
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
        Graph area = initialGraph(readFile(Aco.filePath + "graph.txt"));
        for (int i = 0; i < res.size(); ++i) {
            start_node = (int) Math.round(res.get(i).get(0));
            start_area = (int) Math.round(res.get(i).get(1));
            end_node = (int) Math.round(res.get(i).get(2));
            end_area = (int) Math.round(res.get(i).get(3));
            weight = res.get(i).get(4);

            graph.addVertex(start_node);
            graph.addVertex(end_node);

            graph.vertex.get(start_node).addNbr(end_node, weight);
            graph.vertex.get(end_node).addNbr(start_node, weight);
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
                subGraphs.subGraphs.get(start_area).vertex.get(end_node).addNbr(start_node, weight);
            }
            //在此处可以导入分区图
/*            if (!area.vertex.containsKey(start_area)) {
                area.addVertex(start_area);
            }
            if (!area.vertex.containsKey(end_area)) {
                area.addVertex(end_area);
            }
            //如果没有边，那么权值为1，如果已经有边，那么权值加1
            if (start_area != end_area &&start_area!=15&&start_area!=22&&end_area!=15&&end_area!=22) {
                double w=area.vertex.get(start_area).getWeight(end_area);
                if (w!=Integer.MAX_VALUE) {
                    area.vertex.get(start_area).addNbr(end_area, w + 1);
                }
            }*/
        }
        //设置分区图的权值
        subGraphs.areaGraph = area;
    }

    public static double distance(List<Double> x, List<Double> y) {
        //double xx = x.get(0) * 100;
        //double xy = x.get(1) * 100;
        //double yx = y.get(0) * 100;
        //double yy = y.get(1) * 100;
        double xx = x.get(0);
        double xy = x.get(1);
        double yx = y.get(0);
        double yy = y.get(1);
        return Math.sqrt((xx - yx) * (xx - yx) + (xy - yy) * (xy - yy));
    }

    //每个点的坐标都乘以100 如果坐标是0 ，那么怎么办，分区图，是采用如何以单向图导入
    public static void handleData() {
        List<List<Integer>> area = readIntData(Aco.filePath + "area.txt");
        List<List<Integer>> link = readIntData(Aco.filePath + "ways.txt");
        List<List<Double>> coordinate = readFile(Aco.filePath + "coordinate.txt");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Aco.filePath + "finalLink.txt"));
            for (int i = 0; i < link.size(); ++i) {
                StringBuilder res = new StringBuilder();
                int start_node = link.get(i).get(0);
                int end_node = link.get(i).get(1);
                int start_area = area.get(start_node - 1).get(0);
                int end_area = area.get(end_node - 1).get(0);
                double weight = distance(coordinate.get(start_node - 1), coordinate.get(end_node - 1)) * 100000;
                res.append(start_node).append(' ');
                res.append(start_area).append(' ');
                res.append(end_node).append(' ');
                res.append(end_area).append(' ');
                res.append(weight);
                writer.write(res.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            logger.error("write file failure");
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("close writer failure");
                }
            }
        }
    }

    public static void fromCorAndWaysToGraph() {
        List<List<Integer>> ways = readIntData(Aco.filePath + "ways.txt");
        List<List<Double>> cor = readFile(Aco.filePath + "coordinate.txt");
        System.out.println(ways.size());
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Aco.filePath + "graph.txt"));
            for (int i = 0; i < ways.size(); ++i) {
                StringBuilder res = new StringBuilder();
                int start = ways.get(i).get(0);
                int end = ways.get(i).get(1);
                res.append(start).append(' ');
                res.append(end).append(' ');
                res.append(distance(cor.get(start - 1), cor.get(end - 1)) * 100000);
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

    public static void fromPartitionToArea() {
        List<List<Integer>> area = readIntData(Aco.filePath + "finalPath.txt");
        System.out.println(area.size());
        int sum = 0;
        for (int i = 0; i < area.size(); ++i) {
            if (area.get(i).get(0) == -1) {
                ++sum;
            } else {
                area.get(i).add(sum);
            }
        }
        area.sort((o1,o2)->o1.get(0)-o2.get(0));
        BufferedWriter writer = null;
        try {
            int s = 0;
            writer = new BufferedWriter(new FileWriter(Aco.filePath + "area.txt"));
            for (int i = 0; i < area.size(); ++i) {
                if (area.get(i).get(0) != -1) {
                    int t = area.get(i).get(1);
                    writer.write(String.valueOf(t));
                    writer.write("\n");
                }else {
                    ++s;
                }
            }
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void divideDataByRange() {
        final int L = 6;
        String fileName = Aco.filePath + "finalPath.txt";
        List<List<Double>> lists = readFile(fileName);
        Map<Double, Integer> tmp = new TreeMap<>((o1, o2) -> o1 > o2 ? 1 : -1);
        for (int i = 0; i < lists.size(); ++i) {
            double sum = 0;
            for (Double x : lists.get(i)) {
                sum += x;
            }
            tmp.put(sum, i);
        }
        int avg = (int) Math.ceil(lists.size() / L);
        int[] label = new int[lists.size()];
        int[] sum = new int[L];
        int i = 0;
        for (Map.Entry<Double, Integer> e : tmp.entrySet()) {
            int index = 0;
            int rank = e.getValue();
            while (rank > avg) {
                rank -= avg;
                ++index;
            }
            label[i++] = index;
            sum[index] += 1;
        }
        for (i = 0; i < label.length; ++i) {
            System.out.println(label[i]);
        }
        System.out.println(Arrays.toString(sum));
    }

    public static void main(String[] args) {
        /*List<List<Double>> coordinate=readFile(Aco.filePath + "coordinate.txt");
        System.out.println(coordinate.get(7));
        System.out.println(coordinate.get(8));
        System.out.println(coordinate.get(9));
        System.out.println(distance(coordinate.get(8),coordinate.get(9)));*/
        handleData();
        //fromPartitionToArea();
    }
}
