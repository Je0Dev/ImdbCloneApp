# IMDb Clone App - Updates & Improvements Document

## Overview
This document outlines improvements for cleaning up, fixing, and enhancing the existing IMDb Clone application. Focus is on functionality, removing unused code, and proper documentation.

---

## 1. Code Cleanup & Dead Code Removal

### 1.1 Unused Files & Directories
- [ ] **Remove duplicate directories**: Delete `home/user/project/` (contains duplicate source files)
- [ ] **Clean up generated files**: Remove `.ruff_cache/`, `master.aux`, `master.log` from project root
- [ ] **Remove old data files**: Check if legacy data files in `src/main/resources/data/` are still needed

### 1.2 Unused Code in Controllers
- [ ] **MainController.java (lines 66-75)**: Remove unused fields (`homeContent`, `data`, `sessionToken`)
- [ ] **MainController.java (lines 126-270)**: Simplify `showHome()` - duplicate navigation logic
- [ ] **MoviesController.java**: Remove duplicate column setup code (lines 456-621 have repeats)
- [ ] **MoviesController.java**: Remove bug in `sortMovieTable()` - lines 886-900 incorrectly trigger edit dialog after sorting

### 1.3 Unused FXML Components
- [ ] **Unused FXML files**: Check if all FXML files are actually used
- [ ] **Dead button handlers**: Remove `@FXML` methods that are never called

### 1.4 Unused Imports
- [ ] **Run PMD/Maven**: Identify and remove unused imports across all Java files
- [ ] **Common unused imports**:
  - `javafx.scene.layout.GridPane` in controllers where not used
  - `java.util.HashMap` where `Map.of()` could be used

---

## 2. Bug Fixes

### 2.1 Critical Bugs
- [ ] **MoviesController.sortMovieTable()** - Method incorrectly triggers movie edit dialog after sorting (lines 886-900). Remove the conditional block that calls `showMovieEditDialog`
- [ ] **MoviesController genre column** - Duplicate cell value factory setup (lines 416-422 and 466-475)
- [ ] **MainController authentication** - `checkAuthentication()` always returns true (line 309), making auth checks meaningless
- [ ] **Memory leak** - `searchDebounce` timer not properly stopped in `clearSearch()` or controller cleanup

### 2.2 UI/UX Bugs
- [ ] **Search field styling** - Missing semicolons in inline styles cause rendering issues
- [ ] **Dialog sizing** - `showMovieEditDialog()` sets `dialog.setResizable(false)` then tries to set min dimensions on DialogPane (inconsistent)
- [ ] **Status label** - `statusLabel` may be null in some code paths (MoviesController line 687)

### 2.3 Data Issues
- [ ] **Duplicate movies** - `loadMovies()` filters duplicates but `filteredMovies` not properly cleared before adding unique results
- [ ] **Rating system** - `getCurrentUserId()` throws exception for guests but UI allows guests to rate (inconsistent behavior)

---

## 3. Code Quality Improvements

### 3.1 Code Duplication
- [ ] **Extract common dialog methods**: Create utility class for confirmation/error dialogs
- [ ] **Merge similar filter logic**: Movies and Series controllers likely have duplicate filtering code
- [ ] **Common table setup**: Extract table column setup into reusable helper methods

### 3.2 Refactoring Opportunities
- [ ] **MoviesController (1475 lines)**: Split into smaller classes:
  - `MoviesTableManager` - Table setup and rendering
  - `MovieSearchHandler` - Search and filter logic
  - `MovieEditHandler` - Add/Edit/Delete operations
- [ ] **MainController (852 lines)**: Split navigation logic from UI state management
- [ ] **Remove singleton anti-pattern in services**: Consider dependency injection

### 3.3 Error Handling
- [ ] **Improve exception messages**: Add context to exceptions (e.g., "Failed to load movies from file: {path}")
- [ ] **Add fallback data**: If file loading fails, use sample data instead of crashing
- [ ] **User-friendly errors**: Replace technical error messages with user-friendly versions

---

## 4. Documentation Improvements

### 4.1 JavaDoc
- [ ] **Add missing JavaDoc**: Document all public methods in:
  - `ServiceLocator`
  - `NavigationService`
  - `BaseContentService`
  - All repository interfaces
- [ ] **Update outdated JavaDoc**: Fix incorrect parameter names or missing @return tags
- [ ] **Add @throws documentation**: Document all exceptions that can be thrown

