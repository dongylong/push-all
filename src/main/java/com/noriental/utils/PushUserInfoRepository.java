package com.noriental.utils;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author dongyl
 */
public interface PushUserInfoRepository extends CrudRepository<PushUserInfo, Long> {
    List<PushUserInfo> findByAppKey(String appKey);
}
