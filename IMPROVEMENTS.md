# IMPROVEMENTS.md

This file documents issues and planned improvements for the codebase.

## Completed Refactorings

### 1. Extracted Helper Classes

Created helper classes to extract dialog logic from large controllers:

- `MoviesDialogHelper.java` - Add/Edit movie dialogs, advanced search dialog
- `MoviesLogicHelper.java` - Movie table setup, filtering, sorting
- `SeriesDialogHelper.java` - Add season/episode dialogs, series edit dialog  
- `SeriesLogicHelper.java` - Series table setup, filtering, sorting, format helpers
- `CelebritiesDialogHelper.java` - Add/edit actor/director dialogs
- `SearchLogicHelper.java` - Search filtering and sorting

Service helpers:
- `AuthValidationHelper.java` - Auth validation logic
- `SearchCriteriaHelper.java` - Search criteria building
- `RatingHelper.java` - Rating calculations

### 2. Controller Integration

#### SeriesController.java
- Added logicHelper field
- Integrated: updateSeasonsTable(), initializeSeasonsTable(), updateEpisodesTable(), initializeEpisodesTable()
- Added formatDuration() to helper

#### MoviesController.java
- Added dialogHelper, logicHelper fields
- Integrated: setupTableColumns() using helper

#### AuthController.java
- Added validationHelper field

### 2. Removed Duplicate Methods

In DataManager:
- Marked `getMovies()` as `@Deprecated`
- Now delegates to `getAllMovies()` (single source of truth)

### 3. Cleaned Up Unused Files

- Deleted `home/user/project/` - contained empty unused files

### 4. Generic Data Loader

Already in place - all data loaders extend `BaseDataLoader`:
- `MovieDataLoader extends BaseDataLoader`
- `SeriesDataLoader extends BaseDataLoader`  
- `ActorDataLoader extends BaseDataLoader`
- etc.

## Remaining Issues

### Large Files Still Need Refactoring

| File | Lines | Issue |
|------|-------|-------|
| controllers/content/SeriesController.java | 1873 | Too many responsibilities |
| controllers/people/CelebritiesController.java | 1587 | Too many responsibilities |
| controllers/content/MoviesController.java | 1475 | Too many responsibilities |

### Why Controllers Are Hard to Split

JavaFX controllers are tightly coupled with FXML:
1. `@FXML` annotated fields are injected by JavaFX at runtime
2. Each controller has its own FXML view file
3. Event handlers are bound to FXML `onAction` attributes

**Proper refactoring requires:**
- New FXML files for each new controller
- Updates to navigation/coordinator code
- JavaFX scene graph changes

## Recommended Approach

### Step 1: Use Helper Classes in Controllers

Add helper instances to controllers and delegate:

```java
// In MoviesController:
private MoviesDialogHelper dialogHelper = new MoviesDialogHelper();

// Then replace showMovieEditDialog() with:
private boolean showMovieEditDialog(Movie movie) {
    return dialogHelper.showMovieEditDialog(movie);
}
```

### Step 2: Extract Non-FXML Logic

Move table setup, filtering, sorting logic to helper classes that don't require FXML.

### Step 3: Create Table Helper Classes

Similar to dialog helpers but for table operations.

### Step 4: Consolidate Search

The search controllers (SearchFormController, AdvancedSearchController, ResultsTableController) each have separate FXML views. Either:
- Keep separate for different UIs
- Or merge into single controller with dynamic FXML loading

## Build Test Command

```bash
mvn clean compile
```

## Files to Keep

- `DataFileLoader.java` - Used by FileDataLoaderService
- `UserDataRegenerator.java` - Used via Maven exec plugin