package org.mediagate.core.dto;

import org.mediagate.db.enums.Availability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatFullDto {
    private Long id;
    private String title;
    private String photo;
    private Availability type;
    private List<UserFullDto> participants;
    private List<AttachmentDto> attachments;
}
