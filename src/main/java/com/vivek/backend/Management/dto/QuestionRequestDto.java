package com.vivek.backend.Management.dto;


import com.vivek.backend.Management.enums.QuestionType;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDto {

    private QuestionType questionType;

     // @NotBlank
    private String question;

    // @NotBlank
    private String answer;

    // @Min(1)
    private int mark;

   //  @NotNull
    private Long quizId; // Reference to the quiz this question belongs to

    // @Valid
    private List<OptionRequestDto> optionsList;
}