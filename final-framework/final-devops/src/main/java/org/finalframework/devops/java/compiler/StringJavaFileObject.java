package org.finalframework.devops.java.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/21 23:31:09
 * @since 1.0
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    private final String contents;

    public StringJavaFileObject(String className, String contents) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = contents;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }

}
