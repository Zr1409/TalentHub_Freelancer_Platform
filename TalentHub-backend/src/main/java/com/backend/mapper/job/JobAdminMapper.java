package com.backend.mapper.job;

import com.backend.dto.request.job.JobAdminDTOResponse;
import com.backend.entity.child.job.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobAdminMapper {
    @Mapping(source = "job.id", target = "id")
    @Mapping(source = "job.client.user.account.email", target = "clientEmail")
    @Mapping(target = "appliedQuantity", expression = "java(appliedQuantity)")
    @Mapping(target = "cancelledQuantity", expression = "java(cancelledQuantity)")
//    @Mapping(target = "inProgressQuantity", expression = "java(inProgressQuantity)")
    @Mapping(target = "approvedQuantity", expression = "java(approvedQuantity)")
    @Mapping(target = "rejectedQuantity", expression = "java(rejectedQuantity)")
    @Mapping(target = "viewedQuantity", expression = "java(viewedQuantity)")
    @Mapping(source="job.category.categoryTitle", target="categoryName")
    JobAdminDTOResponse toResponseDto(Job job, Long appliedQuantity, Long cancelledQuantity,
                                      Long approvedQuantity, Long rejectedQuantity, Long viewedQuantity);
}
