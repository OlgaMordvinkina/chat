package org.mediagate.core.dto;

import org.mediagate.db.enums.Availability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDto {
    private Long id;
    private String title;
    private String photo;
    private Availability type;
    private Set<Long> participants;
}
