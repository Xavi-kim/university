package org.example.university.repository;

import org.example.university.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender_IdOrReceiver_IdOrderBySentAtDesc(Long senderId, Long receiverId);
    List<Message> findByReceiver_IdAndIsReadFalse(Long receiverId);

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId OR m.receiver.id = :userId) ORDER BY m.sentAt DESC")
    List<Message> findUserMessages(Long userId);

    @Query("SELECT m FROM Message m WHERE ((m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR (m.sender.id = :user2Id AND m.receiver.id = :user1Id)) ORDER BY m.sentAt")
    List<Message> findConversation(Long user1Id, Long user2Id);

    Long countByReceiver_IdAndIsReadFalse(Long receiverId);
}

