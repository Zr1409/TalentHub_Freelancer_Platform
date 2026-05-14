package com.backend.controller.account;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.client.AppointmentDetailDTORequest;
import com.backend.dto.response.account.client.AppointmentDetailDTOResponse;
import com.backend.service.intf.account.client.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/client")
    public ResponseEntity<ResponseObject<AppointmentDetailDTOResponse>> createAppointment(@RequestBody AppointmentDetailDTORequest request) {
        AppointmentDetailDTOResponse response = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<AppointmentDetailDTOResponse>builder()
                .message("Successfully create appointment")
                .status(201)
                .data(response)
                .build());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ResponseObject<List<AppointmentDetailDTOResponse>>> getAllAppointmentsByClientId(@PathVariable("id") Long id) {
        List<AppointmentDetailDTOResponse> response = appointmentService.getAllAppointmentsByClientId(id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<AppointmentDetailDTOResponse>>builder()
                .message("Successfully get all appointment by client id")
                .status(200)
                .data(response)
                .build());
    }

    @GetMapping("/freelancers/{id}")
    public ResponseEntity<ResponseObject<List<AppointmentDetailDTOResponse>>> getAllAppointmentsByFreelancerId(@PathVariable("id") Long id) {
        List<AppointmentDetailDTOResponse> response = appointmentService.getAllAppointmentsByFreelancerId(id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<AppointmentDetailDTOResponse>>builder()
                .message("Successfully get all appointment by freelancer id")
                .status(200)
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<AppointmentDetailDTOResponse>> updateAppointment(
            @PathVariable("id") Long id,
            @RequestBody AppointmentDetailDTORequest request) {
        AppointmentDetailDTOResponse response = appointmentService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<AppointmentDetailDTOResponse>builder()
                .message("Successfully updated appointment")
                .status(200)
                .data(response)
                .build());
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<String> markAppointmentAsCompleted(@PathVariable Long id) {
        String result = appointmentService.markAppointmentAsCompleted(id);
        return ResponseEntity.ok(result);
    }

}
