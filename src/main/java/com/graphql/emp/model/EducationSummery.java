package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class EducationSummery implements Serializable {

    private String education;
    private String grade;
    private String location;

    public EducationSummery(String education, String grade, String location) {
        this.education = education;
        this.grade = grade;
        this.location = location;
    }
}
