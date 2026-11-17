# IMDB Clone JavaFX Desktop 

A desktop application that clones some of the most core IMDb functionality such as: browsing movies, TV series, and celebrities with search, ratings, and user authentication.

> Note: This is an older project but remains fairly well-functional 

---

## Features

| Category | Details |
|---|---|
| **Content Browsing** | Browse, search, filter, and sort movies and TV series with full metadata |
| **Celebrity Profiles** | Actor and director profiles with filmography and biographical data |
| **User Authentication** | Registration and login with BCrypt password hashing and session management |
| **Rating System** | Rate content on a 1–10 scale with per-user tracking |
| **Advanced Search** | Multi-criteria search across movies, series, and people (title, genre, year range, rating range) |
| **CRUD Operations** | Full create, read, update, delete for movies, series, seasons, episodes, actors, directors |
| **Data Import** | Parses structured TXT data files at startup into in-memory storage |

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| JavaFX | 21.0.3 | Desktop UI framework |
| Maven | 3.8+ | Build tool and dependency management |
| ControlsFX | 11.2.1 | Enhanced JavaFX UI components |
| Jackson | 2.17.1 | JSON serialization |
| jBCrypt | 0.4 | Password hashing (BCrypt) |
| Apache Commons Text | 1.11.0 | String utilities |
| SLF4J + Simple | 2.0.13 | Logging |
| JUnit 5 | 5.10.2 | Unit testing |
| PMD | 3.22.0 | Static code analysis |

All dependencies are managed via `pom.xml` with Maven. The `javafx-maven-plugin` handles JavaFX runtime and module configuration.

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+

### Build & Run

```bash
git clone https://github.com/Je0Dev/ImdbCloneApp.git
cd ImdbCloneApp
mvn clean compile          # Compile
mvn exec:java              # Run (uses exec-maven-plugin with UserDataRegenerator)
mvn clean compile exec:java  # Build & run in one step
mvn test                   # Run tests
```

The compiler is configured with `-Xlint:all` for full warnings and deprecation checks.

---

## Architecture

### Layered Model View Controller Pattern

```
┌─────────────────────────────────────────────────────────┐
│                    FXML Views (resources/fxml/)          │
│                    (login, movies, series, search, etc.) │
└─────────────────────┬───────────────────────────────────┘
                      │ @FXML injection
┌─────────────────────▼───────────────────────────────────┐
│              Controllers (controllers/)                  │
│    Handle UI events, coordinate views ↔ services         │
│    Extend BaseController or BaseSearchController          │
└─────────────────────┬───────────────────────────────────┘
                      │ method calls
┌─────────────────────▼───────────────────────────────────┐
│               Services (service/)                       │
│    Business logic, data processing, validation          │
│    Singleton pattern via ServiceLocator                  │
└─────────────────────┬───────────────────────────────────┘
                      │ calls
┌─────────────────────▼───────────────────────────────────┐
│           Repositories (repository/)                    │
│    Data access interfaces + in-memory implementations    │
│    Repository pattern for abstraction                    │
└─────────────────────┬───────────────────────────────────┘
                      │ stores/retrieves
┌─────────────────────▼───────────────────────────────────┐
│              Models (model/) + Data files               │
│    Domain objects + TXT file data sources                │
└─────────────────────────────────────────────────────────┘
```

### Service Locator Pattern

Dependency management uses a custom `ServiceLocator` (singleton registry) rather than a DI framework:
All services follow the lazy-initialized double-checked locking singleton pattern.

---



## License
- https://en.wikipedia.org/wiki/MIT_License
---

## Contributing
See the [`docs/`](docs/) directory for comprehensive architecture documentation before contributing. Every source directory also contains a `README.md` with its specific contents and purpose.


*Last updated: June 2026*
