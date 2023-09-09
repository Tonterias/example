package com.example.web.service.mapper;

import com.example.web.domain.Appuser;
import com.example.web.domain.User;
import com.example.web.service.dto.AppuserDTO;
import com.example.web.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appuser} and its DTO {@link AppuserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppuserMapper extends EntityMapper<AppuserDTO, Appuser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    AppuserDTO toDto(Appuser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
