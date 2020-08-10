package com.jemsbond.jbgallery;

public class LocalAppSettings {

    private static int shape = R.id._rect;
    private static int theme = R.id._light;
    private static String orderBy = " ASC";

    public static int getShape() {
        return shape;
    }

    public static void setShape(int shape) {
        LocalAppSettings.shape = shape;
    }

    public static int getTheme() {
        return theme;
    }

    public static void setTheme(int theme) {
        LocalAppSettings.theme = theme;
    }

    public static String getOrderBy() {
        return orderBy;
    }

    public static void setOrderBy(String orderBy) {
        LocalAppSettings.orderBy = orderBy;
    }
}
