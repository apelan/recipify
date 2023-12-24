# Recipify API
<hr/>

### Technology used
Recipify API is created using Java and web framework Spring Boot
- Secured using Bearer Authentication with [Json Web Tokens](https://jwt.io/)
- All data is persisted in PostgreSQL database, integration tests are performed with H2 in-memory database
- Database migrations are performed with Flyway
- OpenAPI(Swagger) is used for API documentation
- Emails are verified with [Hunter](https://hunter.io/)
- User information is fetched from [Clearbit](https://clearbit.com/)
- Application and database are dockerized, using [Docker Compose](https://docs.docker.com/compose/)


### Link to access
Recipify API is hosted on Railway and can be access [via this link](#TODO). <br>


### Prerequisites to run on local machine
- Docker / Docker compose


### Run on local machine
In order to run on local machine go to projects root folder, open command prompt and run the following command to build a project. <br>
**Note**: this step will also run all unit and integrations tests
```shell
gradlew build
```

When project is build, we will use docker compose to build database and service image and run them. 
To achieve this we can use following command:
```shell
docker compose up
```
Application by default is running on port 8080, it will be available at:
http://localhost:8080


<hr/>

### How to's

- <b>Docker installation</b> <br>
Download from [website](https://www.docker.com/), if needed follow official instructions for 
[MacOS](https://docs.docker.com/desktop/install/mac-install/), 
[Windows](https://docs.docker.com/desktop/install/windows-install/) 
or [Linux](https://docs.docker.com/desktop/install/linux-install/) installations.


- <b>Test API</b> <br>
  We can test API with Swagger by accessing [link](#TODO) or using any rest client. I've provided [Postman Collection](#TODO) in root folder of project.<br> 
Postman Collection is configured for local usage, to test against deployed application on Railway you need to change url. <br>

- <b>Use API</b> <br>
  In order to use API, we must hit /login endpoint which will return JWT. <br>
  We can use that JWT token as Bearer Authentication header for other endpoints, keep in mind that it expires in 60mins. <br>
  If you use provided Postman Collection there is pre-script which calls /login endpoint before each request, thus making API testing easier.
