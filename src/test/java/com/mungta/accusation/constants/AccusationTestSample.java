package com.mungta.accusation.constants;

import com.mungta.accusation.api.dto.AccusationContentsRequest;
import com.mungta.accusation.api.dto.AccusationRequest;
import com.mungta.accusation.api.dto.AccusedMemberRequest;
import com.mungta.accusation.api.dto.PartyInfoRequest;

public class AccusationTestSample {

    public static final long ACCUSATION_ID = 1L;
    public static final String MEMBER_ID = "1";
    public static final long PARTY_ID = 1L;
    public static final String PLACE_OF_DEPARTURE = "정자역";
    public static final String DESTINATION = "사당역";
    public static final String STARTED_DATE_TIME = "2022-07-18 18:20";
    public static final String ACCUSED_MEMBER_ID = "2";
    public static final String ACCUSED_MEMBER_NAME = "통통이";
    public static final String ACCUSED_MEMBER_EMAIL = "xxx@sk.com";
    public static final String ACCUSED_MEMBER_DEPARTMENT = "금융부서";
    public static final String CONTENTS_TITLE = "신고합니다.";
    public static final String CONTENTS_DESC = "내용";

    public static final AccusationRequest ACCUSATION_REQUEST;

    static {
        ACCUSATION_REQUEST = AccusationRequest.builder()
                .partyInfo(
                        PartyInfoRequest.builder()
                                .partyId(PARTY_ID)
                                .placeOfDeparture(PLACE_OF_DEPARTURE)
                                .destination(DESTINATION)
                                .startedDateTime(STARTED_DATE_TIME)
                                .build()
                )
                .accusedMember(
                        AccusedMemberRequest.builder()
                                .id(ACCUSED_MEMBER_ID)
                                .name(ACCUSED_MEMBER_NAME)
                                .email(ACCUSED_MEMBER_EMAIL)
                                .build()
                )
                .accusationContents(
                        AccusationContentsRequest.builder()
                                .title(CONTENTS_TITLE)
                                .desc(CONTENTS_DESC)
                                .build()
                )
                .build();
    }

}
