package zsp.diploma.mintriang.model.geometry.impl;

import zsp.diploma.mintriang.model.geometry.DicotNetwork;
import zsp.diploma.mintriang.model.geometry.Vertex;

import java.util.ArrayList;
import java.util.List;

public class DicotNetworkImpl implements DicotNetwork {

    private Vertex source;
    private Vertex sink;
    private List<Vertex> part1;
    private List<Vertex> part2;

    public DicotNetworkImpl(Vertex source, Vertex sink, List<Vertex> part1, List<Vertex> part2) {
        this.source = source;
        this.sink = sink;
        this.part1 = part1;
        this.part2 = part2;
    }

    @Override
    public Vertex getSource() {
        return source;
    }

    @Override
    public Vertex getSink() {
        return sink;
    }

    @Override
    public List<Vertex> getPart1() {
        return part1;
    }

    @Override
    public List<Vertex> getPart2() {
        return part2;
    }

    @Override
    public DicotNetwork setVisited(boolean visited) {
        source.setVisited(visited);
        sink.setVisited(visited);
        part1.parallelStream().forEach(v -> v.setVisited(visited));
        part2.parallelStream().forEach(v -> v.setVisited(visited));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DicotNetworkImpl that = (DicotNetworkImpl) o;

        if (source != null ? !source.equals(that.source) : that.source != null) {
            return false;
        }
        if (sink != null ? !sink.equals(that.sink) : that.sink != null) {
            return false;
        }
        if (part1 != null ? !part1.equals(that.part1) : that.part1 != null) {
            return false;
        }
        return part2 != null ? part2.equals(that.part2) : that.part2 == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (sink != null ? sink.hashCode() : 0);
        result = 31 * result + (part1 != null ? part1.hashCode() : 0);
        result = 31 * result + (part2 != null ? part2.hashCode() : 0);
        return result;
    }
}
