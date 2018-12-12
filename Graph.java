package sample;

import java.util.List;

/**
 * Created by Alexey on 25.11.2016.
 */
public interface Graph {
    Object getObject();
    void  setObject(Object object);
    String getName();
    List<Graph> getChild();
}