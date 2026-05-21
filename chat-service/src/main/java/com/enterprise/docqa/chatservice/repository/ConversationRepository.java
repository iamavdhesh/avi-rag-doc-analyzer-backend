package com.enterprise.docqa.chatservice.repository;

import com.enterprise.docqa.chatservice.model.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<ConversationEntity, String> {
    List<ConversationEntity> findByUserId(String userId);
}
