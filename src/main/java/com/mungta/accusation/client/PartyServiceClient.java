package com.mungta.accusation.client;

import com.mungta.accusation.client.dto.PartyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "party", url = "${api.url.party}")
public interface PartyServiceClient {

    @GetMapping
    PartyResponse getParty(@RequestParam long partyId);

}
