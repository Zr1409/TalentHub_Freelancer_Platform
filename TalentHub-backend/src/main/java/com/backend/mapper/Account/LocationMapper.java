package com.backend.mapper.Account;

import com.backend.dto.response.account.LocationDTOResponse;
import com.backend.entity.child.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "address", expression = "java(getAddress(account))")
    @Mapping(source = "lat", target = "lat")
    @Mapping(source = "lng", target = "lng")
    @Mapping(source = "role", target = "role")
    LocationDTOResponse toResponseDTO(Account account);

    @Named("getAddress")
    default String getAddress(Account account) {
        if (account.getUser() == null) {
            return "";
        }

        String province = account.getUser().getProvince();
        String ward = account.getUser().getWard();

        if (ward != null && !ward.isEmpty()) {
            if (province != null && !province.isEmpty()) {
                return ward + ", " + province;
            }
            return ward;
        } else if (province != null && !province.isEmpty()) {
            return province;
        }

        return "";
    }
}