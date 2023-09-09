package com.example.web.service.mapper;

import com.example.web.domain.Appuser;
import com.example.web.domain.Notification;
import com.example.web.service.dto.AppuserDTO;
import com.example.web.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "appuser", source = "appuser", qualifiedByName = "appuserPlateNumber")
    NotificationDTO toDto(Notification s);

    @Named("appuserPlateNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "plateNumber", source = "plateNumber")
    AppuserDTO toDtoAppuserPlateNumber(Appuser appuser);
}
