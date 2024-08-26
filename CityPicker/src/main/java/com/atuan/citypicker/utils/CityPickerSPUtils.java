package com.atuan.citypicker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public class CityPickerSPUtils {

    public static SharedPreferences spPersistence;
    private final static String DATA_NAME = "cp_share_data";

    /**
     * 初始化sp
     *
     * @param context
     */
    public static void init(Context context) {
        if (spPersistence == null) {
            spPersistence = context.getSharedPreferences(context.getPackageName() + DATA_NAME, Context.MODE_PRIVATE);
        }
    }

    public static void saveData(String key, Object object) {
        if (object == null) {
            remove(key, spPersistence);
            return;
        }
        SharedPreferences.Editor editor = spPersistence.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Serializable) {
            save(key, object, spPersistence);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    @Nullable
    public static <T> T getData(String key, Object object) {
        if (spPersistence == null) return null;
        if (object == null) {
            return (T) spPersistence.getString(key, null);
        } else if (object instanceof String) {
            return (T) spPersistence.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return (T) (Integer) spPersistence.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return (T) (Boolean) spPersistence.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return (T) (Float) spPersistence.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return (T) (Long) spPersistence.getLong(key, (Long) object);
        } else if (object instanceof Serializable) {
            return (T) getObject(key, spPersistence);
        } else {
            return null;
        }
    }

    /**
     * 移除用户数据某个key值已经对应的值
     */
    public static void remove(String key, SharedPreferences sp) {
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    /**
     * 清除所有数据
     */
    public static void clearPersistence() {
        if (spPersistence != null) {
            SharedPreferences.Editor editor = spPersistence.edit();
            editor.clear();
            editor.apply();
        }
    }

    /**
     * 获取sp里面的obj
     */
    public static Object getObject(String key, SharedPreferences sharedPreferences) {
        if (sharedPreferences.contains(key)) {
            String string = sharedPreferences.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {

            }
            try {
                // 将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                // 返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 所有异常返回null
        return null;
    }

    /**
     * sp保存对象  要保存的对象，只能保存实现了serializable的对象
     */
    public static String objToString(Object obj) {
        try {
            // 先将序列化结果写到byte缓存中，其实就分配一个内存空间
            if (obj != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                // 将对象序列化写入byte缓存
                os.writeObject(obj);
                //刷新流
                os.flush();
                //关闭流
                os.close();
                // 将序列化的数据转为16进制保存
                String bytesToHexString = bytesToHexString(bos.toByteArray());
                // 保存该16进制数组
                return bytesToHexString;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * sp保存对象  要保存的对象，只能保存实现了serializable的对象
     */
    public static void save(String key, Object obj, SharedPreferences sharedPreferences) {
        try {
            // 先将序列化结果写到byte缓存中，其实就分配一个内存空间
            if (obj != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                // 将对象序列化写入byte缓存
                os.writeObject(obj);
                //刷新流
                os.flush();
                //关闭流
                os.close();
                // 将序列化的数据转为16进制保存
                String bytesToHexString = bytesToHexString(bos.toByteArray());
                // 保存该16进制数组
                sharedPreferences.edit().putString(key, bytesToHexString).commit();
            } else {
                sharedPreferences.edit().putString(key, "").commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * desc:将数组转为16进制
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        String bytesToHexString = sb.toString();
        return bytesToHexString;
    }

    /**
     * 将16进制的数据转为数组
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch; // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); // //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9') {
                int_ch1 = (hex_char1 - 48) * 16; // // 0 的Ascll
            } else if (hex_char1 >= 'A' && hex_char1 <= 'F') {
                int_ch1 = (hex_char1 - 55) * 16; // // A 的Ascll
            } else {
                return null;
            }
            i++;
            char hex_char2 = hexString.charAt(i); // /两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9') {
                int_ch2 = (hex_char2 - 48); // // 0 的Ascll - 48
            } else if (hex_char2 >= 'A' && hex_char2 <= 'F') {
                int_ch2 = hex_char2 - 55; // // A 的Ascll - 65
            } else {
                return null;
            }
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;// 将转化后的数放入Byte里
        }
        return retData;
    }

    /**
     * 获取sp里面的obj
     */
    public static Object getPersistenceObject(String key) {
        return getData(key, "ObjectSerializable");
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static String getPersistenceString(String key) {
        return getData(key, "");
    }

    /**
     * 获取存储的int型数据
     */
    @Deprecated
    public static int getPersistenceInt(String key) {
        return getData(key, -1);
    }

    /**
     * 获取存储的int型数据
     */
    @Deprecated
    public static int getPersistenceInt(String key, int def) {
        return getData(key, def);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static long getPersistenceLong(String key) {
        return getData(key, -1L);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static long getPersistenceLong(String key, long def) {
        return getData(key, def);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static boolean getPersistenceBoolean(String key) {
        return getData(key, false);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static boolean getPersistenceBoolean(String key, boolean def) {
        return getData(key, def);
    }
}
