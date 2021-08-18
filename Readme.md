## Clean-me-up

### Preconditions
* The application exposes a public REST end-point for sending emails
* The application acts like a facade towards a legacy email library located in module **clean-me-up-support**
* The current implementation is rather ugly
* The API contract has not yet been published so you don't need to be backwards compatible
* Application is located in module **clean-me-up-rest**
* Don't change the legacy module **clean-me-up-support**

### Assignment
* Refactor the code in module **clean-me-up-rest** as you wish
* When you are done you should be rather happy with the code when it comes to: maintenance, quality, structure, architecture, testing, validation etc
* If you left things behind - please note that down in a read-me file

Estimated time ~4 hours, but feel free to impress us. 

### Stretch goals
When implementing a service - there are lots of things to consider. Some of those are:
* DevOps
* Security
* Documentation
* Versioning
* Monitoring
* Logging
* CI / CD
* Configuration
* Data integrity
* Fault handling

### Implementation
REST API started at https://localhost:8080/api/v1
Open API docs available at https://localhost:8080/api/docs
Swagger UI available at https://localhost:8080/api/swagger-ui
Actuator endpoints available at https://localhost:8080/api/actuator

### Endpoints
http://localhost:8080/api/v1/mail/send POST - send mail message
http://localhost:8080/api/v1/mail/messages GET - view sent and received messages
http://localhost:8080/api/v1/auth/token POST - retrieve authentication token
http://localhost:8080/api/v1/auth/info GET - retrieve authentication details
http://localhost:8080/api/v1/auth/users GET - retrieve registered users

