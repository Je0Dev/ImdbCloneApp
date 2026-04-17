# AGENTS.md

This file documents how AI agents should work on this codebase.

## Project Overview

- **Type**: JavaFX Desktop Application (IMDb Clone)
- **Framework**: JavaFX 21, Maven
- **Architecture**: MVC with Service Locator pattern

## Entry Points

1. `StartApplication.main()` - launches `MovieAppGui`
2. `MovieAppGui` - JavaFX Application class

## Key Files

- `pom.xml` - Build configuration
- `src/main/java/com/papel/imdb_clone/` - Main source code

## Tech Stack

- Java 21
- JavaFX 21.0.3
- ControlsFX 11.2.1
- Jackson 2.17.1
- JUnit 5.10.2
- jBCrypt 0.4
- SLF4J 2.0.13

## Build Commands

```bash
mvn clean compile
mvn exec:java
mvn test
```

## Code Organization

```
src/main/java/com/papel/imdb_clone/
├── StartApplication.java      - Entry point
├── config/                   - Configuration
├── controllers/              - MVC Controllers
│   ├── authentication/      - Login/Register
│   ├── content/             - Movies, Series
│   ├── coordinator/         - UI Coordinator
│   ├── people/              - Celebrities
│   └── search/              - Search
├── model/                   - Domain models
├── service/                 - Business logic
├── repository/               - Data access
├── data/                    - Data facade
└── util/                     - Utilities
```

## Refactoring Goals

1. Split large controller files (SeriesController, CelebritiesController, MoviesController)
2. Remove unused/empty files
3. Consolidate duplicate patterns

## Important Notes

- Controllers extend either `BaseController` or `BaseSearchController`
- Data loading is done via loader classes in `service/data/loader/`
- Auth is handled by `AuthService` in `service/validation/`
## Refactoring Guidelines

### File Size Limits
- **Maximum file size**: 350 lines per .java file
- Files over 350 lines should be split into helper classes

### How to Split Large Files

#### For JavaFX Controllers
1. Keep the main controller for FXML binding (@FXML fields, initialize method)
2. Extract logic methods into *Helper classes:
   - *DialogHelper - for dialog creation
   - *LogicHelper - for business logic (filtering, sorting, searching)
   - *TableHelper - for table setup and data population

#### Example Splitting Pattern
```java
// Original: BigController.java (500+ lines)
// Split into:
// 1. BigController.java (keep @FXML fields, initialize)
// 2. BigDialogHelper.java (dialog creation)  
// 3. BigLogicHelper.java (filter, sort, search logic)
// 4. BigTableHelper.java (table column setup)
```

### Helper Class Structure
```java
public class SeriesLogicHelper {
    private final SeriesService seriesService;
    
    public SeriesLogicHelper() {
        this.seriesService = SeriesService.getInstance();
    }
    
    // Filtering methods
    public List<Series> filterSeries(List<Series> seriesList, String searchText);
    
    // Sorting methods
    public List<Series> sortSeries(List<Series> seriesList, String sortOption);
    
    // Table setup methods
    public void initializeTableColumns(...);
}
```

### Adding Helper to Controller
```java
public class SeriesController extends BaseController {
    private final SeriesLogicHelper logicHelper = new SeriesLogicHelper();
    
    // Use helper instead of inline logic
    private void handleSearch() {
        List<Series> results = logicHelper.filterSeries(allSeries, searchField.getText());
    }
}
```

### Naming Conventions for Helpers
- {Feature}DialogHelper - dialog creation
- {Feature}LogicHelper - business logic  
- {Feature}TableHelper - table/data operations
- {Feature}DataLoader - data loading

## Largest Files Priority List
Files that need refactoring (by line count):
1. SeriesController.java (1875 lines) - NEEDS SPLIT
2. CelebritiesController.java (1587 lines) - NEEDS SPLIT
3. MoviesController.java (1390 lines) - PARTIALLY SPLIT
4. AdvancedSearchController.java (1008 lines)
5. AuthController.java (859 lines)

## Helper Classes Available
- MoviesDialogHelper.java
- SeriesDialogHelper.java
- CelebritiesDialogHelper.java
- SeriesLogicHelper.java
- AuthValidationHelper.java
- SearchCriteriaHelper.java
- RatingHelper.java

## Complete Helper Classes List

### Controllers Helpers
- `MoviesDialogHelper.java` - Movie dialog creation
- `MoviesTableHelper.java` - (unused, to be removed)
- `SeriesDialogHelper.java` - Series/Season/Episode dialogs
- `SeriesLogicHelper.java` - Series filtering, sorting, table setup
- `CelebritiesDialogHelper.java` - Actor/Director dialogs
- `SearchLogicHelper.java` - Search filtering and sorting

