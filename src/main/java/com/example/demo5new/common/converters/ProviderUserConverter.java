package com.example.demo5new.common.converters;

public interface ProviderUserConverter<T,R> {
    R convert(T t);
}
