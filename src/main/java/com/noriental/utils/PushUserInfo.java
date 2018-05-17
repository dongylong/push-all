// tag::sample[]
package com.noriental.utils;

import javax.persistence.*;

@Entity
@Table(name = "entity_user_push_info")
public class PushUserInfo {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private long systemId;
    private String pushId;
    private String appKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "PushUserInfo{" +
                "id=" + id +
                ", systemId=" + systemId +
                ", pushId='" + pushId + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}

