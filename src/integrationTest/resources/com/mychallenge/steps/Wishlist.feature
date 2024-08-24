Feature: Wishlist

    Scenario: Check if wishlist does not contains the product
        Given I make a request to verify if client "client1" wishlist has product "product"
        And API status code must be 200
        Then Wishlist must not contain the product

    Scenario: Check if wishlist contains the product
        Given Exist a client "client2" wishlist
            | product1 |
            | product2 |
        When I make a request to verify if client "client2" wishlist has product "product1"
        And API status code must be 200
        Then Wishlist must contain the product

    Scenario: Get wishlist
        Given Exist a client "client3" wishlist
            | product1 |
            | product2 |
        When I make a request to get client "client3" wishlist
        And API status code must be 200
        Then Returned wishlist contains the products
            | product1 |
            | product2 |

    Scenario: Try to get non-existent wishlist
        Given I make a request to get client "client171" wishlist
        Then API status code must be 404

    Scenario: Upsert new product in a newly wishlist
        Given I make a request to upsert product in client "client4" wishlist
            | product_name |
            | product1     |
        And API status code must be 202
        Then Returned wishlist contains the products
            | product1 |

    Scenario: Upsert new product in an pre-existing wishlist
        Given Exist a client "client5" wishlist
            | product1 |
            | product2 |
        When I make a request to upsert product in client "client5" wishlist
            | product_name |
            | product3     |
        And API status code must be 202
        And Returned wishlist contains the products
            | product1 |
            | product2 |
            | product3 |
        Then Stored wishlist must contain the product "product3"

    Scenario: Try to upsert new invalid product
        Given I make a request to upsert product in client "client171" wishlist
            | product_name |
            |              |
        And API status code must be 400

    Scenario: Try to upsert new product in a wishlist too large
        Given Exist a client "client6" wishlist
            | product1 |
            | product2 |
            | product3 |
            | product5 |
            | product5 |
        When I make a request to upsert product in client "client6" wishlist
            | product_name |
            | product6     |
        And API status code must be 400
        Then Stored wishlist must not contain the product "product6"

    Scenario: Remove a product from wishlist
        Given Exist a client "client7" wishlist
            | product1 |
        When I make a request to delete a product "product1" from client "client7" wishlist
        And API status code must be 204
        Then Stored wishlist must not contain the product "product1"

    Scenario: Try to remove a product from wishlist that does not contains it
        Given Exist a client "client8" wishlist
            | product1 |
        When I make a request to delete a product "product2" from client "client8" wishlist
        Then API status code must be 404

    Scenario: Try to remove a product from non-existent wishlist
        Given I make a request to delete a product "product1" from client "client171" wishlist
        Then API status code must be 404

