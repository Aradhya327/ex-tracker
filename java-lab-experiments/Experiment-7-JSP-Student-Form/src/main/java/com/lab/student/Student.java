package com.lab.student;

/**
 * Plain JavaBean so the JSP can read the fields with EL (${student.name}).
 *
 * EL looks for getName(), so public getters are required - a Java record's
 * accessors (name()) are not picked up by EL.
 */
public class Student {

    private String name = "";
    private String email = "";
    private String course = "";
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
