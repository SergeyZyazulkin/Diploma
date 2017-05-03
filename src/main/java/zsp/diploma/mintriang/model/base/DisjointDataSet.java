package zsp.diploma.mintriang.model.base;

import java.util.Arrays;

public class DisjointDataSet {

    private static final int NO_PARENT = -1;

    private int[] parents;
    private int[] size;

    public DisjointDataSet(int size) {
        this.parents = new int[size];
        this.size = new int[size];
        Arrays.fill(this.parents, NO_PARENT);
        Arrays.fill(this.size, 1);
    }

    public int getSet(int index) {
        while (parents[index] != NO_PARENT) {
            index = parents[index];
        }

        return index;
    }

    public boolean isDifferent(int index1, int index2) {
        return getSet(index1) != getSet(index2);
    }

    public void merge(int index1, int index2) {
        int parent1 = getSet(index1);
        int parent2 = getSet(index2);

        if (parent1 != parent2) {
            if (size[parent1] >= size[parent2]) {
                parents[parent2] = parent1;
                size[parent1] += size[parent2];
            } else {
                parents[parent1] = parent2;
                size[parent2] += size[parent1];
            }
        }
    }
}
