package com.mungta.accusation.domain;

import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accusations")
public class Accusation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Embedded
    private AccusedMember accusedMember;

    @Embedded
    private PartyInfo partyInfo;

    @Embedded
    private AccusationContents accusationContents;

    @Column(name = "accusation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccusationStatus accusationStatus;

    @Column(name = "result_comment")
    private String resultComment;

    @CreatedDate
    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "modified_date_time")
    private LocalDateTime modifiedDateTime;

    @Builder
    public Accusation(String memberId, AccusedMember accusedMember, PartyInfo partyInfo, AccusationContents accusationContents) {
        this.memberId = memberId;
        this.accusedMember = accusedMember;
        this.partyInfo = partyInfo;
        this.accusationContents = accusationContents;
        this.accusationStatus = AccusationStatus.REGISTERED;
        this.resultComment = "";
    }

    public boolean isNotWriter(String memberId) {
        return !this.memberId.equals(memberId);
    }

    public boolean isNotRegisteredStatus() {
        return this.accusationStatus != AccusationStatus.REGISTERED;
    }

    public void modifyAccusationContents(AccusationContents accusationContents) {
        this.accusationContents = accusationContents;
    }

    public void process(AccusationStatus accusationStatus, String resultComment) {
        log.debug("Request change status. id: {} status: {} -> {}", this.id, this.accusationStatus, accusationStatus);
        if (isRejectedOrCompletedStatus()) {
            throw new ApiException(ApiStatus.INVALID_CHANGE_STATUS);
        }
        this.accusationStatus = accusationStatus;
        this.resultComment = StringUtils.stripToEmpty(resultComment);
    }

    private boolean isRejectedOrCompletedStatus() {
        return this.accusationStatus == AccusationStatus.REJECTED || this.accusationStatus == AccusationStatus.COMPLETED;
    }

    public void resetComment() {
        log.debug("Reset comment. id: {} status: {} -> {}", this.id, this.accusationStatus, AccusationStatus.REGISTERED);
        this.accusationStatus = AccusationStatus.REGISTERED;
        this.resultComment = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accusation that = (Accusation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
