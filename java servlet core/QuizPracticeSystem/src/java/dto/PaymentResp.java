package dto;

import lombok.Builder;

public record PaymentResp(String subjectName, String packageName,
                          String price, String registrantName,
                          String email) {
}
