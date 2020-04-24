import java.util.concurrent.Semaphore;
import java.lang.Thread;

class HairSalon
{
  private Semaphore hairdresserSleep = new Semaphore(1);
  private Semaphore hairdresserWork = new Semaphore(1);
  private Semaphore visitorSleep = new Semaphore(1);
  private Thread hairdresser = null;
  private volatile Boolean hairdresserIsRunning = false;
  public HairSalon()
  {
    try {
      hairdresserSleep.acquire();
      visitorSleep.acquire();
    } catch (InterruptedException e) {}
    hairdresserIsRunning = true;
    hairdresser = new Thread(() ->
      {
        while (hairdresserIsRunning)
        {
          try {
          hairdresserSleep.acquire();
          }
          catch (InterruptedException e)
          {

          }
          if (!hairdresserIsRunning) {return;}
          try {
            Thread.sleep(100);
          }
          catch (Exception e) {}
          System.out.println("Processed the visitor");
          visitorSleep.release();
          hairdresserWork.release();
        }
      });
    hairdresser.start();
  }

  @Override
  public void finalize()
  {
    EndWork();
  }

  public void AddVisitor()
  {
    Thread visitor = new Thread(() ->
      {
        try {
          hairdresserWork.acquire();
          hairdresserSleep.release();
          visitorSleep.acquire();
        }
        catch (InterruptedException e) {}
      });
    visitor.start();
  }

  public void EndWork()
  {
    hairdresserIsRunning = false;
    hairdresserSleep.release();
  }
}

class Program
{
  static void Main(String[] args)
  {
    HairSalon hairSalon = new HairSalon();

    for (int i = 0; i < 10; ++i)
    {
      hairSalon.AddVisitor();
    }
    try {
      Thread.sleep(1000);
    } catch (Exception e) {}
    hairSalon.EndWork();
  }
}
