package reentrantlock1;

import java.util.Random;

public class PriceUpdater extends Thread {
    private PricesContainer pricesContainer;
    private Random random = new Random();

    public PriceUpdater(PricesContainer pricesContainer) {
        this.pricesContainer = pricesContainer;
    }

    @Override
    public void run() {
        while(true) {
            pricesContainer.getLockObject().lock();
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                pricesContainer.setBitcoinPrice(random.nextInt(20000));
                pricesContainer.setEtherPrice(random.nextInt(2000));
                pricesContainer.setLitecoinPrice(random.nextInt(500));
                pricesContainer.setBitcoinCashPrice(random.nextInt(5000));
                pricesContainer.setRipplePrice(random.nextDouble());
            } finally {
                pricesContainer.getLockObject().unlock();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}