package com.backend.dto.request.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.StatusJob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobAdminDTOResponse {
    private Long id;
    private String title;
    private StatusJob status;
    private String clientEmail;
    private String categoryName;
    private Long quantity;
    private Long appliedQuantity;
    private Long cancelledQuantity;
    //    private Long inProgressQuantity;
    private Long approvedQuantity;
    private Long rejectedQuantity;
    private Long viewedQuantity;
}