### 4.2 Inline Comments
- [ ] **Remove obvious comments**: Delete comments that describe simple operations (e.g., `// Add button to list`)
- [   ] **Keep meaningful comments**: Keep comments explaining WHY, not WHAT
- [ ] **Fix grammar**: Comments should be complete sentences with proper grammar

### 4.3 Example Improvements

**Before:**
```java
// Set up the movie title column
movieTitleColumn.setCellValueFactory(cellData -> {
```

**After:**
```java
/**
 * Configures the title column to display movie titles.
 * Uses SimpleStringProperty for observable updates.
 */
movieTitleColumn.setCellValueFactory(cellData -> {
```

---

## 5. Feature Verification Checklist

### 5.1 Working Features (Verify & Document)
- [ ] User registration and login
- [ ] Movie listing and table display
- [ ] Search movies by title, director, actor, genre
- [ ] Sort movies (title, year, rating)
- [ ] Advanced search with multiple filters
- [ ] Add new movie
- [ ] Edit existing movie
- [ ] Delete movie (with confirmation)
- [ ] Rate movies (1-10 scale)
- [ ] View rated movies
- [ ] Navigation between views (Home, Movies, TV Shows, Celebrities, Search)
- [ ] Refresh movie list

### 5.2 Potentially Broken Features (Investigate & Fix)
- [ ] Series/TV Shows view - Check if fully functional
- [ ] Celebrity management - Verify CRUD operations
- [ ] User session persistence - Check if data persists after restart
- [ ] Watchlist functionality - Does it exist or was it planned?

### 5.3 Missing Features (Plan or Remove)
- [ ] Movie posters/images - Not loaded, may need implementation
- [ ] Watchlist - No UI for managing watchlist
- [ ] User profiles - Limited functionality
- [ ] Sharing/recommendations - Not implemented

---

## 6. Technical Debt

### 6.1 Data Persistence
- [ ] **Current state**: Uses serialization (`user_data.ser`) - fragile and not portable
- [ ] **Consider**: SQLite or JSON file storage for better reliability
- [ ] **Immediate fix**: Add error handling for corrupted serialization files

### 6.2 Architecture Issues
- [ ] **ServiceLocator**: Tightly coupled, consider using dependency injection
- [ ] **Global state**: Heavy reliance on static instances makes testing difficult
- [ ] **FXMLController coupling**: Controllers have too much responsibility

### 6.3 Performance Concerns
- [ ] **Large datasets**: Table doesn't use virtualization - will be slow with 1000+ movies
- [ ] **File I/O**: Data loaded synchronously on startup - shows blank screen
- [ ] **Threading**: Background tasks exist but don't show proper loading states

---

## 7. Testing Strategy

### 7.1 Manual Testing Checklist
- [ ] Test all CRUD operations for movies
- [ ] Test all search filters individually and combined
- [ ] Test sort options for all columns
- [ ] Test authentication flow (register, login, logout)
- [ ] Test edge cases (empty results, invalid input)
- [ ] Test window resize behavior
- [ ] Test keyboard navigation

### 7.2 Unit Tests to Add
- [ ] `UserInputValidator` - Test all validation methods
- [ ] `PasswordHasher` - Test hashing and verification
- [ ] `MoviesService` - Test CRUD operations
- [ ] `SearchService` - Test filtering logic
- [ ] Model classes - Test getters/setters, equals, hashCode

---

## 8. File-Specific Changes

### 8.1 MoviesController.java
```
Priority  | Issue                          | Fix
----------|--------------------------------|------------------------------------
HIGH      | sortMovieTable bug (L886-900) | Remove edit dialog trigger after sort
HIGH      | Duplicate column setup        | Consolidate setupTableColumns()
MEDIUM    | Too many responsibilities     | Split into helper classes
MEDIUM    | Missing null checks           | Add defensive null checks
LOW       | Console debug output          | Remove or use proper logging
```

### 8.2 MainController.java
```
Priority  | Issue                          | Fix
----------|--------------------------------|------------------------------------
HIGH      | Unused fields                  | Remove dead code
HIGH      | Duplicate showHome() logic     | Simplify navigation
MEDIUM    | checkAuthentication()          | Implement proper auth checks
MEDIUM    | Too many Platform.runLater()   | Consolidate UI updates
LOW       | Session token handling         | Improve security
```

