package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class EmployeeRecord {

    private String email;
    private Integer salary;
    private String name;
    private Integer age;
    private List<EducationSummery> eduSummery;
    private List<SkillSet> skillSets;
}
