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
}
