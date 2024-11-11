package com.liubs.vcb.domain.assemblycode;

import org.objectweb.asm.ClassWriter;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2024/11/11
 */
public class MyClassWriter extends ClassWriter {

    private URLClassLoader urlClassLoader;

    public MyClassWriter(List<URL> urls, int flags) {
        super(flags);
        urlClassLoader = new URLClassLoader(urls.toArray(new URL[0]));  //每次都new一个，不污染环境
    }

    /**
     * 重载这里的classloader，父类的classloader是基于当前类的classloader，而当前类都是在PluginClassLoader中
     * @return
     */
    @Override
    protected ClassLoader getClassLoader() {
        return urlClassLoader;
    }
}
