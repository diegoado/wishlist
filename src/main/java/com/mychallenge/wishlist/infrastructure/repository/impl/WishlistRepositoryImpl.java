package com.mychallenge.wishlist.infrastructure.repository.impl;

import com.mychallenge.wishlist.domain.wishlist.repository.WishlistRepository;
import com.mychallenge.wishlist.domain.wishlist.ProductEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistId;
import com.mychallenge.wishlist.infrastructure.persistence.dao.WishlistMongoDBDao;
import com.mychallenge.wishlist.infrastructure.persistence.entity.ProductMongoDBEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishlistRepositoryImpl implements WishlistRepository {

    private final WishlistMongoDBDao wishlistMongoDBDao;

    @Autowired
    public WishlistRepositoryImpl(WishlistMongoDBDao wishlistMongoDBDao) {
        this.wishlistMongoDBDao = wishlistMongoDBDao;
    }

    @Override
    public boolean existsProductInWishlistById(final WishlistId wishlist, final ProductEntity product) {
        return wishlistMongoDBDao.existsByIdAndProductNameEqualsTo(wishlist.client(), product.name());
    }

    @Override
    public int countProductsInWishlist(final WishlistId wishlist) {
        return wishlistMongoDBDao.countProductsById(wishlist.client());
    }

    @Override
    public WishlistEntity getWishlistById(final WishlistId wishlist) {
        var document = wishlistMongoDBDao.findByIdAndProductsIsNotEmpty(wishlist.client());

        return document
            .map(it ->
                new WishlistEntity(
                    wishlist,
                    it.products().stream().map(product -> new ProductEntity(product.name())).toList())
                )
            .orElse(null);
    }

    @Override
    public WishlistEntity upsertProductToWishlistById(final WishlistId wishlist, final ProductEntity product) {
        var document = wishlistMongoDBDao.upsertProductById(
            wishlist.client(),
            new ProductMongoDBEntity(product.name())
        );

        return new WishlistEntity(
            wishlist,
            document.products().stream().map(it -> new ProductEntity(it.name())).toList()
        );
    }

    @Override
    public WishlistEntity removeProductFromWishlistById(final WishlistId wishlist, final ProductEntity product) {
        var document = wishlistMongoDBDao.removeProductById(
            wishlist.client(),
            new ProductMongoDBEntity(product.name())
        );

        return document
            .map(newWishlist ->
                new WishlistEntity(
                    wishlist,
                    newWishlist.products().stream().map(it -> new ProductEntity(it.name())).toList())
            )
            .orElse(null);
    }
}
