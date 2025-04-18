# {{ project-title }}

**// TODO:** Add description of your project's business function.

Generated from the [Java Spring Boot gRPC Service Archetype](https://github.com/p6m-archetypes/java-spring-boot-grpc-service-archetype). 

[[_TOC_]]

## Prereqs
Running this service requires JDK 21+ and Maven to be configured with an Artifactory encrypted key.
For development, be sure to have Docker installed and running locally.

## Overview


## Build System
This project uses [Maven](https://maven.apache.org/) as its build system. Common goals include

| Goal    | Description                                                           |
|---------|-----------------------------------------------------------------------|
| clean   | Removes previously generated build files within `target` directories  |
| package | Builds and tests the project.                                         |
| install | Installs the project to your local repository for use as a dependency |

For information on Spring-specific tasks, see the [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#?.?) docs.

This is a multi-module project. To run a goal in a particular module, use the `-f` flag. For example, we could clean and
regenerate the files from our protobuf definition like this:
```bash
$ mvn -f {{ artifact-id }}-grpc clean generate-sources
```

## Running the Server
This server accepts connections on the following ports:
- {{ service-port }}: used for application gRPC Service traffic.
- {{ management-port }}: used to monitor the application over HTTP (see [Actuator endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)).{% if persistence != 'None' %}
- {{ database-port }}: exposes the ephemeral database port{% endif %}
- {{ debug-port }}: remote debugging port

Before starting the server, you must first create the application jars:
```bash
$ mvn install
```

Next, start the server locally or using Docker. You can verify things are up and running by looking at the [/health](http://localhost:{{ management-port }}/health) endpoint:
```bash
$ curl localhost:{{ management-port }}/health
```
You can try to create an entity using a gRPC client, like [grpcurl](https://github.com/fullstorydev/grpcurl) (CLI) or [grpcui](https://github.com/fullstorydev/grpcui) (GUI).
For example,
```bash
$ grpcurl -plaintext -d '{"name": "test"}' localhost:{{ service-port }} \
    {{ root_package }}.grpc.v1.{{ ProjectPrefix }}{{ ProjectSuffix }}/Create{{ ProjectPrefix }}
```

### Local
From the project root, install then run the server:
```bash
$ mvn clean install
$ mvn -f {{ artifact-id }}-server spring-boot:run -Dspring-boot.run.jvmArguments="-Dtemp-db -Dlocalstack"
```
To run the server in a non-blocking fashion, refer to the `spring-boot:start` and `spring-boot:stop` goals. More information
about the available JVM arguments can be found in the [Runtime Switches](#runtime-switches) section.

## Runtime Switches

The following are switches/settings that can be turned on or off when starting the server to affect how it operates. These
switches are use primarily as a developer convenience.

| System Property | Environment Variable | Function |
|:---|---|:---|
| -Dtemp-db |   | Starts the server with a dynamically generated Postgres DB in a Docker container on a randomized port. The JDBC URL for the database is written to the logs, and can be copy/pasted into your database IDE. The username/password is postgres/password. |
| -Dlogging-structured |   | Turns on structured (JSON) logging, and turns off the Spring Boot banner. |
| -Dlogging-pretty |   | Turn on pretty printing when using structured-logging. |
| -Dlog-sql |   | Turns on SQL logging. |
| -Dlocalstack |   | Spins up AWS resources (ex. SQS) along side the server using [LocalStack](https://github.com/localstack/localstack) and Docker.|

## Modules

| Directory | Description |
| --------- | ----------- |
| [{{ artifact-id }}-api]({{ artifact-id }}-api/README.md) | Service Interfaces with a gRPC model. |
| [{{ artifact-id }}-bom]({{ artifact-id }}-bom/README.md) | {{ project-title }} Bill of Materials. |
| [{{ artifact-id }}-client]({{ artifact-id }}-client/README.md) | gRPC Client. Implements the API. |
| [{{ artifact-id }}-core]({{ artifact-id }}-core/README.md) | Business Logic. Abstracts Persistence, defines Transaction Boundaries. Implements the API. |
| [{{ artifact-id }}-grpc]({{ artifact-id }}-grpc/README.md) | gRPC/Protobuf spec. |
| [{{ artifact-id }}-integration-tests]({{ artifact-id }}-integration-tests/README.md) | Leverages the Client to test the Server and it's dependencies. |{% if persistence != 'None' %}
| [{{ artifact-id }}-persistence]({{ artifact-id }}-persistence/README.md) | Persistence Entities and Data Repositories. Wrapped by Core. | {% endif %}
| [{{ artifact-id }}-server]({{ artifact-id }}-server/README.md) | Transport/Protocol Host.  Wraps Core. |

## Key Dependencies

| Name                                                                                           | Scope                                           | Description                                                         |
|------------------------------------------------------------------------------------------------|-------------------------------------------------|---------------------------------------------------------------------|
| [gRPC Spring Boot Starter](https://github.com/LogNet/grpc-spring-boot-starter)                 | API/Remoting                                    | Auto-configures an embedded gRPC server integrated with Spring Boot |
| [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/2.5.1/reference/html/#reference) | Persistence                                     | ORM.                                                                | 
| [AssertJ](https://joel-costigliola.github.io/assertj/)                                         | Unit Tests                                      | Fluent test assertions.                                             |
| [Mockito](https://site.mockito.org/)                                                           | Unit Tests                                      | Provides mocking and spying functionality.                          |
| [TestContainers](https://www.testcontainers.org/)                                              | Unit Tests, Containers                          | Programmatic container management.                                  |
| [LocalStack](https://github.com/localstack/localstack)                                         | Provides Docker images for common AWS services. |                                                                     |

## Contributions
**// TODO:** Add description of how you would like issues to be reported and people to reach out.
