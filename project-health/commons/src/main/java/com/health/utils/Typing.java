package com.health.utils;


import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.internal.MoreTypes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Typing {
    public static List<Type> getTypeParameters(Type type) {
        Preconditions.checkArgument(type instanceof ParameterizedType, "No type parameter was found");
        ParameterizedType pType = (ParameterizedType) type;
        Type[] types = pType.getActualTypeArguments();
        return Lists.newArrayList( Iterables.transform(Arrays.asList(types), new Function<Type, Type>() {
            @Override
            public Type apply(Type input) {
                return MoreTypes.canonicalize(input);
            }
        }));
    }
}
