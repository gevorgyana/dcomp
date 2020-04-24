import java.lang.Thread;
import java.util.concurrent.CyclicCyclicBarrier;
import java.util.Random;

class Recruits
{
  private  int threadCount = 2;
  private  int recruitCount = 100;
  private CyclicBarrier barrier = new CyclicBarrier(threadCount);
  private Thread[] threads = new Thread[threadCount];
  private object toLockIsEnd = new object();
  private volatile bool isRunning = true;
  private bool[] isRotated = new bool[threadCount];
  private bool[] recruits = new bool[recruitCount];
  private Random random = new Random();

  public Recruits()
  {
    for (int i = 0; i < recruitCount; ++i)
    {
      recruits[i] = random.Next(2) == 0;
    }
    for (int i = 0; i < threadCount; ++i)
    {
      threads[i] = new Thread(new ParameterizedThreadStart((object i) ->
        {
          int index = (int)i;
          while (isRunning)
          {
            lock (isRotated)
            {
              isRotated[index] = false;
            }
            for (int j = index; j < recruitCount; j += threadCount)
            {
              lock (recruits)
              {
                if (recruits[j] && j < recruitCount - 1 && !recruits[j + 1])
                {
                  recruits[j] = false;
                  isRotated[index] = true;
                }
                else if (!recruits[j] && j > 0 && recruits[j - 1])
                {
                  recruits[j] = true;
                  isRotated[index] = true;
                }
              }
            }
            barrier.SignalAndWait();
            lock (toLockIsEnd)
            {
              isRunning = false;
              for (int j = 0; j < threadCount; ++j)
              {
                if (isRotated[j])
                {
                  isRunning = true;
                }
              }
              if (!isRunning)
              {
                System.out.println("Completed");
              }
            }
            barrier.SignalAndWait();
          }
        }));
      threads[i].start(i);
    }
  }
}


class Program
{
  static void Main(String[] args)
  {
    Recruits recruits = new Recruits();
    try {
      Thread.sleep(1000);
    } catch (Exception e) {}
  }
}
