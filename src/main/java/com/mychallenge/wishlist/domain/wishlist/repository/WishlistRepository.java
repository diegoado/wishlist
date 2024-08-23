package com.mychallenge.wishlist.domain.wishlist.repository;

import com.mychallenge.wishlist.domain.wishlist.ProductEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistId;

public interface WishlistRepository {

    boolean existsProductInWishlistById(WishlistId wishlist, ProductEntity product);

    int countProductsInWishlist(WishlistId wishlist);

    WishlistEntity getWishlistById(WishlistId wishlist);

    WishlistEntity upsertProductToWishlistById(WishlistId wishlist, ProductEntity product);

    WishlistEntity removeProductFromWishlistById(WishlistId wishlist, ProductEntity product);
}