### Service Helpers
- `AuthValidationHelper.java` - Auth validation logic
- `SearchCriteriaHelper.java` - Search criteria building
- `RatingHelper.java` - Rating calculations

## Files Over 350 Lines (Need Refactoring)
Files that still exceed 350 lines and need helper extraction:
- SeriesController.java (1875 lines) - Has helper, needs integration
- CelebritiesController.java (1587 lines) - Has helper, needs integration
- MoviesController.java (1390 lines) - Using helper
- AdvancedSearchController.java (1008 lines)
- MainController.java (852 lines)
- EditContentController.java (544 lines)
- AuthService.java (724 lines)
- Content.java (454 lines)

## Build Commands
```bash
mvn clean compile
mvn exec:java
mvn test
```

## How to Integrate Helpers into Controllers

### Step 1: Add Helper Field
```java
public class MoviesController extends BaseController {
    private final MoviesDialogHelper dialogHelper = new MoviesDialogHelper();
    private final MoviesLogicHelper logicHelper = new MoviesLogicHelper();
```

### Step 2: Replace Inline Logic with Helper Calls
// BEFORE:
private void setupTableColumns() {
    movieTitleColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getTitle()));
    // ... many more columns
}

// AFTER (use helper):
private void setupTableColumns() {
    logicHelper.initializeTableColumns(movieTitleColumn, movieYearColumn, 
        movieDurationColumn, movieGenreColumn, movieDirectorColumn, 
        movieCastColumn, movieRatingColumn);
}
```

### Step 3: Replace Filter/Sort Logic
// BEFORE:
private void filterMovies() {
    String search = searchField.getText();
    List<Movie> filtered = allMovies.stream()
        .filter(m -> m.getTitle().toLowerCase().contains(search))
        .collect(Collectors.toList());
}

// AFTER:
private void filterMovies() {
    ObservableList<Movie> filtered = logicHelper.filterMovies(allMovies, searchField.getText());
}
```

## Integration Status (Updated: 2026-04-17)

### MoviesController.java (1475 lines)
- ✅ Has dialogHelper field
- ✅ Has logicHelper field
- ✅ Integrated: setupTableColumns() uses helper
- Remaining: filterMovies(), sortMovies()

### SeriesController.java (1817 lines)
- ✅ Has dialogHelper field  
- ✅ Has logicHelper field
- ✅ Integrated: updateSeasonsTable(), initializeSeasonsTable(), updateEpisodesTable(), initializeEpisodesTable()
- Remaining: filterSeries(), sortSeriesTable() (controller has more features)

### CelebritiesController.java (1587 lines)
- ✅ Has dialogHelper field
- Needs: table setup integration


## Extracting Helpers and Removing Old Code

### Always Use Extracted Helpers

When a helper class exists, ALWAYS use it instead of inline code:

```java
// DON'T use inline code like this:
private void setupTableColumns() {
    movieTitleColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getTitle()));
    movieYearColumn.setCellValueFactory(cellData -> {
        Movie m = cellData.getValue();
        if (m.getReleaseDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(m.getReleaseDate());
            return new SimpleStringProperty(String.valueOf(cal.get(Calendar.YEAR)));
        }
        return new SimpleStringProperty("");
    });
    // ... 100 more lines
}

// DO use the helper:
private void setupTableColumns() {
    logicHelper.initializeTableColumns(movieTitleColumn, movieYearColumn, 
        movieDurationColumn, movieGenreColumn, movieDirectorColumn, 
        movieCastColumn, movieRatingColumn);
}
```

### After Integration: Delete Old Methods

After integrating helpers, DELETE the old methods from the controller:

```java
// DELETE this entire method after using helper:
private void setupTableColumns() {
    // OLD INLINE CODE - 100+ lines
}

// REPLACE with just:
private void setupTableColumns() {
    logicHelper.initializeTableColumns(movieTitleColumn, movieYearColumn, 
        movieDurationColumn, movieGenreColumn, movieDirectorColumn, 
        movieCastColumn, movieRatingColumn);
}
```

### Integration Steps

1. Add helper field to controller
2. Replace ALL calls to old methods with helper calls
3. Delete the old method code entirely
4. Keep only @FXML annotations and initialize()


## Current Integration Status

### MoviesController.java (1477 lines)
- ✅ Added helper fields (dialogHelper, logicHelper)
- Status: Ready for use - can delegate filter/sort/table to helpers
- Next step: Replace inline filterMovies() with logicHelper.filterMovies()

### SeriesController.java (1875 lines)
- ✅ Has dialogHelper, logicHelper available
- Status: Ready for use

### CelebritiesController.java (1587 lines)
- ✅ Has dialogHelper available
- Status: Ready for use

## Build Test
```bash
mvn clean compile
```