### 8.3 MoviesService.java
```
Priority  | Issue                          | Fix
----------|--------------------------------|------------------------------------
MEDIUM    | Sample data fallback           | Add logging when using fallback
MEDIUM    | Error handling improvements    | Better error messages
LOW       | Hardcoded file path            | Use ApplicationConfig
```

---

## 9. Immediate Action Items

### Day 1: Critical Bugs
1. Fix `sortMovieTable()` bug that triggers edit dialog
2. Remove duplicate genre column setup in MoviesController
3. Add null checks for statusLabel before use
4. Remove duplicate `home/user/project/` directory

### Day 2: Code Cleanup
1. Remove unused imports across all files
2. Add proper JavaDoc to public API
3. Remove obvious dead code
4. Clean up console debug statements

### Day 3: Feature Verification
1. Test all working features
2. Document what works vs. what's broken
3. Fix any found issues
4. Create testing checklist

### Day 4: Documentation
1. Update this document with findings
2. Add JavaDoc to remaining public methods
3. Fix inline comments
4. Document any removed features

---

## 10. Removed/Deferred Features

Based on code analysis, the following features appear to be planned but not implemented:

- [ ] **Movie poster images** - Not loaded from data files
- [ ] **Watchlist** - No UI or storage mechanism
- [ ] **User profile customization** - Limited
- [ ] **Social features** - Not implemented
- [ ] **Recommendations** - Not implemented
- [ ] **Offline mode** - Not applicable (desktop app)

**Decision**: Either implement these features or explicitly document them as "planned but not implemented" to manage expectations.

---

## Summary

### Quick Wins
1. Fix the sort bug in MoviesController
2. Remove duplicate directory `home/user/project/`
3. Clean up unused imports
4. Add null checks for UI components

### Medium Effort
1. Refactor MoviesController to reduce size
2. Add proper JavaDoc documentation
3. Verify all features work correctly
4. Implement proper error handling

### Long Term
1. Migrate to proper database
2. Add unit tests
3. Implement dependency injection
4. Add missing features (watchlist, etc.)

---

*Last Updated: April 2026*
*Primary Focus: Functionality, Cleanup, Documentation*

---

# ALTERNATIVE MIGRATION OPTIONS

The following sections outline complete rewrite options for the IMDb Clone application using modern technology stacks.

---

## Option A: Next.js/React + Spring Boot Stack

### A.1 Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (Next.js 14+)                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   Pages     │  │ Components  │  │   Hooks     │         │
│  │  (App Dir)  │  │  (UI Kit)   │  │  (State)    │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│         │                │                │                 │
│         └────────────────┼────────────────┘                 │
│                          │                                  │
│                    ┌─────▼─────┐                             │
│                    │  TanStack │                             │
│                    │   Query   │                             │
│                    └─────┬─────┘                             │
└──────────────────────────┼──────────────────────────────────┘
                           │ REST API
