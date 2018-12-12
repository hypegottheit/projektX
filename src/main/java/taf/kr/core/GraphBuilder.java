package taf.kr.core;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import java.util.HashMap;
import java.util.Map;

public class GraphBuilder {

    static int lv = 0;

    public static mxGraph build(Graph graph){
        mxGraph g = new mxGraph();
        applyEdgeDefaults(g);
        g.getModel().beginUpdate();
        g.setAutoSizeCells(true);
        g.setCellsEditable(false);
        g.setCellsMovable(false);
        g.setCellsLocked(true);
        g.alignCells(mxConstants.ALIGN_RIGHT);
        int x = 100;
        int y = 1000;
        build(g,graph,x,y);
        g.getModel().endUpdate();
        return g;
    }

    private static void build(mxGraph g, Graph graph, int x, int y){
        lv++;
        graph.setObject(addNode(g,graph.getName(),y,x));
        Graph gp = graph;
        int size = gp.getChild().size();
        int count = 0;
        for(Graph gr : gp.getChild()){
            int cy = (count < size/2) ? y - 400/(lv+1) * (size/2 - count) : y + 400/(lv+1) * (count - size/2);
            build(g,gr,x+40, cy);
            addEdge(g,gp,gr);
            count++;
        }
        lv--;
    }

    private static void applyEdgeDefaults(mxGraph graph) {
        // Settings for edges
        Map<String, Object> edge = new HashMap();
        edge.put(mxConstants.STYLE_ROUNDED, true);
        edge.put(mxConstants.STYLE_ORTHOGONAL, false);
        edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        edge.put(mxConstants.STYLE_STROKECOLOR, "#6482B9"); // default is #6482B9
        edge.put(mxConstants.STYLE_FONTCOLOR, "#FFE4E1");

        mxStylesheet edgeStyle = new mxStylesheet();
        edgeStyle.setDefaultEdgeStyle(edge);
        Map<String, Object> v = new HashMap<>();
        v.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        v.put(mxConstants.STYLE_FONTCOLOR, "black");
        //edgeStyle.setDefaultVertexStyle(v);
        /*Map<String, Object> v = new HashMap<>();
        v.put(mxConstants.STYLE_ROUNDED, true);
        v.put(mxConstants.STYLE_ORTHOGONAL, false);
        v.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");

        v.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        v.put(mxConstants.STYLE_AUTOSIZE, true);
        v.put(mxConstants.STYLE_STROKEWIDTH, 20);
        v.put(mxConstants.STYLE_STROKECOLOR, "black"); // default is #6482B9

        */
        graph.setStylesheet(edgeStyle);
    }

    //Добавление узла графа
    public static Object addNode(mxGraph graph,String str, int x, int y){
        return graph.insertVertex(null, null, str, x, y, 60, 20);
    }
    //Добавление связей медлу узлами
    public static void addEdge(mxGraph graph, Graph n1, Graph n2){
        graph.insertEdge(null, null, null, n2.getObject(), n1.getObject());
    }
}
