package util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Tuple<A, B> implements Iterable<Object> {

    private final A a;
    private final B b;

    public Tuple(A a, B b) {
        this.a = Objects.requireNonNull(a);
        this.b = Objects.requireNonNull(b);
    }

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    public String toString() {
        return "Tuple(" + a + ", " + b + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return a.equals(tuple.a) &&
                b.equals(tuple.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public Iterator<Object> iterator() {
        return Arrays.stream(new Object[]{a, b}).iterator();
    }

    public static <A, B> BiFunction<A, B, Tuple<A, B>> combine() {
        return Tuple::new;
    }

    public static <A, B> Collection<Tuple<A, B>> getCrossProduct(Stream<A> as, Stream<B> bs) {
        List<Tuple<A, B>> res = new ArrayList<>();
        as.iterator().forEachRemaining(a -> bs.iterator().forEachRemaining(b -> res.add(new Tuple<>(a, b))));
        return res;
    }
}
