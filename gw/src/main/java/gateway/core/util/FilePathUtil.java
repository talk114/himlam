package gateway.core.util;

import java.util.Objects;

public final class FilePathUtil {

    public static String getAbsolutePath(Class<?> loader, String path){
        ClassLoader classLoader = loader.getClassLoader();
        return Objects.requireNonNull(classLoader.getResource(path).getFile());
    }
    public static String getAbsolutePath(String path) {
        ClassLoader classLoader = FilePathUtil.class.getClassLoader();
        return Objects.requireNonNull(classLoader.getResource(path).getFile());
    }

    private FilePathUtil(){}
}
