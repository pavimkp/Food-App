# Food Ordering Backend (Spring Boot 3.5, Java 17, MySQL)

## 1. Database (one time, MySQL Workbench)
Run `database/01_create_database_and_user.sql` then `database/02_create_tables.sql`.
(Skip if you already did this. Tables must exist: the app only VALIDATES the schema.)

## 2. Open in IntelliJ
File > Open > select the `pom.xml` > Open as Project > Trust.
Maven home path = your working Maven (Settings > Build Tools > Maven). Project SDK = 17.

## 3. Environment variables (Run > Edit Configurations > FoodOrderingApplication > Environment variables)
DB_USERNAME=food_app
DB_PASSWORD=<password from script 1>
ADMIN_USERNAME=admin
ADMIN_PASSWORD=<choose one>

## 4. Run
Run `FoodOrderingApplication`. Success = "Started FoodOrderingApplication" on port 8080.

## 5. Test with Postman
Import `postman/FoodOrdering.postman_collection.json`, set the collection variable `adminPassword`,
then run folders 1 to 5 in order (Collection Runner works).

## API
Public : GET /api/restaurants, GET /api/restaurants/{id}, GET /api/restaurants/{id}/food-items,
         GET /api/food-items/search?name=, POST /api/orders, GET /api/orders/{id}
Admin (HTTP Basic, ROLE_ADMIN): GET|POST /api/admin/restaurants, POST /api/admin/food-items,
         PUT|DELETE /api/admin/food-items/{id}, GET /api/admin/orders
