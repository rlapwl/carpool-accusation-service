package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.AccusationPartyMemberListResponse;
import com.mungta.accusation.client.dto.PartyResponse;
import com.mungta.accusation.service.AccusationPartyMembersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mungta.accusation.constants.AccusationTestSample.*;
import static com.mungta.accusation.api.dto.AccusationPartyMemberListResponse.MemberResponse;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccusationPartyMembersController.class)
class AccusationPartyMembersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccusationPartyMembersService accusationPartyMembersService;

    @DisplayName("[회원] 신고 내역 리스트 조회 API")
    @Test
    void getAccusationList() throws Exception {

        doReturn(AccusationPartyMemberListResponse.of(getPartyResponse(), getMemberResponseList()))
                .when(accusationPartyMembersService).getAccusationPartyMembers(MEMBER_ID, PARTY_ID);

        ResultActions result = mockMvc.perform(
                get("/api/accusation/party-members")
                        .header("userId", MEMBER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("partyId", String.valueOf(PARTY_ID))
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.partyId").value(PARTY_ID))
                .andExpect(jsonPath("$.placeOfDeparture").value(PLACE_OF_DEPARTURE))
                .andExpect(jsonPath("$.destination").value(DESTINATION))
                .andExpect(jsonPath("$.startedDateTime").value(STARTED_DATE_TIME))
                .andExpect(jsonPath("$.members").exists())
                .andExpect(jsonPath("$.members.length()").value(1))
                .andExpect(jsonPath("$.members[0].id").value(ACCUSED_MEMBER_ID))
                .andExpect(jsonPath("$.members[0].name").value(ACCUSED_MEMBER_NAME))
                .andExpect(jsonPath("$.members[0].email").value(ACCUSED_MEMBER_EMAIL))
                .andExpect(jsonPath("$.members[0].department").value(ACCUSED_MEMBER_DEPARTMENT))
                .andExpect(jsonPath("$.members[0].userPhoto").exists())
                .andExpect(jsonPath("$.members[0].accusedYN").value(false));
    }

    private PartyResponse getPartyResponse() {
        return PartyResponse.builder()
                .partyId(PARTY_ID)
                .placeOfDeparture(PLACE_OF_DEPARTURE)
                .destination(DESTINATION)
                .startDate(STARTED_DATE_TIME)
                .userIds(List.of(MEMBER_ID, ACCUSED_MEMBER_ID))
                .build();
    }

    private List<MemberResponse> getMemberResponseList() {
        MemberResponse memberResponse = MemberResponse.builder()
                .id(ACCUSED_MEMBER_ID)
                .name(ACCUSED_MEMBER_NAME)
                .email(ACCUSED_MEMBER_EMAIL)
                .department(ACCUSED_MEMBER_DEPARTMENT)
                .userPhoto(new byte[0])
                .accusedYN(false)
                .build();
        return List.of(memberResponse);
    }

}
