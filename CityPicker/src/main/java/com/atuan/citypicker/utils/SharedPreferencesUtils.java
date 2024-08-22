package com.atuan.citypicker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
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
import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtils {
    private static MMKV mmkvKeep;//长期保存
    private static MMKV mmkvUser;//退出清除
    public static SharedPreferences sp;//退出登录便清除
    public static SharedPreferences spPersistence;//长期保存
    private final static String DATA_NAME = "cp_share_data";

    /**
     * 初始化sp
     *
     * @param context
     * @param spName
     */
    @Deprecated //全新应用 不初始化
    public static void init(Context context, String spName) {
        if (sp == null) {
            if (TextUtils.isEmpty(spName)) {
                spName = context.getPackageName();
            }
            sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
        if (spPersistence == null) {
            spPersistence = context.getSharedPreferences(context.getPackageName() + DATA_NAME, Context.MODE_PRIVATE);
        }
    }

    /**
     * 初始化mmkv
     *
     * @param context
     * @param spName
     */
    public static void initMMKV(Context context, String spName) {
        //初始化长期保存的数据
        MMKV.initialize(context.getApplicationContext());
        mmkvKeep = MMKV.defaultMMKV();
        if (spPersistence != null) { //判空 旧sp不使用初始化时
            mmkvKeep.importFromSharedPreferences(spPersistence);
        }
        //初始化退出登录的数据
        mmkvUser = MMKV.mmkvWithID("userConfig");
        if (sp != null) {
            sp = context.getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
            mmkvUser.importFromSharedPreferences(sp);
        }
    }

    /**
     * 保存SharedPreferences数据
     *
     * @param map 键值对，值必须是int，float,boolean,string,long类型，其余类型不支持
     */
    public static synchronized void save(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            save(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 保存用户的数据,退出需要清除的
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * 例子:SPUtils.save(this,"username","a123456789");
     * 例子:SPUtils.save(this,"password",123456);
     */
    public static void save(String key, Object object) {
        saveData(key, object, mmkvUser, sp);
    }

    /**
     * 使用原本sp的方法
     */
    public static void save(String key, Object object, boolean useSp) {
        saveData(key, object, null, sp);
    }

    /**
     * 保存长久数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * 例子:SPUtils.save(this,"username","a123456789");
     * 例子:SPUtils.save(this,"password",123456);
     */
    public static void savePersistence(String key, Object object) {
        saveData(key, object, mmkvKeep, spPersistence);
    }

    /**
     * 使用原本sp的方法
     */
    public static void savePersistence(String key, Object object, boolean useSp) {
        saveData(key, object, null, spPersistence);
    }

    private static void saveData(String key, Object object, MMKV mmkv, SharedPreferences sp) {
        if (object == null) {
            remove(key, mmkv, sp);
            return;
        }
        if (mmkv != null) {
            if (object instanceof String) {
                mmkv.encode(key, (String) object);
            } else if (object instanceof Integer) {
                mmkv.encode(key, (Integer) object);
            } else if (object instanceof Boolean) {
                mmkv.encode(key, (Boolean) object);
            } else if (object instanceof Float) {
                mmkv.encode(key, (Float) object);
            } else if (object instanceof Long) {
                mmkv.encode(key, (Long) object);
            } else if (object instanceof byte[]) {
                mmkv.encode(key, (byte[]) object);
            } else if (object instanceof Set) {
                mmkv.encode(key, (Set<String>) object);
            } else if (object instanceof Parcelable) {
                mmkv.encode(key, (Parcelable) object);
            } else if (object instanceof Serializable) {
                String str = objToString(object);
                mmkv.encode(key, str);
            } else {
                mmkv.encode(key, object.toString());
            }
        } else {
            SharedPreferences.Editor editor = sp.edit();
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
                save(key, object, sp);
            } else {
                editor.putString(key, object.toString());
            }
            editor.apply();
        }
    }

    /**
     * 返回mmkv中,Parcelable对应的反序列化
     * 例子:UserInfo userInfo = (UserInfo)SPUtils.get("userinfo", UserInfo.class);
     *
     * @param key    取值的key
     * @param tClass .class
     * @param <T>    返回的泛型,必须是parcelable的子类
     * @return 返回指定的泛型的值
     */
    public static <T extends Parcelable> T getPersistence(String key, Class<T> tClass) {
        if (mmkvKeep != null) {
            return mmkvKeep.decodeParcelable(key, tClass);
        } else {
            return null;
        }
    }

    /**
     * 返回mmkv中,Parcelable对应的反序列化
     * 例子:UserInfo userInfo = (UserInfo)SPUtils.get("userinfo", UserInfo.class);
     *
     * @param key    取值的key
     * @param tClass .class
     * @param <T>    返回的泛型,必须是parcelable的子类
     * @return 返回指定的泛型的值
     */
    public static <T extends Parcelable> T get(String key, Class<T> tClass) {
        if (mmkvUser != null) {
            return mmkvUser.decodeParcelable(key, tClass);
        } else {
            return null;
        }
    }

    /**
     * 得到长久数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * 例子:String username = SPUtils.get("username", "");
     * 例子:int max = SPUtils.get("username", 0);
     *
     * @param key    取值的key
     * @param object 默认值
     * @param <T>    返回的泛型
     * @return 返回指定的泛型的值
     */
    public static <T> T getPersistence(String key, Object object) {
        return getData(key, object, mmkvKeep, spPersistence);
    }

    /**
     * 得到用户数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * 例子:String username = SPUtils.get("username", "");
     * 例子:int max = SPUtils.get("username", 0);
     *
     * @param key    取值的key
     * @param object 默认值
     * @param <T>    返回的泛型
     * @return 返回指定的泛型的值
     */
    public static <T> T get(String key, Object object) {
        return getData(key, object, mmkvUser, sp);
    }

    @Nullable
    public static <T> T getData(String key, Object object, MMKV mmkv, SharedPreferences sp) {
        if (mmkv != null) {
            if (object == null) {
                return (T) mmkv.decodeString(key, null);
            } else if ("ObjectSerializable".equals(object)) {
                return (T) getObject(key, mmkv);
            } else if (object instanceof String) {
                return (T) mmkv.decodeString(key, (String) object);
            } else if (object instanceof Integer) {
                return (T) (Integer) mmkv.decodeInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                return (T) (Boolean) mmkv.decodeBool(key, (Boolean) object);
            } else if (object instanceof Float) {
                return (T) (Float) mmkv.decodeFloat(key, (Float) object);
            } else if (object instanceof Long) {
                return (T) (Long) mmkv.decodeLong(key, (Long) object);
            } else if (object instanceof byte[]) {
                return (T) mmkv.decodeBytes(key, (byte[]) object);
            } else if (object instanceof Set) {
                return (T) mmkv.decodeStringSet(key, (Set<String>) object);
            } else {
                return null;
            }
        } else {
            if (sp == null) return null;
            if (object == null) {
                return (T) sp.getString(key, null);
            } else if (object instanceof String) {
                return (T) sp.getString(key, (String) object);
            } else if (object instanceof Integer) {
                return (T) (Integer) sp.getInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                return (T) (Boolean) sp.getBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                return (T) (Float) sp.getFloat(key, (Float) object);
            } else if (object instanceof Long) {
                return (T) (Long) sp.getLong(key, (Long) object);
            } else if (object instanceof Serializable) {
                return (T) getObject(key, sp);
            } else {
                return null;
            }
        }
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean containsPersistence(String key) {
        if (mmkvKeep != null) {
            return mmkvKeep.contains(key);
        } else {
            return spPersistence != null && spPersistence.contains(key);
        }
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(String key) {
        if (mmkvUser != null) {
            return mmkvUser.contains(key);
        } else {
            return sp != null && sp.contains(key);
        }
    }

    /**
     * 移除用户数据某个key值已经对应的值
     */
    public static void removePersistence(String key) {
        if (mmkvKeep != null) {
            mmkvKeep.remove(key);
        }
        if (spPersistence != null) {
            SharedPreferences.Editor editor = spPersistence.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    /**
     * 移除用户数据某个key值已经对应的值
     */
    public static void remove(String key, MMKV mmkv, SharedPreferences sp) {
        if (mmkv != null) {
            mmkv.remove(key);
        }
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    /**
     * 移除用户数据某个key值已经对应的值
     */
    public static void remove(String key) {
        if (mmkvUser != null) {
            mmkvUser.remove(key);
        }
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
        if (mmkvKeep != null) {
            mmkvKeep.clear();
        }
        if (spPersistence != null) {
            SharedPreferences.Editor editor = spPersistence.edit();
            editor.clear();
            editor.apply();
        }
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        if (mmkvUser != null) {
            mmkvUser.clear();
        }
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
        }
    }

    /**
     * 清除所有数据
     */
    public static void clearSp() {
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
        }
        if (spPersistence != null) {
            SharedPreferences.Editor editor = spPersistence.edit();
            editor.clear();
            editor.apply();
        }
    }


    /**
     * 删除用户数据对应key的SharedPreferences数据
     */
    public static synchronized void remove(String[] keys) {
        if (sp != null) {
            for (String key : keys) {
                remove(key);
            }
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
     * 获取sp里面的obj
     */
    public static Object getObject(String key, MMKV mmkv) {
        if (mmkv.contains(key)) {
            String string = mmkv.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
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
            }
            else if (hex_char1 >= 'A' && hex_char1 <= 'F') {
                int_ch1 = (hex_char1 - 55) * 16; // // A 的Ascll
            }
            else {
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
     * 获取存储的字符型数据
     */
    @Deprecated
    public static String getString(String key) {
        return getData(key, "", mmkvUser, sp);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static String getString(String key, String str) {
        return getData(key, str, mmkvUser, sp);
    }

    /**
     * 获取存储的布尔型数据
     */
    @Deprecated
    public static boolean getBoolean(String key, boolean def) {
        return getData(key, def, mmkvUser, sp);
    }

    /**
     * 获取存储的布尔型数据
     */
    @Deprecated
    public static boolean getBoolean(String key) {
        return getData(key, false, mmkvUser, sp);
    }

    /**
     * 获取存储的int型数据
     */
    @Deprecated
    public static int getInt(String key) {
        return getData(key, -1, mmkvUser, sp);
    }

    /**
     * 获取存储的int型数据
     */
    @Deprecated
    public static int getInt(String key, int defaultInt) {
        return getData(key, defaultInt, mmkvUser, sp);
    }

    /**
     * 获取存储的Long型数据
     */
    @Deprecated
    public static long getLong(String key) {
        return getData(key, -1L, mmkvUser, sp);
    }

    /**
     * 获取存储的Long型数据
     */
    @Deprecated
    public static long getLong(String key, long defaultLong) {
        return getData(key, defaultLong, mmkvUser, sp);
    }

    /**
     * 获取sp里面的obj
     */
    public static Object getObject(String key) {
        return getData(key, "ObjectSerializable", mmkvUser, sp);
    }

    /**
     * 获取sp里面的obj
     */
    public static Object getPersistenceObject(String key) {
        return getData(key, "ObjectSerializable", mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static String getPersistenceString(String key) {
        return getData(key, "", mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的int型数据
     */
    @Deprecated
    public static int getPersistenceInt(String key) {
        return getData(key, -1, mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的int型数据
     */
    @Deprecated
    public static int getPersistenceInt(String key, int def) {
        return getData(key, def, mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static long getPersistenceLong(String key) {
        return getData(key, -1L, mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static long getPersistenceLong(String key, long def) {
        return getData(key, def, mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static boolean getPersistenceBoolean(String key) {
        return getData(key, false, mmkvKeep, spPersistence);
    }

    /**
     * 获取存储的字符型数据
     */
    @Deprecated
    public static boolean getPersistenceBoolean(String key, boolean def) {
        return getData(key, def, mmkvKeep, spPersistence);
    }
}
