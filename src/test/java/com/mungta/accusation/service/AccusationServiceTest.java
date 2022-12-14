package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.*;
import com.mungta.accusation.domain.*;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
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

import static com.mungta.accusation.api.dto.AccusationListResponse.AccusationInfoResponse;
import static com.mungta.accusation.constants.AccusationTestSample.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class AccusationServiceTest {

    @InjectMocks
    @Spy
    private AccusationService accusationService;

    @Mock
    private AccusationRepository accusationRepository;

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

    @DisplayName("[??????] ?????? ??????.")
    @Test
    void addAccusation() {
        given(accusationRepository.save(any())).willReturn(accusation);

        long response = accusationService.addAccusation(MEMBER_ID, ACCUSATION_REQUEST);

        assertThat(response).isEqualTo(ACCUSATION_ID);
    }

    @DisplayName("[??????] ?????? ?????? ??????.")
    @Test
    void getAccusation() {
        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        AccusationResponse response = accusationService.getAccusation(ACCUSATION_ID, MEMBER_ID);

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
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED)
        );
    }

    @DisplayName("[??????] ??????????????? ?????? id??? accusation ????????? ?????? ??? ?????? ?????? Exception ??????.")
    @Test
    void getAccusation_not_found_by_id() {
        given(accusationRepository.findById(2L))
                .willThrow(new ApiException(ApiStatus.NOT_FOUND_ACCUSATION));

        assertThatThrownBy(() -> accusationService.getAccusation(2L, MEMBER_ID))
                .isInstanceOf(ApiException.class)
                .hasMessage("?????? ???????????? ?????? ??? ????????????.");
    }

    @DisplayName("[??????] ??????(accusation) ????????? ?????? ID??? ??????????????? ?????? ?????? ID??? ???????????? ?????? ?????? Exception ??????.")
    @Test
    void getAccusation_not_writer_by_memberId() {
        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        assertThatThrownBy(() -> accusationService.getAccusation(ACCUSATION_ID, "2"))
                .isInstanceOf(ApiException.class)
                .hasMessage("????????? ?????? ?????? ??????????????? ?????? ????????? ??? ????????? ????????????.");
    }

    @DisplayName("[??????] ?????? ?????? ????????? ?????? ??????.")
    @Test
    void getAccusationList() {
        given(accusationRepository.findByMemberIdOrderByCreatedDateTimeDesc(MEMBER_ID)).willReturn(List.of(accusation));

        AccusationListResponse response = accusationService.getAccusationList(MEMBER_ID);
        List<AccusationInfoResponse> responseList = response.getAccusations();

        assertAll(
                () -> assertThat(responseList.size()).isEqualTo(1),
                () -> assertThat(responseList.get(0)).isEqualTo(
                        AccusationInfoResponse.builder()
                                .id(ACCUSATION_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .partyStartedDateTime(STARTED_DATE_TIME)
                                .title(CONTENTS_TITLE)
                                .accusationStatus(AccusationStatus.REGISTERED)
                                .modifiedDateTime(nowDateTime.format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                ))
                                .build()
                )
        );
    }

    @DisplayName("[??????] ?????? memberId??? ????????? ????????? ????????? ?????? ?????? ??? ???????????? ??????.")
    @Test
    void getAccusationList_size_zero() {
        given(accusationRepository.findByMemberIdOrderByCreatedDateTimeDesc("2")).willReturn(List.of());

        AccusationListResponse response = accusationService.getAccusationList("2");

        assertThat(response.getAccusations().size()).isZero();
    }

    @DisplayName("[??????] ?????? ?????? ?????? ??????.")
    @Test
    void modifyAccusationContents() {
        AccusationContentsRequest request = new AccusationContentsRequest("?????? ??????", "?????? ??????");

        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        AccusationResponse response = accusationService.modifyAccusationContents(ACCUSATION_ID, request);

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
                                .title(request.getTitle())
                                .desc(request.getDesc())
                                .build()),
                () -> assertThat(response.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED)
        );
    }

    @DisplayName("[??????] ???????????? ?????? ????????? ??? ??????(?????? or ??????)??? ?????? ?????? ?????? ????????? Exception ??????.")
    @Test
    void modifyAccusationContents_not_registered_status() {
        AccusationContentsRequest request = new AccusationContentsRequest("?????? ??????", "?????? ??????");
        accusation.process(AccusationStatus.REJECTED, "");

        given(accusationRepository.findById(ACCUSATION_ID)).willReturn(Optional.ofNullable(accusation));

        assertThatThrownBy(() -> accusationService.modifyAccusationContents(ACCUSATION_ID, request))
                .isInstanceOf(ApiException.class)
                .hasMessage("?????? ?????? ????????? ????????? ????????? ????????? ??? ????????????.");
    }

    @DisplayName("[??????] ?????? ??????.")
    @Test
    void deleteAccusation() {
        accusationService.deleteAccusation(ACCUSATION_ID);
        verify(accusationRepository, times(1)).deleteById(ACCUSATION_ID);
    }
    
}
