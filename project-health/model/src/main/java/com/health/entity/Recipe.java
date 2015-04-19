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
@Table(name = "RECIPES")
@JsonSerialize(include = NON_NULL)
public class Recipe {
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

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CALORIES_CONSUMED", nullable = false)
    private Integer caloriesConsumed;

    @OneToMany(mappedBy = "recipe", targetEntity = FoodRecord.class, cascade = ALL)
    private List<FoodRecord> foodRecords;

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

    public Integer getCaloriesConsumed() {
        return caloriesConsumed;
    }
    public void setCaloriesConsumed(Integer caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public List<FoodRecord> getFoodRecords() {
        return foodRecords;
    }
    public void setFoodRecords(List<FoodRecord> foodRecords) {
        this.foodRecords = foodRecords;
    }

    public Recipe withId(final Long id) {
        setId(id);
        return this;
    }
    public Recipe withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public Recipe withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }
    public Recipe withAccount(final Account account) {
        setAccount(account);
        return this;
    }
    public Recipe withAccountId(final Long accountId) {
        setAccountId(accountId);
        return this;
    }
    public Recipe withName(final String name) {
        setName(name);
        return this;
    }
    public Recipe withDescription(final String description) {
        setDescription(description);
        return this;
    }
    public Recipe withCaloriesConsumed(final Integer caloriesConsumed) {
        setCaloriesConsumed(caloriesConsumed);
        return this;
    }
    public Recipe withFoodRecords(final List<FoodRecord> foodRecords) {
        setFoodRecords(foodRecords);
        return this;
    }

    @JsonIgnore
    public void clean() {
        account = null;
        foodRecords = null;
    }
}
