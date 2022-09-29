package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.AccusationContentsRequest;
import com.mungta.accusation.api.dto.AccusationListResponse;
import com.mungta.accusation.api.dto.AccusationResponse;
import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.AccusedMember;
import com.mungta.accusation.domain.PartyInfo;
import com.mungta.accusation.service.AccusationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mungta.accusation.domain.AccusationContents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.mungta.accusation.constants.AccusationTestSample.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccusationController.class)
class AccusationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccusationService accusationService;

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
        accusation.setModifiedDateTime(LocalDateTime.now());
    }

    @DisplayName("[회원] 신고 등록 API")
    @Test
    void addAccusation() throws Exception {

        doReturn(ACCUSATION_ID)
                .when(accusationService).addAccusation(anyString(), any());

        ResultActions result = mockMvc.perform(
                post("/api/accusation")
                        .header("userId", MEMBER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ACCUSATION_REQUEST))
        );

        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/accusation/list/" + ACCUSATION_ID));
    }

    @DisplayName("[회원] 신고 조회 API")
    @Test
    void getAccusation() throws Exception {

        doReturn(AccusationResponse.of(accusation))
                .when(accusationService).getAccusation(ACCUSATION_ID, MEMBER_ID);

        ResultActions result = mockMvc.perform(
                get("/api/accusation/list/" + ACCUSATION_ID)
                        .header("userId", MEMBER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", MEMBER_ID)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusedMember.id").value(ACCUSED_MEMBER_ID))
                .andExpect(jsonPath("$.accusedMember.name").value(ACCUSED_MEMBER_NAME))
                .andExpect(jsonPath("$.partyInfo.partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.partyInfo.placeOfDeparture").value(PLACE_OF_DEPARTURE))
                .andExpect(jsonPath("$.partyInfo.destination").value(DESTINATION))
                .andExpect(jsonPath("$.partyInfo.startedDateTime").value(STARTED_DATE_TIME))
                .andExpect(jsonPath("$.accusationContents.title").value(CONTENTS_TITLE))
                .andExpect(jsonPath("$.accusationContents.desc").value(CONTENTS_DESC))
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"));
    }

    @DisplayName("[회원] 신고 내역 리스트 조회 API")
    @Test
    void getAccusationList() throws Exception {

        doReturn(AccusationListResponse.of(List.of(accusation)))
                .when(accusationService).getAccusationList(MEMBER_ID);

        ResultActions result = mockMvc.perform(
                get("/api/accusation")
                        .header("userId", MEMBER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("memberId", MEMBER_ID)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accusations").exists())
                .andExpect(jsonPath("$.accusations.length()").value(1))
                .andExpect(jsonPath("$.accusations[0].id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusations[0].title").value(CONTENTS_TITLE))
                .andExpect(jsonPath("$.accusations[0].accusationStatus").value("REGISTERED"))
                .andExpect(jsonPath("$.accusations[0].modifiedDateTime").exists());
    }

    @DisplayName("[회원] 신고 내용 수정 API")
    @Test
    void modifyAccusation() throws Exception {
        accusation.modifyAccusationContents(new AccusationContents("제목 수정", "내용 수정"));

        doReturn(AccusationResponse.of(accusation))
                .when(accusationService).modifyAccusationContents(anyLong(), any());

        ResultActions result = mockMvc.perform(
                put("/api/accusation/list/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(
                                        AccusationContentsRequest.builder()
                                                .title("제목 수정")
                                                .desc("내용 수정")
                                                .build()
                                )
                        )
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusedMember.id").value(ACCUSED_MEMBER_ID))
                .andExpect(jsonPath("$.accusedMember.name").value(ACCUSED_MEMBER_NAME))
                .andExpect(jsonPath("$.partyInfo.partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.partyInfo.placeOfDeparture").value(PLACE_OF_DEPARTURE))
                .andExpect(jsonPath("$.partyInfo.destination").value(DESTINATION))
                .andExpect(jsonPath("$.partyInfo.startedDateTime").value(STARTED_DATE_TIME))
                .andExpect(jsonPath("$.accusationContents.title").value("제목 수정"))
                .andExpect(jsonPath("$.accusationContents.desc").value("내용 수정"))
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"));
    }

    @DisplayName("[회원] 신고 삭제 API")
    @Test
    void deleteAccusation() throws Exception {
        doNothing()
                .when(accusationService).deleteAccusation(ACCUSATION_ID);

        ResultActions result = mockMvc.perform(
                delete("/api/accusation/list/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

}
