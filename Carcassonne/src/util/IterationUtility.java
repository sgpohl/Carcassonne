package util;

import logic.Direction;
import logic.Type;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class IterationUtility {


    public static void forEachTypeDirection(BiConsumer<Type, Direction> consumer) {
        for (var type : Type.values())
            for (var direction : Direction.values())
                consumer.accept(type, direction);
    }

    public static <T> Collection<T> mapOnTypeDirection(BiFunction<Type, Direction, T> function) {
        List<T> res = new ArrayList<>();
        for (var type : Type.values())
            for (var direction : Direction.values())
                res.add(function.apply(type, direction));
        return res;
    }




}
