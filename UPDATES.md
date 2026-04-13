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
