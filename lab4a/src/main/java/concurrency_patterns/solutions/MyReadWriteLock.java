package concurrency_patterns.solutions;

public interface MyReadWriteLock {
    void lockReader();
    void unlockReader();
    void lockWriter();
    void unlockWriter();
}
