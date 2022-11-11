package cn.erning.aabinstaller.util;

import cn.erning.aabinstaller.entity.Device;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.tools.build.bundletool.BundleToolMain;
import com.android.tools.build.bundletool.device.DdmlibAdbServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author erning
 * @date 2021-06-08 16:17
 * des:
 */
public class Installer {
    /**
     * aab转apks
     */
    public static void buildApks(String aabPath,String jksPath,String jksPass,String jksAlias,String jksAliasPass) throws Exception{
        File aab = new File(aabPath);
        File jks = new File(jksPath);

        Checker.checkFileExists(aab,"aab");
        Checker.checkFileExists(jks,"jks");
        Checker.checkAab(aab);
        Checker.checkBlank(jksPass,"jksPass");
        Checker.checkBlank(jksAlias,"jksAlias");
        Checker.checkBlank(jksAliasPass,"jksAliasPass");

        File apks = new File(aab.getParentFile(),Checker.getFileName(aab.getName())+".apks");

        String[] args = new String[7];
        args[0] = "build-apks";
        args[1] = "--bundle="+aabPath;
        args[2] = "--output="+apks.getAbsolutePath();
        args[3] = "--ks="+jksPath;
        args[4] = "--ks-pass=pass:"+jksPass;
        args[5] = "--ks-key-alias="+jksAlias;
        args[6] = "--key-pass=pass:"+jksAliasPass;

        printArgs(args);

        BundleToolMain.main(args);
    }

    /**
     * 安装apks
     */
    public static void installApks(String apksPath,Device device) throws Exception{
        File apks = new File(apksPath);

        Checker.checkFileExists(apks,"apks");
        Checker.checkApks(apks);

        String adbPath = getAdbPath();

        String[] args;
        if(adbPath!=null && device!=null){
            args = new String[4];
            args[2] = "--adb="+adbPath+"";
            args[3] = "--device-id="+device.getId();
        }else if(adbPath != null){
            args = new String[3];
            args[2] = "--adb="+adbPath+"";
        }else if(device!=null){
            args = new String[3];
            args[2] = "--device-id="+device.getId();
        }else{
            args = new String[2];
        }
        args[0] = "install-apks";
        args[1] = "--apks="+apksPath;

        printArgs(args);

        // 这货在用完ADB后会关闭，导致不能继续用
        BundleToolMain.main(args);
    }

    /**
     * 安装apk
     */
    public static void installApk(String apksPath,Device device) throws Exception{
        File apk = new File(apksPath);
        Checker.checkFileExists(apk,"apk");
        Checker.checkApk(apk);

        String adbPath = getAdbPath();

        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(Objects.requireNonNullElse(adbPath, "adb"));
        if(device != null){
            cmd.add("-s");
            cmd.add(device.getId());
        }
        cmd.add("install");
        cmd.add(apksPath);

        executeCmd(cmd);
    }

    /**
     * 安装xapk
     */
    public static void installXapk(String apksPath,Device device) throws Exception{
        File apk = new File(apksPath);
        Checker.checkFileExists(apk,"xapk");
        Checker.checkXapk(apk);

        // 临时解压文件夹
        String path = apk.getAbsolutePath()+".f";
        UZipFile.unZipFiles(apk, path);

        String adbPath = getAdbPath();

        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(Objects.requireNonNullElse(adbPath, "adb"));
        if(device != null){
            cmd.add("-s");
            cmd.add(device.getId());
        }
        cmd.add("install-multiple");
        File dir = new File(path);
        File[] apks = dir.listFiles();
        if(apks != null){
            for (File f:apks){
                if(f.getName().toLowerCase(Locale.ENGLISH).endsWith(".apk")){
                    cmd.add(f.getAbsolutePath());
                }
            }
        }

        executeCmd(cmd);
        // 复制obb文件夹
        copyObb(dir,device);
        // 删除临时文件
        FileUtil.delete(dir);
    }

    /**
     * 安装xapk后复制obb文件
     * @param dir 本地解压的临时文件夹
     */
    private static void copyObb(File dir,Device device) throws Exception{
        File obb = new File(dir,"Android/obb");
        if(!obb.exists()){
            // 没有obb文件
            return;
        }

        String adbPath = getAdbPath();

        File[] obbs = obb.listFiles();
        if(obbs != null){
            for (File oneObbDir:obbs){
                ArrayList<String> cmd = new ArrayList<>();
                cmd.add(Objects.requireNonNullElse(adbPath, "adb"));
                if(device != null){
                    cmd.add("-s");
                    cmd.add(device.getId());
                }
                cmd.add("push");
                cmd.add(oneObbDir.getAbsolutePath());
                cmd.add("/sdcard/Android/obb/"+oneObbDir.getName());

                executeCmd(cmd);
            }
        }
    }

