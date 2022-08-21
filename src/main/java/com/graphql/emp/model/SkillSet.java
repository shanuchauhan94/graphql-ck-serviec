package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class SkillSet implements Serializable {

    private String skill;
    private int experience;

}
