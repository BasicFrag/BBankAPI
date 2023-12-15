package io.github.basicfrag.persistence.dto;

import io.github.basicfrag.validation.groups.UserMandatoryInfo;
import io.github.basicfrag.validation.groups.UserUpdateInfo;
import jakarta.validation.constraints.*;

public class UserDto {

    @NotBlank(message = "Field cannot be blank/null!", groups = UserMandatoryInfo.class)
    @Size(message = "Must be between 5 and 50 characters",
            min = 5, max = 50, groups = UserMandatoryInfo.class)
    String name;


    @NotNull(message = "Field cannot be blank!", groups = UserMandatoryInfo.class)
    @Min(value = 18, message = "Age cannot be less than 18!", groups = UserMandatoryInfo.class)
    Integer age;

    @NotBlank(message = "Field cannot be blank!", groups = {UserMandatoryInfo.class, UserUpdateInfo.class})
    @Size(min = 15, message = "Field cannot be less than 15 characters!", groups = {UserMandatoryInfo.class, UserUpdateInfo.class})
    String telNumber;

    @NotBlank(message = "Field cannot be blank!", groups = {UserMandatoryInfo.class, UserUpdateInfo.class})
    String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelNumber() {
        return telNumber;

    }
    public void setTelNumber (String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
