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

import static com.health.collect.MoreIterables.asFluent;
import static java.lang.Boolean.FALSE;
import static javax.persistence.CascadeType.ALL;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

@Entity
@Table(name = "ACCOUNTS")
@JsonSerialize(include = NON_NULL)
public class Account {
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

    @Column(name = "LAST_LOGIN_DATETIME")
    @JsonSerialize(using=TimestampSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=TimestampDeserializer.class)
    private Timestamp lastLoginDateTime;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "HASH_KEY", unique = true)
    private Integer hashKey;

    @Column(name = "FIRST_NAME", length = 50, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    private String lastName;

    @Column(name = "MIDDLE_NAME", length = 50)
    private String middleName;

    @Column(name = "GENDER", length = 1)
    private String gender;

    @Column(name = "SHARE_ACCOUNT")
    private Boolean isShareAccount;

    @OneToMany(mappedBy = "account", targetEntity = SleepRecord.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<SleepRecord> sleepRecords;

    @OneToMany(mappedBy = "account", targetEntity = FoodRecord.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<FoodRecord> foodRecords;

    @OneToMany(mappedBy = "account", targetEntity = ExerciseRecord.class, fetch = FetchType.EAGER, cascade = ALL)
    private List<ExerciseRecord> exerciseRecords;

    @OneToMany(mappedBy = "account", targetEntity = Recipe.class, cascade = ALL)
    private List<Recipe> recipes;

    @PrePersist
    protected void onCreate() {
        createdDatetime = new Timestamp(DateTime.now().getMillis());
        lastModifiedDatetime = new Timestamp(DateTime.now().getMillis());
        if (null == isShareAccount) {
            isShareAccount = FALSE;
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

    public Timestamp getLastLoginDateTime() {
        return lastLoginDateTime;
    }
    public void setLastLoginDateTime(Timestamp lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getHashKey() {
        return hashKey;
    }
    public void setHashKey(Integer hashKey) {
        this.hashKey = hashKey;
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

    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getIsShareAccount() {
        return isShareAccount;
    }
    public void setIsShareAccount(Boolean isShareAccount) {
        this.isShareAccount = isShareAccount;
    }

    public List<SleepRecord> getSleepRecords() {
        return sleepRecords;
    }
    public void setSleepRecords(List<SleepRecord> sleepRecords) {
        this.sleepRecords = sleepRecords;
    }

    public List<FoodRecord> getFoodRecords() {
        return foodRecords;
    }
    public void setFoodRecords(List<FoodRecord> foodRecords) {
        this.foodRecords = foodRecords;
    }

    public List<ExerciseRecord> getExerciseRecords() {
        return exerciseRecords;
    }
    public void setExerciseRecords(List<ExerciseRecord> exerciseRecords) {
        this.exerciseRecords = exerciseRecords;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
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
    public Account withLastLoginDateTime(final Timestamp lastLoginDateTime) {
        setLastLoginDateTime(lastLoginDateTime);
        return this;
    }
    public Account withEmail(final String email) {
        setEmail(email);
        return this;
    }
    public Account withHashKey(final Integer hashKey) {
        setHashKey(hashKey);
        return this;
    }
    public Account withFirstName(final String firstName) {
        setFirstName(firstName);
        return this;
    }
    public Account withLastName(final String lastName) {
        setLastName(lastName);
        return this;
    }
    public Account withMiddleName(final String middleName) {
        setMiddleName(middleName);
        return this;
    }
    public Account withGender(final String gender) {
        setGender(gender);
        return this;
    }
    public Account withIsShareAccount(final Boolean isShareAccount) {
        setIsShareAccount(isShareAccount);
        return this;
    }
    public Account withSleepRecords(final List<SleepRecord> sleepRecords) {
        setSleepRecords(sleepRecords);
        return this;
    }
    public Account withFoodRecords(final List<FoodRecord> foodRecords) {
        setFoodRecords(foodRecords);
        return this;
    }
    public Account withExerciseRecords(final List<ExerciseRecord> exerciseRecords) {
        setExerciseRecords(exerciseRecords);
        return this;
    }
    public Account withRecipes(final List<Recipe> recipes) {
        setRecipes(recipes);
        return this;
    }

    @JsonIgnore
    public void clean() {
        for (SleepRecord sleepRecord : asFluent(sleepRecords)) {
            sleepRecord.clean();
        }
        for (FoodRecord foodRecord : asFluent(foodRecords)) {
            foodRecord.clean();
        }
        for (ExerciseRecord exerciseRecord : asFluent(exerciseRecords)) {
            exerciseRecord.clean();
        }
        recipes = null;
    }
}