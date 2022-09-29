package com.mungta.accusation.api;

import com.mungta.accusation.api.dto.AccusationResponse;
import com.mungta.accusation.api.dto.admin.AccusationStatusRequest;
import com.mungta.accusation.api.dto.admin.AdminAccusationListResponse;
import com.mungta.accusation.api.dto.admin.AdminAccusationResponse;
import com.mungta.accusation.service.AdminAccusationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ACCUSATION ADMIN", description = "관리자용 신고 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accusation/admin/accusations")
public class AdminAccusationController {

    private final AdminAccusationService adminAccusationService;

    @Operation(summary = "신고 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 내용 수정 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccusationResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdminAccusationResponse> getAccusation(@Parameter(description = "신고 ID") @PathVariable long id) {
        AdminAccusationResponse response = adminAccusationService.getAccusation(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 신고 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 리스트 조회 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminAccusationListResponse.class))})
    })
    @GetMapping
    public ResponseEntity<AdminAccusationListResponse> getAccusationList() {
        AdminAccusationListResponse response = adminAccusationService.getAccusationList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신고 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 처리 성공",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminAccusationResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdminAccusationResponse> processAccusation(@Parameter(description = "신고 ID") @PathVariable long id,
                                                                     @RequestBody AccusationStatusRequest request) {
        AdminAccusationResponse response = adminAccusationService.processAccusation(id, request);
        return ResponseEntity.ok(response);
    }

}
