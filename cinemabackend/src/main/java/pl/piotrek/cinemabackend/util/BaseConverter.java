package pl.piotrek.cinemabackend.util;

import java.util.Collection;
import java.util.stream.Collectors;

@FunctionalInterface
public interface BaseConverter<F,T> {

    T convert(F from);

    default Collection<T> convertAll(Collection<F> fElements){
        Collection<T> convertedElement =
                fElements.stream()
                        .map(element -> convert(element))
                        .collect(Collectors.toList());

        return convertedElement;
    }
}