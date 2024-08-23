package com.mychallenge.wishlist.infrastructure.persistence.dao.custom;

import com.mychallenge.wishlist.infrastructure.persistence.entity.ProductMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.entity.WishlistMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.projection.AggregationCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CustomWishlistMongoDbDaoImplTest {

    private final ProductMongoDBEntity product = new ProductMongoDBEntity("product");

    @Mock
    private WishlistMongoDBEntity wishlist;

    @Mock
    private AggregationResults<AggregationCount> aggregationResults;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CustomWishlistMongoDBDaoImpl suite;

    @Nested
    class CountProductsById {

        @Test
        @DisplayName("should return an empty counter when wishlist not exists")
        void shouldReturnEmptyCounterWhenWishlistNotExists() {
            when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), eq(AggregationCount.class)))
                .thenReturn(mock());

            var result = suite.countProductsById("wishlistId");

            verify(mongoTemplate).aggregate(any(Aggregation.class), anyString(), eq(AggregationCount.class));

            assertEquals(0, result);
        }

        @Test
        @DisplayName("should return wishlist products count")
        void shouldReturnWishlistProductsCount() {
            var wishlistProductsCount = 1;
            when(aggregationResults.getUniqueMappedResult()).thenReturn(new AggregationCount(wishlistProductsCount));

            when(mongoTemplate.aggregate(
                any(Aggregation.class),
                anyString(),
                eq(AggregationCount.class))
            ).thenReturn(aggregationResults);

            var result = suite.countProductsById("wishlistId");

            verify(mongoTemplate).aggregate(any(Aggregation.class), anyString(), eq(AggregationCount.class));

            assertEquals(wishlistProductsCount, result);
        }
    }

    @Nested
    class UpsertProductById {

        @Test
        @DisplayName("should call mongo template method when upsert product to wishlist")
        void shouldCallMongoTemplateMethod() {
            when(
                mongoTemplate.findAndModify(
                    any(Query.class),
                    any(Update.class),
                    any(FindAndModifyOptions.class),
                    eq(WishlistMongoDBEntity.class)
                )
            ).thenReturn(wishlist);

            suite.upsertProductById("wishlistId", product);

            verify(mongoTemplate).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(WishlistMongoDBEntity.class)
            );
        }
    }

    @Nested
    class RemoveProductById {

        @Test
        @DisplayName("should call mongo template method when remove product from wishlist")
        void shouldCallMongoTemplateMethod() {
            when(
                mongoTemplate.findAndModify(
                    any(Query.class),
                    any(Update.class),
                    any(FindAndModifyOptions.class),
                    eq(WishlistMongoDBEntity.class)
                )
            ).thenReturn(wishlist);

            suite.removeProductById("wishlistId", product);

            verify(mongoTemplate).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(WishlistMongoDBEntity.class)
            );
        }
    }
}
