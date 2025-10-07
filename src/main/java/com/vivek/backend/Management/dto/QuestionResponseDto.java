package com.vivek.backend.Management.dto;


import com.vivek.backend.Management.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {

    // private Long questionId;
    // private QuestionType questionType;
    private String question;
    // private String answer;
    // private int mark;
    // private Long quizId;

    private List<OptionResponseDto> optionsList;


}