package com.mychallenge.wishlist.application.controller;

import com.mychallenge.wishlist.application.contract.ResponseFragment;
import com.mychallenge.wishlist.application.contract.request.ProductRecord;
import com.mychallenge.wishlist.domain.wishlist.WishlistEntity;
import com.mychallenge.wishlist.domain.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PutMapping(path = "/{client}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFragment<WishlistEntity>> upsertProductToClientWishlist(
        @PathVariable
        @NotNull
        @NotBlank
        String client,
        @RequestBody
        @Valid
        ProductRecord product
    ) {
        var response = wishlistService.upsertProductToClientWishlist(
            client,
            product.name()
        );
        return ResponseEntity.accepted().body(new ResponseFragment<>(response));
    }

    @GetMapping(path = "/{client}")
    public ResponseEntity<ResponseFragment<WishlistEntity>> getClientWishlist(
        @PathVariable
        @NotNull
        @NotBlank
        final String client
    ) {
        var response = wishlistService.getClientWishlist(
            client
        );
        return ResponseEntity.ok(new ResponseFragment<>(response));
    }

    @GetMapping(path = "/{client}/products/{product}/check")
    public ResponseEntity<ResponseFragment<Boolean>> existsProductInClientWishlist(
        @PathVariable
        @NotNull
        @NotBlank
        final String client,
        @PathVariable
        @NotNull
        @NotBlank
        final String product
    ) {
        var response = wishlistService.existsProductInClientWishlist(
            client,
            product
        );
        return ResponseEntity.ok(new ResponseFragment<>(response));
    }

    @DeleteMapping(path = "/{client}/products/{product}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeProductFromClientWishlist(
        @PathVariable
        @NotNull
        @NotBlank
        final String client,
        @PathVariable
        @NotNull
        @NotBlank
        final String product
    ) {
        wishlistService.removeProductFromClientWishlist(
            client,
            product
        );
        return ResponseEntity.noContent().build();
    }
}
