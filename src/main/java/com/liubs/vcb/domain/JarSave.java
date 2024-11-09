package com.liubs.vcb.domain;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.liubs.vcb.entity.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;

/**
 * @author Liubsyy
 * @date 2024/11/9
 */
public class JarSave {
    private VirtualFile virtualFile;

    public JarSave(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public Result<String> saveTemp(byte[] bytes){
        String filePath = virtualFile.getPath();

        Result<String> result = new Result<>();
        // 分离 jar 文件路径和相对路径（使用 .jar!）
        if (!filePath.contains(".jar!")) {
            result.setErrorMessage("Not jar!");
            return result;
        }
        String[] parts = filePath.split(".jar!");
        String jarPath = parts[0] + ".jar";
        String jarRelativePath = parts[1].substring(1);

        VirtualFile jarRoot = VirtualFileManager.getInstance().findFileByUrl("jar://" + jarPath + "!/");
        VirtualFile jarFile = jarRoot.findFileByRelativePath(jarRelativePath);
        if (jarFile == null) {
            result.setErrorMessage("Could not find file inside the jar.");
            return result;
        }

        // 选择目标目录
        String destinationDirectory = parts[0]+"_temp/jar_edit_out";

        // 将 jar 文件内的文件复制到目标目录
        try {
            String destinationPath = Paths.get(destinationDirectory, jarRelativePath).toString();
            File destinationFile = new File(destinationPath);
            destinationFile.getParentFile().mkdirs();

            Files.write(Paths.get(destinationPath),bytes);

            result.setSuccess(true);
            result.setData(destinationPath);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setErrorMessage(ex.getMessage());
        }

        return result;
    }


    public Result<String> updateJar(byte[] bytes){

        String filePath = virtualFile.getPath();

        Result<String> result = new Result<>();
        // 分离 jar 文件路径和相对路径（使用 .jar!）
        if (!filePath.contains(".jar!")) {
            result.setErrorMessage("Not jar!");
            return result;
        }
        String[] parts = filePath.split(".jar!");
        String jarPath = parts[0] + ".jar";
        String jarRelativePath = parts[1].substring(1);

        try{
            File tempJarFile = Files.createTempFile("tempJar", ".jar").toFile();

            try (JarFile originalJar = new JarFile(jarPath);
                 JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tempJarFile))) {

                Enumeration<JarEntry> entries = originalJar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if(entry.getName().equals(jarRelativePath)) {
                        JarEntry newEntry = new JarEntry(entry.getName());
                        tempJarOutputStream.putNextEntry(newEntry);
                        tempJarOutputStream.write(bytes);
                        tempJarOutputStream.closeEntry();
                    }else {
                        JarEntry newEntry = copyNewEntry(originalJar,entry,entry.getName());
                        tempJarOutputStream.putNextEntry(newEntry);

                        try (InputStream entryInputStream = originalJar.getInputStream(entry)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = entryInputStream.read(buffer)) != -1) {
                                tempJarOutputStream.write(buffer, 0, bytesRead);
                            }
                        }

                        tempJarOutputStream.closeEntry();
                    }

                }
            }

            writeTargetJar(jarPath,tempJarFile);

            result.setSuccess(true);
            result.setData(jarPath);

        }catch (Exception ex){
            ex.printStackTrace();
            result.setErrorMessage(ex.getMessage());
        }

        return result;
    }


    public JarEntry copyNewEntry(JarFile originalJar,JarEntry entry, String newEntryName) throws IOException {

        JarEntry newEntry = new JarEntry(newEntryName);
        newEntry.setTime(entry.getTime());
        // 如果原条目使用 STORED 方法，需要显式设置大小、压缩大小和 CRC-32
        if (entry.getMethod() == JarEntry.STORED) {
            long size = entry.getSize();
            long compressedSize = entry.getCompressedSize();
            long crc = entry.getCrc();

            if (size == -1 || compressedSize == -1 || crc == -1) {
                CRC32 crc32 = new CRC32();
                long computedSize = 0;

                try (InputStream entryInputStream = originalJar.getInputStream(entry)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = entryInputStream.read(buffer)) != -1) {
                        crc32.update(buffer, 0, bytesRead);
                        computedSize += bytesRead;
                    }
                }

                size = computedSize;
                compressedSize = computedSize;
                crc = crc32.getValue();
            }

            newEntry.setSize(size);
            newEntry.setCompressedSize(compressedSize);
            newEntry.setCrc(crc);
            newEntry.setMethod(JarEntry.STORED);
        } else {
            newEntry.setMethod(JarEntry.DEFLATED);
        }
        return newEntry;
    }

    public void writeTargetJar(String jarFile,File tempJarFile) throws IOException {
        // 将临时 JAR 文件内容写回目标 JAR 文件
        try (FileInputStream tempInputStream = new FileInputStream(tempJarFile);
             FileOutputStream fileOutputStream = new FileOutputStream(jarFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = tempInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }


}
