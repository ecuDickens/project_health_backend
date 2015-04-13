package com.health.entity;

import com.health.datetime.TimestampDeserializer;
import com.health.datetime.TimestampSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ACCOUNT")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_DATETIME")
    @JsonSerialize(using=TimestampSerializer.class)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDatetime;

    @Column(name = "LAST_MODIFIED_DATETIME")
    @JsonSerialize(using=TimestampSerializer.class)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp lastModifiedDatetime;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MIDDLE_INITIAL")
    private String middleInitial;

    @Column(name = "GENDER")
    private String gender;

    public Account() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }
    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Timestamp getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }
    public void setLastModifiedDatetime(Timestamp lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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

    public String getMiddleInitial() {
        return middleInitial;
    }
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Account withEmail(final String email) {
        setEmail(email);
        return this;
    }
    public Account withGender(final String gender) {
        setGender(gender);
        return this;
    }
    public Account withMiddleInitial(final String middleInitial) {
        setMiddleInitial(middleInitial);
        return this;
    }
    public Account withLastName(final String lastName) {
        setLastName(lastName);
        return this;
    }
    public Account withFirstName(final String firstName) {
        setFirstName(firstName);
        return this;
    }
    public Account withId(final Long id) {
        setId(id);
        return this;
    }
    public Account withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public Account withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }
}
