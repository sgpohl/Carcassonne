package util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Triple<A, B, C> implements Iterable<Object> {

    private final A a;
    private final B b;
    private final C c;

    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    public C getThird() {
        return c;
    }

    public Tuple<B, C> ignoreFirst() {
        return new Tuple<>(b, c);
    }

    public Tuple<A, C> ignoreMiddle() {
        return new Tuple<>(a, c);
    }

    public Tuple<A, B> ignoreLast() {
        return new Tuple<>(a, b);
    }

    @Override
    public Iterator<Object> iterator() {
        return Arrays.stream(new Object[]{a, b, c}).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(a, triple.a) &&
                Objects.equals(b, triple.b) &&
                Objects.equals(c, triple.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}
