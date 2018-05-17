package com.noriental.utils;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PushUserInfoRepository extends CrudRepository<PushUserInfo, Long> {
    List<PushUserInfo> findByAppKey(String appKey);
}
