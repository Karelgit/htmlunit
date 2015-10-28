package com.cloudpioneer.htmlUnit.util;

import java.io.Serializable;

    public class Student extends People {
        private String name;
        private String sex;
        private Integer age;

        public Family getFamily() {
            return family;
        }

        private Family family;

        public Student() {
        }

        public Student(String name, String sex, int age,Family f) {
            super();
            this.name = name;
            this.sex = sex;
            this.age = age;
            this.family = f;
        }

        public String getName() {
            return name;
        }


        public String getSex() {
            return sex;
        }

        public Integer getAge() {
            return age;
        }
    }

