package com.backend.mapper.Account.client;

import com.backend.dto.request.account.client.UpdatePriceAndTypeDTORequest;
import com.backend.dto.response.account.client.UpdatePriceAndTypeDTOResponse;
import com.backend.entity.child.account.client.Client;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdatePriceAndTypeMapper extends BaseMapper<Client, UpdatePriceAndTypeDTORequest, UpdatePriceAndTypeDTOResponse> {

    @Mapping(source = "id", target = "clientId")
    UpdatePriceAndTypeDTOResponse toResponseDto(Client entity);

    @Mapping(source = "clientId", target = "id")
    Client toEntity(UpdatePriceAndTypeDTOResponse dto);
}
