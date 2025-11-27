-- Insert test user
INSERT INTO users (id, email, password_hash, display_name, profile_picture_url, account_status, last_login_at,
                   created_at, updated_at)
VALUES (100, 'test.user@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Test User',
        'https://i.pravatar.cc/150?u=100', 'active', '2025-11-25 10:30:00', '2025-01-15 08:00:00',
        '2025-11-25 10:30:00');

-- Insert topics
INSERT INTO topics (id, user_id, name, description, conversation_count, file_count, status, last_accessed_at,
                    created_at, updated_at)
VALUES (1001, 100, 'Data Structures & Algorithms',
        'Learning fundamental data structures like trees, graphs, hash tables and algorithms for sorting, searching, and optimization',
        3, 2, 'active', '2025-11-24 15:20:00', '2025-02-01 09:00:00', '2025-11-24 15:20:00'),
       (1002, 100, 'System Design Fundamentals',
        'Understanding distributed systems, scalability patterns, microservices architecture, and designing large-scale applications',
        2, 3, 'active', '2025-11-23 14:45:00', '2025-02-15 10:30:00', '2025-11-23 14:45:00'),
       (1003, 100, 'Java Spring Boot',
        'Deep dive into Spring Framework, Spring Boot, dependency injection, Spring Data JPA, REST APIs, and Spring Security',
        2, 1, 'active', '2025-11-22 11:00:00', '2025-03-01 13:15:00', '2025-11-22 11:00:00'),
       (1004, 100, 'React & Frontend Development',
        'Modern React patterns, hooks, state management with Redux, TypeScript integration, and component architecture',
        1, 2, 'active', '2025-11-20 16:30:00', '2025-03-10 14:00:00', '2025-11-20 16:30:00'),
       (1005, 100, 'Database Design & SQL',
        'Relational database design, normalization, indexing strategies, query optimization, and advanced SQL techniques',
        0, 3, 'active', '2025-11-18 09:15:00', '2025-04-05 11:20:00', '2025-11-18 09:15:00'),
       (1006, 100, 'Machine Learning Basics',
        'Introduction to ML algorithms, supervised and unsupervised learning, neural networks, and practical applications',
        0, 0, 'active', '2025-11-15 13:45:00', '2025-05-12 15:30:00', '2025-11-15 13:45:00');

-- Insert topic files
-- Insert topic files with icon field
INSERT INTO topic_files (id, topic_id, user_id, filename, icon, file_type, file_size, storage_path, storage_key,
                         uploaded_at)
VALUES (2001, 1001, 100, 'introduction-to-algorithms.pdf', '📚', 'application/pdf', 2458624,
        '/storage/files/2025/02/intro-algorithms.pdf', 'file_2001_intro_algo', '2025-02-01 10:00:00'),
       (2002, 1001, 100, 'big-o-notation-cheatsheet.png', '📊', 'image/png', 145920,
        '/storage/files/2025/02/bigo-cheatsheet.png', 'file_2002_bigo_chart', '2025-02-03 14:30:00'),
       (2003, 1002, 100, 'system-design-primer.pdf', '🏗️', 'application/pdf', 3872512,
        '/storage/files/2025/02/sysdesign-primer.pdf', 'file_2003_sysdesign', '2025-02-15 11:00:00'),
       (2004, 1002, 100, 'microservices-patterns.pdf', '🔧', 'application/pdf', 1923584,
        '/storage/files/2025/02/microservices.pdf', 'file_2004_microservices', '2025-02-16 09:45:00'),
       (2005, 1002, 100, 'caching-strategies-diagram.png', '💾', 'image/png', 298016,
        '/storage/files/2025/02/caching-diagram.png', 'file_2005_caching', '2025-02-20 16:20:00'),
       (2006, 1003, 100, 'spring-boot-reference.pdf', '🍃', 'application/pdf', 5243392,
        '/storage/files/2025/03/spring-boot-ref.pdf', 'file_2006_spring_ref', '2025-03-01 14:00:00'),
       (2007, 1004, 100, 'react-hooks-guide.pdf', '⚛️', 'application/pdf', 876544,
        '/storage/files/2025/03/react-hooks.pdf', 'file_2007_react_hooks', '2025-03-10 15:00:00'),
       (2008, 1004, 100, 'typescript-react-patterns.md', '📝', 'text/markdown', 45120,
        '/storage/files/2025/03/ts-react.md', 'file_2008_ts_patterns', '2025-03-12 10:30:00'),
       (2009, 1005, 100, 'sql-optimization-guide.pdf', '⚡', 'application/pdf', 1567890,
        '/storage/files/2025/04/sql-optimization.pdf', 'file_2009_sql_opt', '2025-04-05 12:00:00'),
       (2010, 1005, 100, 'database-normalization.png', '🗄️', 'image/png', 234560,
        '/storage/files/2025/04/db-normalization.png', 'file_2010_normalization', '2025-04-06 13:15:00'),
       (2011, 1005, 100, 'indexing-strategies.pdf', '🔍', 'application/pdf', 982400,
        '/storage/files/2025/04/indexing.pdf', 'file_2011_indexing', '2025-04-08 11:45:00');

