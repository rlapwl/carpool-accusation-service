package com.mungta.accusation.domain;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccusationContents {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String desc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccusationContents that = (AccusationContents) o;
        return Objects.equals(title, that.title) && Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, desc);
    }

}
