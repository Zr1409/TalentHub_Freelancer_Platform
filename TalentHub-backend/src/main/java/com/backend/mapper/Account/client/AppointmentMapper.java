package com.backend.mapper.Account.client;

import com.backend.dto.request.account.client.AppointmentDetailDTORequest;
import com.backend.dto.response.account.client.AppointmentDetailDTOResponse;
import com.backend.entity.child.account.client.Appointment;
import com.backend.mapper.BaseMapper;
import com.backend.service.intf.account.client.ClientService;
import com.backend.service.intf.job.FreelancerJobService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClientService.class, FreelancerJobService.class})
public interface AppointmentMapper extends BaseMapper<Appointment, AppointmentDetailDTORequest, AppointmentDetailDTOResponse> {

    @Mapping(source = "appointment.id", target = "id")
    @Mapping(source = "appointment.startTime", target = "startTime")
    @Mapping(source = "appointment.duration", target = "duration")
    @Mapping(source = "appointment.type", target = "type")
    @Mapping(source = "appointment.topic", target = "topic")
    @Mapping(source = "appointment.description", target = "description")
    @Mapping(source = "appointment.link", target = "link")
    AppointmentDetailDTOResponse toResponseDto(Appointment appointment);

    @Mapping(source = "dto.startTime", target = "startTime")
    @Mapping(source = "dto.duration", target = "duration")
    @Mapping(source = "dto.type", target = "type")
    @Mapping(source = "dto.topic", target = "topic")
    @Mapping(source = "dto.description", target = "description")
    @Mapping(source = "dto.link", target = "link")
    Appointment toEntity(AppointmentDetailDTORequest dto);

}
