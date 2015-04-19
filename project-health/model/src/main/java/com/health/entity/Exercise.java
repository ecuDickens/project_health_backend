package com.health.entity;

import com.health.datetime.TimestampDeserializer;
import com.health.datetime.TimestampSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "EXERCISES")
@JsonSerialize(include = NON_NULL)
public class Exercise {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_DATETIME", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp createdDatetime;

    @Column(name = "LAST_MODIFIED_DATETIME", nullable = false)
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp lastModifiedDatetime;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "CALORIES_BURNED_PER_HOUR", nullable = false)
    private Integer caloriesBurnedPerHour;

    @OneToMany(mappedBy = "exercise", targetEntity = ExerciseRecord.class, cascade = ALL)
    private List<ExerciseRecord> exerciseRecords;

    @PrePersist
    protected void onCreate() {
        createdDatetime = new Timestamp(DateTime.now().getMillis());
        lastModifiedDatetime = new Timestamp(DateTime.now().getMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDatetime = new Timestamp(DateTime.now().getMillis());
    }

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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCaloriesBurnedPerHour() {
        return caloriesBurnedPerHour;
    }
    public void setCaloriesBurnedPerHour(Integer caloriesBurnedPerHour) {
        this.caloriesBurnedPerHour = caloriesBurnedPerHour;
    }

    public List<ExerciseRecord> getExerciseRecords() {
        return exerciseRecords;
    }
    public void setExerciseRecords(List<ExerciseRecord> exerciseRecords) {
        this.exerciseRecords = exerciseRecords;
    }

    public Exercise withId(final Long id) {
        setId(id);
        return this;
    }
    public Exercise withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public Exercise withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }
    public Exercise withName(final String name) {
        setName(name);
        return this;
    }
    public Exercise withDescription(final String description) {
        setDescription(description);
        return this;
    }
    public Exercise withCaloriesBurnedPerHour(final Integer caloriesBurnedPerHour) {
        setCaloriesBurnedPerHour(caloriesBurnedPerHour);
        return this;
    }
    public Exercise withExerciseRecords(final List<ExerciseRecord> exerciseRecords) {
        setExerciseRecords(exerciseRecords);
        return this;
    }

    @JsonIgnore
    public void clean() {
        exerciseRecords = null;
    }
}
