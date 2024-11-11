package com.liubs.vcb.domain.assemblycode;

import com.liubs.vcb.entity.Result;
import com.liubs.vcb.util.ExceptionUtil;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class MyAssemblyClass {
    private ClassNode classNode;
    private JavaClass javaClass;

    public MyAssemblyClass(byte[] bytes) {

        try ( ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            ClassReader reader = new ClassReader(inputStream);
            classNode = new ClassNode();
            reader.accept(classNode, 0);

            ClassParser parser = new ClassParser(new ByteArrayInputStream(bytes), classNode.name);
            javaClass = parser.parse();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConstantPool getConstantPool() {
        return javaClass.getConstantPool();
    }

    public ClassNode getClassNode() {
        return classNode;
    }


    public Result<byte[]> dumpBytes(List<URL> dependentURLs){
        Result<byte[]> result = new Result<>();
        try{
            // 创建ClassWriter，自动设置栈帧信息
//            ClassWriter classWriter = new ClassWriter( ClassWriter.COMPUTE_FRAMES );
            MyClassWriter classWriter = new MyClassWriter(dependentURLs, ClassWriter.COMPUTE_FRAMES );

            //校验字节码
            CheckClassAdapter checkAdapter = new CheckClassAdapter(classWriter, true);
            classNode.accept(checkAdapter);

            byte[] bytes = classWriter.toByteArray();
            result.setSuccess(true);
            result.setData(bytes);
        }catch (Throwable e){
            e.printStackTrace();
            result.setSuccess(false);

            if(e instanceof TypeNotPresentException) {
                if(e.getCause() instanceof ClassNotFoundException) {
                    result.setErrorMessage("Class not found:"+e.getCause().getMessage());
                }else {
                    result.setErrorMessage(e.getMessage());
                }
            }else {
                result.setErrorMessage(ExceptionUtil.getExceptionTracing(e));
            }
        }

        return result;
    }

    public Result<byte[]> saveConstPoolToClass(){
        Result<byte[]> result = new Result<>();

        try {
            ClassGen classGen = new ClassGen(javaClass);
            classGen.setConstantPool(new ConstantPoolGen(getConstantPool()));
            javaClass = classGen.getJavaClass();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            javaClass.dump(byteArrayOutputStream);
            result.setSuccess(true);
            result.setData(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErrorMessage(ExceptionUtil.getExceptionTracing(e));
        }

        return result;
    }

}
