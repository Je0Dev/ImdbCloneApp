# Roadmap

This document outlines the planned improvements and new features for the IMDb Clone application.

---

## Phase 1: Refactoring (Current)

### 1.1 Controller Helper Integration

| Task | Status | Notes |
|------|--------|-------|
| SeriesController helper integration | ✅ Done | Integrated table methods |
| MoviesController helper integration | ⚠️ Partial | Added fields, needs more integration |
| CelebritiesController helper integration | 🔲 Pending | Has dialogHelper, needs table integration |

**Remaining for Phase 1:**
- [ ] Integrate filter/sort methods from helpers into controllers
- [ ] Remove duplicate code from controllers after integration
- [ ] Add table helper integration for CelebritiesController

### 1.2 File Size Reduction

| File | Lines | Target | Status |
|------|-------|--------|--------|
| SeriesController.java | ~1817 | <350 | ⚠️ Partial |
| CelebritiesController.java | ~1587 | <350 | 🔲 Pending |
| MoviesController.java | ~1475 | <350 | ⚠️ Partial |
| AdvancedSearchController.java | ~1008 | <350 | 🔲 Pending |
| MainController.java | ~852 | <350 | 🔲 Pending |
| EditContentController.java | ~544 | <350 | 🔲 Pending |
| AuthService.java | ~724 | <350 | 🔲 Pending |

---

## Phase 2: Code Quality

### 2.1 Warnings & Deprecations

- [ ] Add serialVersionUID to serializable exception classes
- [ ] Fix unchecked raw type warnings in dialog helpers
- [ ] Replace deprecated Date constructor in models
- [ ] Fix "this" escape warnings in constructors

### 2.2 Code Organization

- [ ] Remove empty/unused files in project
- [ ] Consolidate duplicate utility methods
- [ ] Add proper Javadoc to public APIs

---

## Phase 3: New Features

### 3.1 User Experience

- [ ] **Watchlist**: Allow users to add movies/series to personal watchlist
- [ ] **Watch History**: Track watched content
- [ ] **Favorites**: Mark content as favorite
- [ ] **User Reviews**: Allow full text reviews, not just ratings

### 3.2 Search Improvements

- [ ] **Filter by multiple criteria**: Year range, rating range, genre combinations
- [ ] **Sort options**: More sorting (duration, popularity, alphabetically)
- [ ] **Search history**: Recent searches

### 3.3 Content Management

- [ ] **Bulk import**: Import multiple movies/shows at once
- [ ] **Export data**: Export favorites/watchlist to CSV/JSON
- [ ] **Content recommendations**: Suggest similar movies/shows

### 3.4 UI Enhancements

- [ ] **Dark mode**: Theme toggle
- [ ] **Responsive layouts**: Better window resizing
- [ ] **Poster images**: Display movie/show posters (currently text-only)
- [ ] **Star ratings**: Visual star rating display
- [ ] **Loading states**: Show loading indicators

### 3.5 Social Features

- [ ] **User profiles**: Profile pictures, bio
- [ ] **Activity feed**: Show recent ratings/reviews
- [ ] **Follow users**: Follow other users' watchlists

---

## Phase 4: Technical Improvements

### 4.1 Performance

- [ ] Lazy loading for large data sets
- [ ] Pagination for search results
- [ ] Cache frequently accessed data

### 4.2 Data Persistence

- [ ] **Database migration**: Move from serialized files to SQLite/PostgreSQL
- [ ] **Backup/restore**: Export/import functionality
- [ ] **Data validation**: More robust input validation

### 4.3 Testing

- [ ] Add unit tests for service layer
- [ ] Add integration tests for controllers
- [ ] Add test coverage reporting

---

## Feature Priority

| Priority | Feature | Reason |
|----------|---------|--------|
| High | Complete helper integration | Maintainability |
| High | Fix compiler warnings | Code quality |
| Medium | Watchlist | User engagement |
| Medium | Dark mode | User experience |
| Medium | Better search filters | Core functionality |
| Low | Database migration | Technical debt |
| Low | User profiles | Social features |

---

## Contribution Guidelines

1. Pick a task from the roadmap
2. Create a feature branch: `feature/task-name`
3. Follow existing code conventions
4. Update documentation if needed
5. Submit pull request

---

## Version History

- **v1.0** (Current): Basic movie/series browsing, auth, ratings
- **v1.1** (Planned): Watchlist, improved search
- **v1.2** (Planned): Dark mode, UI enhancements
- **v2.0** (Planned): Database backend, social features

---

*Last updated: April 2026*