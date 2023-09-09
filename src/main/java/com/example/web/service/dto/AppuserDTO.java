package com.example.web.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.example.web.domain.Appuser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppuserDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private String plateNumber;

    private String firstName;

    private String lastName;

    private String email;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppuserDTO)) {
            return false;
        }

        AppuserDTO appuserDTO = (AppuserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appuserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppuserDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", plateNumber='" + getPlateNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
