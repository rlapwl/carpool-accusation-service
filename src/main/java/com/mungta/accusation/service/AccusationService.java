package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.*;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusationContents;
import com.mungta.accusation.domain.AccusedMember;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.domain.PartyInfo;
import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationService {

    private final AccusationRepository accusationRepository;

    @Transactional
    public long addAccusation(final String memberId, final AccusationRequest request) {
        PartyInfoRequest partyInfo = request.getPartyInfo();
        AccusedMemberRequest accusedMember = request.getAccusedMember();

        long id = accusationRepository.save(
                Accusation.builder()
                        .memberId(memberId)
                        .partyInfo(
                                PartyInfo.builder()
                                        .partyId(partyInfo.getPartyId())
                                        .placeOfDeparture(partyInfo.getPlaceOfDeparture())
                                        .destination(partyInfo.getDestination())
                                        .startedDateTime(partyInfo.getStartedDateTime())
                                        .build()
                        )
                        .accusedMember(
                                AccusedMember.builder()
                                        .id(accusedMember.getId())
                                        .name(accusedMember.getName())
                                        .email(accusedMember.getEmail())
                                        .build()
                        )
                        .accusationContents(
                                new AccusationContents(request.getAccusationContents().getTitle(),
                                        request.getAccusationContents().getDesc())
                        )
                        .build()
        ).getId();
        log.info("Saved Accusation. id: {}", id);
        return id;
    }

    public AccusationResponse getAccusation(final long id, final String memberId) {
        Accusation accusation = getAccusationById(id);

        validateWriter(accusation, memberId);

        return AccusationResponse.of(accusation);
    }

    private Accusation getAccusationById(long id) {
        log.debug("Find Accusation. id: {}", id);
        return accusationRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiStatus.NOT_FOUND_ACCUSATION));
    }

    private void validateWriter(Accusation accusation, String memberId) {
        log.debug("Validate writer. id: {}, memberId: {}", accusation.getId(), memberId);
        if (accusation.isNotWriter(memberId)) {
            throw new ApiException(ApiStatus.INVALID_READ_ACCUSATION);
        }
    }

    public AccusationListResponse getAccusationList(final String memberId) {
        return AccusationListResponse.of(accusationRepository.findByMemberIdOrderByCreatedDateTimeDesc(memberId));
    }

    @Transactional
    public AccusationResponse modifyAccusationContents(final long id, final AccusationContentsRequest request) {
        Accusation accusation = getAccusationById(id);

        validateStatus(accusation);

        accusation.modifyAccusationContents(new AccusationContents(request.getTitle(), request.getDesc()));
        return AccusationResponse.of(accusation);
    }

    private void validateStatus(Accusation accusation) {
        log.debug("Validate Accusation Status. id: {}, status: {}", accusation.getId(), accusation.getAccusationStatus());
        if (accusation.isNotRegisteredStatus()) {
            throw new ApiException(ApiStatus.INVALID_MODIFY_ACCUSATION);
        }
    }

    @Transactional
    public void deleteAccusation(final long id) {
        accusationRepository.deleteById(id);
        log.info("Deleted Accusation. id: {}", id);
    }

}
