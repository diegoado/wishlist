package com.mychallenge.wishlist.infrastructure.persistence.dao.custom;

import com.mychallenge.wishlist.infrastructure.persistence.entity.ProductMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.entity.WishlistMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.projection.AggregationCount;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

public class CustomWishlistMongoDBDaoImpl implements CustomWishlistMongoDBDao {

    private final MongoTemplate mongoTemplate;

    public CustomWishlistMongoDBDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int countProductsById(String id) {
        var aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").is(id)),
            Aggregation.project().and("products").size().as("count")
        );

        var result = mongoTemplate.aggregate(aggregation, "wishlist", AggregationCount.class);
        return result.getUniqueMappedResult() != null ? result.getUniqueMappedResult().count() : 0;
    }

    @Override
    public WishlistMongoDBEntity upsertProductById(String id, ProductMongoDBEntity product) {
        var query = new Query().addCriteria(Criteria.where("_id").is(id));
        var updateDefinition = new Update().addToSet("products", product);
        var options = new FindAndModifyOptions().returnNew(true).upsert(true);

        return mongoTemplate.findAndModify(query, updateDefinition, options, WishlistMongoDBEntity.class);
    }

    @Override
    public Optional<WishlistMongoDBEntity> removeProductById(String id, ProductMongoDBEntity product) {
        var findQuery = new Query()
            .addCriteria(
                Criteria.where("_id").is(id)
                    .and("products").elemMatch(Criteria.where("name").is(product.name()))
            );
        var pullQuery = new Query()
            .addCriteria(
                Criteria.where("name").is(product.name())
            );

        var updateDefinition = new Update().pull("products",  pullQuery);
        var options = new FindAndModifyOptions().returnNew(true);

        return Optional.ofNullable(
            mongoTemplate.findAndModify(findQuery, updateDefinition, options, WishlistMongoDBEntity.class)
        );
    }
}
