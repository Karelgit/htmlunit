package com.cloudpioneer.htmlUnit.TestKryo_v1;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.ObjectMap;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2015/10/30.
 */
public class JavaSerializer {
    public void write (Kryo kryo, Output output, Object object) throws Exception{
        ObjectMap graphContext = kryo.getGraphContext();
        ObjectOutputStream objectStream = (ObjectOutputStream)graphContext.get(this);
        if (objectStream == null) {
            objectStream = new ObjectOutputStream(output);
            graphContext.put(this, objectStream);
        }
        objectStream.writeObject(object);

        objectStream.flush();
    }

    public Object read (Kryo kryo, Input input, Class type) throws Exception{
        ObjectMap graphContext = kryo.getGraphContext();
        ObjectInputStream objectStream = (ObjectInputStream)graphContext.get(this);
        if (objectStream == null) {
            objectStream = new ObjectInputStream(input);
            graphContext.put(this, objectStream);
        }
        return objectStream.readObject();

    }
}
