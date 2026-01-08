CREATE TABLE conversations
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id         BIGINT       NOT NULL,
    company_id           BIGINT       NOT NULL,
    last_message_content TEXT,
    created_at           DATETIME(6)  NOT NULL,
    updated_at           DATETIME(6)  NOT NULL,
    
    CONSTRAINT fk_conversations_candidate FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id),
    CONSTRAINT fk_conversations_company FOREIGN KEY (company_id) REFERENCES companies (id),
    UNIQUE KEY uk_conversation_candidate_company (candidate_id, company_id)
);

CREATE TABLE chat_messages
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT       NOT NULL,
    direction       VARCHAR(50)  NOT NULL,
    content         TEXT         NOT NULL,
    is_read         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    
    CONSTRAINT fk_chat_messages_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id) ON DELETE CASCADE
);

CREATE TABLE notifications
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipient_id BIGINT       NOT NULL,
    sender_id    BIGINT,
    content      TEXT         NOT NULL,
    is_read      BOOLEAN      NOT NULL DEFAULT FALSE,
    type         VARCHAR(50)  NOT NULL,
    related_id   BIGINT,
    created_at   DATETIME(6)  NOT NULL,
    updated_at   DATETIME(6)  NOT NULL,
    
    CONSTRAINT fk_notifications_recipient FOREIGN KEY (recipient_id) REFERENCES users (id),
    CONSTRAINT fk_notifications_sender FOREIGN KEY (sender_id) REFERENCES users (id)
);

CREATE INDEX idx_conversations_last_message ON conversations(updated_at);
CREATE INDEX idx_chat_messages_conversation ON chat_messages(conversation_id);
CREATE INDEX idx_notifications_recipient ON notifications(recipient_id);