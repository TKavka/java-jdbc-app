package com.botscrew.university;


public class Lector {

    private int id;

    private String firstName;

    private String lastName;

    private String degreeName;

    private Integer salary;

    public Lector() {
    }


    public Lector(String firstName, String lastName, String degreeName, Integer salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.degreeName = degreeName;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Lector{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", degreeName='" + degreeName + '\'' +
                ", salary=" + salary +
                '}';
    }
}
