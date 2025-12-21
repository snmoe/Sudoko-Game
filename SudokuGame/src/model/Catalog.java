package model;

public class Catalog {

    private static boolean current;
    private static boolean allModesExist;

    public static boolean isCurrent() {
        return current;
    }

    public static void setCurrent(boolean curr) {
        current = curr;
    }

    public static boolean isAllModesExist() {
        return allModesExist;
    }

    public static void setAllModesExist(boolean Exist) {
        allModesExist = Exist;
    }
    
    
}
