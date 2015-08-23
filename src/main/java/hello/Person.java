package hello;

import java.util.Date;

public class Person {
    private String lastName;
    private String firstName;
    private Integer age;
    private Double money;
    private Date birthday = new Date();


    public Person() {

    }

    public Person(String firstName, String lastName, Integer age, Double money) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.money = money;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", birthday=" + birthday +
                '}';
    }
}
