-- Users table: Core user account information
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                     BIGINT UNSIGNED PRIMARY KEY COMMENT 'Snowflake generated ID',
    email                  VARCHAR(255)                                                                                  NOT NULL UNIQUE COMMENT 'User email address (unique)',
    password_hash          VARCHAR(255) COMMENT 'Hashed password (null for OAuth-only users)',
    display_name           VARCHAR(100) COMMENT 'User display name',
    profile_picture_url    VARCHAR(512) COMMENT 'URL to profile picture',
    account_status         ENUM ('active', 'suspended', 'deleted') DEFAULT 'active'                                      NOT NULL COMMENT 'Account status',
    last_login_at          DATETIME COMMENT 'Last login timestamp',
    created_at             DATETIME                                DEFAULT CURRENT_TIMESTAMP                             NOT NULL COMMENT 'Account creation time',
    updated_at             DATETIME                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT 'Last update time',
    deleted_at             DATETIME COMMENT 'Soft delete timestamp',

    INDEX idx_email (email),
    INDEX idx_account_status (account_status),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='User accounts';

-- Topics: Learning subject areas
DROP TABLE IF EXISTS topics;
CREATE TABLE topics
(
    id                 BIGINT UNSIGNED PRIMARY KEY COMMENT 'Snowflake generated ID',
    user_id            BIGINT UNSIGNED                                                                  NOT NULL COMMENT 'Topic owner',
    name               VARCHAR(255)                                                                     NOT NULL COMMENT 'Topic name',
    description        TEXT COMMENT 'Topic description',
    conversation_count INT UNSIGNED               DEFAULT 0                                             NOT NULL COMMENT 'Cached count of conversations',
    file_count         INT UNSIGNED               DEFAULT 0                                             NOT NULL COMMENT 'Cached count of files',
    status             ENUM ('active', 'deleted') DEFAULT 'active'                                      NOT NULL COMMENT 'Topic status',
    last_accessed_at   DATETIME COMMENT 'Last time topic was accessed',
    created_at         DATETIME                   DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    updated_at         DATETIME                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    deleted_at         DATETIME COMMENT 'Soft delete timestamp',
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_user_updated (user_id, updated_at),
    INDEX idx_user_accessed (user_id, last_accessed_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Learning topics';

-- Topic files: Files uploaded to topics
DROP TABLE IF EXISTS topic_files;
CREATE TABLE topic_files (
                             id BIGINT UNSIGNED PRIMARY KEY COMMENT 'Snowflake generated ID',
                             topic_id BIGINT UNSIGNED NOT NULL COMMENT 'Reference to topics table',
                             user_id BIGINT UNSIGNED NOT NULL COMMENT 'File uploader',
                             filename VARCHAR(255) NOT NULL COMMENT 'Original filename',
                             icon VARCHAR(32) NOT NULL COMMENT 'File emoji icon',
                             file_type VARCHAR(50) NOT NULL COMMENT 'File MIME type or extension',
                             file_size BIGINT UNSIGNED NOT NULL COMMENT 'File size in bytes',
                             storage_path VARCHAR(512) NOT NULL COMMENT 'Path to stored file',
                             storage_key VARCHAR(255) NOT NULL UNIQUE COMMENT 'Unique storage identifier',
                             uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             deleted_at DATETIME COMMENT 'Soft delete timestamp',

                             FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                             INDEX idx_topic_uploaded (topic_id, uploaded_at),
                             INDEX idx_storage_key (storage_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Files uploaded to topics';


-- Conversations: Chat sessions within topics
DROP TABLE IF EXISTS conversations;
CREATE TABLE conversations
(
    id                    BIGINT UNSIGNED PRIMARY KEY COMMENT 'Snowflake generated ID',
    topic_id              BIGINT UNSIGNED                                                    NOT NULL COMMENT 'Reference to topics table',
    user_id               BIGINT UNSIGNED                                                    NOT NULL COMMENT 'Conversation owner',
    title                 VARCHAR(255)                                                       NOT NULL COMMENT 'Conversation title',
    message_count         INT UNSIGNED DEFAULT 0                                             NOT NULL COMMENT 'Cached count of messages',
    first_message_preview TEXT COMMENT 'Preview of first message for display',
    last_message_at       DATETIME COMMENT 'Timestamp of last message',
    created_at            DATETIME     DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    updated_at            DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    deleted_at            DATETIME COMMENT 'Soft delete timestamp',

    FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_topic_updated (topic_id, updated_at),
    INDEX idx_user_updated (user_id, updated_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Chat conversations within topics';

-- Summary notes: AI-generated knowledge summaries for topics
DROP TABLE IF EXISTS summary_notes;
CREATE TABLE summary_notes (
                               id BIGINT UNSIGNED PRIMARY KEY COMMENT 'Snowflake generated ID',
                               topic_id BIGINT UNSIGNED NOT NULL UNIQUE COMMENT 'Reference to topics table (one summary per topic)',
                               user_id BIGINT UNSIGNED NOT NULL COMMENT 'Summary owner',
                               title VARCHAR(255) NOT NULL COMMENT 'Summary title',
                               content LONGTEXT NOT NULL COMMENT 'Full summary content (markdown format)',
                               content_sections JSON COMMENT 'Structured sections for ToC navigation',
                               word_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Cached word count',
                               estimated_read_time_minutes INT UNSIGNED COMMENT 'Estimated reading time',
                               generation_source ENUM('auto_first', 'auto_update', 'manual') NOT NULL COMMENT 'How summary was generated',
                               auto_update_enabled BOOLEAN DEFAULT TRUE NOT NULL COMMENT 'Auto-regenerate on new conversations',
                               source_conversation_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Number of conversations used',
                               last_regenerated_at DATETIME NOT NULL COMMENT 'Last generation/regeneration time',
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT 'First creation time',
                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                               deleted_at DATETIME COMMENT 'Soft delete timestamp',

                               FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               INDEX idx_topic_updated (topic_id, updated_at),
                               INDEX idx_user_updated (user_id, updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI-generated topic summaries';
