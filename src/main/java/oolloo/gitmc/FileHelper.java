package oolloo.gitmc;

import com.mojang.brigadier.arguments.StringArgumentType;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FileHelper {
    public static List<File> findFiles(String path,String name){
        return findFiles(path,name,false);
    }
    public static List<File> findFiles(String path,String name,boolean findDir){
        List<File> fileList = new ArrayList<>();
        File file=new File(path);
        if (file.exists()){
            File[] files = file.listFiles();
            for (File file1:files){
                if (file1.isDirectory()){
                    if(findDir && file1.getName().equals(name)){
                        fileList.add(file1);
                    }
                    fileList.addAll(findFiles(file1.getPath(),name,findDir));
                }else {
                    if(!findDir && file1.getName().equals(name)){
                        fileList.add(file1);
                    }
                }
            }
        }
        return fileList;
    }
    public static List<File> findThisDir(String path,String name,boolean findDir){
        File file=new File(path);
        if (file.exists()){
            File[] files = file.listFiles((dir, fileName) -> fileName.equals(name)&(file.isDirectory()==findDir));
            return Arrays.asList(files);
        }
        return null;
    }

    public static String readText(File file) throws IOException {
        byte[] content = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(content);
        fis.close();
        return new String(content);
    }
    public static void writeText(File file,String text) throws IOException {
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text.getBytes());
        fos.flush();
        fos.close();
    }
}
