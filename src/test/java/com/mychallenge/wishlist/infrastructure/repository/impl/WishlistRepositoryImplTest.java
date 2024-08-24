package com.mychallenge.wishlist.infrastructure.repository.impl;

import com.mychallenge.wishlist.domain.wishlist.ProductEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistId;
import com.mychallenge.wishlist.infrastructure.persistence.dao.WishlistMongoDBDao;
import com.mychallenge.wishlist.infrastructure.persistence.entity.ProductMongoDBEntity;
import com.mychallenge.wishlist.infrastructure.persistence.entity.WishlistMongoDBEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishlistRepositoryImplTest {

    private final WishlistId wishlistId = new WishlistId("client");

    private final ProductEntity product = new ProductEntity("product");

    @Mock
    private WishlistMongoDBDao wishlistMongoDBDao;

    @InjectMocks
    private WishlistRepositoryImpl suite;

    @Nested
    class ExistsProductInWishlistById {

        @Test
        @DisplayName("should call dao method when verify if product exists in wishlist")
        void shouldCallDaoMethod() {
            assertFalse(suite.existsProductInWishlistById(wishlistId, product));

            verify(wishlistMongoDBDao).existsByIdAndProductNameEqualsTo(any(), any());
        }
    }

    @Nested
    class CountProductsInWishlist {

        @Test
        @DisplayName("should call dao method when count products in wishlist")
        void shouldCallDaoMethod() {
            var wishlistSize = 1;
            when(wishlistMongoDBDao.countProductsById(any())).thenReturn(wishlistSize);

            assertEquals(wishlistSize, suite.countProductsInWishlist(wishlistId));

            verify(wishlistMongoDBDao).countProductsById(any());
        }
    }

    @Nested
    class GetWishlistById {

        @Test
        @DisplayName("should call dao method when get wishlist by id")
        void shouldCallDaoMethod() {
            var wishlist = new WishlistMongoDBEntity(
                wishlistId.client(),
                Collections.singletonList(new ProductMongoDBEntity(product.name()))
            );

            when(wishlistMongoDBDao.findByIdAndProductsIsNotEmpty(any())).thenReturn(Optional.of(wishlist));

            assertNotNull(suite.getWishlistById(wishlistId));

            verify(wishlistMongoDBDao).findByIdAndProductsIsNotEmpty(any());
        }
    }

    @Nested
    class UpsertProductToWishlistById {

        @Test
        @DisplayName("should call dao method when add a new product to wishlist")
        void shouldCallDaoMethod() {
            var wishlist = new WishlistMongoDBEntity(
                wishlistId.client(),
                Collections.singletonList(new ProductMongoDBEntity(product.name()))
            );

            when(wishlistMongoDBDao.upsertProductById(any(), any())).thenReturn(wishlist);

            assertNotNull(suite.upsertProductToWishlistById(wishlistId, product));

            verify(wishlistMongoDBDao).upsertProductById(any(), any());
        }
    }

    @Nested
    class RemoveProductFromWishlistById {

        @Test
        @DisplayName("should call dao method when remove a product from wishlist")
        void shouldCallDaoMethod() {
            var wishlist = new WishlistMongoDBEntity(
                wishlistId.client(),
                Collections.emptyList()
            );

            when(wishlistMongoDBDao.removeProductById(any(), any())).thenReturn(Optional.of(wishlist));

            assertNotNull(suite.removeProductFromWishlistById(wishlistId, product));

            verify(wishlistMongoDBDao).removeProductById(any(), any());
        }
    }
}