┌──────────────────────────┼──────────────────────────────────┐
│                    BACKEND (Spring Boot 3.x)                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │ Controllers │  │  Services   │  │ Repositories│         │
│  │   (REST)    │  │  (Business) │  │   (JPA)     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│         │                │                │                 │
│         └────────────────┼────────────────┘                 │
│                          │                                  │
│                    ┌─────▼─────┐                             │
│                    │   PostgreSQL   │                       │
│                    │   (Database)  │                       │
│                    └──────────────┘                             │
└─────────────────────────────────────────────────────────────┘
```

### A.2 Technology Stack

#### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| Next.js | 14+ | React framework with App Router |
| React | 18+ | UI library |
| TypeScript | 5.x | Type safety |
| Tailwind CSS | 3.x | Styling |
| TanStack Query | 5.x | Data fetching/caching |
| Zustand | 4.x | Client state management |
| React Hook Form | 7.x | Form handling |
| Zod | 3.x | Schema validation |
| Framer Motion | 11.x | Animations |
| React Hot Toast | 4.x | Notifications |

#### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Spring Boot | 3.2+ | Web framework |
| Spring Security | 6.x | Authentication |
| Spring Data JPA | 3.x | Data access |
| PostgreSQL | 16+ | Primary database |
| H2 | 2.x | Development DB |
| Jackson | 2.x | JSON processing |
| Lombok | 1.18+ | Boilerplate reduction |
| MapStruct | 1.5+ | DTO mapping |

### A.3 Project Structure

```
imdb-clone/
├── frontend/                    # Next.js application
│   ├── src/
│   │   ├── app/               # App Router pages
│   │   │   ├── (auth)/        # Auth route group
│   │   │   │   ├── login/
│   │   │   │   └── register/
│   │   │   ├── (main)/        # Main app routes
│   │   │   │   ├── dashboard/
│   │   │   │   ├── movies/
│   │   │   │   ├── series/
│   │   │   │   ├── search/
│   │   │   │   └── profile/
│   │   │   ├── api/           # API routes (BFF)
│   │   │   ├── layout.tsx
│   │   │   └── page.tsx
│   │   ├── components/
│   │   │   ├── ui/            # Reusable UI components
│   │   │   │   ├── Button/
│   │   │   │   ├── Card/
│   │   │   │   ├── Modal/
│   │   │   │   ├── Table/
│   │   │   │   └── Input/
│   │   │   ├── movies/        # Movie-specific components
│   │   │   ├── search/
│   │   │   └── layout/
│   │   ├── hooks/             # Custom React hooks
│   │   │   ├── useMovies.ts
│   │   │   ├── useAuth.ts
│   │   │   └── useSearch.ts
│   │   ├── lib/               # Utilities
│   │   │   ├── api.ts         # API client
│   │   │   ├── auth.ts        # Auth utilities
│   │   │   └── utils.ts
│   │   ├── stores/            # Zustand stores
│   │   │   ├── authStore.ts
│   │   │   └── uiStore.ts
│   │   └── types/             # TypeScript types
│   │       └── index.ts
│   ├── public/
│   ├── package.json
│   ├── tailwind.config.ts
│   └── tsconfig.json
│
├── backend/                    # Spring Boot application
│   ├── src/main/java/com/papel/imdb/
│   │   ├── config/            # Configuration
│   │   │   ├── SecurityConfig.java
│   │   │   ├── CorsConfig.java
│   │   │   └── QueryConfig.java
│   │   ├── controller/         # REST controllers
│   │   │   ├── MovieController.java
│   │   │   ├── SeriesController.java
│   │   │   ├── AuthController.java
│   │   │   └── SearchController.java
│   │   ├── service/           # Business logic
│   │   │   ├── MovieService.java
│   │   │   ├── RatingService.java
│   │   │   └── UserService.java
│   │   ├── repository/         # Data access
│   │   │   ├── MovieRepository.java
│   │   │   ├── UserRepository.java
│   │   │   └── custom/
│   │   ├── model/             # Domain models
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   └── enums/
│   │   ├── security/           # Security
│   │   │   ├── JwtTokenProvider.java
│   │   │   ├── UserDetailsService.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   └── ImdbApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── data/               # Seed data
│   └── pom.xml
│
├── docker-compose.yml          # Local development
└── README.md
```

### A.4 Migration Steps

#### Phase 1: Backend (Week 1-2)
- [ ] Set up Spring Boot project with dependencies
- [ ] Configure PostgreSQL and JPA
- [ ] Create entity models from existing Java models
- [ ] Implement repositories with Spring Data JPA
- [ ] Create REST controllers
- [ ] Implement JWT authentication
- [ ] Add data seeding from existing .txt files

#### Phase 2: Frontend Setup (Week 3)
- [ ] Initialize Next.js 14 project
- [ ] Configure Tailwind CSS
- [ ] Set up API client with Axios
- [ ] Implement authentication flow
- [ ] Create layout components

#### Phase 3: Core Features (Week 4-5)
- [ ] Movie listing and details pages
- [ ] Search functionality with filters
- [ ] Rating system
- [ ] User watchlist
- [ ] Series/TV shows pages

#### Phase 4: Polish (Week 6)
- [ ] Animations and transitions
- [ ] Loading states and error handling
- [ ] Responsive design
- [ ] Performance optimization
- [ ] Deploy to Vercel + Railway

### A.5 Key Features to Implement

#### Authentication
- [ ] JWT-based authentication
- [ ] Login/Register pages
- [ ] Password hashing with BCrypt
- [ ] Session management
- [ ] Role-based access (USER, ADMIN)

#### Movie Management
- [ ] CRUD operations
- [ ] Poster image upload/download
- [ ] Cast and crew management
- [ ] Genre filtering
- [ ] Pagination

#### Search & Discovery
- [ ] Full-text search
- [ ] Advanced filters (year, rating, genre)
- [ ] Sorting options
- [ ] Auto-complete suggestions

#### User Features
- [ ] Watchlist
- [ ] Rating history
- [ ] Favorite actors/directors
- [ ] Profile customization

### A.6 API Endpoints Design

```
Authentication:
POST   /api/auth/register     - Register new user
POST   /api/auth/login        - Login user
POST   /api/auth/refresh      - Refresh token
GET    /api/auth/me           - Get current user

