package util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Tuple<A, B> implements Iterable<Object>{

    private final A a;
    private final B b;

    public Tuple(A a, B b){
        this.a = Objects.requireNonNull(a);
        this.b = Objects.requireNonNull(b);
    }

    public A getFirst(){
        return a;
    }

    public B getSecond(){
        return b;
    }

    public String toString(){
        return "Tuple(" + a + ", " + b + ")";
    }

    @Override
    public Iterator<Object> iterator() {
        return Arrays.stream(new Object[]{a, b}).iterator();
    }
}
