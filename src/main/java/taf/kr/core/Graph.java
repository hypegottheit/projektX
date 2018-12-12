package taf.kr.core;

import java.util.List;

/**
 * Created by
 */
public interface Graph {
    Object getObject();
    void  setObject(Object object);
    String getName();
    List<Graph> getChild();
}