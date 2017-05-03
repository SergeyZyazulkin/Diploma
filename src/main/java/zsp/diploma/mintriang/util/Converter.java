package zsp.diploma.mintriang.util;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static <T> List<T> toUniqueList(List<T> source) {
        if (source != null) {
            return source.parallelStream().distinct().collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
