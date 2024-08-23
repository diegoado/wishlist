package com.mychallenge.wishlist.infrastructure.persistence.dao;

import com.mychallenge.wishlist.infrastructure.persistence.dao.custom.CustomWishlistMongoDBDao;
import com.mychallenge.wishlist.infrastructure.persistence.entity.WishlistMongoDBEntity;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistMongoDBDao extends CustomWishlistMongoDBDao, MongoRepository<WishlistMongoDBEntity, String> {

    @ExistsQuery(value = "{ '_id': ?0, 'products': { $elemMatch: { 'name': ?1 }}}")
    boolean existsByIdAndProductNameEqualsTo(String id, String productName);

    @Query(value = "{ '_id':  ?0, 'products': { $exists: true, $type: 'array', $ne: [] }}")
    Optional<WishlistMongoDBEntity> findByIdAndProductsIsNotEmpty(String id);
}
