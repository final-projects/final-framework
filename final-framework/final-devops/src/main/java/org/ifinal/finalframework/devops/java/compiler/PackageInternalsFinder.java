package org.ifinal.finalframework.devops.java.compiler;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;

public class PackageInternalsFinder {
    private static final String CLASS_FILE_EXTENSION = ".class";
    private final ClassLoader classLoader;

    public PackageInternalsFinder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public List<JavaFileObject> find(String packageName) throws IOException {
        String javaPackageName = packageName.replace("\\.", "/");

        List<JavaFileObject> result = new ArrayList<>();

        Enumeration<URL> urlEnumeration = classLoader.getResources(javaPackageName);
        while (urlEnumeration.hasMoreElements()) { // one URL for each jar on the classpath that has the given package
            URL packageFolderURL = urlEnumeration.nextElement();
            result.addAll(listUnder(packageName, packageFolderURL));
        }

        return result;
    }

    private Collection<JavaFileObject> listUnder(String packageName, URL packageFolderURL) {
        File directory = new File(packageFolderURL.getFile());
        if (directory.isDirectory()) { // browse local .class files - useful for local execution
            return processDir(packageName, directory);
        } else { // browse a jar file
            return processJar(packageFolderURL);
        } // maybe there can be something else for more involved class loaders
    }

    private List<JavaFileObject> processJar(URL packageFolderURL) {
        List<JavaFileObject> result = new ArrayList<>();
        try {
            String jarUri = packageFolderURL.toExternalForm().substring(0, packageFolderURL.toExternalForm().lastIndexOf("!/"));

            JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
            String rootEntryName = jarConn.getEntryName();
            int rootEnd = rootEntryName.length() + 1;

            Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
            while (entryEnum.hasMoreElements()) {
                JarEntry jarEntry = entryEnum.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_FILE_EXTENSION)) {
                    URI uri = URI.create(jarUri + "!/" + name);
                    String binaryName = name.replace("/", ".");
                    binaryName = binaryName.replace(CLASS_FILE_EXTENSION + "$", "");

                    result.add(new CustomJavaFileObject(binaryName, uri));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
        }
        return result;
    }

    private List<JavaFileObject> processDir(String packageName, File directory) {
        List<JavaFileObject> result = new ArrayList<>();
        File[] childFiles = directory.listFiles();

        if (Objects.isNull(childFiles)) {
            return result;
        }

        for (File childFile : childFiles) {

            if (!childFile.isFile() || !childFile.getName().endsWith(CLASS_FILE_EXTENSION)) {
                continue;
            }

            // We only want the .class files.
            String binaryName = packageName + "." + childFile.getName();
            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");

            result.add(new CustomJavaFileObject(binaryName, childFile.toURI()));
        }

        return result;
    }
}
