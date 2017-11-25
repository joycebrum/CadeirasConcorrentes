import java.util.*;
/**
 * protege uma secao critica do codigo dando acesso a apenas uma thread e bloqueando as demais 
 * ate que a primeira encerre o seu processamento
 *
 * @param  vlock  um booleano que indica se alguma thread ja possui o lock
 */
public class Lock
{
	public boolean vlock;
	public Lock()
	{
		vlock = false;
	}
	/**
	 * Concede o lock a uma determinada thread, fazendo com que quaisquer outras threads sejam bloqueadas
	 * ate que a primeira chame o metodo unlock()
	 */
	public synchronized void lock() throws InterruptedException
	{
		while(vlock)
		{
			wait();
		}
		vlock = true;
		
	}
	/**
	 * Libera o lock, colocando vlock como false e acordando todas as threads que estao esperando
	 */
	public synchronized void unlock()
	{
		vlock = false;
		notifyAll();
	}	
	
}
