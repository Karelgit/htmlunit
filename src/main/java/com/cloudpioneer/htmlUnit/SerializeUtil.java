package com.cloudpioneer.htmlUnit;

/**
 * @Description 序列化、反序列化一个对象
 * @author WangFei
 * @date 2014-11-6 上午8:30:39
 * @version
 */

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;

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
        Kryo kryo = new Kryo();
        Registration registration = kryo.register(HtmlPage.class);
        Output output = null;
        output = new Output(1,4096);
        kryo.writeObject(output, object);
        byte[] bb = output.toBytes();
        output.flush();
        return bb;
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        Kryo kryo = new Kryo();
        Registration registration = kryo.register(HtmlPage.class);
        Input input = null;
        input = new Input(bytes);
        HtmlPage hp = (HtmlPage) kryo.readObject(input, registration.getType());
        input.close();
        return hp;
    }
}

