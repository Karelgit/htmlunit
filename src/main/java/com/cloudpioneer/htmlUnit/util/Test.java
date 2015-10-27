package com.cloudpioneer.htmlUnit.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Created by Administrator on 2015/10/27.
 */
public class Test {
    public static void main(String[] args) {
        Kryo kryo = new Kryo();
        Registration registration = kryo.register(Student.class);
//序列化
            Output output = null;
            output = new Output(4096);
            Student student = new Student("zhangsan", "man", 23);
            kryo.writeObject(output, student);
            byte[] bb = output.toBytes();
            output.flush();

//反序列化
            Input input = null;
            input = new Input(bb);
            Student s = (Student) kryo.readObject(input, registration.getType());
            System.out.println(s.getName()+","+s.getSex());
            input.close();
    }
}
