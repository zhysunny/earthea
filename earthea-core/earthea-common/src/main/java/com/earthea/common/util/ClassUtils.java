package com.earthea.common.util;

import com.earthea.common.exception.EartheaException;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射扫描包下面所有的类
 *
 * @author 章云
 * @date 2019/2/22 11:09
 */
public class ClassUtils {

    private static final String FILE = "file";
    private static final String JAR = "jar";

    /**
     * 反射扫描包下面所有的类
     *
     * @param packageName 包名
     * @return 类列表
     */
    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        URL url = ClassUtils.class.getClassLoader().getResource(packageName.replace(".", "/"));
        String protocol = url.getProtocol();
        if (FILE.equals(protocol)) {
            findClassLocal(packageName, classes);
        } else if (JAR.equals(protocol)) {
            findClassJar(packageName, classes);
        }
        return classes;
    }

    private static void findClassLocal(String packageName, List<Class<?>> classes) {
        URI uri;
        try {
            uri = ClassUtils.class.getClassLoader().getResource(packageName.replace(".", "/")).toURI();
        } catch (URISyntaxException e) {
            throw new EartheaException(e);
        }
        File path = new File(uri);
        findAndAddClasses(packageName, path, classes);
    }

    private static void findAndAddClasses(String packageName, File dir, List<Class<?>> classes) {
        File[] listFiles = dir.listFiles(file -> (file.isDirectory()) || (file.getName().endsWith(".class")));
        for (File file : listFiles) {
            if (file.isDirectory()) {
                findAndAddClasses(packageName + "." + file.getName(), file, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> cls = Class.forName(packageName + "." + className);
                    if (!cls.getSimpleName().isEmpty()) {
                        classes.add(cls);
                    }
                } catch (ClassNotFoundException e) {
                    throw new EartheaException(e);
                }

            }
        }
    }

    private static void findClassJar(String packageName, List<Class<?>> classes) {
        packageName = packageName.replace(".", "/");
        URL url = ClassUtils.class.getClassLoader().getResource(packageName);
        JarFile jarFile;
        try {
            JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
            jarFile = jarUrlConnection.getJarFile();
        } catch (IOException e) {
            throw new EartheaException(e);
        }
        //这里获得jar包里面所有目录和文件
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        JarEntry jarEntry;
        String jarEntryName;
        while (jarEntries.hasMoreElements()) {
            jarEntry = jarEntries.nextElement();
            jarEntryName = jarEntry.getName();
            // jarEntryName的目录结构都是/
            if (jarEntryName.contains(packageName) && !jarEntryName.endsWith("/")) {
                if (jarEntryName.endsWith(".class")) {
                    String className = jarEntryName.substring(0, jarEntryName.length() - 6);
                    try {
                        Class<?> cls = Class.forName(className.replace("/", "."));
                        if (!cls.getSimpleName().isEmpty()) {
                            classes.add(cls);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new EartheaException(e);
                    }
                }
            }
        }
    }
}
