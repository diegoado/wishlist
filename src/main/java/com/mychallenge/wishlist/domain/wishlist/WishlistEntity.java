package com.mychallenge.wishlist.domain.wishlist;

import java.util.List;

public record WishlistEntity(WishlistId wishlist, List<ProductEntity> products) {}
