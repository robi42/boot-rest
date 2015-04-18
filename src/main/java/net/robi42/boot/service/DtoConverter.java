package net.robi42.boot.service;

import javax.annotation.Nullable;
import java.util.List;

public interface DtoConverter<A, B> {
    B convert(@Nullable A entity);

    List<B> convert(Iterable<A> entities);
}
