package com.backend.service.impl.payment;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.payment.TransactionsDTORequest;
import com.backend.dto.response.payment.TransactionsDTOResponse;
import com.backend.entity.child.account.User;
import com.backend.mapper.payment.TransactionsMapper;
import com.backend.repository.TransactionRepository;
import com.backend.repository.UserRepository;
import com.backend.service.intf.payment.TransactionsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionsService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;



    private final TransactionsMapper transactionsMapper;

    public List<TransactionsDTOResponse> getTransactionsByAccountId(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.getAllTransactionsByAccountId(user.getAccount().getId()).stream()
                .map(transactionsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionsDTOResponse create(TransactionsDTORequest transactionsDTORequest) {
        return null;
    }

    @Override
    public Optional<TransactionsDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

@Override
public List<TransactionsDTOResponse> getAll() {
    return transactionRepository.findAll().stream()
            .map(transactionsMapper::toResponseDto)
            .collect(Collectors.toList());
}

    @Override
    public Boolean deleteById(Long aLong) {
        return null;
    }
}
