package com.backend.mapper;

import org.mapstruct.Mapping;

import java.util.List;

public interface BaseMapper<E, ReqDTO, ResDTO> {
    // Chuyển đổi từ Entity sang Response DTO
    ResDTO toResponseDto(E entity);

    // Chuyển đổi từ Request DTO sang Entity
    E toEntity(ReqDTO dto);

    // Chuyển đổi danh sách Entity sang danh sách Response DTO
    List<ResDTO> toResponseDtoList(List<E> entityList);

    // Chuyển đổi danh sách Request DTO sang danh sách Entity
    List<E> toEntityList(List<ReqDTO> dtoList);
}
