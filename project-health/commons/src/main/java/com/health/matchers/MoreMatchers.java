package com.health.matchers;


import com.google.common.base.Function;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

public class MoreMatchers {

    public static <S, T> Matcher<S> transform(final Matcher<T> m, final Function<S, T> func) {
        return new AbstractMatcher<S>() {
            @Override
            public boolean matches(S o) {
                return m.matches(func.apply(o));
            }
        };
    }

    public static Matcher<TypeLiteral<?>> literalSubclassesOf(Class<?> clazz) {
        return MoreMatchers.transform(Matchers.subclassesOf(clazz), new Function<TypeLiteral<?>, Class>() {
            @Override
            public Class apply(TypeLiteral<?> input) {
                return input.getRawType();
            }
        });
    }
}
