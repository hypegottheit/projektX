package taf.kr.core;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Node implements Graph{
    private String name;
    private List<Node> nodeList = new ArrayList();
    private Object object;

    public Node(String name) {
        this.name = name;
    }

    public void addNode(Node node){
        nodeList.add(node);
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Graph> getChild(){
        List<Graph> graph = new ArrayList();
        for(Node node : nodeList) graph.add(node);
        return graph;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setObject(Object object) {
        this.object = object;
    }

    public void out(){
        mxGraph g = GraphBuilder.build(this);
        g.setAutoSizeCells(true);
        g.setGridSize(10);
        JFrame frame = new JFrame();
        frame.setSize(700, 600);
        mxGraphComponent graphComponent = new mxGraphComponent(g);
        graphComponent.setCenterPage(true);
        graphComponent.setAutoExtend(true);
        graphComponent.setCenterZoom(true);
        frame.add(graphComponent);
        frame.setVisible(true);
    }
}
