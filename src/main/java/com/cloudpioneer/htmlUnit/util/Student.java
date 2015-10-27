package com.cloudpioneer.htmlUnit.util;

import java.io.Serializable;

    public class Student {
        private String name;
        private String sex;
        private int age;

        public Student() {
        }

        public Student(String name, String sex, int age) {
            super();
            this.name = name;
            this.sex = sex;
            this.age = age;
        }

        public String getName() {
            return name;
        }


        public String getSex() {
            return sex;
        }

        public int getAge() {
            return age;
        }
    }

