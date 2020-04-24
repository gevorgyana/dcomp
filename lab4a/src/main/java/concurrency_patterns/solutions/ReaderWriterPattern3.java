package concurrency_patterns.solutions;

import java.util.concurrent.Semaphore;

public class ReaderWriterPattern3 implements MyReadWriteLock {
    private Semaphore serviceQueue;
    private Semaphore readCountAccess;
    private Semaphore write;
    private int readCount;

    public ReaderWriterPattern3(){
        readCount = 0;
        serviceQueue = new Semaphore(1, true);
        readCountAccess = new Semaphore(1, true);
        write = new Semaphore(1, true);
    }

    @Override
    public void lockReader() {
        try{
            serviceQueue.acquire();
            readCountAccess.acquire();
            try {
                if (readCount == 0)
                    write.acquire();
                readCount++;
            } catch (InterruptedException e){
            } finally {
                readCountAccess.release();
            }
        } catch (InterruptedException e){
        } finally {
            serviceQueue.release();
        }
    }

    @Override
    public void unlockReader() {
        try {
            readCountAccess.acquire();
            readCount--;
            if (readCount == 0) {
                write.release();
            }
            readCountAccess.release();
        } catch (InterruptedException e){
        }
    }

    @Override
    public void lockWriter() {
        try {
            serviceQueue.acquire();
            write.acquire();
        } catch (InterruptedException e){
        } finally {
            serviceQueue.release();
        }
    }

    @Override
    public void unlockWriter() {
        write.release();
    }
}
