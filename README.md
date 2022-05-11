# qikserve-challenge Project

- [About the project](#about-the-project)
- [Running the project](#running-the-project)
- [Follow-up questions](#follow-up-questions)

## About the project

The project was build using Quarkus framework and an H2 _in memory_ database. I also used
Hibernate and Panache, to simplify database tasks.

The API has the following endpoints:

### `POST /basket` - To add a new item to a basket

The method receives an object with a customer ID, a product ID and the amount of itens.
The system allows multiple customer to shop at the same time, but each custome is
allowed to have just one open basket at time.

When adding a product that is already in the basket, then the amount is increased by
the given amount.

Example of a request body:

```json
{
  "productId": "PWWe3w1SDU",
  "customerId": "CUSTOMER_ID",
  "amount": 1
}
```

### `GET /basket/{customerId}` - To show the currently open basket of the user

This method returns the basket data of the open basket of the given customer.
If there is no open basket, then an error with 404 status is returned.

### `POST /basket/{customerId}/checkout` - To close a basket

This method closes the current basket of the customer, preventing to add new items.
If the `POST /basket` is called aftwards, then a new basket is created.

The method receives no body.

### `POST /promotion` - To create a new promotion

The promotion is applied if the basket has equal or more unities of the product in the
promotion. The discount in the promotion is apllied to each unity of the product in the
basket.

It's not possible to have more than one promotion to a single product.

Example of a request body:
```json
{
    "productId": "Dwt5F7KAhi",
    "minAmount": 5,
    "unitDiscount": 549
}
```

### `GET /promotion` - To list all promotions

This method return the list of the promotions that has been inserted.

### `POST /promotion/example` - To fill the database with a promotion for every product

This method allows to populate the promotion database with ease for testing purposes.
The promotions are all inserted with `minAmount = 5` and discount equal to half the price
of the product.

## Running the project

If maven is installed in the machine just run:
```shell
>mvn clean compile quarkus:dev
```

It's also possible to use the embed maven version by running:
```shell
>.\mvnw clean compile quarkus:dev
```

The Wiremock of the products API is not included in the project, but it's configurated
to access the API at the port 8081, this can be changed in `src/main/resources/application.properties` file.

## Follow-up questions

1. How long did you spend on the test?

    R: It took me two days to finish the test. Approximately 10 hours straight of work.

2. What would you add if you had more time?

    R: I would add a more sophisticated promotion system, with combo options, for example.

3. How would you improve the product APIs that you had to consume?

    R: I would add the capacity to filter products by its name.

4. What did you find most difficult?

    R: The most difficult part to me was to decide how to implement the promotions system.

5. How did you find the overall experience, any feedback for us?

    R: I liked the challenge, I felt that it was a good way to explore various skills,
    but I think it would be nice to have more information on how about the promotions
    should be defined and applied to the basket. I just assumed one way to make it. 