package com.health.entity;

import com.health.datetime.TimestampDeserializer;
import com.health.datetime.TimestampSerializer;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "HASH_KEY")
@JsonSerialize(include = NON_NULL)
public class HashKey {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "HASH_CODE")
    private Integer hashCode;

    @Column(name = "CREATED_DATETIME", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDatetime;

    @PrePersist
    protected void onCreate() {
        createdDatetime = new Timestamp(DateTime.now().getMillis());
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHashCode() {
        return hashCode;
    }
    public void setHashCode(Integer hashCode) {
        this.hashCode = hashCode;
    }

    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }
    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public HashKey withId(final Long id) {
        setId(id);
        return this;
    }
    public HashKey withHashCode(final Integer hashCode) {
        setHashCode(hashCode);
        return this;
    }
    public HashKey withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }

    @JsonIgnore
    public static int generateHashCode(final String email, final String password) {
        return new HashCodeBuilder(11, 13)
                .append(email)
                .append(password)
                .toHashCode();
    }
}
