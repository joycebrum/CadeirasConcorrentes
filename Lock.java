import java.util.*;

public class Lock
{
	public boolean vlock;
	public Lock()
	{
		vlock = false;
	}
	
	public synchronized void lock() throws InterruptedException
	{
		while(vlock)
		{
			wait();
		}
		vlock = true;
		
	}
	
	public synchronized void unlock()
	{
		vlock = false;
		notifyAll();
	}	
	
}
