package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.AccusationContentsResponse;
import com.mungta.accusation.api.dto.AccusedMemberResponse;
import com.mungta.accusation.api.dto.PartyInfoResponse;
import com.mungta.accusation.api.dto.admin.AccusationStatusRequest;
import com.mungta.accusation.api.dto.admin.AdminAccusationListResponse;
import com.mungta.accusation.api.dto.admin.AdminAccusationResponse;
import com.mungta.accusation.domain.*;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import com.mungta.accusation.messagequeue.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.mungta.accusation.api.dto.admin.AdminAccusationListResponse.AdminAccusationInfoResponse;
import static com.mungta.accusation.constants.AccusationTestSample.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class AdminAccusationServiceTest {

    @InjectMocks
    @Spy
    private AdminAccusationService adminAccusationService;

    @Mock
    private AccusationRepository accusationRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private PenaltyMailService penaltyMailService;

    private Accusation accusation;

    private final LocalDateTime nowDateTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        accusation = Accusation.builder()
                .memberId(MEMBER_ID)
                .accusedMember(
                        AccusedMember.builder()
                                .id(ACCUSED_MEMBER_ID)
                                .name(ACCUSED_MEMBER_NAME)
                                .email(ACCUSED_MEMBER_EMAIL)
                                .build()
                )
                .partyInfo(
                        PartyInfo.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()
                )
                .accusationContents(
                        new AccusationContents(CONTENTS_TITLE, CONTENTS_DESC)
                )
                .build();
        accusation.setId(ACCUSATION_ID);
        accusation.setModifiedDateTime(nowDateTime);
    }

    @DisplayName("[?????????] ?????? ?????? ??????.")
    @Test
    void getAccusation() {
        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        AdminAccusationResponse response = adminAccusationService.getAccusation(ACCUSATION_ID);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(ACCUSATION_ID),
                () -> assertThat(response.getAccusedMember()).isEqualTo(
                        AccusedMemberResponse.builder()
                                .id(ACCUSED_MEMBER_ID)
                                .name(ACCUSED_MEMBER_NAME)
                                .build()),
                () -> assertThat(response.getPartyInfo()).isEqualTo(
                        PartyInfoResponse.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()),
                () -> assertThat(response.getAccusationContents()).isEqualTo(
                        AccusationContentsResponse.builder()
                                .title(CONTENTS_TITLE)
                                .desc(CONTENTS_DESC)
                                .build()),
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED),
                () -> assertThat(response.getResultComment()).isEmpty()
        );
    }

    @DisplayName("[?????????] ??????????????? ?????? id??? accusation ????????? ?????? ??? ?????? ?????? Exception ??????.")
    @Test
    void getAccusation_not_found_by_id() {
        given(accusationRepository.findById(anyLong()))
                .willThrow(new ApiException(ApiStatus.NOT_FOUND_ACCUSATION));

        assertThatThrownBy(() -> adminAccusationService.getAccusation(2L))
                .isInstanceOf(ApiException.class)
                .hasMessage("?????? ???????????? ?????? ??? ????????????.");
    }

    @DisplayName("[?????????] ?????? ?????? ????????? ?????? ??????.")
    @Test
    void getAccusationList() {
        given(accusationRepository.findAllByOrderByCreatedDateTimeDesc()).willReturn(List.of(accusation));

        AdminAccusationListResponse response = adminAccusationService.getAccusationList();
        List<AdminAccusationInfoResponse> responseList = response.getAccusations();

        assertAll(
                () -> assertThat(responseList.size()).isEqualTo(1),
                () -> assertThat(responseList.get(0)).isEqualTo(
                        AdminAccusationInfoResponse.builder()
                                .id(ACCUSATION_ID)
                                .partyId(PARTY_ID)
                                .memberId(MEMBER_ID)
                                .title(CONTENTS_TITLE)
                                .accusationStatus(AccusationStatus.REGISTERED)
                                .modifiedDateTime(nowDateTime.format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                ))
                                .build()
                )
        );
    }

    @DisplayName("[?????????] ?????? ?????? ?????? ??????.")
    @Test
    void processAccusation_to_completed_status() {
        AccusationStatusRequest request = new AccusationStatusRequest(AccusationStatus.COMPLETED, "????????? ??????");

        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        AdminAccusationResponse response = adminAccusationService.processAccusation(ACCUSATION_ID, request);

        verify(kafkaProducer, times(1)).send(anyString(), any());
        //verify(penaltyMailService, times(1)).send(any());
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(ACCUSATION_ID),
                () -> assertThat(response.getAccusedMember()).isEqualTo(
                        AccusedMemberResponse.builder()
                                .id(ACCUSED_MEMBER_ID)
                                .name(ACCUSED_MEMBER_NAME)
                                .build()),
                () -> assertThat(response.getPartyInfo()).isEqualTo(
                        PartyInfoResponse.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()),
                () -> assertThat(response.getAccusationContents()).isEqualTo(
                        AccusationContentsResponse.builder()
                                .title(CONTENTS_TITLE)
                                .desc(CONTENTS_DESC)
                                .build()),
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.COMPLETED),
                () -> assertThat(response.getResultComment()).isEqualTo(request.getResultComment())
        );
    }

    @DisplayName("[?????????] ?????? ?????? ?????? ??????.")
    @Test
    void processAccusation_to_rejected_status() {
        AccusationStatusRequest request = new AccusationStatusRequest(AccusationStatus.REJECTED, "?????? ????????? ??????.");

        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        AdminAccusationResponse response = adminAccusationService.processAccusation(ACCUSATION_ID, request);

        verify(kafkaProducer, never()).send(anyString(), any());
        verify(penaltyMailService, never()).send(any());
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(ACCUSATION_ID),
                () -> assertThat(response.getAccusedMember()).isEqualTo(
                        AccusedMemberResponse.builder()
                                .id(ACCUSED_MEMBER_ID)
                                .name(ACCUSED_MEMBER_NAME)
                                .build()),
                () -> assertThat(response.getPartyInfo()).isEqualTo(
                        PartyInfoResponse.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()),
                () -> assertThat(response.getAccusationContents()).isEqualTo(
                        AccusationContentsResponse.builder()
                                .title(CONTENTS_TITLE)
                                .desc(CONTENTS_DESC)
                                .build()),
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REJECTED),
                () -> assertThat(response.getResultComment()).isEqualTo(request.getResultComment())
        );
    }

    @DisplayName("[?????????] ????????? ??????. PenaltySucceed Event ????????? ??????.")
    @Test
    void sendPenaltyEmail() {
        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        adminAccusationService.sendPenaltyEmail(ACCUSATION_ID);

        verify(penaltyMailService, times(1)).send(any());
    }

    @DisplayName("[?????????] ???????????? ?????? ????????? ????????? ??????(REGISTERED)??? ??????. PenaltyFailed Event ????????? ??????.")
    @Test
    void resetComment() {
        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));
        processAccusation_to_completed_status();

        adminAccusationService.resetComment(ACCUSATION_ID);

        assertThat(accusation.getResultComment()).isEmpty();
        assertThat(accusation.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED);
    }

}
