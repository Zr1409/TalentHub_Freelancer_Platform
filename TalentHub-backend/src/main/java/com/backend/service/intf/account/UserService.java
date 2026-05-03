package com.backend.service.intf.account;

import com.backend.dto.request.account.UserDTORequest;
import com.backend.dto.response.account.UserDTOResponse;
import com.backend.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<UserDTORequest, UserDTOResponse, Long> {
    UserDTOResponse update(UserDTORequest userDTORequest);
    List<UserDTOResponse> getAllActiveAdmins();
}
