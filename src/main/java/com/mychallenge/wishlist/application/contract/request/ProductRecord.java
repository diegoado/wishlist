package com.mychallenge.wishlist.application.contract.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRecord(
    @NotNull
    @NotBlank
    @Size(max = 127)
    @JsonProperty("product_name")
    String name
) {}
