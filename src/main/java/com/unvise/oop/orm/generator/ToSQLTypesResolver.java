package com.unvise.oop.orm.generator;

public class ToSQLTypesResolver {
    public static String resolve(Class<?> clazz) {
        return switch (clazz.getSimpleName().trim().toLowerCase()) {
            case "string" -> "VARCHAR(255)";
            case "integer", "int" -> "INTEGER";
            case "long" -> "LONG";
            case "double" -> "FLOAT";
            case "date", "localdate", "localdatetime" -> "TIMESTAMP";
            default -> "";
        };
    }
}
