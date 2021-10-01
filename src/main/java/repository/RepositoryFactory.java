package repository;

public class RepositoryFactory {
    public RepositoryFactory() {
    }

    public Repository createRepository() {
        return RepositoryImpl.getInstance();
    }
}