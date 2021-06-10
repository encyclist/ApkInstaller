package cn.erning.aabinstaller.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by wzj on 2016/9/9.
 */
public class UZipFile
{
    /**
     * 解压到指定目录
     */
    public static void unZipFiles(String zipPath,String descDir)throws IOException
    {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile,String descDir)throws IOException
    {
        File pathFile = new File(descDir);
        if(pathFile.exists() && pathFile.isDirectory())
        {
            FileUtil.delete(pathFile);
        }
        pathFile.mkdirs();
        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for(Enumeration entries = zip.entries(); entries.hasMoreElements();)
        {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            File file = new File(pathFile,zipEntryName);
            //判断路径是否存在,不存在则创建文件路径
            File parent = file.getParentFile();
            if(!parent.exists()){
                parent.mkdirs();
            }
            //输出文件路径信息
            System.out.println(file.getAbsolutePath());
            OutputStream out = new FileOutputStream(file);
            byte[] buf1 = new byte[1024];
            int len;
            while((len=in.read(buf1))>0)
            {
                out.write(buf1,0,len);
            }
            in.close();
            out.close();
        }
        System.out.println("******************解压完毕********************");
    }

    public static void main(String[] args) throws IOException {
        File zipFile = new File("C:\\Users\\B0582\\Desktop\\YouTube_v16.22.35_apkfab.com.xapk");
        String path = "C:\\Users\\B0582\\Desktop\\YouTube_v16.22.35_apkfab.com.xapk"+".f";
        unZipFiles(zipFile, path);
    }
}