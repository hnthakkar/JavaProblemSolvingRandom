import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class FifoReadWriterLock implements ReadWriteLock {
	int readAcquires, readReleases;
	boolean writer;
	Lock lock;
	Condition condition;
	Lock readLock, writeLock;

	public FifoReadWriterLock() {
		readAcquires = readReleases = 0;
		writer = false;
		lock = new ReentrantLock();
		condition = lock.newCondition();
		readLock = new ReadLock();
		writeLock = new WriteLock();
	}

	public Lock readLock() {
		return readLock;
	}

	public Lock writeLock() {
		return writeLock;
	}
	
	private final class ReadLock implements Lock {
		
		public void lock() {
			lock.lock();
			try {
				while (writer) {
					condition.await();
				}
				readAcquires++;
			} catch(InterruptedException e){
				//TODO
				System.out.println("Exception in ReadLock.lock");
			} finally {
				lock.unlock();
				//condition.signalAll();
			}
		}

		public void unlock() {
			lock.lock();
			try {
				readReleases++;
				if (readAcquires == readReleases)
					condition.signalAll();
			} finally {
				lock.unlock();
			}
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
				throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private class WriteLock implements Lock {
		@Override
		public void lock() {
			lock.lock();
			try {
				while(writer){
					condition.await();
				}
				writer = true;
				while ((readAcquires != readReleases)){
					condition.await();
				}
				
			} catch(InterruptedException e){
				//TODO
				System.out.println("Exception in WriterLock.lock");
			} finally {
				lock.unlock();
			}
		}
		@Override
		public void unlock() {
			lock.lock();
			writer = false;
			condition.signalAll();
			lock.unlock();
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
				throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
