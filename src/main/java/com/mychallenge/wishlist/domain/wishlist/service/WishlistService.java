package com.mychallenge.wishlist.domain.wishlist.service;

import com.mychallenge.wishlist.domain.exception.EntityNotFoundException;
import com.mychallenge.wishlist.domain.exception.MaxWishlistSizeReachedException;
import com.mychallenge.wishlist.domain.wishlist.ProductEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistEntity;
import com.mychallenge.wishlist.domain.wishlist.WishlistId;
import com.mychallenge.wishlist.domain.wishlist.repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private static final int MAX_WISHLIST_SIZE = 10;

    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    private final WishlistRepository wishlistRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public boolean existsProductInClientWishlist(final String client, final String product) {
        var result = wishlistRepository.existsProductInWishlistById(
            new WishlistId(client),
            new ProductEntity(product)
        );

        logger.atInfo()
            .setMessage("check if client wishlist has product")
            .addKeyValue("client", client)
            .addKeyValue("product", product)
            .addKeyValue("result", result)
            .log();

        return result;
    }

    public WishlistEntity getClientWishlist(final String client) {
        var wishlist = wishlistRepository.getWishlistById(new WishlistId(client));

        if (wishlist == null) {
            logger.atError()
                .setMessage("wishlist not found")
                .addKeyValue("client", client)
                .log();

            throw new EntityNotFoundException("Wishlist not found.");
        }

        logger.atInfo()
            .setMessage("wishlist found with {} product(s)")
            .addArgument(wishlist.products().size())
            .addKeyValue("client", client)
            .log();

        return wishlist;
    }

    public WishlistEntity upsertProductToClientWishlist(final String client, final String product) {
        var wishlistSize = wishlistRepository.countProductsInWishlist(new WishlistId(client));

        /*
         *  This if statement may be product a wishlist inconsistent state when
         *  the application instance receiver 2 or two request and process them concurrently
         *  and the current wishlist size is MAX_WISHLIST_SIZE - 1.
         *
         *  Approach to catch this scenario: optimistic lock
         */
        if (wishlistSize + 1 > MAX_WISHLIST_SIZE) {
            logger.atError()
                .setMessage("wishlist size exceeds max wishlist size")
                .addKeyValue("client", client)
                .addKeyValue("product", product)
                .addKeyValue("wishlistSize", wishlistSize)
                .addKeyValue("maxWishlistSize", MAX_WISHLIST_SIZE)
                .log();

            throw new MaxWishlistSizeReachedException();
        }

        var wishlist = wishlistRepository.upsertProductToWishlistById(
            new WishlistId(client),
            new ProductEntity(product)
        );

        logger.atInfo()
            .setMessage("wishlist updated with {} product(s)")
            .addArgument(wishlist.products().size())
            .addKeyValue("client", client)
            .addKeyValue("newProduct", product)
            .log();

        return wishlist;
    }

    public void removeProductFromClientWishlist(final String client, final String product) {
        var existsProductInClientWishlist = existsProductInClientWishlist(client, product);

        if (!existsProductInClientWishlist) {
            logger.atError()
                .setMessage("wishlist not found or empty")
                .addKeyValue("client", client)
                .log();

            throw new EntityNotFoundException("Wishlist not found or empty.");
        }

        var wishlist = wishlistRepository.removeProductFromWishlistById(
            new WishlistId(client),
            new ProductEntity(product)
        );

        logger.atInfo()
            .setMessage("product removed from client wishlist")
            .addKeyValue("client", client)
            .addKeyValue("product", product)
            .addKeyValue("wishlistSize", wishlist.products().size())
            .log();
    }
}
