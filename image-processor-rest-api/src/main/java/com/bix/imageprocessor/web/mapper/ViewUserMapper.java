package com.bix.imageprocessor.web.mapper;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.user.ViewUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface ViewUserMapper extends Converter<User, ViewUserDto> {

    @Mapping(source = "subscription.type", target = "subscriptionType")
    ViewUserDto convert(User user);
}