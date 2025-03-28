package com.bix.imageprocessor.web.mapper;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.ViewUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface CreateUserMapper extends Converter<User, CreateUserDto> {

    User convert(CreateUserDto createUserDto);
}