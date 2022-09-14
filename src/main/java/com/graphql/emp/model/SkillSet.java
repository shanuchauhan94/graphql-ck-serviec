package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class SkillSet implements Serializable {

    private String skill;
    private int experience;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SkillSet skillSet = (SkillSet) o;

        return new EqualsBuilder().append(experience, skillSet.experience).append(skill, skillSet.skill).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(skill).append(experience).toHashCode();
    }
}
