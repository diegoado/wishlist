# Wishlist

Demo wishlist application developed using Java 21 and Spring Boot 3.2.X.

## Setup application

To run wishlist application, firstly we should set up our local machine

### Download docker-compose

1. You need verify if docker already have been installed, otherwise install it:

```shell
docker --version
```

2. Now, install docker-compose:

```shell
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin /docker-compose
```

3. As superuser:

```shell
sudo -i
```

4. Make docker-compose file executable:

```shell
chmod +x /usr/local/bin/docker-compose
```

5. And check if it was installed correctly:

```shell
docker compose-version
```

### Download do SdkMan

We will use the [SDKMan](https://get.sdkman.io) to manage our Java local versions.

1. Install it running the follow command:

```shell
curl -s "https://get.sdkman.io" | bash
```

2. Active the SDKMan:

```shell
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

3. And check if it was installed correctly:

```shell
sdk version
```

**You should see something like that:
```
SDKMAN!
script: 5.18.2
native: 0.4.6**
```

### Download Java

Now you already have the SDKMan, choose one available Java distribution:

```shell
sdk install java x.y.z-open
```

Or, if you prefer a oracle distribution, like *21.0.2-oracle*, type:

```shell
sdk install java 21.0.2-oracle
```

## Getting Started

Now, we can start wishlist application container and their dependencies:

```bash
docker-compose up
```

After that, you could open [local Swagger](http://localhost:8080/swagger-ui/index.htm)
and try to execute one of available functionality.

1. Upsert new product on client wishlist

````shell
curl --location --request PUT 'http://localhost:8080/wishlist/client' \
--header 'Content-Type: application/json' \
--data-raw '{"product_name": "product1"}'
````

2. Get client wishlist

````shell
curl --location --request GET 'http://localhost:8080/wishlist/client' \
--header 'Content-Type: application/json'
````

3. Remove a product from client wishlist

````shell
curl --location --request DELETE 'http://localhost:8080/wishlist/client/products/product1' \
--header 'Content-Type: application/json'
````

4. Check if a product exists on client wishlist

````shell
curl --location --request GET 'http://localhost:8080/wishlist/client/products/product1/check' \
--header 'Content-Type: application/json'
````

The wishlist application uses a MongoDB as database,
so the local stack makes available a container of [mongo-express](https://github.com/mongo-express/mongo-express)

**According to credentials:
```
username: user
password: mongodb
```

## Getting Started without docker

If you prefer, is possible start the wishlist application standalone, so we first make gradlew wrapper executable:

```bash
chmod +x gradlew
```

And now, type the follow command, replacing application required environment variables:

```bash
SPRING_PROFILES_ACTIVE=local MONGODB_HOST=<<host>> MONGODB_PORT=<<port>> MONGODB_USERNAME=<<username>> MONGODB_PASSWORD=<<password>> ./gradlew bootRun
```