    private static void executeCmd(String cmd) throws Exception{
        System.out.println("执行命令：");
        System.out.println(cmd);
        Runtime run = Runtime.getRuntime();
        Process p = run.exec(cmd);
        InputStream ins= p.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));
        while (true){
            String line = bufferedReader.readLine();
            if (line == null){
                break;
            }
            System.out.println(line);
        }
        p.destroy();
    }

    private static void executeCmd(List<String> cmd) throws Exception{
        String[] cmdList = ArrayUtil.list2Array(cmd);
        System.out.println("执行命令：");
        System.out.println(Arrays.toString(cmdList));
        Runtime run = Runtime.getRuntime();
        Process p = run.exec(cmdList);
        InputStream ins= p.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));
        while (true){
            String line = bufferedReader.readLine();
            if (line == null){
                break;
            }
            System.out.println(line);
        }
        p.destroy();
    }

    /**
     * BundleToolMain 用完ADB后会关闭
     * 想个法子给他设置成未初始化状态，下次用的时候它会重新初始化
     * 需要将DdmlibAdbServer的state改为DdmlibAdbServer.State.UNINITIALIZED
     * 需要将AndroidDebugBridge的sThis改为null
     */
    public static void resetAdbServer() {
        try {
            Class<?> clz = Class.forName("com.android.tools.build.bundletool.device.DdmlibAdbServer$State");
            Object[] objects = clz.getEnumConstants();
            Object UNINITIALIZED = null;
            for (Object obj : objects){
                if(obj.toString().equals("UNINITIALIZED")){
                    UNINITIALIZED = obj;
                    break;
                }
            }
            if(UNINITIALIZED != null){
                System.out.println("已找到com.android.tools.build.bundletool.device.DdmlibAdbServer.State.UNINITIALIZED");
                Class<? extends DdmlibAdbServer> ddmlibAdbServer = DdmlibAdbServer.getInstance().getClass();
                Field state = ddmlibAdbServer.getDeclaredField("state");
                state.setAccessible(true);
                state.set(DdmlibAdbServer.getInstance(), UNINITIALIZED);
                state.setAccessible(false);
                System.out.println("已将DdmlibAdbServer.state设置为UNINITIALIZED");
            }else{
                System.out.println("未找到com.android.tools.build.bundletool.device.DdmlibAdbServer.State.UNINITIALIZED");
            }

            Field sInitialized = AndroidDebugBridge.class.getDeclaredField("sThis");
            sInitialized.setAccessible(true);
            sInitialized.set(null,null);
            sInitialized.setAccessible(false);
            System.out.println("已将AndroidDebugBridge.sThis设置为null");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打印将要执行的指令，使用bundletool时才有效
     */
    private static void printArgs(String[] args){
        if(args == null){
            System.out.println("args is null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nexecute:\n");
        sb.append("java -jar bundletool-1.2.0.jar");
        for (String s:args){
            sb.append(" ").append(s);
        }
        sb.append("\n");
        System.out.println(sb.toString());
    }

    /**
     * 获取已连接的adb设备列表
     * 使用cmd执行 adb的devices指令
     */
    public static Device[] getDevices(){
        try {
            String adbPath = getAdbPath();

            ArrayList<String> cmd = new ArrayList<>();
            cmd.add(Objects.requireNonNullElse(adbPath, "adb"));
            cmd.add("devices");
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(ArrayUtil.list2Array(cmd));
            InputStream ins= p.getInputStream();
            byte[] bytes = new byte[1024];
            ins.read(bytes);
            System.out.println("设备列表：");
            System.out.println(new String(bytes));
            p.destroy();
            return parsingDevices(new String(bytes));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Device[0];
    }

    /**
     * 解析设备列表
     */
    private static Device[] parsingDevices(String content){
        if(content == null || content.isEmpty()){
            return new Device[0];
        }
        String[] lines = content.split("\n");
        if(lines.length <= 1){
            return new Device[0];
        }
        Device[] devices = new Device[lines.length-1];
        for (int i=1;i< lines.length;i++){
            String line = lines[i];
            devices[i-1] = new Device(line);
        }
        int count = 0;
        for (Device device :devices){
            if (!device.isNull()){
                count++;
            }
        }
        Device[] newDevices = new Device[count];
        int index = 0;
        for (Device device : devices) {
            if (!device.isNull()) {
                newDevices[index++] = device;
            }
        }
        return newDevices;
    }

    private static String getAdbPath(){
        String adbPath;
        if(System.getProperties().getProperty("os.name").toLowerCase().startsWith("windows")){
            adbPath = new File("").getAbsolutePath()+"\\adb.exe";
        }else{
            adbPath = new File("").getAbsolutePath()+"/adb";
        }
        boolean hasAdb = new File(adbPath).exists();
        System.out.println("ADB文件路径："+adbPath);
        if(!hasAdb){
            System.out.println("ADB文件不存在，请在此jar文件目录下运行-jar命令，如环境变量中有ADB则可以忽略");
        }
        return hasAdb ? adbPath : null;
    }
}
