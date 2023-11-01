package org.tune.one;

import java.io.Serializable;
import java.util.Optional;

public class Idol implements Serializable { // Serializable 직렬화 가능

    private String name;
    private int age;
    private String groupName;

    public Idol() {
        super();
    }

    public Idol(String name, int age, String groupName) {
        this.name = name;
        this.age = age;
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGroupName() {
        // return groupName == null ? "No Group" : groupName;
        // Java 8 Optional
        return Optional.ofNullable(groupName).orElse("No Group");
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // Transfer Object 구현시 반드시 구현
    @Override
    public String toString() {
        return "Idol{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
