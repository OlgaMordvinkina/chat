package org.mediagate.db.model;

import org.mediagate.db.enums.StateMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatPreviewDto {
    private Long chatId;
    private Long messageId;
    private Long companionId;
    private String photo;
    private String title;
    private Long senderId;
    private String lastMessage;
    private String dateLastMessage;
    private StateMessage stateMessage;
    private Long unreadMessages;
}
