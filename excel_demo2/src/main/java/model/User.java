package model;

public class User {

  private String name;
  private String surname;
  private String email;
  private String country;
  private String city;
  private int age;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "\nUser{" +
        "name='" + name + '\'' +
        ", surname='" + surname + '\'' +
        ", email='" + email + '\'' +
        ", country='" + country + '\'' +
        ", city='" + city + '\'' +
        ", age=" + age +
        "}";
  }
}
