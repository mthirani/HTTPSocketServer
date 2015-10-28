import java.util.LinkedList;

public class WorkQueue {

    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList<Runnable> queue;
    private volatile boolean shutdownReq=false;

    /**
     * Construct a WorkQueue with 10 default workers.
     */
    public WorkQueue()
    {
        this(10);
    }

    public WorkQueue(int nThreads)
    {
        this.nThreads=nThreads;
        queue=new LinkedList<Runnable>();
        threads=new PoolWorker[nThreads];
        for (int i=0; i<nThreads; i++)
        {
            threads[i]=new PoolWorker();
            threads[i].start();
        }
    }

    /**
     * Execute a new Runnable job.
     * @param r
     */
    public void execute(Runnable r)
    {
    	if(!shutdownReq)
    	{
    		synchronized(queue)
            {
            	queue.addLast(r);
                queue.notifyAll();
            }
    	}
    }

    /**
     * Stop accepting new jobs.
     * This method should not block until work is complete.
     */
    public void shutdown()
    {
        shutdownReq=true;
        synchronized(queue)
        {
        	queue.notifyAll();
        }
    }
    
    /**
     * Block until all jobs in the queue are complete.
     */
    public void awaitTermination()
    {
        for(int i=0;i<nThreads;i++)
        {
            try
            {
                threads[i].join();
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * This Class is used as a thread-pool.
     */
    private class PoolWorker extends Thread
    {
        public void run()
        {
            Runnable r;
            while(true)
            {
                synchronized(queue)
                {
                    while(queue.isEmpty() && shutdownReq==false)
                    {
                        try
                        {
                            queue.wait();
                        }catch(InterruptedException ignored)
                        {
                            ignored.printStackTrace();
                        }
                    }
                    if(shutdownReq && queue.isEmpty())
                    {
                        break;
                    }
                    r=(Runnable)queue.removeFirst();
                }
                try
                {
                    r.run();
                }catch(RuntimeException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}