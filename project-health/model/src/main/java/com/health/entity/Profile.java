package com.health.entity;

import com.health.datetime.TimestampDeserializer;
import com.health.datetime.TimestampSerializer;
import com.google.common.base.Strings;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PROFILE")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Profile {
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

    @Column(name = "NAME")
    private String name;

    public Profile () {}

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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Profile withEmail(final String email) {
        setEmail(email);
        return this;
    }
    public Profile withName(final String name) {
        setName(name);
        return this;
    }
    public Profile withId(final Long id) {
        setId(id);
        return this;
    }
    public Profile withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public Profile withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if (null != id) {
            builder.append(String.format("\"id\":\"%s\"", id));
        }
        if (!Strings.isNullOrEmpty(name)) {
            builder.append(String.format(",\"name\":\"%s\"", name));
        }
        if (!Strings.isNullOrEmpty(email)) {
            builder.append(String.format(",\"email\":\"%s\"", email));
        }
        if (null != createdDatetime) {
            builder.append(String.format(",\"created_datetime\":\"%s\"", createdDatetime));
        }
        if (null != lastModifiedDatetime) {
            builder.append(String.format(",\"last_modified_datetime\":\"%s\"", lastModifiedDatetime));
        }
        builder.append("}");
        return builder.toString();
    }
}