Movies:
GET    /api/movies            - List movies (paginated)
GET    /api/movies/{id}       - Get movie details
POST   /api/movies            - Create movie (admin)
PUT    /api/movies/{id}       - Update movie (admin)
DELETE /api/movies/{id}       - Delete movie (admin)
POST   /api/movies/{id}/rate  - Rate movie

Search:
GET    /api/search             - Search all content
GET    /api/search/movies      - Search movies
GET    /api/search/actors      - Search actors

User:
GET    /api/users/me           - Get profile
PUT    /api/users/me           - Update profile
GET    /api/users/me/watchlist - Get watchlist
POST   /api/users/me/watchlist - Add to watchlist
DELETE /api/users/me/watchlist/{id} - Remove from watchlist
GET    /api/users/me/ratings  - Get user ratings
```

### A.7 Database Schema

```sql
-- Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Movies
CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    duration INT,  -- minutes
    rating DECIMAL(3,1),  -- 0-10
    poster_url VARCHAR(500),
    backdrop_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Movie Genres (many-to-many)
CREATE TABLE movie_genres (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(id),
    PRIMARY KEY (movie_id, genre_id)
);

-- Actors
CREATE TABLE actors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    biography TEXT,
    photo_url VARCHAR(500)
);

-- Movie Cast (many-to-many with role)
CREATE TABLE movie_cast (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    actor_id BIGINT REFERENCES actors(id),
    role VARCHAR(255),
    display_order INT,
    PRIMARY KEY (movie_id, actor_id)
);

-- Directors
CREATE TABLE directors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    biography TEXT
);

-- Movie Directors (many-to-many)
CREATE TABLE movie_directors (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    director_id BIGINT REFERENCES directors(id),
    PRIMARY KEY (movie_id, director_id)
);

-- User Ratings
CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    rating INT NOT NULL,  -- 1-10
    review TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id, movie_id)
);

-- Watchlist
CREATE TABLE watchlist (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    added_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id, movie_id)
);

-- Series (TV Shows)
CREATE TABLE series (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    rating DECIMAL(3,1),
    poster_url VARCHAR(500),
    status VARCHAR(20)  -- CONTINUING, ENDED
);

-- Seasons
CREATE TABLE seasons (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT REFERENCES series(id) ON DELETE CASCADE,
    season_number INT NOT NULL,
    release_date DATE,
    episode_count INT
);

