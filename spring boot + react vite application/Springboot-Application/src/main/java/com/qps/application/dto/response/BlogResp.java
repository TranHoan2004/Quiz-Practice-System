package com.qps.application.dto.response;

public record BlogResp(String id, String title, String briefInfo,
                       String content, String categoryId, String category,
                       String accountId, String accountName, String avatarUrl,
                       String createdDate, String updatedDate, boolean flagFeature,
                       int views, boolean status, String blogMediaJson) {
}
