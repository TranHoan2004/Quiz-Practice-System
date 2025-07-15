package dto;

import java.util.List;

public record CustomizedLearningTargetReq(
        List<String> learningTarget,
        String identified,
        String educationLevel,
        List<String> selectedTopics) {
}
