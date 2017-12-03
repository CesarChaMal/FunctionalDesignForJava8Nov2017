package automobiles;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        List<String> stringList = Arrays.asList(
                "Fred", "womble", "Jim", "Sheila", "banana");

        stringList.forEach(x -> System.out.println(x));
        System.out.println("-------------");

        System.out.println("------------- Names filter by length equal 4 -------------");
        stringList.stream().filter(x -> x.length() > 4).forEach(x -> System.out.println(x));

        System.out.println("------------- All original names -------------");
        stringList.forEach(System.out::println);

        System.out.println("------------- Names filter by length equal 4 and map to Uppercase -------------");
        stringList.stream().filter(x -> x.length() > 4).map(x -> x.toUpperCase()).forEach(x -> System.out.println(x));
        System.out.println("------------- All original names -------------");
        stringList.forEach(System.out::println);

        List<Car> fleet = Arrays.asList(
                new Car("Red", 8, "Fred", "Jim", "Sheila"),
                new Car("Blue", 7, "Alice", "Bob"),
                new Car("Black", 3, "Maverick"),
                new Car("Blue", 9, true, "Sarah", "Tara", "Ellie", "Jane"),
                new Car("Green", 5, "Brian", "Mary"),
                new Car("Red", 6, "William", "Joseph", "Lilly")
        );

        System.out.println("------------- All original cars -------------");
        fleet.forEach(System.out::println);

        System.out.println("------------- All blue cars -------------");
        fleet.stream().filter(c -> c.getColor().equals("Blue")).forEach(System.out::println);

        System.out.println("------------- All original cars -------------");
        fleet.forEach(System.out::println);
        System.out.println("------------- All blue cars with map -------------");
        fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .map(x -> x.getPassengers() + " are in a " + x.getColor() + " car ")
                .forEach(System.out::println);

        System.out.println("------------- All original cars -------------");
        fleet.forEach(System.out::println);
        System.out.println("------------- All passengers from blue cars with flat map -------------");
        fleet.stream()
//                .parallel() // won't improve anything here.
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap(c -> c.getPassengers().stream())
                .forEach(System.out::println);

        System.out.println("------------- All original cars -------------");
        fleet.forEach(System.out::println);
        System.out.println("------------- All blue cars and his passengers with flat map -------------");
        fleet.stream()
//                .parallel() // won't improve anything here.
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap(c -> c.getPassengers().stream()
                        .map(x -> x + " is in a " + c.getColor() + " car "))
                .forEach(System.out::println);

        System.out.println("------------- All blue cars and his passengers with flat map in a block lambda -------------");
        fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap((Car c) -> {
                    return c.getPassengers().stream()
                            .map(x -> x + " is in a " + c.getColor() + " car ");
                })
                .forEach(System.out::println);

        System.out.println("------------- All blue cars and his passengers with flat map in 2 block lambda -------------");
        fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap((Car c) -> {
                    return c.getPassengers().stream()
                            .map((String x) -> {
                                return x + " is in a " + c.getColor() + " car ";
                            });
                })
                .forEach(System.out::println);

        System.out.println("------------- All blue cars and his passengers with flat map in 3 block lambda -------------");
        fleet.stream()
                .filter((Car c) -> {
                    return c.getColor().equals("Blue");
                })
                .flatMap((Car c) -> {
                    return c.getPassengers().stream()
                            .map((String x) -> {
                                return x + " is in a " + c.getColor() + " car ";
                            });
                })
                .forEach(x -> System.out.println(x));

        System.out.println("------------- All blue cars and his passengers with flat map in 4 block lambda -------------");
        fleet.stream()
                .filter((Car c) -> {
                    return c.getColor().equals("Blue");
                })
                .flatMap((Car c) -> {
                    return c.getPassengers().stream()
                            .map((String x) -> {
                                return x + " is in a " + c.getColor() + " car ";
                            });
                })
                .forEach((String x) -> {
                    System.out.println(x);
                });

        System.out.println("------------- All blue cars and his passengers with anonymous classes -------------");
        fleet.stream()
                .filter(new Predicate<Car>() {
                    @Override
                    public boolean test(Car c) {
                        return c.getColor().equals("Blue");
                    }
                })
                .flatMap(new Function<Car, Stream<? extends String>>() {
                    @Override
                    public Stream<? extends String> apply(Car c) {
                        return c.getPassengers().stream()
                                .map(new Function<String, String>() {
                                    @Override
                                    public String apply(String x) {
                                        return x + " is in a " + c.getColor() + " car ";
                                    }
                                });
                    }
                })
                .forEach(new Consumer<String>() {
                    @Override
                    public void accept(String x) {
                        System.out.println(x);
                    }
                });

        System.out.println("------------- Just playing around -------------");
        Stream.of(fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .map(x -> x.getPassengers() + " are in a " + x.getColor() + " car ")
        )
//                .flatMap(x -> x)
                .flatMap(Function.identity())
                .forEach(System.out::println);

        System.out.println("------------- Just playing around -------------");
        Arrays.asList(fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .map(x -> x.getPassengers() + " are in a " + x.getColor() + " car ")
        ).stream()
                .flatMap(Function.identity())
                .forEach(System.out::println);

        System.out.println("------------- Just playing around -------------");
        Stream.of(fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap(c -> c.getPassengers().stream().map(x -> x + " is in a " + c.getColor() + " car "))
        )
//                .flatMap(x -> x)
                .flatMap(Function.identity())
                .forEach(System.out::println);

        System.out.println("------------- Just playing around -------------");
        Arrays.asList(fleet.stream()
                .filter(c -> c.getColor().equals("Blue"))
                .flatMap(c -> c.getPassengers().stream().map(x -> x + " is in a " + c.getColor() + " car "))
        ).stream()
                .flatMap(Function.identity())
                .forEach(System.out::println);
    }
}
