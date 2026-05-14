package com.backend.mapper.Account;

import java.time.LocalDateTime;  // Thay đổi import
import com.backend.dto.request.account.AccountDTORequest;
import com.backend.dto.response.account.AccountDTOResponse;
import com.backend.entity.child.account.Account;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends BaseMapper<Account, AccountDTORequest, AccountDTOResponse> {

 default String map(LocalDateTime dateTime) {  // Thay đổi kiểu dữ liệu
  if (dateTime == null) {
   return null;
  }
  return dateTime.toString();
 }

 default LocalDateTime map(String dateStr) {  // Thay đổi kiểu dữ liệu trả về
  if (dateStr == null) {
   return null;
  }
  return LocalDateTime.parse(dateStr);  // Sử dụng LocalDateTime.parse thay vì DateTime.parse
 }
}