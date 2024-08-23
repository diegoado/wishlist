package com.mychallenge.wishlist.infrastructure.persistence.dao.custom;

import com.mychallenge.wishlist.infrastructure.persistence.entity.ProductMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.entity.WishlistMongoDBEntity;

import java.util.Optional;

public interface CustomWishlistMongoDBDao {

    int countProductsById(String id);

    WishlistMongoDBEntity upsertProductById(String id, ProductMongoDBEntity product);

    Optional<WishlistMongoDBEntity> removeProductById(String id, ProductMongoDBEntity product);
}
