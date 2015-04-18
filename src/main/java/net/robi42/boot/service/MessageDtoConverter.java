package net.robi42.boot.service;

import com.google.common.collect.ImmutableList;
import lombok.val;
import net.robi42.boot.domain.MessageDto;
import net.robi42.boot.domain.MessageEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class MessageDtoConverter implements DtoConverter<MessageEntity, MessageDto> {

    public @Override MessageDto convert(MessageEntity entity) {
        val dto = new MessageDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public @Override List<MessageDto> convert(Iterable<MessageEntity> entities) {
        val dtoBuilder = new ImmutableList.Builder<MessageDto>();
        entities.forEach(entity -> dtoBuilder.add(convert(entity)));
        return dtoBuilder.build();
    }
}
