package cn.erning.aabinstaller.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author erning
 * @date 2021-06-10 10:05
 * des:
 */
public class PropertiesUtil {
    public static final String KEY_APK_PATH = "apkPath";
    public static final String KEY_JKS_PATH = "jksPath";
    public static final String KEY_JKS_PASS = "jksPass";
    public static final String KEY_JKS_ALIAS = "jksAlias";
    public static final String KEY_JKS_ALIAS_PASS = "jksAliasPass";

    public static Map<String, String> read() {
        Map<String,String> map = new HashMap<>();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("apkinstaller.properties"));

            Properties prop = new Properties();
            prop.load(in);     ///加载属性列表
            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);
                map.put(key,value);
                System.out.println(key+"--->"+value);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    public static String get(String key) {
        return read().get(key);
    }

    public static void put(String key,String value){
        try {
            Properties prop = new Properties();
            ///保存属性到b.properties文件
            prop.setProperty(key, value);

            FileOutputStream oFile = new FileOutputStream("apkinstaller.properties", true);//true表示追加打开
            prop.store(oFile,null);
            oFile.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
