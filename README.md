# Farm Management API

This API was made to simulate the management of farms, including its crops and fertilizers. It's been built using Java, Spring Web, Spring Data, Spring Boot, Spring Security, JWT, SpEL,
JUnit, Mockito, MySQL, Docker.


One farm can have many crops in it and one crop can have several fertilizers being used, so one-to many and many-to-many relationships have been created between the tables farm and crop, and crop and fertilizers, respectively. This can be better understood through the entity relation diagram below:

![Entity Relation Diagram](./ERD.png)

The application uses a MySQL database hosted inside a Docker container. Authentication and authorization have been configured using Spring Security and JWT. Error handling is done by a Global Controller Advice inside the advice folder.

# Entities
The application is composed of four entities:

### Farm
Represents a farm. To create one, the following data have to be supplied (FarmCreationDTO):
```
{
  name,
  size
}
```
Whenever the response includes a farm, the FarmDTO format is used, returning the following data:
```
{
  id,
  name,
  size
}
```
### Crop
Represents a crop. To create one, the following data have to be supplied (CropCreationDTO):
```
{
    name,
    plantedArea,
    plantedDate,
    harvestDate
}
```
Whenever the response includes a crop, the CropDTO format is used, returning the following data:
```
{
    id,
    name,
    plantedArea,
    plantedDate,
    harvestDate,
    farmId
}
```
### Fertilizer
Represents a fertilizer. To create one, the following data have to be supplied (FertilizerCreationDTO):
```
{
    name,
    brand,
    composition
}
```
Whenever the response includes a fertilizer, the FertilizerDTO format is used, returning the following data:
```
{
    id,
    name,
    brand,
    composition
}
```
### Person
Represents a person. To create one, the following data have to be supplied (PersonCreationDTO):
```
{
  username,
  password,
  role
}
```
Whenever the response includes a person, the PersonDTO format is used, returning the following data:
```
{
  id,
  username,
  role
}
```

# Routes
The following routes and methods have been created in the project:

### /farms
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

### /crop

- GET
  - Description: returns all crops in the CropDto format with a HTTP status 200.
  - This route uses authorization to limit the access to it to the following roles: MANAGER, ADMIN


- GET /cropId
  - Description: returns a crop by its id. If found, returns the crop in the CropDTO format with a http status 200. If not, returns a not found message with a HTTP status 404.
  

- GET ?start=dd/MM/yyyy&end=dd/MM/yyyy
  - Description: returns all crops that have been harvested between two set dates passed as query strings.


- POST /cropId/fertilizers/fertilizerId
  - Description: associates a fertilizer with a crop. Returns a success message with a HTTP status 201. If the crop or the fertilizer doesn't exist, returns a not found message with a HTTP status 404.


- GET /cropId/fertilizers
  - Description: returns all of the fertilizers belonging to a crop in a list, each one in the FertilizerDTO format with a HTTP status 200. If the crop doesn't exist, returns a not found message with a HTTP status 404. 


### /fertilizers
- POST
  - Description: creates a fertilizer. Receives the FertilizerCreationDTO format in the body of the request and returns the created fertilizer in the FertilizerDTO format with a HTTP status 201.


- GET
  - Description: returns all fertilizers in the FertilizerDto format with a HTTP status 200.


- GET /fertilizerId
  - Description: returns a fertilizer by its id. If found, returns the fertilizer in the FertilizerDTO format with a http status 200. If not, returns a not found message with a HTTP status 404.


### /persons
- POST
  - Description: creates a person. Receives a PersonCreationDTO in the request body and returns a PersonDTO with a HTTP status 201.
  - In order to create a Person, it is necessary to inform its role. This role is the atribute that's going to be used to grant or prohibit access to certain routes.
  - Due to security reasons, the password is included when creating a Person but excluded from the return of the request.


### /auth
- POST
  - Description: route used to sign in to the system. Receives an AuthDTO in the body of the request and returns a TokenDTO with the token created by JWT (if the credential supplied are correct)

# Database and deploy
The application uses a MySQL database hosted inside a Docker container. There is a docker-compose file with the instructions to create the container with the database and a Dockerfile with the instructions to build the application using the JDK and copy the built .jar to the container in order to save resources.