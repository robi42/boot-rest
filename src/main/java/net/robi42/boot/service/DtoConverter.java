package net.robi42.boot.service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface DtoConverter<A, B> {
    Optional<B> convert(@Nullable A entity);

    List<B> convert(Iterable<A> entities);
}
