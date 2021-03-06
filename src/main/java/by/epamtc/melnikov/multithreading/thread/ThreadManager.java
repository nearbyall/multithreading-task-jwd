package by.epamtc.melnikov.multithreading.thread;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamtc.melnikov.multithreading.bean.matrix.Matrix;
import by.epamtc.melnikov.multithreading.bean.matrix.MatrixState;
import by.epamtc.melnikov.multithreading.logic.Writer;

public class ThreadManager {

	private final static Logger logger = LogManager.getLogger(ThreadManager.class);
	
	private ReentrantLock lock;
	private Writer writer;
	private CountDownLatch latch;
	private MatrixState matrixState;
	
	public ThreadManager(ReentrantLock lock, Writer writer, MatrixState matrixState) {
		this.lock = lock;
		this.writer = writer;
		this.matrixState = matrixState;
	}
    
	public void runThreads(int y, int n) throws InterruptedException, IOException {
		int threadsCount = y * n;
		ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
		latch = new CountDownLatch(n);
		for (int i = 1; i < threadsCount + 1; i++) {
			MatrixFillingThread action = new MatrixFillingThread(i, lock, latch, matrixState);
			executorService.submit(action);
			TimeUnit.MILLISECONDS.sleep(100);
			if (i % n == 0) {
				writeInfo(n);
				matrixState.resetStates();
			}
		}
		executorService.shutdown();
	}

	public void writeInfo(int n) throws IOException, InterruptedException {
		
		TempInfo info = TempInfo.getInstance();
		Matrix matrix = Matrix.getInstance();
		
		Map<Integer, String> infoMap = info.getTempResults();
		
		writer.writeTemp(infoMap);
		logger.info("Writing is started");
		writer.writeMatrix(matrix);
		logger.info("Writing is ended");
		
		infoMap.clear();
		latch.await();
		latch = new CountDownLatch(n);
		
	}
    
}
