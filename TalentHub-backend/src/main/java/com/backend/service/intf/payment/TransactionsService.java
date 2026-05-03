package com.backend.service.intf.payment;

import com.backend.dto.request.payment.TransactionsDTORequest;
import com.backend.dto.response.payment.TransactionsDTOResponse;
import com.backend.service.BaseService;

import java.util.List;


public interface TransactionsService extends BaseService<TransactionsDTORequest, TransactionsDTOResponse, Long> {
    List<TransactionsDTOResponse> getTransactionsByAccountId(Long accountId);
}
