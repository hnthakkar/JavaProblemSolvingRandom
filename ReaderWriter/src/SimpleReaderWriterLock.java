import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleReaderWriterLock implements ReadWriteLock {
	//int readers;
	//boolean writer;
	ReadWriteLock lock;
	//Condition condition;
	Lock readLock, writeLock;

	public SimpleReaderWriterLock() {
		//writer = false;
		//readers = 0;
		lock = new ReentrantReadWriteLock();
		readLock = lock.readLock();
		writeLock = lock.writeLock();
		//condition = lock.newCondition();
	}

	public Lock readLock() {
		return readLock;
	}

	public Lock writeLock() {
		return writeLock;
	}
	
	/*private class ReadLock implements Lock {
		public void lock() {
			lock.lock();
			try {
				while (writer) {
					condition.await();
				}
				readers++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

		public void unlock() {
			lock.lock();
			try {
				readers--;
				if (readers == 0)
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
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tryLock(long arg0, TimeUnit arg1)
				throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private class WriteLock implements Lock {
		public void lock() {
			lock.lock();
			try {
				while (readers > 0) {
					condition.await();
				}
				writer = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

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
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}
	}*/
		 
}


