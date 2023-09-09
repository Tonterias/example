package com.example.web.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.web.domain.Appuser} entity. This class is used
 * in {@link com.example.web.web.rest.AppuserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appusers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppuserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private StringFilter plateNumber;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private LongFilter userId;

    private LongFilter notificationId;

    private Boolean distinct;

    public AppuserCriteria() {}

    public AppuserCriteria(AppuserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.plateNumber = other.plateNumber == null ? null : other.plateNumber.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppuserCriteria copy() {
        return new AppuserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public StringFilter getPlateNumber() {
        return plateNumber;
    }

    public StringFilter plateNumber() {
        if (plateNumber == null) {
            plateNumber = new StringFilter();
        }
        return plateNumber;
    }

    public void setPlateNumber(StringFilter plateNumber) {
        this.plateNumber = plateNumber;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getNotificationId() {
        return notificationId;
    }

    public LongFilter notificationId() {
        if (notificationId == null) {
            notificationId = new LongFilter();
        }
        return notificationId;
    }

    public void setNotificationId(LongFilter notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppuserCriteria that = (AppuserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(plateNumber, that.plateNumber) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(notificationId, that.notificationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, plateNumber, firstName, lastName, email, userId, notificationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppuserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (plateNumber != null ? "plateNumber=" + plateNumber + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
