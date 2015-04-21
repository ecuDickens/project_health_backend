package com.health.entity;

import com.health.datetime.DateDeserializer;
import com.health.datetime.DateSerializer;
import com.health.datetime.TimestampDeserializer;
import com.health.datetime.TimestampSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "EXERCISE_RECORDS")
@JsonSerialize(include = NON_NULL)
public class ExerciseRecord {
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

    @ManyToOne(optional=false)
    @JoinColumn(name="ACCOUNT_ID", referencedColumnName="ID")
    private Account account;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @ManyToOne(optional=false)
    @JoinColumn(name="EXERCISE_ID", referencedColumnName="ID")
    private Exercise exercise;

    @Column(name = "EXERCISE_ID")
    private Long exerciseId;

    @Column(name = "RECORD_DATE", nullable = false)
    @JsonSerialize(using=DateSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=DateDeserializer.class)
    private Date recordDate;

    @Column(name = "HOURS_EXERCISED", nullable = false)
    private Double hoursExercised;

    @Column(name = "CALORIES_BURNED", nullable = false)
    private Integer caloriesBurned;

    @PrePersist
    protected void onCreate() {
        createdDatetime = new Timestamp(DateTime.now().getMillis());
        lastModifiedDatetime = new Timestamp(DateTime.now().getMillis());
        if (null == recordDate) {
            recordDate = new Date(DateTime.now().getMillis());
        }
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

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Long getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Double getHoursExercised() {
        return hoursExercised;
    }
    public void setHoursExercised(Double hoursExercised) {
        this.hoursExercised = hoursExercised;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }
    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public ExerciseRecord withId(final Long id) {
        setId(id);
        return this;
    }
    public ExerciseRecord withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public ExerciseRecord withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }
    public ExerciseRecord withAccount(final Account account) {
        setAccount(account);
        return this;
    }
    public ExerciseRecord withAccountId(final Long accountId) {
        setExerciseId(accountId);
        return this;
    }
    public ExerciseRecord withExercise(final Exercise exercise) {
        setExercise(exercise);
        return this;
    }
    public ExerciseRecord withExerciseId(final Long exerciseId) {
        setExerciseId(exerciseId);
        return this;
    }
    public ExerciseRecord withRecordDate(final Date recordDate) {
        setRecordDate(recordDate);
        return this;
    }
    public ExerciseRecord withHoursExercised(final Double hoursExercised) {
        setHoursExercised(hoursExercised);
        return this;
    }
    public ExerciseRecord withCaloriesBurned(final Integer caloriesBurned) {
        setCaloriesBurned(caloriesBurned);
        return this;
    }

    @JsonIgnore
    public void clean() {
        account = null;
        exerciseId = null;
        exercise.clean();
    }
}

