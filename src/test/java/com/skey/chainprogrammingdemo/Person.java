package com.skey.chainprogrammingdemo;

/**
 * Description:
 * <br/>
 * Date: 2020/1/11 23:36
 *
 * @author ALion
 */
public class Person {
    String name;
    int age;
    String address;

    Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
