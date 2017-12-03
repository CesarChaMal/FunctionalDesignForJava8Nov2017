package automobiles;

@FunctionalInterface
public interface Criterion<E> {
    boolean test(E c);

    default Criterion<E> negate() {
        return x -> !this.test(x);
    }

    default Criterion<E> and(Criterion<E> second) {
        return x -> this.test(x) && second.test(x);
    }

    default Criterion<E> or(Criterion<E> second) {
        return x -> this.test(x) || second.test(x);
    }

//  public static <X> Criterion<X> negate(Criterion<X> crit) {
//    return x -> ! crit.test(x);
//  }
//  
//  public static <X> Criterion<X> and(Criterion<X> first, Criterion<X> second) {
//    return x -> first.test(x) && second.test(x);
//  }
//
//  public static <X> Criterion<X> or(Criterion<X> first, Criterion<X> second) {
//    return x -> first.test(x) || second.test(x);
//  }
}
