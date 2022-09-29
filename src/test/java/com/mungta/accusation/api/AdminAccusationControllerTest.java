package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.admin.AccusationStatusRequest;
import com.mungta.accusation.api.dto.admin.AdminAccusationListResponse;
import com.mungta.accusation.api.dto.admin.AdminAccusationResponse;
import com.mungta.accusation.domain.*;
import com.mungta.accusation.service.AdminAccusationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static com.mungta.accusation.constants.AccusationTestSample.CONTENTS_DESC;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminAccusationController.class)
class AdminAccusationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminAccusationService adminAccusationService;

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

    @DisplayName("[관리자] 신고 조회 API")
    @Test
    void getAccusation() throws Exception {
        AdminAccusationResponse response = AdminAccusationResponse.of(accusation);

        doReturn(response)
                .when(adminAccusationService).getAccusation(ACCUSATION_ID);

        ResultActions result = mockMvc.perform(
                get("/api/accusation/admin/accusations/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
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
                .andExpect(jsonPath("$.accusationStatus").value("REGISTERED"))
                .andExpect(jsonPath("$.resultComment").value(""));
    }

    @DisplayName("[관리자] 회원 신고 리스트 조회 API")
    @Test
    void getAccusationList() throws Exception {
        AdminAccusationListResponse response = AdminAccusationListResponse.of(List.of(accusation));

        doReturn(response)
                .when(adminAccusationService).getAccusationList();

        ResultActions result = mockMvc.perform(
                get("/api/accusation/admin/accusations")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accusations").exists())
                .andExpect(jsonPath("$.accusations.length()").value(1))
                .andExpect(jsonPath("$.accusations[0].id").value(ACCUSATION_ID))
                .andExpect(jsonPath("$.accusations[0].partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.accusations[0].memberId").value(MEMBER_ID))
                .andExpect(jsonPath("$.accusations[0].title").value(CONTENTS_TITLE))
                .andExpect(jsonPath("$.accusations[0].accusationStatus").value("REGISTERED"))
                .andExpect(jsonPath("$.accusations[0].modifiedDateTime").exists());
    }

    @DisplayName("[관리자] 신고 처리 API")
    @Test
    void processAccusation() throws Exception {
        accusation.process(AccusationStatus.COMPLETED, "완료");
        AdminAccusationResponse response = AdminAccusationResponse.of(accusation);

        doReturn(response)
                .when(adminAccusationService).processAccusation(anyLong(), any());

        ResultActions result = mockMvc.perform(
                put("/api/accusation/admin/accusations/" + ACCUSATION_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(
                                        new AccusationStatusRequest(AccusationStatus.COMPLETED, "완료")
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
                .andExpect(jsonPath("$.accusationContents.title").value(CONTENTS_TITLE))
                .andExpect(jsonPath("$.accusationContents.desc").value(CONTENTS_DESC))
                .andExpect(jsonPath("$.accusationStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.resultComment").value("완료"));
    }

}
