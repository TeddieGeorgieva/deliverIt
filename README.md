# DeliverIT

## Prerequisites
To be able to run this project you will need to have at least [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

## Description
In this project we develop DeliverIT - a web application that serves the needs of a freight forwarding company.

DeliverIT's customers can place orders on international shopping sites that don’t provide delivery to their location (like Amazon.de or eBay.com) and have their parcels delivered either to the company's warehouses or their own address.

DeliverIT has two types of users - customers and employees. The customers can trach the status of their parcels. The employees have a bit more capabilities. They can add new parcels to the system group them in shipments and track their location and status.


## Functional Operations

**Customers have the ability to:**
- see their past parcels as well as the parcels they have on the way.
- see the status of the shipment that holds a given parcel of theirs.
- pick between “pick up” or “delivery” for each parcel, but only if it has not departed yet (the status of its shipment is not “completed”).

**Employees have the ability to:**
- list/create/modify parcels.
- list/create/modify shipments.
- add/remove parcels from a shipment.
- see shipments that are on the way.
- list/create/modify warehouses.
- see which the next arriving shipment is for a given warehouse.


## Database
The data of the application must be stored in a relational database.
![scheme](/db/db.png)

## Swagger API documentation
http://localhost:8080/swagger-ui/#/

## Running tests
To run the tests:
- Click on Project, then right-click on tests package and choose 'Run all tests'.

## Acknowledgements
This project is an assignment from the [Telerik Academy](https://www.telerikacademy.com/) Alpha program.
