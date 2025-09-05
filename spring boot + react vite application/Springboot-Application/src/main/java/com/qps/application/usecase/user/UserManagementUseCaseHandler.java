package com.qps.application.usecase.user;

import com.qps.application.dto.request.CreateAccountRequest;
import com.qps.application.dto.response.AccountResponse;
import com.qps.domain.user.UserException;
import com.qps.domain.user.model.Account;
import com.qps.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserManagementUseCaseHandler {
    UserService srv;

    public Page<AccountResponse> getListOfUsers(int page, Integer size) {
        return srv.getUsersListByPageIndex(
                PageRequest.of(
                        page,
                        getNumberOfRecordsPerPage(size)
                )
        );
    }

    public Page<AccountResponse> filterUsersByRoleOrStatusOrKeyword(String role, String status, String kw, int page, Integer size) {
        if (status != null) {
            switch (status.toLowerCase()) {
                case "active" -> status = "true";
                case "inactive" -> status = "false";
                default -> status = null;
            }
        }

        Pageable pageable = PageRequest.of(page, getNumberOfRecordsPerPage(size));

        // filter by role
        Page<Account> prevResult = srv.filterUsersByRole(
                role,
                pageable
        );

        // filter by status
        Page<Account> result = srv.filterUsersByStatus(
                prevResult,
                status,
                pageable,
                (prevResult.getContent().isEmpty() && role == null)
        );

        return srv.getUsersByKeyword(
                result,
                kw,
                pageable,
                (role == null && status == null)
        );
    }

    public void createNewUser(CreateAccountRequest request) {
        if (srv.isEmailExist(request.email())) {
            throw new UserException(UserException.ErrorCodes.EMAIL_EXISTED, "Email has been existed");
        }
        if (srv.isPhoneNumberExist(request.phoneNumber())) {
            throw new UserException(UserException.ErrorCodes.PHONE_NUMBER_EXISTED, "Phone number has been existed");
        }
        srv.createAndSaveNewUser(request.name(), request.email(), request.role(), request.phoneNumber());
    }

    private int getNumberOfRecordsPerPage(Integer size) {
        final int NUMBER_RECORDS_PER_PAGE = 10;
        return (size == null) ? NUMBER_RECORDS_PER_PAGE : size;
    }
}
