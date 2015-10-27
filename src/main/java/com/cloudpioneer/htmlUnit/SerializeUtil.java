package com.cloudpioneer.htmlUnit;

/**
 * @Description 序列化、反序列化一个对象
 * @author WangFei
 * @date 2014-11-6 上午8:30:39
 * @version
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2015/10/27.
 */

    public class SerializeUtil {
        public static final String TAG = "SerializeUtil";

        /**
         * 序列化
         *
         * @param object
         * @return
         * @throws IOException
         */
        public static byte[] serialize(Object object) throws IOException {
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                byte[] bytes = baos.toByteArray();
                return bytes;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println( "序列化对象发生错误，返回NULL");
                return null;
            }
        }

        /**
         * 反序列化
         *
         * @param bytes
         * @return
         */
        public static Object unserialize(byte[] bytes) {
            ByteArrayInputStream bais = null;
            try {
                // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("反序列化对象发生错误，返回NULL");
                return null;
            }
        }
    }

