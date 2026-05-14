package com.backend.mapper.Account;


import com.backend.dto.response.account.AdminAccountDTOResponse;
import com.backend.entity.child.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminAccountMapper {
    @Mapping(source = "mfaEnabled", target = "mfaEnabled")
    AdminAccountDTOResponse toResponseDTO(Account account);

}
