package zsp.diploma.mintriang.util;

public class Checker {

    public static boolean notNull(Object... objects) {
        if (objects != null) {
            for (Object object : objects) {
                if (object == null) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
