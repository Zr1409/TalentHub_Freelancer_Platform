package com.backend.service.intf.account.client;

import com.backend.dto.request.account.client.AppointmentDetailDTORequest;
import com.backend.dto.response.account.client.AppointmentDetailDTOResponse;
import com.backend.service.BaseService;

import java.util.List;

public interface AppointmentService extends BaseService<AppointmentDetailDTORequest, AppointmentDetailDTOResponse, Long> {
    List<AppointmentDetailDTOResponse> getAllAppointmentsByClientId(Long clientId);

    List<AppointmentDetailDTOResponse> getAllAppointmentsByFreelancerId(Long freelancerId);

    AppointmentDetailDTOResponse update(Long id, AppointmentDetailDTORequest request);

    String markAppointmentAsCompleted(Long id);
}
