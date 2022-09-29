package com.mungta.accusation.domain;

import com.mungta.accusation.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.mungta.accusation.constants.AccusationTestSample.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccusationTest {

    private Accusation accusation;

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
    }

    @DisplayName("신고를 등록한 회원이 아닐 경우 true 반환합니다.")
    @Test
    void isNotWriter_true() {
        boolean result = accusation.isNotWriter("2");
        assertThat(result).isTrue();
    }

    @DisplayName("신고를 등록한 회원일 경우 false 반환합니다.")
    @Test
    void isNotWriter_false() {
        boolean result = accusation.isNotWriter(MEMBER_ID);
        assertThat(result).isFalse();
    }

    @DisplayName("신고 상태가 REGISTERED 가 아닌 경우 true 반환합니다.")
    @Test
    void isNotRegisteredStatus_true() {
        accusation.process(AccusationStatus.REJECTED, "신고 사유가 적합하지 않음.");

        boolean result = accusation.isNotRegisteredStatus();

        assertThat(result).isTrue();
    }

    @DisplayName("신고 상태가 REGISTERED 인 경우 false 반환합니다.")
    @Test
    void isNotRegisteredStatus_false() {
        boolean result = accusation.isNotRegisteredStatus();

        assertThat(result).isFalse();
    }

    @DisplayName("신고 제목과 신고 내용을 수정합니다.")
    @Test
    void modifyAccusationContents() {
        AccusationContents accusationContents = new AccusationContents("제목 수정", "내용 수정");

        accusation.modifyAccusationContents(accusationContents);

        assertThat(accusation.getAccusationContents()).isEqualTo(accusationContents);
    }

    @DisplayName("신고 상태 변경하면서 코멘트 내용도 추가합니다.")
    @Test
    void process() {
        accusation.process(AccusationStatus.REJECTED, "신고 사유가 적합하지 않음.");

        assertThat(accusation.getAccusationStatus()).isEqualTo(AccusationStatus.REJECTED);
        assertThat(accusation.getResultComment()).isEqualTo("신고 사유가 적합하지 않음.");
    }

    @DisplayName("이미 관리자에 의해 신고처리된(COMPLETED/REJECTED) 경우, 신고 상태를 변경할 수 없습니다.")
    @Test
    void process_fail() {
        process();
        assertThatThrownBy(() -> accusation.process(AccusationStatus.COMPLETED, "없음"))
                .isInstanceOf(ApiException.class)
                .hasMessage("이미 관리자에 의해 처리된 신고글입니다.");
    }

    @DisplayName("처리했던 신고 상태를 처리전 상태(REGISTERED)로 변경할 수 있습니다.")
    @Test
    void resetComment() {
        process();
        accusation.resetComment();

        assertThat(accusation.getAccusationStatus()).isEqualTo(AccusationStatus.REGISTERED);
        assertThat(accusation.getResultComment()).isEmpty();
    }

}
