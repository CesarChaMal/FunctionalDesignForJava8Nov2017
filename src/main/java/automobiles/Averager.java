package automobiles;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

class Average {
    private double sum;
    private long count;

    public Average() {
    }

    public void include(double data) {
        sum += data;
        count++;
    }

    public void merge(Average other) {
        sum += other.sum;
        count += other.count;
    }

    public double get() {
        return sum / count;
    }
}

public class Averager {
    public static void main(String[] args) {
        long start = System.nanoTime();
        Average av = DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(-Math.PI, Math.PI))
                .unordered()
                .parallel()
                .limit(100_000_000L)
                .map(Math::sin)
//        .map(x -> Math.sin(x))
                .collect(
                        Average::new,
//            () -> new Average(),
                        Average::include,
//            (a, i) -> a.include(i),
                        Average::merge
//            (a, a2) -> a.merge(a2)
                );
        long time = System.nanoTime() - start;

        System.out.println("Average is " + av.get() + " took " + (time / 100_000_000.0) + " seconds");
//        .forEach(x -> System.out.println(x));
    }
}