-- Insert conversations
INSERT INTO conversations (id, topic_id, user_id, title, message_count, first_message_preview, last_message_at,
                           created_at, updated_at)
VALUES (3001, 1001, 100, 'Understanding Binary Search Trees', 12,
        'Can you explain how binary search trees work and when to use them?', '2025-11-24 15:20:00',
        '2025-11-24 14:00:00', '2025-11-24 15:20:00'),
       (3002, 1001, 100, 'Graph Traversal Algorithms', 8, 'What are the differences between DFS and BFS?',
        '2025-11-22 10:45:00', '2025-11-22 09:30:00', '2025-11-22 10:45:00'),
       (3003, 1001, 100, 'Dynamic Programming Introduction', 15,
        'I am struggling with understanding dynamic programming. Can you help?', '2025-11-20 16:30:00',
        '2025-11-20 14:00:00', '2025-11-20 16:30:00'),
       (3004, 1002, 100, 'Load Balancing Strategies', 10,
        'How do different load balancing algorithms compare in distributed systems?', '2025-11-23 14:45:00',
        '2025-11-23 13:00:00', '2025-11-23 14:45:00'),
       (3005, 1002, 100, 'Database Sharding Techniques', 7, 'When should I use database sharding vs replication?',
        '2025-11-21 11:20:00', '2025-11-21 10:00:00', '2025-11-21 11:20:00'),
       (3006, 1003, 100, 'Spring Boot Dependency Injection', 9,
        'Can you explain how @Autowired works and best practices?', '2025-11-22 11:00:00', '2025-11-22 09:45:00',
        '2025-11-22 11:00:00'),
       (3007, 1003, 100, 'REST API Design with Spring', 11,
        'What are the best practices for designing RESTful APIs in Spring Boot?', '2025-11-19 15:30:00',
        '2025-11-19 14:00:00', '2025-11-19 15:30:00'),
       (3008, 1004, 100, 'React useEffect Hook Patterns', 6,
        'I keep running into infinite loops with useEffect. What am I doing wrong?', '2025-11-20 16:30:00',
        '2025-11-20 15:00:00', '2025-11-20 16:30:00');

-- Insert summary notes for each topic
INSERT INTO summary_notes (id, topic_id, user_id, title, content, content_sections, word_count,
                           estimated_read_time_minutes, generation_source, auto_update_enabled,
                           source_conversation_count, last_regenerated_at, created_at, updated_at)
VALUES (4001, 1001, 100, 'Data Structures & Algorithms - Learning Summary', '', '', 1850, 8, 'auto_update', TRUE, 3,
        '2025-11-24 16:00:00', '2025-02-05 10:00:00', '2025-11-24 16:00:00'),

       (4002, 1002, 100, 'System Design Fundamentals - Learning Summary', '', '', 2240,
        10, 'auto_update', TRUE, 2, '2025-11-23 15:30:00', '2025-02-20 11:00:00', '2025-11-23 15:30:00'),

       (4003, 1003, 100, 'Java Spring Boot - Learning Summary', '', '',
        1920, 9, 'auto_update', TRUE, 2, '2025-11-22 12:00:00', '2025-03-05 14:00:00', '2025-11-22 12:00:00'),

       (4004, 1004, 100, 'React & Frontend Development - Learning Summary', '', '', 1450, 7, 'auto_first', TRUE, 1,
        '2025-11-20 17:00:00', '2025-03-15 16:00:00', '2025-11-20 17:00:00'),

       (4005, 1005, 100, 'Database Design & SQL - Learning Summary', '', '', 0, 0, 'manual', FALSE, 0,
        '2025-11-18 10:00:00', '2025-04-10 12:00:00', '2025-11-18 10:00:00'),

       (4006, 1006, 100, 'Machine Learning Basics - Learning Summary', '', '', 0, 0, 'manual', TRUE, 0,
        '2025-11-15 14:00:00', '2025-05-15 16:00:00', '2025-11-15 14:00:00');