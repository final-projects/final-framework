package org.finalframework.devops.java.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DynamicClassLoader extends ClassLoader {
    private final Map<String, BytesJavaFileObject> byteCodes = new HashMap<String, BytesJavaFileObject>();

    public DynamicClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public void registerCompiledSource(BytesJavaFileObject byteCode) {
        byteCodes.put(byteCode.getClassName(), byteCode);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        BytesJavaFileObject byteCode = byteCodes.get(name);
        if (byteCode == null) {
            return super.findClass(name);
        }

        return super.defineClass(name, byteCode.getByteCode(), 0, byteCode.getByteCode().length);
    }

    public Map<String, Class<?>> getClasses() throws ClassNotFoundException {
        Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
        for (BytesJavaFileObject byteCode : byteCodes.values()) {
            classes.put(byteCode.getClassName(), findClass(byteCode.getClassName()));
        }
        return classes;
    }

    public Map<String, byte[]> getByteCodes() {
        Map<String, byte[]> result = new HashMap<String, byte[]>(byteCodes.size());
        for (Entry<String, BytesJavaFileObject> entry : byteCodes.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getByteCode());
        }
        return result;
    }
}
