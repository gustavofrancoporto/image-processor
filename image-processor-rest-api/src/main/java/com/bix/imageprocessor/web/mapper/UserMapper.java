package com.bix.imageprocessor.web.mapper;

import com.bix.imageprocessor.domain.subscription.model.SubscriptionType;
import com.bix.imageprocessor.domain.user.model.Role;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.UserDto;
import org.apache.commons.collections4.SetUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.PREMIUM;
import static com.bix.imageprocessor.domain.user.model.Role.ROLE_ADMIN;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<User, UserDto> {

    User convert(CreateUserDto createUserDto);

    @Mapping(source = "subscription.type", target = "isPremium", qualifiedByName = "isPremium")
    @Mapping(source = "roles", target = "isAdmin", qualifiedByName = "isAdmin")
    UserDto convert(User user);

    @Named("isPremium")
    static boolean isPremium(SubscriptionType subscriptionType) {
        return PREMIUM == subscriptionType;
    }

    @Named("isAdmin")
    static boolean isAdmin(Set<Role> roles) {
        return SetUtils.emptyIfNull(roles).stream()
                .map(Role::getName)
                .anyMatch(r -> r.equalsIgnoreCase(ROLE_ADMIN));
    }
}