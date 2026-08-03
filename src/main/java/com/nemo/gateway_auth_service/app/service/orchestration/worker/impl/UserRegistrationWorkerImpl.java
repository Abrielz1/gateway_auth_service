package com.nemo.gateway_auth_service.app.service.orchestration.worker.impl;

import com.nemo.gateway_auth_service.app.domain.EmailData;
import com.nemo.gateway_auth_service.app.domain.LoginData;
import com.nemo.gateway_auth_service.app.domain.PhoneData;
import com.nemo.gateway_auth_service.app.repository.UserRepository;
import com.nemo.gateway_auth_service.app.service.orchestration.worker.UserRegistrationWorker;
import com.nemo.gateway_auth_service.util.mapper.client.to.ClientTo;
import com.nemo.gateway_auth_service.web.model.request.UserRegistrationRequestDTO;
import com.nemo.gateway_auth_service.web.model.response.UserRegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class UserRegistrationWorkerImpl implements UserRegistrationWorker {

    private final UserRepository clientRepository;

    private final ClientTo clientTo;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDTO clientRegisterRequestDTO) {

        log.info("Worker started registration for user: {}", clientRegisterRequestDTO.username());

            var newClientEntity = this.clientTo.toEntity(clientRegisterRequestDTO);
            var clientEntity = this.clientRepository.save(newClientEntity);
            return new UserRegistrationResponseDto(clientEntity.getUserUUID().toString(), new ArrayList<>(List.of(
                    clientEntity.getUserLogins().stream().map(LoginData::getLogin).collect(Collectors.joining(", ")),
                    clientEntity.getUserEmails().stream().map(EmailData::getEmail).collect(Collectors.joining(", ")),
                    clientEntity.getUserPhones().stream().map(PhoneData::getPhone).collect(Collectors.joining(", "))
                    )));
    }
}
