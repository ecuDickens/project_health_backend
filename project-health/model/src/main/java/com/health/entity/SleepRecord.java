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
@Table(name = "SLEEP_RECORDS")
@JsonSerialize(include = NON_NULL)
public class SleepRecord {
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

    @Column(name = "RECORD_DATE", nullable = false)
    @JsonSerialize(using=DateSerializer.class, include = NON_NULL)
    @JsonDeserialize(using=DateDeserializer.class)
    private Date recordDate;

    @Column(name = "HOURS_SLEPT")
    private Double hoursSlept;

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

    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Double getHoursSlept() {
        return hoursSlept;
    }
    public void setHoursSlept(Double hoursSlept) {
        this.hoursSlept = hoursSlept;
    }

    public SleepRecord withId(final Long id) {
        setId(id);
        return this;
    }
    public SleepRecord withCreatedDatetime(final Timestamp createdDatetime) {
        setCreatedDatetime(createdDatetime);
        return this;
    }
    public SleepRecord withLastModifiedDatetime(final Timestamp lastModifiedDatetime) {
        setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }
    public SleepRecord withAccount(final Account account) {
        setAccount(account);
        return this;
    }
    public SleepRecord withAccountId(final Long accountId) {
        setAccountId(accountId);
        return this;
    }
    public SleepRecord withRecordDate(final Date sleepDate) {
        setRecordDate(sleepDate);
        return this;
    }
    public SleepRecord withHoursSlept(final Double hoursSlept) {
        setHoursSlept(hoursSlept);
        return this;
    }

    @JsonIgnore
    public void clean() {
        account = null;
    }
}