-- Episodes
CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT REFERENCES seasons(id) ON DELETE CASCADE,
    episode_number INT NOT NULL,
    title VARCHAR(255),
    description TEXT,
    release_date DATE,
    duration INT,
    rating DECIMAL(3,1)
);
```

---

## Option B: Complete Rust Rewrite

### B.1 Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     RUST APPLICATION                        │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Web Layer (Axum)                    │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │ Routes   │  │  Middleware │  │Errors   │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                          │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │              Service Layer (Business Logic)           │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │ MovieSvc │  │ AuthSvc  │  │SearchSvc │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                          │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │              Repository Layer (Data)                │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │  Movies  │  │  Users   │  │ Cache    │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────┬──────────────────────────────┘   │
│                          │                                    │
│  ┌──────────────────────▼──────────────────────────────┐   │
│  │                    Database                           │   │
│  │              (SQLite / PostgreSQL)                    │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  FRONTEND (Alternative)                     │
│                     (Web + Desktop)                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                  Tauri + React/                       │   │
│  │                  Leptos/Yew (WASM)                   │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### B.2 Technology Stack

#### Core Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Rust | 1.75+ | Systems programming language |
| Axum | 0.7+ | Web framework |
| Tokio | 1.x | Async runtime |
| Serde | 1.x | Serialization |
| SQLx | 0.7+ | Database (with compile-time checks) |
| PostgreSQL | 16+ | Production database |
| SQLite | 3.x | Development database |
| Redis | 7.x | Caching (optional) |
| JWT | 0.11+ | Authentication |
| BCrypt | 0.4+ | Password hashing |

#### Frontend (Tauri)
| Technology | Version | Purpose |
|------------|---------|---------|
| Tauri | 2.x | Desktop app wrapper |
| React | 18+ | UI components |
| TypeScript | 5.x | Type safety |
| CSS | 3.x | Styling |

#### Alternative: Leptos (WASM)
| Technology | Version | Purpose |
|------------|---------|---------|
| Leptos | 0.6+ | WASM-first framework |
| Sycamore | 0.9+ | Reactive WASM framework |
| Yew | 0.21+ | WASM components |

### B.3 Project Structure

```
imdb-rust/
├── Cargo.toml
├── rust-toolchain.toml
├── .env.example
├── docker-compose.yml
│
├── src/
│   ├── main.rs                 # Application entry
│   ├── lib.rs                  # Library root
│   │
│   ├── web/                    # Web layer
│   │   ├── mod.rs
│   │   ├── router.rs           # Route definitions
│   │   ├── routes/
│   │   │   ├── mod.rs
│   │   │   ├── movies.rs
│   │   │   ├── series.rs
│   │   │   ├── auth.rs
│   │   │   ├── search.rs
│   │   │   └── users.rs
│   │   ├── middleware/
│   │   │   ├── mod.rs
│   │   │   ├── auth.rs
│   │   │   └── logging.rs
│   │   └── error.rs           # Error handling
│   │
│   ├── service/               # Business logic
│   │   ├── mod.rs
│   │   ├── movie_service.rs
│   │   ├── rating_service.rs
│   │   ├── auth_service.rs
│   │   ├── search_service.rs
│   │   └── user_service.rs
│   │
│   ├── repository/             # Data access
│   │   ├── mod.rs
│   │   ├── movie_repo.rs
│   │   ├── user_repo.rs
│   │   └── cache_repo.rs
│   │
│   ├── model/                  # Domain models
│   │   ├── mod.rs
│   │   ├── entity/
│   │   │   ├── mod.rs
│   │   │   ├── movie.rs
│   │   │   ├── user.rs
│   │   │   ├── rating.rs
│   │   │   └── series.rs
│   │   ├── dto/
│   │   │   ├── mod.rs
│   │   │   ├── request/
│   │   │   │   └── mod.rs
│   │   │   └── response/
│   │   │       ├── mod.rs
│   │   │       └── movie_resp.rs
│   │   └── enums/
│   │       ├── mod.rs
│   │       └── genre.rs
│   │
│   ├── security/              # Security
│   │   ├── mod.rs
│   │   ├── jwt.rs
│   │   ├── password.rs
│   │   └── claims.rs
│   │
│   ├── config/                # Configuration
│   │   ├── mod.rs
│   │   ├── app.rs
│   │   └── database.rs
│   │
│   └── utils/                 # Utilities
│       ├── mod.rs
│       └── logging.rs
│
├── migrations/                # Database migrations (SQLx)
│   ├── 001_create_users.sql
│   ├── 002_create_movies.sql
│   └── ...
│
├── tests/                     # Integration tests
│   ├── mod.rs
│   ├── movies_test.rs
│   └── auth_test.rs
│
└── docs/                      # Documentation
    └── API.md
```

### B.4 Key Crates & Dependencies

```toml
[package]
name = "imdb-rust"
version = "0.1.0"
edition = "2021"

[dependencies]
# Web Framework
axum = "0.7"
tokio = { version = "1", features = ["full"] }
tower = "0.4"
tower-http = { version = "0.5", features = ["cors", "trace"] }

# Database
sqlx = { version = "0.7", features = ["runtime-tokio", "postgres", "sqlite", "chrono", "uuid"] }

# Serialization
serde = { version = "1", features = ["derive"] }
serde_json = "1"

# Authentication
jsonwebtoken = "0.11"
bcrypt = "0.4"

# Validation
validator = { version = "0.18", features = ["derive"] }

# Async
futures = "0.3"

# Error Handling
thiserror = "1"
anyhow = "1"

# Date/Time
chrono = { version = "0.4", features = ["serde"] }

# UUID
uuid = { version = "1", features = ["v4", "serde"] }

# Logging
tracing = "0.1"
tracing-subscriber = "0.3"
tracing-appender = "0.2"

