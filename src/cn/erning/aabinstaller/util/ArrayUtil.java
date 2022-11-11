package cn.erning.aabinstaller.util;

import java.util.List;

public class ArrayUtil {
    public static <T> String[] list2Array(List<T> data){
        if(data == null || data.isEmpty()){
            return new String[0];
        }

        String[] result = new String[data.size()];
        for (int i=0;i<data.size();i++){
            result[i] = data.get(i).toString();
        }

        return result;
    }
}
