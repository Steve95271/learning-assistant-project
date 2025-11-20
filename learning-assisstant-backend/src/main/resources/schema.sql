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
#                         Add foreign key when having user table
#                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_updated (user_id, updated_at),
    INDEX idx_user_accessed (user_id, last_accessed_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Learning topics';