[dev-dependencies]
tower = { version = "0.4", features = ["util"] }
http-body = "0.4"
axum-test = "0.6"
```

### B.5 Migration Steps

#### Phase 1: Project Setup (Week 1)
- [ ] Initialize Rust project with Cargo
- [ ] Set up logging and error handling
- [ ] Configure database with SQLx
- [ ] Create migrations
- [ ] Set up Axum router
- [ ] Implement basic CRUD for movies

#### Phase 2: Core Backend (Week 2-3)
- [ ] Implement user authentication (JWT)
- [ ] Create all repository structs
- [ ] Implement service layer
- [ ] Add search functionality
- [ ] Implement rating system
- [ ] Add series/TV show support

#### Phase 3: Data Migration (Week 4)
- [ ] Write parser for existing .txt data files
- [ ] Create migration script
- [ ] Import existing movie data
- [ ] Import user data (passwords need rehashing)
- [ ] Verify data integrity

#### Phase 4: Desktop Client (Week 5-6)
- [ ] Set up Tauri project
- [ ] Implement React frontend
- [ ] Connect to Rust backend
- [ ] Build native desktop app

#### Phase 5: Polish (Week 7)
- [ ] Add tests
- [ ] Performance optimization
- [ ] Error handling improvements
- [ ] Documentation

### B.6 Comparison: JavaFX vs Rust

| Aspect | JavaFX (Current) | Rust (Rewrite) |
|--------|------------------|----------------|
| Performance | Good | Excellent |
| Memory | Higher (~200MB) | Lower (~30MB) |
| Binary Size | Large (~50MB+) | Small (~10MB) |
| Startup Time | Slow (~3s) | Fast (<1s) |
| Cross-Platform | Yes | Yes |
| Developer Experience | Good | Steep learning curve |
| Safety | JVM memory managed | Memory safe by design |
| Concurrency | Thread-based | Async/await |
| Build Time | Moderate | Slower |
| Ecosystem | Mature | Growing |

### B.7 Database Schema (SQLx)

```sql
-- Users table
CREATE TABLE users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Movies table
CREATE TABLE movies (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    duration INTEGER,
    rating DECIMAL(3,1),
    poster_url VARCHAR(500),
    backdrop_url VARCHAR(500),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Genres enum
CREATE TYPE genre AS ENUM (
    'ACTION', 'ADVENTURE', 'ANIMATION', 'BIOGRAPHY', 'COMEDY',
    'CRIME', 'DOCUMENTARY', 'DRAMA', 'FAMILY', 'FANTASY',
    'HISTORY', 'HORROR', 'MUSIC', 'MYSTERY', 'ROMANCE',
    'SCIENCE_FICTION', 'SPORT', 'THRILLER', 'WAR', 'WESTERN'
);

-- Movie genres junction
CREATE TABLE movie_genres (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    genre GENRE NOT NULL,
    PRIMARY KEY (movie_id, genre)
);

-- Actors
CREATE TABLE actors (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    biography TEXT,
    photo_url VARCHAR(500)
);

-- Movie cast with roles
CREATE TABLE movie_cast (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    actor_id BIGINT REFERENCES actors(id) ON DELETE CASCADE,
    role VARCHAR(255),
    display_order INTEGER DEFAULT 0,
    PRIMARY KEY (movie_id, actor_id)
);

-- Directors
CREATE TABLE directors (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    biography TEXT
);

-- Movie directors
CREATE TABLE movie_directors (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    director_id BIGINT REFERENCES directors(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, director_id)
);

-- User ratings
CREATE TABLE ratings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 10),
    review TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, movie_id)
);

-- Watchlist
CREATE TABLE watchlist (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    added_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, movie_id)
);

-- Series
CREATE TABLE series (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    rating DECIMAL(3,1),
    poster_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'continuing'
);

-- Seasons
CREATE TABLE seasons (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    series_id BIGINT REFERENCES series(id) ON DELETE CASCADE,
    season_number INTEGER NOT NULL,
    release_date DATE,
    episode_count INTEGER
);

-- Episodes
CREATE TABLE episodes (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    season_id BIGINT REFERENCES seasons(id) ON DELETE CASCADE,
    episode_number INTEGER NOT NULL,
    title VARCHAR(255),
    description TEXT,
    release_date DATE,
    duration INTEGER,
    rating DECIMAL(3,1)
);

