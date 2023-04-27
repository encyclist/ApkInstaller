package cn.erning.aabinstaller.util;

import java.io.File;

/**
 * @author erning
 * @date 2021-06-10 16:28
 * des:
 */
public class FileUtil {
    public static void delete(File file){
        if (file.exists()){
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        delete(f);
                    }
                }
            }
            file.delete();
        }
    }

    public static String getSelfPath(){
        // 方法一：获取当前可执行jar包所在目录
        String filePath = System.getProperty("java.class.path");
        //得到当前操作系统的分隔符，windows下是";",linux下是":"
        String pathSplit = System.getProperty("path.separator");
        //若没有其他依赖，则filePath的结果应当是该可运行jar包的绝对路径，此时我们只需要经过字符串解析，便可得到jar所在目录
        if(filePath.contains(pathSplit)){
            filePath = filePath.substring(0,filePath.indexOf(pathSplit));
        }else if (filePath.endsWith(".jar")) {

            //截取路径中的jar包名,可执行jar包运行的结果里包含".jar"
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
        }
        System.out.println("jar包所在目录："+filePath);
        return filePath;
    }
}
