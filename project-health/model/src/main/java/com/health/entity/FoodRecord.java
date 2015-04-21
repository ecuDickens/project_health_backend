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
@Table(name = "FOOD_RECORDS")
@JsonSerialize(include = NON_NULL)
public class FoodRecord {
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
    @JoinColumn(name="RECIPE_ID", referencedColumnName="ID")
    private Recipe recipe;

    @Column(name = "RECIPE_ID")
    private Long recipeId;

    @Column(name = "RECORD_DATE", nullable = false)
    @JsonSerialize(using=DateSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=DateDeserializer.class)
    private Date recordDate;

    @Column(name = "CALORIES_CONSUMED", nullable = false)
    private Integer caloriesConsumed;

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

    public Recipe getRecipe() {
        return recipe;
    }
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Long getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getCaloriesConsumed() {
        return caloriesConsumed;
    }
    public void setCaloriesConsumed(Integer caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    public FoodRecord withId(final Long id) {
        setId(id);
        return this;
    }
    public FoodRecord withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public FoodRecord withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }
    public FoodRecord withAccount(final Account account) {
        setAccount(account);
        return this;
    }
    public FoodRecord withAccountId(final Long accountId) {
        setRecipeId(accountId);
        return this;
    }
    public FoodRecord withRecipe(final Recipe recipe) {
        setRecipe(recipe);
        return this;
    }
    public FoodRecord withRecipeId(final Long recipeId) {
        setRecipeId(recipeId);
        return this;
    }
    public FoodRecord withRecordDate(final Date recordDate) {
        setRecordDate(recordDate);
        return this;
    }
    public FoodRecord withCaloriesConsumed(final Integer caloriesConsumed) {
        setCaloriesConsumed(caloriesConsumed);
        return this;
    }

    @JsonIgnore
    public void clean() {
        account = null;
        recipeId = null;
        recipe.clean();
    }
}
