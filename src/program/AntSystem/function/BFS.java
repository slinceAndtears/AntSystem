package program.AntSystem.function;

import program.AntSystem.graph.Graph;

import java.util.*;

public class BFS {
    /**
     * 广度优先遍历，对图进行分层处理，或者直接手动对分区连通性进行处理
     * @Params graph 需要广度优先分层的图
     * @Params start 广度优先遍历的起点
     * @return level 每个节点对应的层次
     * */
    public static Map<Integer,Integer> bfs(Graph graph,int start){
        int l=0;
        Map<Integer,Integer> level=new HashMap<>();
        Queue<Integer> queue=new LinkedList<>();
        Map<Integer,Boolean> tag=new HashMap<>();
        List<Integer> allVertex=graph.getAllVertex();
        for (Integer x:allVertex){
            tag.put(x,false);
        }
        tag.put(start,true);
        queue.offer(start);
        while (!queue.isEmpty()){
            int size=queue.size();
            for (int i=0;i<size;++i){
                int node=queue.poll();
                level.put(node,l);
                tag.put(node,true);
                List<Integer> nbr=graph.vertex.get(node).getAllNbr();
                for (Integer x:nbr){
                    if (!tag.get(x)&&!queue.contains(x)){
                        queue.offer(x);
                    }
                }
            }
            ++l;
        }
        return level;
    }
}
