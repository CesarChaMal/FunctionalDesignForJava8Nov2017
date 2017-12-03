package automobiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SuperIterable<E> implements Iterable<E> {
    private Iterable<E> self;

    public SuperIterable(Iterable<E> self) {
        this.self = self;
    }

    public SuperIterable<E> filter(Predicate<E> predicate) {
        List<E> out = new ArrayList<>();
        for (E e : self) {
            if (predicate.test(e)) {
                out.add(e);
            }
        }
        return new SuperIterable<>(out);
    }

    public <F> SuperIterable<F> map(Function<E, F> op) {
        List<F> out = new ArrayList<>();
        for (E e : self) {
            out.add(op.apply(e));
        }
        return new SuperIterable<>(out);
    }

    public <F> SuperIterable<F> flatMap(Function<E, SuperIterable<F>> op) {
        List<F> out = new ArrayList<>();
        for (E e : self) {
            for (F f : op.apply(e)) {
                out.add(f);
            }
        }
        return new SuperIterable<>(out);
    }

    public SuperIterable<E> distinct() {
        Set<E> out = new HashSet<>();
        self.forEach(e -> out.add(e));
        return new SuperIterable<>(out);
    }

    // Iterator defines forEach, which does this...
//  public void forEvery(Consumer<E> op) {
//    for (E e : self) {
//      op.accept(e);
//    }
//  }
//  
    @Override
    public Iterator<E> iterator() {
        return self.iterator();
    }

    public static void showAll(SuperIterable<?> i) {
        i.forEach(x -> System.out.println(x));
        System.out.println("----------------------------------");
    }

    public static void main(String[] args) {
        SuperIterable<String> stringIter = new SuperIterable<>(Arrays.asList(
                "Fred", "womble", "Jim", "Sheila", "banana"));

        stringIter.forEach(x -> System.out.println(x));
        System.out.println("-------------");
        showAll(stringIter.filter(x -> x.length() > 4));
        showAll(stringIter);
        showAll(stringIter.filter(x -> x.length() > 4).map(x -> x.toUpperCase()));
        showAll(stringIter);

        SuperIterable<Car> fleet = new SuperIterable<>(Arrays.asList(
                new Car("Red", 8, "Fred", "Jim", "Sheila"),
                new Car("Blue", 7, "Alice", "Bob"),
                new Car("Black", 3, "Maverick"),
                new Car("Blue", 9, true, "Sarah", "Tara", "Ellie", "Jane"),
                new Car("Green", 5, "Brian", "Mary"),
                new Car("Red", 6, "William", "Joseph", "Lilly")
        ));

        System.out.println("------------- All original cars -------------");
        showAll(fleet);

        System.out.println("------------- All blue cars -------------");
        showAll(
                fleet
                        .filter(c -> c.getColor().equals("Blue"))
        );

        System.out.println("------------- All original cars -------------");
        showAll(fleet);
        System.out.println("------------- All blue cars with map -------------");
        showAll(
                fleet
                        .filter(c -> c.getColor().equals("Blue"))
                        .map(x -> x.getPassengers() + " are in a " + x.getColor() + " car ")
        );

        System.out.println("------------- All original cars -------------");
        showAll(fleet);
        System.out.println("------------- All passengers with flat map -------------");
        showAll(
                fleet
                        .filter(c -> c.getColor().equals("Blue"))
                        .flatMap(c -> new SuperIterable<>(c.getPassengers()))
        );

        System.out.println("------------- All original cars -------------");
        showAll(fleet);
        System.out.println("------------- All blue cars and his passengers with flat map -------------");
        showAll(
                fleet
                        .filter(c -> c.getColor().equals("Blue"))
                        .flatMap(c -> new SuperIterable<>(c.getPassengers())
                                .map(x -> x + " is in a " + c.getColor() + " car "))
        );

        System.out.println("------------- All blue cars and his passengers with flat map in a block lambda -------------");
        showAll(
                fleet
                        .filter(c -> c.getColor().equals("Blue"))
                        .flatMap((Car c) -> {
                            return new SuperIterable<>(c.getPassengers())
                                    .map(x -> x + " is in a " + c.getColor() + " car ");
                        })
        );

        System.out.println("------------- All blue cars and his passengers with flat map in 2 block lambda -------------");
        showAll(
                fleet
                        .filter(c -> c.getColor().equals("Blue"))
                        .flatMap((Car c) -> {
                            return new SuperIterable<>(c.getPassengers())
                                    .map((String  x) -> {
                                        return x + " is in a " + c.getColor() + " car ";
                                    });
                        })
        );

        System.out.println("------------- All blue cars and his passengers with flat map in 3 block lambda -------------");
        showAll(
                fleet
                        .filter((Car c) -> {
                            return c.getColor().equals("Blue");
                        })
                        .flatMap((Car c) -> {
                            return new SuperIterable<>(c.getPassengers())
                                    .map((String  x) -> {
                                        return x + " is in a " + c.getColor() + " car ";
                                    });
                        })
        );

        System.out.println("------------- All blue cars and his passengers with anonymous classes -------------");
        showAll(
                fleet
                        .filter(new Predicate<Car>() {
                            @Override
                            public boolean test(Car c) {
                                return c.getColor().equals("Blue");
                            }
                        })
                        .flatMap(new Function<Car, SuperIterable<String>>() {
                            @Override
                            public SuperIterable<String> apply(Car c) {
                                return new SuperIterable<>(c.getPassengers())
                                        .map(new Function<String, String>() {
                                            @Override
                                            public String apply(String x) {
                                                return x + " is in a " + c.getColor() + " car ";
                                            }
                                        });
                            }
                        })
        );

        System.out.println("------------- Just playing around -------------");
        Stream.of(fleet
                .filter(c -> c.getColor().equals("Blue"))
                .map(x -> x.getPassengers() + " are in a " + x.getColor() + " car ")
        ).map(x -> x.self)
                .forEach(System.out::println);

        System.out.println("------------- Just playing around -------------");
        Arrays.asList(fleet
                .filter(c -> c.getColor().equals("Blue"))
                .map(x -> x.getPassengers() + " are in a " + x.getColor() + " car ")
        ).stream()
                .map(x -> x.self)
                .forEach(System.out::println);

        System.out.println("------------- Just playing around -------------");
        Stream.of(fleet
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap(c -> new SuperIterable<>(c.getPassengers()).map(x -> x + " is in a " + c.getColor() + " car "))
        ).map(x -> x.self)
                .forEach(System.out::println);

        System.out.println("------------- Just playing around -------------");
        Arrays.asList(fleet
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap(c -> new SuperIterable<>(c.getPassengers()).map(x -> x + " is in a " + c.getColor() + " car "))
        ).stream()
                .map(x -> x.self)
                .forEach(System.out::println);
    }
}
