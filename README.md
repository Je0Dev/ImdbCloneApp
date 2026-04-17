# 🎬 IMDB Clone App
---
An IMDB clone application developed in JavaFX for browsing movies, TV series, and celebrities. The app features seamless data integration by importing information directly from TXT files

## 📚 Overview

### 🎥 Content Management
- **Movies & TV Shows**: Browse, search, and filter movies and TV series with detailed information
- **Celebrity Profiles**: View actor and director profiles with some of their info
- **User Authentication**: Secure registration and login system with session management
- **Rating System**: Rate and review content with a basic rating scale
- **Advanced Search**: Powerful search functionality for both movies or series
- **Responsive UI**: Modern JavaFX interface with FXML
- **Data Management**: In-memory data storage with serialization support for writing into the txt files

### 🛠️ Technical Stack

- **Backend Language**: Java 21
- **UI Framework**: JavaFX 21.0.3
- **Build Tool**: Maven
- **Dependency Injection**: Custom Service Locator pattern
- **Logging**: SLF4J with Simple Binding for buttons,fields etc.

### 🚀 Getting Started

#### Prerequisites
- Java 21 or later
- Maven 3.8.0 or later

#### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/Je0Dev/ImdbCloneApp.git
   cd ImdbCloneApp
   ```

2. Build and run:
   ```bash
   mvn clean compile
   mvn exec:java
   ```

## 🏗️ Architecture

The project follows MVC architecture with service locator pattern:
- **Controllers**: JavaFX controllers handling UI logic
- **Models**: Domain objects (Movie, Series, Actor, etc.)
- **Services**: Business logic layer
- **Repository**: Data access layer
- **Helpers**: Extracted reusable logic from large controllers

### Refactoring

This project is being refactored to reduce file sizes and extract reusable helper classes. See `AGENTS.md` for details.

## MIT Licence

---
*Last updated: April 2026*
