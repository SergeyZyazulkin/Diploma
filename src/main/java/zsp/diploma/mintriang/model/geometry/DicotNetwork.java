package zsp.diploma.mintriang.model.geometry;

import java.util.List;

public interface DicotNetwork {

    Vertex getSource();

    Vertex getSink();

    List<Vertex> getPart1();

    List<Vertex> getPart2();

    DicotNetwork setVisited(boolean visited);
}
