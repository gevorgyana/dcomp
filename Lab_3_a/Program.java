import java.lang.Thread;
import java.util.concurrent.Semaphore;

class SemaphoreW
{
  Semaphore semaphore_p = new Semaphore(1);
  public SemaphoreW() {}
  public Boolean TryLock() {
    return semaphore_p.tryAcquire();
  }
  public void Lock() {
    try {
      semaphore_p.acquire();
    } catch (InterruptedException e) {

    }
  }
  public void UnLock() {
    semaphore_p.release();
  }
}

class Hive
{
  private SemaphoreW accessToHoney = new SemaphoreW();
  private SemaphoreW pingToBear = new SemaphoreW();

  private int iterationCount = 10;
  private int currentIteration = 0;
  private int maxSize = 100;
  private int currentSize = 0;

  private Thread bear = null;
  private Thread[] bees = null;
  private int beesCount = 10;

  private volatile Boolean bearIsRunning = false;
  private volatile Boolean beesIsRunning = false;

  public Hive()
  {
    pingToBear.Lock();
  }

  @Override
  public void finalize()
  {
    bearIsRunning = false;
    beesIsRunning = false;
  }

  public void StartBear()
  {
    bearIsRunning = true;

    bear = new Thread(() ->
      {
        while (bearIsRunning)
        {
          pingToBear.Lock();

          accessToHoney.Lock();

          currentSize = 0;
          System.out.println("Cup is empty");

          ++currentIteration;

          if (currentIteration == iterationCount)
          {
            bearIsRunning = false;
            beesIsRunning = false;
          }

          accessToHoney.UnLock();
        }
      });
    bear.start();
  }

  public void StartBees()
  {
    beesIsRunning = true;

    bees = new Thread[beesCount];
    for (int i = 0; i < beesCount; ++i)
    {
      bees[i] = new Thread(() ->
        {
          while (beesIsRunning)
          {
            accessToHoney.Lock();

            if (currentSize < maxSize)
            {
              ++currentSize;

              if (currentSize == maxSize)
              {
                pingToBear.UnLock();
              }
            }

            accessToHoney.UnLock();
          }
        });
      bees[i].start();
    }
  }
}

class Program
{
  static void Main(String[] args)
  {
    Hive hive = new Hive();

    hive.StartBear();
    hive.StartBees();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {}
  }
}
