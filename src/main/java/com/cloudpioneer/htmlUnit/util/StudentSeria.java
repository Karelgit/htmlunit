package com.cloudpioneer.htmlUnit.util;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/28.
 */
public class StudentSeria  extends Student implements Serializable {
    private String name;
    public StudentSeria(String name)   {
        super();
    }

}
