package ua.tifoha;

import static java.lang.Thread.sleep;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
	private static final Collection<Object> leak = new ArrayList<>();
	private static volatile Object sink;
	static {
//		CompletableFuture.runAsync(() -> {
//				try {
//					while (true) {
//						try {
//							sleep(100);
//						} catch (InterruptedException e) {
//							System.out.println("Interrupted");
//							return;
//						}
//						try {
//							for (int i = 0; i < Integer.MAX_VALUE; i++) {
//								if (i % 10_000_000 == 0) {
//									System.out.println(i + ": " + Thread.currentThread().isInterrupted());
//								}
//							}
//							System.out.println("Do something.");
//						} catch (Exception e) {
//							System.err.println("Exception");
//						}
//					}
//				} finally {
//					System.out.println("finally");
//				}
//			}, Executors.newSingleThreadExecutor());
//		final Thread thread = new Thread("Flush Log Queue Thread") {
//			@Override
//			public void run() {
//				try {
//					while (true) {
//						try {
//							sleep(100);
//						} catch (InterruptedException e) {
//							System.out.println("Interrupted");
//							return;
//						}
//						try {
//							for (int i = 0; i < Integer.MAX_VALUE; i++) {
//								if (i % 10_000_000 == 0) {
//									System.out.println(i + ": " + Thread.currentThread().isInterrupted());
//								}
//							}
//							System.out.println("Do something.");
//						} catch (Exception e) {
//							System.err.println("Exception");
//						}
//					}
//				} finally {
//					System.out.println("finally");
//				}
//			}
//		};
//		thread.setDaemon(true);
//		thread.start();
	}
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.submit(() -> {
			System.out.println("Task1");
			throw new RuntimeException("Exception");
		});
		exec.execute(() -> {
//			try {
//				TimeUnit.MILLISECONDS.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			System.out.println("Task2");
		});
		exec.shutdown();
//		executorTest();


////		Callable<Void> c = () -> {
////			TimeUnit.DAYS.sleep(5);
////			return null;
////		};
//
//		ExecutorService exec = Executors.newSingleThreadExecutor(r -> {
//			final Thread thread = new Thread(r);
//			thread.setDaemon(true);
//			return thread;
//		});
//		exec.submit(()->{
////		final Thread thread = new Thread(() -> {
//			while (true) {
//				try {
//					System.out.println(Thread.interrupted());
//					TimeUnit.MILLISECONDS.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
////					return;
//				}
//			}
//		});
////		thread.setDaemon(true);
////		thread.start();

		TimeUnit.MILLISECONDS.sleep(300);
		System.out.println("end.");
////		thread.interrupt();
////		exec.shutdownNow();
////		while (true) {
////			try {
////				leak.add(new byte[1024 * 1024]);
////				sink = new byte[1024 * 1024];
////			} catch (OutOfMemoryError e) {
////				leak.clear();
////				System.err.println(e.getLocalizedMessage());
////			}
////		}
	}

	private static void executorTest() throws InterruptedException {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.submit(() ->{
			while (!Thread.currentThread().isInterrupted()) {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					if (i % 10_000_000 == 0) {
						System.out.println(i + ": " + Thread.currentThread().isInterrupted());
					}
				}
			}
		});

		TimeUnit.MILLISECONDS.sleep(100);
		exec.shutdownNow();
	}
}
