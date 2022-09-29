package com.mungta.accusation.domain.repositories;

import com.mungta.accusation.domain.Accusation;
import com.mungta.accusation.domain.PartyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccusationRepository extends JpaRepository<Accusation, Long> {

    List<Accusation> findByMemberIdOrderByCreatedDateTimeDesc(String memberId);

    List<Accusation> findByMemberIdAndPartyInfo(String memberId, PartyInfo partyInfo);

    List<Accusation> findAllByOrderByCreatedDateTimeDesc();

}
