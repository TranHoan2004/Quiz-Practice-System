package enumerate;

import lombok.Getter;

public enum SubjectStatus {
    SENT(0), PAID(1), ACCEPTED(2);

    @Getter
    private final int value;

    SubjectStatus(int value) {
        this.value = value;
    }

    public static String fromValue(int value) {
        for (SubjectStatus status : SubjectStatus.values()) {
            if (status.getValue() == value) {
                return status.name();
            }
        }
        return null; // or throw an exception if preferred
    }
}
