package com.mychallenge.wishlist.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "wishlist")
public record WishlistMongoDBEntity(@Id String id, List<ProductMongoDBEntity> products) {}
