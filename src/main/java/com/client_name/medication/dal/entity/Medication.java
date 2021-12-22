package com.client_name.medication.dal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Entity to represent a medication in the database
 * Equals to a record in medication table
 */
@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @Column(name = "id", unique=true, nullable=false)
    private String id;

    @Column(name = "name", unique=true, nullable=false)
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50)
    private String name;

    @Column(name = "description", unique=false, nullable=true)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "released_date", unique=false, nullable=false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date released;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "medication_disease",
            joinColumns = @JoinColumn(name = "medication_id"),
            inverseJoinColumns = @JoinColumn(name = "disease_id"))
    private Set<Disease> diseases;

    public Medication(String name){
        this.name = name;
    }

    public Medication() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date releasedDate) {
        this.released = releasedDate;
    }

    public Set<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(Set<Disease> diseases) {
        this.diseases = diseases;
    }
}
