
package com.backend.service.impl.payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.backend.config.vnpay.ConfigVnPay;
import com.backend.dto.PaymentResDTO;
import com.backend.dto.request.payment.PaymentDTORequest;
import com.backend.dto.request.payment.VNPayCallbackDTORequest;
import com.backend.dto.response.ResultPaymentResponseDTO;
import com.backend.dto.response.payment.BalanceResponseDTO;
import com.backend.dto.response.payment.PaymentDTOResponse;
import com.backend.dto.response.payment.PaymentSummaryDTO;
import com.backend.dto.response.payment.WithdrawResponseDTO;
import com.backend.entity.child.account.Account;
import com.backend.entity.child.account.User;
import com.backend.entity.child.payment.Payment;
import com.backend.entity.child.payment.Transactions;
import com.backend.enums.ActivityType;
import com.backend.enums.TransactionStatus;
import com.backend.repository.AccountRepository;
import com.backend.repository.PaymentRepository;
import com.backend.repository.TransactionRepository;
import com.backend.repository.UserRepository;
import com.backend.service.intf.payment.PaymentService;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final HttpServletRequest httpServletRequest;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

    public PaymentResDTO createVnPayPayment(BigDecimal amount) throws UnsupportedEncodingException {
        String vnp_TxnRef = ConfigVnPay.getRandomNumber(8);
        BigDecimal totalAmount = amount.multiply(BigDecimal.valueOf(100));


        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", ConfigVnPay.vnp_Version);
        vnp_Params.put("vnp_Command", ConfigVnPay.vnp_Command);
        vnp_Params.put("vnp_TmnCode", ConfigVnPay.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", ConfigVnPay.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", httpServletRequest.getRemoteAddr());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        String queryUrl = buildQueryString(vnp_Params);
        String vnp_SecureHash = ConfigVnPay.hmacSHA512(ConfigVnPay.secretKey, queryUrl);
        String paymentUrl = ConfigVnPay.vnp_PayUrl + "?" + queryUrl + "&vnp_SecureHash=" + vnp_SecureHash;

        return PaymentResDTO.builder()
                .status("success")
                .message("Payment URL generated successfully")
                .url(paymentUrl)
                .build();
    }

    private String buildQueryString(Map<String, String> vnp_Params) throws UnsupportedEncodingException {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (i < fieldNames.size() - 1) {
                    query.append("&");
                }
            }
        }
        return query.toString();
    }

 public ResultPaymentResponseDTO handleVnPayCallback(VNPayCallbackDTORequest request) {
     // Lấy thông tin user
     User user = userRepository.findById(request.getUserId())
             .orElseThrow(() -> new IllegalArgumentException("User not found"));

     // Lấy thông tin account từ user
     Account account = accountRepository.findById(user.getAccount().getId())
             .orElseThrow(() -> new IllegalArgumentException("Account not found"));

     Optional<Payment> payment = paymentRepository.findByAccountId(account.getId());

     BigDecimal amount = Optional.ofNullable(request.getVnp_Amount()).orElse(BigDecimal.ZERO);

     if ("00".equals(request.getVnp_ResponseCode())) {
         Payment savePayment = payment.orElseGet(() -> {
             Payment newPayment = new Payment();
             newPayment.setAccount(account);
             newPayment.setBalance(BigDecimal.ZERO);
             return newPayment;
         });

         BigDecimal currentBalance = Optional.ofNullable(savePayment.getBalance()).orElse(BigDecimal.ZERO);
         savePayment.setBalance(currentBalance.add(amount));

         Transactions transaction = new Transactions();
         transaction.setActivity(ActivityType.DEPOSIT);
         transaction.setDescription("Nạp tiền thành công");
         transaction.setMoney(amount);
         transaction.setStatus(TransactionStatus.SUCCESS);
         transaction.setPayment(savePayment);

         paymentRepository.save(savePayment);
         transactionRepository.save(transaction);

         return ResultPaymentResponseDTO.builder()
                 .codeVnp(request.getVnp_ResponseCode())
                 .fristName(user.getFirstName())
                 .lastName(user.getLastName())
                 .amount(request.getVnp_Amount())
                 .activity(ActivityType.DEPOSIT)
                 .description("Nạp tiền thành công")
                 .transactionStatus(TransactionStatus.SUCCESS)
                 .build();
     } else {
         Transactions transaction = new Transactions();
         transaction.setDescription("Nộp tiền thất bại");
         transaction.setActivity(ActivityType.DEPOSIT);
         transaction.setMoney(BigDecimal.ZERO);
         transaction.setStatus(TransactionStatus.FAILED);
         transaction.setPayment(payment.orElse(null));
         transactionRepository.save(transaction);

         return ResultPaymentResponseDTO.builder()
                 .codeVnp(request.getVnp_ResponseCode())
                 .fristName(null)
                 .lastName(null)
                 .amount(BigDecimal.ZERO)
                 .activity(null)
                 .description("Nạp tiền thất bại")
                 .transactionStatus(TransactionStatus.FAILED)
                 .build();
     }
 }


    @Override
    public WithdrawResponseDTO handleVnPayWithCallback(BigDecimal vnpAmount, Long userId, String desc) {

        // Lấy thông tin user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Lấy thông tin account từ user
        Account account = accountRepository.findById(user.getAccount().getId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BigDecimal balance = paymentRepository.findByAccountId(account.getId())
                .map(Payment::getBalance)
                .orElse(null);

        // Kiểm tra số dư trước khi rút tiền
        if (balance == null || balance.compareTo(vnpAmount) < 0) {
            return WithdrawResponseDTO.builder()
                    .amount(vnpAmount)
                    .remainingBalance(balance == null ? BigDecimal.ZERO : balance)
                    .activityType(ActivityType.WITHDRAW)
                    .message(balance == null ? "❌ Bạn chưa nạp tiền lần đầu. Vui lòng nạp tiền trước khi thực hiện giao dịch!" : "❌ Số dư không đủ để đăng ký gói mới!")
                    .transactionStatus(TransactionStatus.FAILED)
                    .build();
        }

        // Tạo bản ghi mới cho giao dịch rút tiền
        Optional<Payment> paymentOptional = paymentRepository.findByAccountId(account.getId());
        Payment payment;

        if (paymentOptional.isPresent()) {
            payment = paymentOptional.get();
            payment.setAccount(account);
            payment.setBalance(payment.getBalance().subtract(vnpAmount));
        } else {
            payment = new Payment();
            payment.setAccount(account);
            payment.setBalance(vnpAmount.negate());
        }

        Transactions transaction = new Transactions();
        transaction.setActivity(ActivityType.WITHDRAW);
        transaction.setMoney(vnpAmount);
        transaction.setDescription(desc);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setPayment(payment);

        paymentRepository.save(payment);
        transactionRepository.save(transaction);

        // Trả về DTO chứa thông tin giao dịch
        return WithdrawResponseDTO.builder()
                .amount(vnpAmount)
                .remainingBalance(payment.getBalance())
                .activityType(ActivityType.WITHDRAW)
                .message(desc)
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();
    }

    public BalanceResponseDTO getLatestBalanceInfo(Long userId) {
        // Lấy thông tin user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ User không tồn tại!"));

        // Lấy thông tin account từ user
        Account account = accountRepository.findById(user.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("❌ Account không tồn tại!"));

        // Fixed: Return 0 balance instead of throwing an exception when payment is not found
        BigDecimal balance = paymentRepository.findByAccountId(account.getId())
                .map(Payment::getBalance)
                .orElse(BigDecimal.ZERO); // Return 0 instead of throwing exception

        // Lấy dữ liệu giao dịch mới nhất
//        List<PaymentSummaryDTO> payments = paymentRepository.getLatestPaymentInfo(account.getId());

        List<PaymentSummaryDTO> payments = paymentRepository.findLatestDeposit(account.getId());
        List<PaymentSummaryDTO> payments2 = paymentRepository.findLatestWithdraw(account.getId());

        // Biến để lưu giá trị cần thiết
        BigDecimal latestDeposit = BigDecimal.ZERO;
        LocalDateTime latestDepositDate = null;
        BigDecimal todaySpending = BigDecimal.ZERO;
        LocalDateTime latestSpendingDate = null;
        LocalDateTime oldestTransactionDate = null;

        BigDecimal totalDepositToday = BigDecimal.ZERO;
        BigDecimal totalWithdrawToday = BigDecimal.ZERO;

        // Duyệt danh sách giao dịch
//        for (PaymentSummaryDTO payment : payments) {
//            if (payment.getActivity() == ActivityType.DEPOSIT) {
//                latestDeposit = totalDepositToday.add(payment.getTotalAmount());
//                latestDepositDate = payment.getLatestTransactionDate();
//            } else if (payment.getActivity() == ActivityType.WITHDRAW) {

//            }
//            oldestTransactionDate = payment.getOldestTransactionDate();
//        }

        for (PaymentSummaryDTO payment : payments) {
            latestDeposit = totalDepositToday.add(payment.getTotalAmount());
            latestDepositDate = payment.getLatestTransactionDate();
        }
        for (PaymentSummaryDTO payment2 : payments2) {
            todaySpending = totalWithdrawToday.add(payment2.getTotalAmount());
            latestSpendingDate = payment2.getLatestTransactionDate();
        }

        oldestTransactionDate = paymentRepository.getCreatedAtById(account.getId());

        // Trả về DTO chứa thông tin tài khoản
        return BalanceResponseDTO.builder()
                .balance(balance)
                .latestDeposit(latestDeposit)
                .latestDepositDate(latestDepositDate)
                .todaySpending(todaySpending)
                .latestSpendingDate(latestSpendingDate)
                .oldestTransactionDate(oldestTransactionDate)
                .build();
    }


    @Override
    public PaymentDTOResponse create(PaymentDTORequest paymentDTORequest) {
        return null;
    }

    @Override
    public Optional<PaymentDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<PaymentDTOResponse> getAll() {
        return List.of();
    }

    @Override
    public Boolean deleteById(Long aLong) {
        return null;
    }


//    private final PaymentRepository paymentRepository;
//    private final EWalletAccountRepository eWalletAccountRepository;
//    private final BankAccountRepository bankAccountRepository;
//
//    private final PaymentMapper paymentMapper;
//
//    @Override
//    public PaymentDTOResponse create(PaymentDTORequest paymentDTORequest) {
//        Long bankAccountId = paymentDTORequest.getBankAccountId();
//        Long eWalletAccountId = paymentDTORequest.getEWalletAccountId();
//
//        if (bankAccountId != null) {
//            Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankAccountId);
//            if (!optionalBankAccount.isPresent()) {
//                throw new IllegalArgumentException("Bank account does not exist or is invalid.");
//            }
//            BankAccount bankAccount = optionalBankAccount.get();
//            if (bankAccount.getPayment() != null) {
//                throw new IllegalArgumentException("Bank account is already linked to another payment.");
//            }
//        }
//
//        if (eWalletAccountId != null) {
//            Optional<EWalletAccount> optionalEWalletAccount = eWalletAccountRepository.findById(eWalletAccountId);
//            if (!optionalEWalletAccount.isPresent()) {
//                throw new IllegalArgumentException("E-wallet account does not exist or is invalid.");
//            }
//            EWalletAccount eWalletAccount = optionalEWalletAccount.get();
//            if (eWalletAccount.getPayment() != null) {
//                throw new IllegalArgumentException("E-wallet account is already linked to another payment.");
//            }
//        }
//
//        Payment payment = paymentMapper.toEntity(paymentDTORequest);
//
//        payment = paymentRepository.save(payment);
//
//        return paymentMapper.toDTOResponse(payment);
//    }
//
//
//    @Override
//    public Optional<PaymentDTOResponse> getById(Long id) {
//        return paymentRepository.findById(id)
//                .map(paymentMapper::toDTOResponse);
//    }
//
//    @Override
//    public List<PaymentDTOResponse> getAll() {
//        return paymentRepository.findAll().stream()
//                .map(paymentMapper::toDTOResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Boolean deleteById(Long id) {
//        if (paymentRepository.existsById(id)) {
//            paymentRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
}
