# Farm Management API

This API was made to simulate the management of farms, including its crops and fertilizers. It's been built using Java, Spring Web, Spring Data, Spring Boot, Spring Security, JWT, SpEL,
JUnit, Mockito, MySQL, Docker.


One farm can have many crops in it and one crop can have several fertilizers being used, so one-to many and many-to-many relationships have been created between the tables farm and crop, and crop and fertilizers, respectively. This can be better understood through the entity relation diagram below:

![Entity Relation Diagram](./ERD.png)

The application uses a MySQL database hosted inside a Docker container. Authentication and authorization have been configured using Spring Security and JWT. Error handling is done by a Global Controller Advice inside the advice folder.

The following routes and methods have been created in the project:

## /farms
- GET
  - Description: returns all farms in the FarmDto format with a HTTP status 200.
  - This route uses authorization to limit the access to it to the following roles: USER, MANAGER, ADMIN


- GET /farmId
  - Description: returns a farm by its id. If found, returns the farm in the FarmDTO format with a http status 200. If not, returns a not found message with a HTTP status 404.


- POST 
  - Description: creates a farm. Receives the FarmCreationDTO format in the body of the request and returns the created farm in the FarmDTO format with a HTTP status 201.


- PUT /farmId
  - Description: updates a farm. Receives the FarmCreationDTO format in the body of the request and returns the updated farm in the FarmDTO format with a HTTP status 200. If the farm is not found, returns a not found message with a HTTP status 404.
  

- DELETE /farmId
  - Description: deletes a farm by its id. If found, returns the deleted farm in the FarmDTO format with a http status 200. If not, returns a not found message with a HTTP status 404.
  

- POST /farmId/crops
  - Description: creates a crop and associates it with a farm. Receives the CropCreationDTO format in the body of the request and returns the created crop in the CropDTO format with a HTTP status 201. If the farm doesn't exist, returns a not found message with a HTTP status 404.
  

- GET /farmId/crops
  - Description: returns all of the crops belonging to a farm in a list, each one in the CropDTO format with a HTTP status 200. If the farm doesn't exist, returns a not found message with a HTTP status 404.