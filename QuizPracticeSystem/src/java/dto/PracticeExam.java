package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PracticeExam {
    private String id;
    private String subjectName;
    private String examName;
    private String dateTaken;
    private int numberOfCorrectQuestions;
    private int numberOfQuestions;
    private String duration;
    private String moreInformation;
}
