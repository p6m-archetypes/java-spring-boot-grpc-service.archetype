# Spring Boot Service Archetype

## Usage

To get started, [install archetect](https://github.com/p6m-dev/development-handbook)
and render this template to your current working directory:

```bash
archetect render git@github.com:p6m-dev/java-spring-boot-grpc-service-archetype.git
```

For information about interacting with the service, refer to the README at the generated
project's root.

## Prompts

When rendering the archetype, you'll be prompted for the following values:

| Property          | Answer File field | Description                                                                                                         | Example               | Optional |
| ----------------- | ---------------- | ------------------------------------------------------------------------------------------------------------------- | --------------------- | -------- |
| `project`         | project-prefix   | General name that represents the service domain that is used to set the entity, service, and RPC stub names.        | Shopping Cart         | No       |
| `suffix`          | suffix           | Used in conjunction with `project` to set package names.                                                            | Service               | No       |
| `group-prefix`    | group-id         | Used in conjunction with `project` to set package names.                                                            | {{ group-id }}        | No       |
| `team-name`       | team-name        | Identifies the team that owns the generated project. Used to label published artifacts and in the generated README. | Growth                | No       |
| `service-port`    | service-port     | Sets the port used for gRPC traffic                                                                                 | {{ service-port }}    | No       |
| `management-port` | management-port  | Sets the port used to monitor the application over HTTP                                                             | {{ management-port }} | No       |
| `persistence`     | persistence      | Type of persistence to use, e.g., CockroachDB or None.                                                             | CockroachDB           | Yes      |
| `platform-version`| platform-version | Version of the parent project.                                                                                      | 1.0.0-SNAPSHOT        | Yes      |

For a list of all derived properties and examples of the property relationships, see [archetype.yml](./archetype.yml).

## What's Inside

This archetype is based on the [SpringBoot](https://spring.io/projects/spring-boot) framework and uses [Maven](https://maven.apache.org/)
as its build system.

Features include:

- Out-of-the box [endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints), including
  `/health`, `/info`, `/metrics`, `/env`, `/liquibase`, and `/threaddump`.
- [Hibernate](https://hibernate.org/) for ORM
- [Liquibase](https://www.liquibase.org/) for changeset management
- Simple CRUD over [gRPC](https://grpc.io/)
- gRPC stub publication based on the project name
- Example GitLab CI configuration
- Docker image publication to artifactory
- Load tests using [k6](https://k6.io/) for both HTTP and gRPC calls
- Application configuration through property files, environment variables, and CLI arguments.
- Integration with [Tilt](https://tilt.dev/) to support local k8s development
- Out-of-the-box Postgres and [LocalStack](https://github.com/localstack/localstack) using [Test Containers](https://www.testcontainers.org/)
