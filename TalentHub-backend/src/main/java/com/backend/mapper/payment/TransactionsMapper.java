package com.backend.mapper.payment;

import com.backend.dto.request.payment.TransactionsDTORequest;
import com.backend.dto.response.payment.TransactionsDTOResponse;
import com.backend.entity.child.payment.Transactions;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionsMapper extends BaseMapper<Transactions,TransactionsDTORequest, TransactionsDTOResponse> {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "activity", target = "activity")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    TransactionsDTOResponse toResponseDto(Transactions transactions);

}
