package net.robi42.boot.service;

import com.google.common.collect.ImmutableList;
import lombok.val;
import net.robi42.boot.domain.MessageDto;
import net.robi42.boot.domain.MessageEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

public class MessageDtoConverter implements DtoConverter<MessageEntity, MessageDto> {

    public @Override Optional<MessageDto> convert(MessageEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }
        val dto = new MessageDto();
        BeanUtils.copyProperties(entity, dto);
        return Optional.of(dto);
    }

    public @Override List<MessageDto> convert(Iterable<MessageEntity> entities) {
        val dtoBuilder = new ImmutableList.Builder<MessageDto>();
        entities.forEach(entity -> convert(entity).ifPresent(dtoBuilder::add));
        return dtoBuilder.build();
    }
}
