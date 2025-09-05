package com.qps.application.usecase.auth;

import com.qps.domain.user.model.Account;
import com.qps.infrastructure.utils.EncodeUtil;

import java.util.Map;

public class ProfileUtil {
    public static void getProfile(Account account, Map<String, Object> responseMap) {
        responseMap.put("id", EncodeUtil.encode(account.getId()));
        responseMap.put("fullName", account.getFullName());
        responseMap.put("gender", account.getEmail());
        responseMap.put("phoneNumber", account.getPhone());
        responseMap.put("avatarUrl", account.getImageUrl());
        responseMap.put("username", account.getUsername());
        responseMap.put("role", account.getRole().getValue());
    }
}
