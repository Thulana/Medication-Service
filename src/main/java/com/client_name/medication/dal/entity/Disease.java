package com.client_name.medication.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Set;

/**
 * Entity to represent a disease in the database
 * Equals to a record in disease table
 */
@Entity
@Table(name = "disease")
@JsonIgnoreProperties({ "id", "medications" })
@JsonDeserialize(using = Disease.DiseaseDeserializer.class)
@JsonSerialize(using = Disease.DiseaseSerializer.class)
public class Disease {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id", unique=true, nullable=false)
    private String id;

    @Column(name = "name", unique=true, nullable=false)
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100)
    private String name;

    @ManyToMany(mappedBy = "diseases")
    private Set<Medication> medications;

    public Disease(String name){
        this.name = name;
    }

    public Disease() {

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    //Custom JsonDeserializer for Disease class for converting just the disease name to an Object
    static class DiseaseDeserializer extends JsonDeserializer<Disease> {

        @Override
        public Disease deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);
            return new Disease(node.asText());
        }

    }

    //Custom JsonSerializer for representing a Disease Object as a String
    static class DiseaseSerializer extends JsonSerializer<Disease> {

        @Override
        public void serialize(Disease value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getName());
        }
    }
}
