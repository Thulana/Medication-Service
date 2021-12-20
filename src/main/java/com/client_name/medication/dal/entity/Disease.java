package com.client_name.medication.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
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

@Entity
@Table(name = "disease")
@JsonIgnoreProperties({ "id", "medications" })
@JsonDeserialize(using = Disease.DiseaseDeserializer.class)
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


    static class DiseaseDeserializer extends StdDeserializer<Disease> {

        public DiseaseDeserializer() {
            this(null);
        }

        @Override
        public Disease deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            return new Disease(node.asText());
        }

        public DiseaseDeserializer(Class<?> vc) {
            super(vc);
        }

    }
}
