package com.mungta.accusation.service;

import com.mungta.accusation.api.dto.AccusationPartyMemberListResponse;
import com.mungta.accusation.domain.PartyInfo;
import com.mungta.accusation.domain.repositories.AccusationRepository;
import com.mungta.accusation.client.PartyServiceClient;
import com.mungta.accusation.client.UserServiceClient;
import com.mungta.accusation.client.dto.PartyResponse;
import com.mungta.accusation.client.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.mungta.accusation.api.dto.AccusationPartyMemberListResponse.MemberResponse;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccusationPartyMembersService {

    private final AccusationRepository accusationRepository;

    private final PartyServiceClient partyServiceClient;
    private final UserServiceClient userServiceClient;

    public AccusationPartyMemberListResponse getAccusationPartyMembers(final String memberId, final long partyId) {
        log.info("Get PartyInfo from PartyService. partyId: {}", partyId);
        PartyResponse party = partyServiceClient.getParty(partyId);

        List<String> memberIds = party.getUserIds().stream()
                .filter(id -> !id.equals(memberId))
                .collect(Collectors.toList());

        log.info("Get MemberList from UserService. memberIds: {}", memberIds);
        List<UserResponse> userList = userServiceClient.getUserList(memberIds);

        List<String> preAccusedMemberIdList = getAccusedMemberIdByPartyId(memberId, party);
        return AccusationPartyMemberListResponse.of(party, getMemberResponse(userList, preAccusedMemberIdList));
    }

    private List<String> getAccusedMemberIdByPartyId(String memberId, PartyResponse party) {
        PartyInfo partyInfo = PartyInfo.builder()
                .partyId(party.getPartyId())
                .placeOfDeparture(party.getPlaceOfDeparture())
                .destination(party.getDestination())
                .startedDateTime(party.getStartDate())
                .build();

        return  accusationRepository.findByMemberIdAndPartyInfo(memberId, partyInfo).stream()
                .map(accusation -> accusation.getAccusedMember().getId())
                .collect(Collectors.toList());
    }

    private List<MemberResponse> getMemberResponse(List<UserResponse> userList, List<String> preAccusedMemberIdList) {
        return userList.stream()
                .map(userResponse -> MemberResponse.builder()
                        .id(userResponse.getUserId())
                        .name(userResponse.getUserName())
                        .email(userResponse.getUserMailAddress())
                        .department(userResponse.getUserTeamName())
                        .userPhoto(userResponse.getUserPhoto())
                        .fileExtension(userResponse.getFileExtension())
                        .accusedYN(preAccusedMemberIdList.contains(userResponse.getUserId()))
                        .build()
                ).collect(Collectors.toList());
    }

}
