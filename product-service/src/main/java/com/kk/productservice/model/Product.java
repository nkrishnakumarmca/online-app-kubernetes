package com.kk.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product implements Serializable {

    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private double price;

    @JsonProperty("image")
    private String imageUrl;

}
