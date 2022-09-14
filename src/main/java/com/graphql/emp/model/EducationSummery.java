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
public class EducationSummery implements Serializable {

    private String education;
    private String grade;
    private String location;

    public EducationSummery(String education, String grade, String location) {
        this.education = education;
        this.grade = grade;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EducationSummery that = (EducationSummery) o;

        return new EqualsBuilder().append(education, that.education).append(grade, that.grade).append(location, that.location).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(education).append(grade).append(location).toHashCode();
    }
}
