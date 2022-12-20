import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataRace {
    private static Random random = new Random();
    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForDataRace();
            }
        });
        thread1.start();
        thread2.start();
    }

    public static class SharedClass {
        private int a = 0, b = 0, c = 0;
        private int x = 0, y = 0;
        private volatile int z = 0;

        private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();

        public void increment() {
            a++;
            b++;
            c++;
            z++; // volatile variable
            x++;
            y++;
        }

        public void checkForDataRace() {
            // Data race error will show as reading from variables is not blocking task
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (c > b || b > a) {
                System.out.println("c > b or b > a — Data race is detected");
            }
            if (y > x) {
                System.out.println("y > x — Data race is detected");
            }
            if (x > a || x > b || x > c || y > a || y > b || y > c) {
                System.out.println("Data race across volatile variable is detected");
            }
        }
    }
}
