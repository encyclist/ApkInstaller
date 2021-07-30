package cn.erning.aabinstaller.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by wzj on 2016/9/9.
 */
public class UZipFile
{
    /**
     * 自定义添加文件生成zip包，zip包中没有目录结构
     * <p>
     * 用法：ZipUtil.zipFiles("/Users/hh/git/job/circle/tmp.zip",
     * "/Users/hh/git/job/circle/tmp/aa.txt",
     * "/Users/hh/Downloads/test_2.jpg",
     * "/Users/hh/git/job/circle/tmp/bb.txt");
     *
     * @param zipFileName 全路径的zip文件包的名字
     * @param files       要添加到zip包中的文件的全路径
     */
    public static void zipFiles(String zipFileName, String... files) {
        try {
            byte[] buffer = new byte[1024];
            File zipFile = new File(zipFileName);
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String file : files) {
                File file1 = new File(file);
                String fileName = file1.getName();
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                FileInputStream fis = new FileInputStream(file1);
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
            }
            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件夹
     *
     * @param zipFileName 全路径的zip文件包的名字
     * @param dir         要压缩的文件夹的路径
     */
    public static void zipDir(String zipFileName, String dir) {
        try {
            File zipFile = new File(zipFileName);
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
            File dirFile = new File(dir);
            addEntry(dirFile, zos, "");
            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addEntry(File file, ZipOutputStream zos, String root) throws IOException {
        byte[] buffer = new byte[1024];
        for (File file1 : file.listFiles()) {
            System.out.println(file1.getName());
            if (file1.isDirectory()) {
                addEntry(file1, zos, file1.getName());
            } else {
                String fileName = file1.getName();
                ZipEntry zipEntry = new ZipEntry(root + "/" + fileName);
                zos.putNextEntry(zipEntry);
                FileInputStream fis = new FileInputStream(file1);
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
            }
        }
    }
    /**
     * 解压到指定目录
     * @param zipPath
     * @param descDir
     * @author isea533
     */
    public static void unZipFiles(String zipPath,String descDir)throws IOException{
        unZipFiles(new File(zipPath), descDir);
    }
    /**
     * 解压文件到指定目录
     * @param zipFile
     * @param descDir
     * @author isea533
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile,String descDir)throws IOException{
        File pathFile = new File(descDir);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for(Enumeration entries = zip.entries();entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir+"/"+zipEntryName).replaceAll("\\\\", "/");;
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if(!file.exists()){
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if(new File(outPath).isDirectory()){
                continue;
            }
            //输出文件路径信息
            System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while((len=in.read(buf1))>0){
                out.write(buf1,0,len);
            }
            in.close();
            out.close();
        }
        System.out.println("******************解压完毕********************");
    }
}