package com.graphql.emp.model;

import com.google.gson.Gson;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "employee")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Employee {

    @EmbeddedId
    private EmployeeCompositeKey ck;
    private String name;
    private Integer age;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<EducationSummery> eduSummery;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<SkillSet> skillSets;

    public Employee(EmployeeRecord employeeRecord) {
        this.ck = new EmployeeCompositeKey(employeeRecord.getEmail(), employeeRecord.getSalary());
        this.age = employeeRecord.getAge();
        this.name = employeeRecord.getName();
        this.eduSummery = employeeRecord.getEduSummery();
        this.skillSets = employeeRecord.getSkillSets();
    }
}
