package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.admin.AccusationStatusRequest;
import com.mungta.accusation.api.dto.admin.AdminAccusationListResponse;
import com.mungta.accusation.api.dto.admin.AdminAccusationResponse;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationStatus;
import com.mungta.accusation.domain.AccusedMember;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import com.mungta.accusation.messagequeue.KafkaProducer;
import com.mungta.accusation.messagequeue.event.AccusationCompleted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminAccusationService {

    private static final String BINDING_NAME = "producer-out-0";

    private final AccusationRepository accusationRepository;
    private final KafkaProducer kafkaProducer;
    private final PenaltyMailService penaltyMailService;

    public AdminAccusationResponse getAccusation(final long id) {
        Accusation accusation = getAccusationById(id);
        return AdminAccusationResponse.of(accusation);
    }

    private Accusation getAccusationById(long id) {
        log.debug("Find Accusation. id: {}", id);
        return accusationRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiStatus.NOT_FOUND_ACCUSATION));
    }

    public AdminAccusationListResponse getAccusationList() {
        return AdminAccusationListResponse.of(accusationRepository.findAllByOrderByCreatedDateTimeDesc());
    }

    @Transactional
    public AdminAccusationResponse processAccusation(final long id, final AccusationStatusRequest request) {
        Accusation accusation = getAccusationById(id);
        accusation.process(request.getAccusationStatus(), request.getResultComment());

        if (accusation.getAccusationStatus() == AccusationStatus.COMPLETED) {
            AccusedMember accusedMember = accusation.getAccusedMember();

            // 회원 시스템으로 신고당한 사람 ID 전송
            kafkaProducer.send(BINDING_NAME,
                    new AccusationCompleted(accusedMember.getId(), accusation.getId()));
        }
        log.info("Changed to '{}' status. id: {}", request.getAccusationStatus(), id);
        return AdminAccusationResponse.of(accusation);
    }

    public void sendPenaltyEmail(final long id) {
        Accusation accusation = getAccusationById(id);
        penaltyMailService.send(accusation.getAccusedMember()); // 신고당한 사람에게 이메일 전송..
    }

    @Transactional
    public void resetComment(final long id) {
        log.info("Reset Comment and Status.");
        Accusation accusation = getAccusationById(id);
        accusation.resetComment();
    }

}
