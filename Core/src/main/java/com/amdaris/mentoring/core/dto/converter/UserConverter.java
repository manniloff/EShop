package com.amdaris.mentoring.core.dto.converter;

import com.amdaris.mentoring.core.dto.UserDto;
import com.amdaris.mentoring.core.model.Address;
import com.amdaris.mentoring.core.model.Role;
import com.amdaris.mentoring.core.model.User;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserConverter {
    public static Function<User, UserDto> toUserDto =
            user -> UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .role(user.getRole().getRoleType())
                    .birthday(user.getBirthday())
                    .phoneNumber(user.getPhoneNumber())
                    .lastName(user.getLastName())
                    .firstName(user.getFirstName())
                    .password(user.getPassword())
                    .addressIds(user.getAddresses().stream().map(Address::getId).collect(Collectors.toSet()))
                    .build();

    public static Function<UserDto, User> toUser =
            userDto -> User.builder()
                    .id(Optional.ofNullable(userDto.getId()).orElse(0L))
                    .email(userDto.getEmail())
                    .role(Role.builder().roleType(userDto.getRole()).build())
                    .birthday(userDto.getBirthday())
                    .phoneNumber(userDto.getPhoneNumber())
                    .lastName(userDto.getLastName())
                    .firstName(userDto.getFirstName())
                    .password(userDto.getPassword())
                    .addresses(Optional.ofNullable(userDto.getAddressIds())
                            .orElse(Set.of()).stream().map(Address::new).collect(Collectors.toSet()))
                    .build();
}
