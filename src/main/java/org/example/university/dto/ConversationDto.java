package org.example.university.dto;

import org.example.university.model.User;
import java.time.LocalDateTime;

/**
 * DTO для отображения диалога в списке сообщений
 */
public class ConversationDto {
    private User otherUser;
    private String lastMessage;
    private LocalDateTime lastTime;
    private boolean hasUnread;

    public ConversationDto(User otherUser, String lastMessage, LocalDateTime lastTime, boolean hasUnread) {
        this.otherUser = otherUser;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
        this.hasUnread = hasUnread;
    }

    public User getOtherUser() { return otherUser; }
    public String getLastMessage() { return lastMessage; }
    public LocalDateTime getLastTime() { return lastTime; }
    public boolean isHasUnread() { return hasUnread; }
}

