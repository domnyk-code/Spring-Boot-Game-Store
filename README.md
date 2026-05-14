# Game Store API 
A simple backend for an online video game store, built with Spring Boot. Visitors can browse and search games; admins can manage the catalogue.
Uses Docker for containerization of the app and database storage.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3, Spring MVC |
| Security | Spring Security (HTTP Basic) |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Testing | JUnit 5, Mockito, Testcontainers |
| Containerisation | Docker, Docker Compose |

---

## Architecture 
The API uses simple Spring Boot Model - Repository - Service - Controller architecture for providing answers to requests.
Both Video Games and Genres they belong to have their own dedicated schemas.
PostgreSQL is used for data storage for the API.

---

## Project Structure
```
game-store
├───.mvn/wrapper/
│   └─── maven-wrapper.properties       
├───src/
│   ├─── main/java/com/example/cdpr_game_store
│   │    ├─── CdprGameStoreApplication.java
│   │    ├─── controller/
│   │    │   ├─── GenreController.java
│   │    │   └─── VideoGameController.java
│   │    ├─── entity/
│   │    │    ├─── Genre.java
│   │    │    └─── VideoGame.java
│   │    ├─── repository/
│   │    │    ├─── GenreRepository.java
│   │    │    └─── VideoGameRepository.java
│   │    ├─── security/
│   │    │    └─── SecurityConfig.java
│   │    └─── service/
│   │    │    ├─── GenreService.java
│   │    │    └─── VideoGameService.java
│   ├─── main/resources/
│   │    └───application.properties
│   └─── test/
│        └─── java/com/example/cdpr_game_store/
│              ├─── repository_test/
│              │    └─── RepositoryTest.java
│              └─── service_test/
│                   └─── ServiceTest.java
├─── docker-compose.yml
├─── Dockerfile
├─── mvnw
├─── mvnw.cmd
└─── pom.xml

```

---

## Launching the API

### Prerequisites
- Java 21+
- Maven
- Docker (Desktop on Windows)

### Run with Docker Compose

After cloning the repository, use Docker compose commands inside it to start up the API:

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`.  
A pgAdmin GUI is available at `http://localhost:5050` (login: `game@store.com` / `admin`).

PGAdmin requires server connection to view docker database. In order to connect, a new server connection must be made within the GUI, using the following credentials:
- **Host name**: postgres (the name of the container)
- **Port**: 5432
- **Database**: gamestore
- **Username**: admin
- **Password** : secret

### Stop the application

```bash
docker compose down
```

---

## API Endpoints

### Games (Public)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/games` | List all games |
| GET | `/api/games/{id}` | Get a game by ID |
| GET | `/api/games/search?title=` | Search games by title |
| GET | `/api/games/genre/{name}` | Filter games by genre |

### Games (Admin only)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/games` | Add a new game |
| PUT | `/api/games/{id}` | Update a game |
| DELETE | `/api/games/{id}` | Delete a game |

### Genres (Public)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/genres` | List all genres |

### Genres (Admin only)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/genres` | Add a new genre |
| DELETE | `/api/genres/{id}` | Delete a genre |

---

## Authentication

The API uses **HTTP Basic Auth**. Default credentials:

| User | Password | Role |
|---|---|---|
| `admin` | `secret` | ADMIN |
| `visitor` | `password` | USER |

GET endpoints are public. All write operations require the `ADMIN` role.

---

## Example Requests

```http
### Create a genre (admin)
POST http://localhost:8080/api/genres
Authorization: Basic admin secret
Content-Type: application/json

{ "name": "RPG" }

### Add a game with multiple genres (admin)
POST http://localhost:8080/api/games
Authorization: Basic admin secret
Content-Type: application/json

{
  "title": "The Witcher 3",
  "price": 29.99,
  "description": "Open world RPG",
  "genres": [{ "id": 1 }, { "id": 2 }]
}

### Browse all games (public)
GET http://localhost:8080/api/games
```

---


## Data Model

```
Genre ◄────────────────────── game_genres ──────────────────────► Game
(id, name)                  (genre_id, game_id)            (id, title,
                                                             price, description)
```

A game can belong to multiple genres (Many-to-Many relationship).

---

## Problems and limitations (TODO)
- There is currently no error handling behaviour present in controllers. Custom message and error code should be introduced for invalid requests.
- Passwords and links in Docker are stored in plaintext. For development this is okay, but should be using .env file and secrets instead.
- Not every component has its own unit tests - only Repository and Service parts tests were made due to time constraints.
