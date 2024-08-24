package com.mychallenge.wishlist.domain.wishlist.service;

import com.mychallenge.wishlist.domain.exception.EntityNotFoundException;
import com.mychallenge.wishlist.domain.exception.MaxWishlistSizeReachedException;
import com.mychallenge.wishlist.domain.wishlist.ProductEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistId;
import com.mychallenge.wishlist.domain.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    private final WishlistEntity wishlist = new WishlistEntity(
        new WishlistId("client"),
        Collections.singletonList(new ProductEntity("product"))
    );

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistService suite;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(suite, "maxWishlistSize", 20);
    }

    @Nested
    class ExistsProductInClientWishlist {

        @Test
        @DisplayName("should call repository method when verify if product exists in wishlist")
        void shouldCallDaoMethod() {
            suite.existsProductInClientWishlist("client", "product");

            verify(wishlistRepository).existsProductInWishlistById(any(), any());
        }
    }

    @Nested
    class GetClientWishlist {

        @Test
        @DisplayName("should throw an error when wishlist is empty")
        void shouldThrowAnErrorWhenWishlistIsEmpty() {
            assertThrows(EntityNotFoundException.class, () -> suite.getClientWishlist("client"));

            verify(wishlistRepository).getWishlistById(any());
        }

        @Test
        @DisplayName("should return wishlist")
        void shouldReturnWishlist() {
            when(wishlistRepository.getWishlistById(any())).thenReturn(wishlist);

            var wishlist = suite.getClientWishlist("client");

            verify(wishlistRepository).getWishlistById(any());

            assertNotNull(wishlist);
        }
    }

    @Nested
    class UpsertProductToClientWishlist {

        @Test
        @DisplayName("should throw an error when max wishlist size reached")
        void shouldThrowAnErrorWhenMaxWishlistSizeReached() {
            when(wishlistRepository.countProductsInWishlist(any())).thenReturn(20);

            assertThrows(MaxWishlistSizeReachedException.class, () -> suite.upsertProductToClientWishlist("client", "product"));

            verify(wishlistRepository, times(0)).upsertProductToWishlistById(any(), any());
        }

        @Test
        @DisplayName("should upsert a product to wishlist until its maximum size")
        void shouldUpsertAProductToWishlistUntilItsMaximumSize() {
            when(wishlistRepository.countProductsInWishlist(any())).thenReturn(19);

            when(wishlistRepository.upsertProductToWishlistById(any(), any())).thenReturn(wishlist);

            var wishlist = suite.upsertProductToClientWishlist("client", "product");

            verify(wishlistRepository).upsertProductToWishlistById(any(), any());

            assertNotNull(wishlist);
        }

        @Test
        @DisplayName("should upsert new product to wishlist")
        void shouldUpsertNewProductToWishlist() {
            when(wishlistRepository.upsertProductToWishlistById(any(), any())).thenReturn(wishlist);

            var wishlist = suite.upsertProductToClientWishlist("client", "product");

            verify(wishlistRepository).upsertProductToWishlistById(any(), any());

            assertNotNull(wishlist);
        }
    }

    @Nested
    class RemoveProductFromClientWishlist {

        @Test
        @DisplayName("should throw an error when wishlist not exists or already empty")
        void shouldThrowAnErrorWhenWishlistIsEmpty() {
            assertThrows(EntityNotFoundException.class, () -> suite.removeProductFromClientWishlist("client", "product"));

            verify(wishlistRepository, times(0)).removeProductFromWishlistById(any(), any());
        }

        @Test
        @DisplayName("should remove given product from wishlist")
        void shouldRemoveProductFromClientWishlist() {
            when(wishlistRepository.existsProductInWishlistById(any(), any()))
                .thenReturn(true);

            when(wishlistRepository.removeProductFromWishlistById(any(), any()))
                .thenReturn(wishlist);

            suite.removeProductFromClientWishlist("client", "product");

            verify(wishlistRepository).removeProductFromWishlistById(any(), any());
        }
    }
}
