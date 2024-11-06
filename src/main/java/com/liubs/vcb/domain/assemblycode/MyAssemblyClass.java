package com.liubs.vcb.domain.assemblycode;

import com.liubs.vcb.entity.Result;
import com.liubs.vcb.util.ExceptionUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.ByteArrayInputStream;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class MyAssemblyClass {
    private ClassNode classNode;

    public MyAssemblyClass(byte[] bytes) {

        try ( ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            ClassReader reader = new ClassReader(inputStream);
            classNode = new ClassNode();
            reader.accept(classNode, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClassNode getClassNode() {
        return classNode;
    }


    public Result<byte[]> dumpBytes(){
        Result<byte[]> result = new Result<>();
        try{
            // 创建ClassWriter，自动设置栈帧信息
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            //校验字节码
            CheckClassAdapter checkAdapter = new CheckClassAdapter(classWriter, true);

            classNode.accept(checkAdapter);
            byte[] bytes = classWriter.toByteArray();

            result.setSuccess(true);
            result.setData(bytes);
        }catch (Throwable e){
            result.setSuccess(false);
            result.setErrorMessage(ExceptionUtil.getExceptionTracing(e));
        }

        return result;
    }

}
