package automobiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class UseCars {

    public static <X> List<X> getByCriterion(List<X> in, Predicate<X> crit) {
        List<X> out = new ArrayList<>();
        for (X c : in) {
            if (crit.test(c)) {
                out.add(c);
            }
        }
        return out;
    }

    public static void showAll(List<?> lc) {
        for (Object c : lc) {
            System.out.println("> " + c);
        }
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {
        List<Car> fleet = Arrays.asList(
                new Car("Red", 8, "Fred", "Jim", "Sheila"),
                new Car("Blue", 7, "Alice", "Bob"),
                new Car("Black", 3, "Maverick"),
                new Car("Blue", 9, true, "Sarah", "Tara", "Ellie", "Jane"),
                new Car("Green", 5, "Brian", "Mary"),
                new Car("Red", 6, "William", "Joseph", "Lilly")
        );

        showAll(fleet);

        showAll(getByCriterion(fleet, Car.getRedCarCriterion()));
        showAll(getByCriterion(fleet, Car.getFuelLevelCriterion(5)));

        Predicate<Car> greenCarCrit = c -> c.getColor().equals("Green");
        System.out.println("Green:");
        showAll(getByCriterion(fleet, greenCarCrit));

        Predicate<Car> blueCarCrit = c -> c.getColor().equals("Blue");
        System.out.println("Blue:");
        showAll(getByCriterion(fleet, blueCarCrit));

        // static member version
//      Criterion<Car> notGreen = Criterion.negate(greenCarCrit);
        Predicate<Car> notGreen = greenCarCrit.negate();
        System.out.println("Not Green:");
        showAll(getByCriterion(fleet, notGreen));

        Predicate<Car> notBlue = blueCarCrit.negate();
        System.out.println("Not Blue:");
        showAll(getByCriterion(fleet, notBlue));

        System.out.println("Less than six fuel and not Green");
        showAll(getByCriterion(fleet, notGreen.and(Car.getFuelLevelCriterion(6))));

        System.out.println("Less than six fuel and not Blue");
        showAll(getByCriterion(fleet, notBlue.and(Car.getFuelLevelCriterion(6))));

        System.out.println("Green and Blue Cars");
        showAll(getByCriterion(fleet, greenCarCrit.or(blueCarCrit)));

        showAll(getByCriterion(fleet, Car.getOneOfManyBlueCriteria()));

        List<String> ls = Arrays.asList("Fred", "Jim", "Sheila", "womble", "banana");
        showAll(getByCriterion(ls, s -> s.length() > 4));
        showAll(getByCriterion(ls, s -> Character.isUpperCase(s.charAt(0))));
    }
}
