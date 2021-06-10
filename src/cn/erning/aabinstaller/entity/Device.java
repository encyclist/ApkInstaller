package cn.erning.aabinstaller.entity;

/**
 * @author erning
 * @date 2021-06-09 16:29
 * des: 代表一个已连接到ADB的安卓设备
 */
public class Device {
    private String id;
    private String name;
    private String str;

    public Device(String str) {
        if(str == null){
            return;
        }
        str = str.replaceAll("\r","").replaceAll("\n","").replaceAll("\u0000","");
        if(str.isEmpty()){
            return;
        }
        System.out.println("添加设备："+str);
        String[] strs = str.split("\t");
        if(strs.length != 2){
            return;
        }
        id = strs[0];
        name = strs[1];
        this.str = str.replace("\t","    ");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isNull(){
        return id == null;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
