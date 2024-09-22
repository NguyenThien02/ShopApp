package com.example.ShopApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1,message = "Product Id must be > 0")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min=5, max = 200,message = "Image's name")
    private String imageUrl;
}
