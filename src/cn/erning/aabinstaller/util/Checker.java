package cn.erning.aabinstaller.util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author erning
 * @date 2021-06-08 16:31
 * des:
 */
public class Checker {
    public static void checkFileExists(File file) throws FileNotFoundException {
        if(!file.exists()){
            throw new FileNotFoundException("文件:"+file.getAbsolutePath()+"不存在");
        }
    }
    public static void checkFileExists(File file,String name) throws FileNotFoundException {
        if(!file.exists()){
            throw new FileNotFoundException(name+"文件不存在");
        }
    }

    public static void checkAab(File file){
        String fileName = file.getName();
        if(!fileName.endsWith(".aab")){
            throw new IllegalArgumentException(file.getAbsolutePath()+"不是aab文件");
        }
    }
    public static void checkApks(File file){
        String fileName = file.getName();
        if(!fileName.endsWith(".apks")){
            throw new IllegalArgumentException(file.getAbsolutePath()+"不是apks文件");
        }
    }
    public static void checkApk(File file){
        String fileName = file.getName();
        if(!fileName.endsWith(".apk")){
            throw new IllegalArgumentException(file.getAbsolutePath()+"不是apk文件");
        }
    }

    public static String getFileName(String name){
        String[] names = name.split("\\.");
        if(names.length>0){
            return names[0];
        }else{
            throw new IllegalArgumentException("未找到文件名");
        }
    }

    public static void checkBlank(String content,String name){
        if (name == null || name.trim().isEmpty()){
            name = "值";
        }
        if (content == null || content.trim().isEmpty()){
            throw new IllegalArgumentException(name+"不能为空");
        }
    }
    public static void checkBlank(String content){
        checkBlank(content,null);
    }
}
