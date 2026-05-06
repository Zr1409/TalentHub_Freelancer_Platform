package com.backend.dto.response.account.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.TypeAppointment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailDTOResponse {
    private long id;
    private String name;
    private String mail;
    private String image;
    private String phone;
    private LocalDateTime startTime;
    private Long duration;
    private TypeAppointment type;
    private String topic;
    private String description;
    private String link;
    private Long jobId;
    private String jobTitle;
    private Boolean isCompleted = false;
}