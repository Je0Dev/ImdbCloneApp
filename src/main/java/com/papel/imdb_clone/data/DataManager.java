package com.papel.imdb_clone.data;

import com.papel.imdb_clone.model.content.Movie;
import com.papel.imdb_clone.model.content.Series;
import com.papel.imdb_clone.model.people.Actor;
import com.papel.imdb_clone.model.people.Director;
import com.papel.imdb_clone.repository.impl.InMemoryMovieRepository;
import com.papel.imdb_clone.repository.impl.InMemorySeriesRepository;
import com.papel.imdb_clone.repository.impl.InMemoryUserRepository;
import com.papel.imdb_clone.repository.impl.InMemoryCelebritiesRepository;
import com.papel.imdb_clone.service.people.CelebrityService;
import com.papel.imdb_clone.service.content.MoviesService;
import com.papel.imdb_clone.service.content.SeriesService;
import com.papel.imdb_clone.service.search.ServiceLocator;
import com.papel.imdb_clone.service.data.base.DataLoaderService;
import com.papel.imdb_clone.service.data.base.FileDataLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.List;

/**
 * DataManager that delegates to specialized services and repositories.
 * This class serves as a facade for the data layer, providing a unified interface
 * while maintaining separation of concerns.
 */
public class DataManager {


    //data manager instance being volatile to ensure visibility of changes across threads
    private static volatile DataManager instance;
    private static final Object lock = new Object();
    
    private boolean dataLoaded;
    private static final Logger logger = LoggerFactory.getLogger(DataManager.class);

    // Repositories
    private final InMemoryUserRepository userRepository;
    private final InMemoryMovieRepository movieRepository;
    private final InMemorySeriesRepository seriesRepository;
    private final InMemoryCelebritiesRepository celebritiesRepository;

    // Services
    private final MoviesService moviesService;
    private final SeriesService seriesService;
    private final CelebrityService<Actor> actorService;
    private final CelebrityService<Director> directorService;
    private final DataLoaderService dataLoaderService;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private DataManager() {

        // Initialize repositories first
        this.userRepository = new InMemoryUserRepository();
        this.movieRepository = new InMemoryMovieRepository();
        this.seriesRepository = new InMemorySeriesRepository();
        this.celebritiesRepository = new InMemoryCelebritiesRepository();

        // Initialize services
        this.moviesService = MoviesService.getInstance();
        this.seriesService = SeriesService.getInstance();
        this.actorService = new CelebrityService<>(Actor.class, celebritiesRepository);
        this.directorService = new CelebrityService<>(Director.class, celebritiesRepository);


        // Initialize the data loader service last (depends on all repositories and services)
        this.dataLoaderService = new FileDataLoaderService(
                userRepository,
                movieRepository,
                seriesRepository,
                celebritiesRepository, seriesService,
                actorService,
                directorService,
                moviesService);

        logger.info("DataManager initialization complete");
    }
    
    /**
     * Returns the singleton instance of DataManager.
     * Implements thread-safe lazy initialization with double-checked locking.
     *
     * @return the singleton instance of DataManager
     */
    public static DataManager getInstance() {
        DataManager result = instance;
        if (result == null) {
            synchronized (lock) {
                result = instance;
                if (result == null) {
                    instance = result = new DataManager();
                    // Register services after instance is fully constructed
                    result.initializeServices();
                }
            }
        }
        return result;
    }
    
    /**
     * Initializes and registers services after the object is fully constructed.
     */
    private void initializeServices() {

        // Register services after object is fully constructed
        registerServices();
    }

    /**
     * Registers this instance's services with the ServiceLocator.
     * Should be called after construction to complete setup.
     */
    public void registerServices() {
        ServiceLocator locator = ServiceLocator.getInstance();

        // Register services
        locator.registerService(MoviesService.class, moviesService);
        locator.registerService(SeriesService.class, seriesService);

        // Register celebrity services
        locator.registerService(CelebrityService.class, actorService, "actor");
        locator.registerService(CelebrityService.class, directorService, "director");

        // Register data loading service
        locator.registerService(DataLoaderService.class, dataLoaderService);

        // Register repositories
        locator.registerService(InMemoryUserRepository.class, userRepository);
        locator.registerService(InMemoryMovieRepository.class, movieRepository);
        locator.registerService(InMemorySeriesRepository.class, seriesRepository);
        locator.registerService(InMemoryCelebritiesRepository.class, celebritiesRepository);

        // Register self
        locator.registerService(DataManager.class, this);

        logger.debug("All services registered with ServiceLocator");
    }

    /**
     * Loads all data from configured sources.
     */
    public void loadAllData() throws IOException {
        logger.info("Loading all data using DataLoaderService");
        dataLoaderService.loadAllData();
        logger.info("Data loading completed");
    }

    public List<Movie> getAllMovies() {
        return moviesService.getAll();
    }
    public List<Movie> getMovies() {
        return moviesService.getAll();
    }
    public List<Series> getAllSeries() {
        return seriesService.getAll();
    }


    /**
     * @return  InMemoryRepository instance
     */
    public InMemoryUserRepository getUserRepository() {
        return userRepository;
    }
    public InMemoryMovieRepository getMovieRepository() {
        return movieRepository;
    }
    public InMemorySeriesRepository getSeriesRepository() {
        return seriesRepository;
    }
    public InMemoryCelebritiesRepository getCelebritiesRepository() {
        return celebritiesRepository;
    }

    public CelebrityService<Actor> getActorService() {
        return actorService;
    }
    public CelebrityService<Director> getDirectorService() {
        return directorService;
    }

}