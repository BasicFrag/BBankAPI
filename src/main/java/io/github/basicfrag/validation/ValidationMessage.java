package io.github.basicfrag.validation;

import java.util.Objects;
public class ValidationMessage {

    private String message;

    private String propertyName;

    private Object invalidValue;

    public ValidationMessage() {
    }

    public ValidationMessage(String message, String propertyName, Object invalidValue) {
        this.message = message;
        this.propertyName = propertyName;
        this.invalidValue = invalidValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationMessage that = (ValidationMessage) o;
        return Objects.equals(message, that.message) && Objects.equals(propertyName, that.propertyName) && Objects.equals(invalidValue, that.invalidValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, propertyName, invalidValue);
    }

    @Override
    public String toString() {
        return "ValidationMessage{" +
                "message='" + message + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", invalidValue='" + invalidValue + '\'' +
                '}';
    }
}