-- Indexes
CREATE INDEX idx_movies_title ON movies(title);
CREATE INDEX idx_movies_rating ON movies(rating DESC);
CREATE INDEX idx_movies_release ON movies(release_date DESC);
CREATE INDEX idx_ratings_user ON ratings(user_id);
CREATE INDEX idx_ratings_movie ON ratings(movie_id);
```

### B.8 API Endpoints (Axum)

```rust
use axum::{
    routing::{get, post, put, delete},
    Router, extract::Path,
};

// Auth routes
router.route("/api/auth/register", post(register))
       .route("/api/auth/login", post(login))
       .route("/api/auth/refresh", post(refresh_token))
       .route("/api/auth/me", get(get_current_user));

// Movie routes
router.route("/api/movies", get(list_movies))
       .route("/api/movies", post(create_movie))
       .route("/api/movies/:id", get(get_movie))
       .route("/api/movies/:id", put(update_movie))
       .route("/api/movies/:id", delete(delete_movie))
       .route("/api/movies/:id/rate", post(rate_movie));

// Search routes
router.route("/api/search", get(search))
       .route("/api/search/movies", get(search_movies))
       .route("/api/search/actors", get(search_actors));

// Series routes
router.route("/api/series", get(list_series))
       .route("/api/series/:id", get(get_series));

// User routes
router.route("/api/users/me", get(get_profile))
       .route("/api/users/me", put(update_profile))
       .route("/api/users/me/watchlist", get(get_watchlist))
       .route("/api/users/me/watchlist", post(add_to_watchlist))
       .route("/api/users/me/watchlist/:movie_id", delete(remove_from_watchlist))
       .route("/api/users/me/ratings", get(get_ratings));
```

### B.9 Error Handling Pattern

```rust
use thiserror::Error;

#[derive(Error, Debug)]
pub enum ApiError {
    #[error("User not found")]
    UserNotFound,
    
    #[error("Movie not found")]
    MovieNotFound,
    
    #[error("Invalid credentials")]
    InvalidCredentials,
    
    #[error("Token expired")]
    TokenExpired,
    
    #[error("Database error: {0}")]
    DatabaseError(#[from] sqlx::Error),
    
    #[error("Validation error: {0}")]
    ValidationError(String),
    
    #[error("Unauthorized")]
    Unauthorized,
    
    #[error("Forbidden")]
    Forbidden,
}

impl IntoResponse for ApiError {
    fn into_response(self) -> Response {
        let (status, message) = match self {
            ApiError::UserNotFound => (StatusCode::NOT_FOUND, self.to_string()),
            ApiError::MovieNotFound => (StatusCode::NOT_FOUND, self.to_string()),
            ApiError::InvalidCredentials => (StatusCode::UNAUTHORIZED, self.to_string()),
            ApiError::TokenExpired => (StatusCode::UNAUTHORIZED, self.to_string()),
            ApiError::Unauthorized => (StatusCode::UNAUTHORIZED, self.to_string()),
            ApiError::Forbidden => (StatusCode::FORBIDDEN, self.to_string()),
            ApiError::DatabaseError(_) => (StatusCode::INTERNAL_SERVER_ERROR, "Database error"),
            ApiError::ValidationError(msg) => (StatusCode::BAD_REQUEST, msg),
        };
        
        Json(json!({ "error": message })).into_response(status)
    }
}
```

---

## Decision Matrix

| Factor | Keep JavaFX | Next.js + Spring Boot | Rust |
|--------|-------------|----------------------|------|
| Development Time | - | 6 weeks | 7 weeks |
| Performance | Good | Good | Excellent |
| Memory Usage | ~200MB | ~100MB | ~30MB |
| Maintainability | Medium | High | Medium |
| Learning Curve | Low | Medium | High |
| Desktop Support | Native | Web only | Native |
| Mobile Support | No | Responsive | Tauri mobile |
| Team Expertise | Java | JS + Java | Rust |
| Future Proofing | Medium | High | High |

### Recommendation

**Option 1 (Next.js + Spring Boot)**: Best for web-based application with modern stack, easier hiring, mature ecosystem.

**Option 2 (Rust)**: Best for high-performance desktop application, learning modern systems programming, standalone executable.

**Keep JavaFX**: Only if desktop-only with no web requirement, and team has strong Java skills.

---

*Last Updated: April 2026*
*Additional Options Added: Next.js/React + Spring Boot, Rust Rewrite*

