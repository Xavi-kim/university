package org.example.university.service;

import org.example.university.dto.ConversationDto;
import org.example.university.model.Message;
import org.example.university.model.User;
import org.example.university.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Отправить сообщение
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    // Получить все сообщения пользователя
    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findUserMessages(userId);
    }

    // Получить диалог между двумя пользователями
    public List<Message> getConversation(Long user1Id, Long user2Id) {
        return messageRepository.findConversation(user1Id, user2Id);
    }

    // Получить непрочитанные сообщения
    public List<Message> getUnreadMessages(Long userId) {
        return messageRepository.findByReceiver_IdAndIsReadFalse(userId);
    }

    // Количество непрочитанных
    public Long getUnreadCount(Long userId) {
        return messageRepository.countByReceiver_IdAndIsReadFalse(userId);
    }

    // Отметить сообщение как прочитанное
    public void markAsRead(Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setIsRead(true);
            message.setReadAt(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    // Отметить все сообщения диалога как прочитанные
    public void markConversationAsRead(Long userId, Long otherUserId) {
        List<Message> messages = getConversation(userId, otherUserId);
        for (Message message : messages) {
            if (message.getReceiver().getId().equals(userId) && !message.getIsRead()) {
                message.setIsRead(true);
                message.setReadAt(LocalDateTime.now());
                messageRepository.save(message);
            }
        }
    }

    // Удалить сообщение
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    // Получить сообщение по ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Получить все сообщения в системе
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Получить список диалогов пользователя (как в почте)
     */
    public List<ConversationDto> getConversations(Long userId) {
        List<Message> allMessages = messageRepository.findUserMessages(userId);

        // Группируем по собеседнику
        Map<Long, ConversationDto> convMap = new LinkedHashMap<>();
        for (Message msg : allMessages) {
            User other = msg.getSender().getId().equals(userId) ? msg.getReceiver() : msg.getSender();
            if (!convMap.containsKey(other.getId())) {
                boolean hasUnread = !msg.getSender().getId().equals(userId) && !msg.getIsRead();
                String preview = msg.getContent().length() > 60
                        ? msg.getContent().substring(0, 60) + "..."
                        : msg.getContent();
                convMap.put(other.getId(), new ConversationDto(other, preview, msg.getSentAt(), hasUnread));
            } else if (!msg.getSender().getId().equals(userId) && !msg.getIsRead()) {
                // обновляем hasUnread если есть непрочитанное
                ConversationDto existing = convMap.get(other.getId());
                convMap.put(other.getId(), new ConversationDto(existing.getOtherUser(), existing.getLastMessage(), existing.getLastTime(), true));
            }
        }
        return new ArrayList<>(convMap.values());
    }
}
