/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

-- Table structure for table `account`
DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account`
(
    `id`           varchar(36)  NOT NULL,
    `email`        varchar(255) NOT NULL,
    `full_name`    varchar(255) NOT NULL,
    `password`     varchar(255) NOT NULL,
    `dob`          date         DEFAULT NULL,
    `gender`       tinyint(1)   DEFAULT NULL,
    `created_date` date         NOT NULL,
    `status`       tinyint(1)   NOT NULL,
    `phone`        varchar(255) NOT NULL,
    `image_url`    varchar(255) DEFAULT NULL,
    `role_id`      varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`),
    KEY `role` (`role_id`),
    CONSTRAINT `role` FOREIGN KEY (`role_id`) REFERENCES `setting` (`id`),
    CONSTRAINT `account_chk_1` CHECK ((`gender` in (0, 1, 2))),
    CONSTRAINT `account_chk_2` CHECK (regexp_like(`email`,
                                                  _utf8mb4'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')),
    CONSTRAINT `account_chk_3` CHECK (regexp_like(`password`,
                                                  _utf8mb4'^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;"\'<>,.?/]).{8,}$')),
    CONSTRAINT `account_chk_4` CHECK (regexp_like(`phone`, _utf8mb4'^\\+?[0-9]{10,15}$'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account`
    DISABLE KEYS */;
INSERT INTO `account`
VALUES ('9c9d5b45-5179-11f0-b7ee-088fc33f56c7', 'john.doe@example.com', 'John Doe', 'Abc@12345', '1990-05-15', 1,
        '2023-01-01', 1, '+84123456789', 'img/john.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       ('9c9f87a3-5179-11f0-b7ee-088fc33f56c7', 'user1@example.com', 'Jane Smith', 'Xyz@56789', '1985-11-20', 0,
        '2023-01-05', 1, '+84987654321', 'img/jane.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       ('9ca187f4-5179-11f0-b7ee-088fc33f56c7', 'user2@example.com', 'Peter Jones', 'Pqr!12345', '1992-03-25', 1,
        '2023-01-10', 1, '+84777888999', 'img/peter.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       ('9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', 'user3@example.com', 'Alice Brown', 'Def#67890', '1998-07-01', 0,
        '2023-01-15', 1, '+84333444555', 'img/alice.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21'),
       ('9ca59aff-5179-11f0-b7ee-088fc33f56c7', 'user4@example.com', 'Bob White', 'Ghi$12345', '1980-01-30', 1,
        '2023-01-20', 1, '+84666777888', 'img/bob.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       ('9ca766b5-5179-11f0-b7ee-088fc33f56c7', 'user5@example.com', 'Charlie Green', 'Jkl%98765', '1995-09-10', 1,
        '2023-01-25', 1, '+84111222333', 'img/charlie.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       ('9ca98304-5179-11f0-b7ee-088fc33f56c7', 'user6@example.com', 'Diana Ross', 'Mno^54321', '1987-04-05', 0,
        '2023-01-30', 1, '+84555666777', 'img/diana.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       ('9cab6d2b-5179-11f0-b7ee-088fc33f56c7', 'user7@example.com', 'Frank Adams', 'Pqr&87654', '1993-12-12', 1,
        '2023-02-01', 1, '+84999000111', 'img/frank.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       ('9cad56fd-5179-11f0-b7ee-088fc33f56c7', 'user8@example.com', 'Grace Lee', 'Stu*34567', '1991-06-28', 0,
        '2023-02-05', 1, '+84222333444', 'img/grace.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       ('9caf51fa-5179-11f0-b7ee-088fc33f56c7', 'user9@example.com', 'Henry King', 'Vwx(90123', '1982-08-08', 1,
        '2023-02-10', 1, '+84888999000', 'img/henry.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       ('b287bbd5-397a-11f0-84a1-088fc33f56c7', 'hoana5k44nknd@gmail.com', 'Jane Smith', 'StrongP@ss2', '1992-05-15', 1,
        '2023-01-02', 1, '0922222222', 'img/jane.jpg', 'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b28b4752-397a-11f0-84a1-088fc33f56c7', 'peter.jones@example.com', 'Peter Jones', 'MyP@ssw0rd3', '1988-11-30',
        0, '2023-01-03', 1, '0933333333', 'img/peter.jpg', 'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b2932a61-397a-11f0-84a1-088fc33f56c7', 'alice.white@example.com', 'Alice White', 'Passw0rd!4', '1995-03-22', 1,
        '2023-01-04', 1, '0944444444', 'img/alice.jpg', 'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b2971e45-397a-11f0-84a1-088fc33f56c7', 'bob.brown@example.com', 'Bob Brown', 'SecureP@ss5!', '1991-07-07', 0,
        '2023-01-05', 1, '0955555555', 'img/bob.jpg', 'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       -- Admin
       (UUID(), 'admin@gmail.com', 'Admin Account', 'QEEyMTM0NTY=', '1990-01-01', 1,
        CURDATE(), 1, '0911111111', 'img/default.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21'),
       -- Expert
       (UUID(), 'expert@gmail.com', 'Expert Account', 'QEEyMTM0NTY=', '1990-01-02', 1,
        CURDATE(), 1, '0922222222', 'img/default.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       -- Marketer
       ('b283bfb8-397a-11f0-84a1-088fc33f56c7', 'marketer@gmail.com', 'Marketer Account', 'QEEyMTM0NTY=', '1990-01-03',
        1,
        CURDATE(), 1, '0933333333', 'img/default.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a30'),
       -- Sale
       (UUID(), 'sale@gmail.com', 'Sale Account', 'QEEyMTM0NTY=', '1990-01-04', 1,
        CURDATE(), 1, '0944444444', 'img/default.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a31'),
       -- User
       (UUID(), 'user@gmail.com', 'User Account', 'QEEyMTM0NTY=', '1990-01-05', 1,
        CURDATE(), 1, '0955555555', 'img/default.jpg', 'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       -- Student
       (UUID(), 'student@gmail.com', 'Student Account', 'QEEyMTM0NTY=', '1990-01-06', 1,
        CURDATE(), 1, '0966666666', 'img/default.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23');
/*!40000 ALTER TABLE `account`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `blog`
DROP TABLE IF EXISTS `blog`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog`
(
    `id`           varchar(36)  NOT NULL,
    `updated_date` date                  DEFAULT NULL,
    `title`        varchar(255) NOT NULL,
    `brief_info`   varchar(255) NOT NULL,
    `content`      text,
    `status`       tinyint(1)   NOT NULL DEFAULT '1',
    `created_date` date         NOT NULL,
    `category`     varchar(36)  NOT NULL,
    `account_id`   varchar(36)  NOT NULL,
    `flag_feature` tinyint(1)            DEFAULT '1',
    `views`        int          NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FKBlog922018` (`account_id`),
    KEY `post categories` (`category`),
    CONSTRAINT `FKBlog922018` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `post categories` FOREIGN KEY (`category`) REFERENCES `setting` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `blog` WRITE;
/*!40000 ALTER TABLE `blog`
    DISABLE KEYS */;
INSERT INTO `blog`
VALUES ('b3d1a1b5-397a-11f0-84a1-088fc33f56c7', '2024-03-01',
        'The Future of Artificial Intelligence: Navigating a World Transformed by Smart Technologies',
        'Artificial Intelligence is no longer a futuristic conceptΓÇöit transforming how we live, work, and interact. Discover how AI will shape the next decade of global innovation.',
        'Artificial Intelligence (AI) is rapidly evolving from a supporting technology to a driving force behind transformative change across every sector of society. In this article, we examine the major trends shaping the future of AIΓÇöfrom the rise of generative models and neural networks to the ethical concerns surrounding privacy, bias, and transparency.\r\n		We explore real-world use cases such as AI-assisted healthcare diagnoses, autonomous transportation, and intelligent automation in manufacturing. Experts predict that AI will not only augment human capabilities but also create new roles and industries that were previously unimaginable.\r\n		However, the rapid development of AI technologies also raises critical questions. How do we ensure that these systems are transparent and accountable? What regulatory frameworks are needed to prevent misuse? How will AI affect job markets and economic inequality?\r\n		By examining both the promises and the risks, this article aims to provide a balanced view of how AI is shaping a smarter, faster, and more connected future.',
        1, '2024-02-20', 'b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 1, 2500),
       ('b3d1acf8-397a-11f0-84a1-088fc33f56c7', '2024-03-10',
        'Mastering Python in 30 Days: A Full Roadmap from Beginner to Confident Developer',
        'Want to learn Python fast but effectively? HereΓÇÖs a comprehensive 30-day plan with hands-on projects, learning milestones, and real-world applications.',
        'Python is one of the most beginner-friendly and versatile programming languages today. Whether you`re an aspiring software developer, data analyst, or automation enthusiast, Python has something to offer.\r\n		This 30-day learning plan is crafted to take you from zero experience to writing structured, functional, and object-oriented Python code. The plan is divided into five 6-day modules covering core concepts:\r\n\r\n		Week 1: Data types, variables, loops, and conditionals\r\n\r\n		Week 2: Functions, error handling, and file I/O\r\n\r\n		Week 3: Object-oriented programming and modules\r\n\r\n		Week 4: Working with APIs, data manipulation (pandas), and basic plotting (matplotlib)\r\n\r\n		Final Days: Building a mini project (to-do app, budget tracker, or a web scraper)\r\n		Each week includes coding exercises, reading materials, and small projects to reinforce learning. By the end of the 30 days, youΓÇÖll have a GitHub portfolio and the confidence to tackle Python-based projects or dive deeper into fields like web development, data science, or machine learning.',
        1, '2024-03-05', 'b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 0, 2600),
       ('b3d1b01b-397a-11f0-84a1-088fc33f56c7', '2024-03-15',
        'Quantum Physics Demystified: Making Sense of the UniverseΓÇÖs Most Puzzling Concepts',
        'Quantum physics often seems abstract and intimidating. This guide breaks down its core ideas in a simple, relatable wayΓÇöeven for those with no science background.',
        'Quantum physics challenges everything we think we know about how the universe works. It`s a realm where particles can be in multiple places at once, where events seem to occur without cause, and where observation can alter reality.\r\n		But despite its weirdness, quantum mechanics is the foundation of modern technologyΓÇöpowering everything from lasers and semiconductors to quantum computing and MRI machines.',
        1, '2024-03-12', 'b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7', 0, 4200),
       ('b3d1b1eb-397a-11f0-84a1-088fc33f56c7', '2024-03-20',
        'Web Development in 2024: Top Trends, Tools, and Techniques You Need to Know',
        'The web is evolving fast. Here are the most important web development trends, frameworks, and best practices that developers must embrace this year.',
        '2024 marks a turning point in the web development landscape, with modern users demanding faster, more secure, and more personalized experiences. Developers are rising to the challenge by adopting new frameworks, architectures, and AI-powered tools.',
        1, '2024-03-18', 'b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b2932a61-397a-11f0-84a1-088fc33f56c7', 0, 2800),
       ('b3d1b36e-397a-11f0-84a1-088fc33f56c7', '2024-03-25', 'Effective Study Habits',
        'Tips and tricks for better learning.', 'Strategies for effective studying...', 1, '2024-03-22',
        'b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b2971e45-397a-11f0-84a1-088fc33f56c7', 1, 5800),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a30', '2023-04-10', 'Home Gardening Guide',
        'Start your own vegetable garden.', 'Detailed steps for successful home gardening.', 1, '2023-04-01',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9ca59aff-5179-11f0-b7ee-088fc33f56c7', 0, 950),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a31', '2023-03-01', 'The Future of AI',
        'A brief overview of AI advancements.', 'Detailed content about AI and its future.', 1, '2023-02-20',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', 1, 1500),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a32', '2023-03-05', 'Healthy Eating Habits',
        'Tips for a healthier lifestyle.', 'Comprehensive guide to healthy eating.', 1, '2023-02-25',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7', 0, 2300),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', '2023-03-10', 'Introduction to Python',
        'Get started with Python programming.', 'Basics of Python programming for beginners.', 1, '2023-03-01',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', '9ca187f4-5179-11f0-b7ee-088fc33f56c7', 1, 800),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a34', '2023-03-15', 'Mindfulness for Beginners',
        'Simple practices to achieve mindfulness.', 'Daily exercises for mindfulness and stress reduction.', 1,
        '2023-03-05', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', 0, 1200),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a35', '2023-03-20', 'Web Development Trends',
        'Latest trends in web development.', 'Discussing modern web development technologies.', 1, '2023-03-10',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', 1, 1900),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a36', '2023-03-25', 'Benefits of Regular Exercise',
        'Why staying active is crucial.', 'Impact of exercise on physical and mental health.', 1, '2023-03-15',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9ca187f4-5179-11f0-b7ee-088fc33f56c7', 0, 2500),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a37', '2023-03-30', 'Understanding Blockchain',
        'A simple explanation of blockchain technology.', 'How blockchain works and its applications.', 1, '2023-03-20',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', '9ca59aff-5179-11f0-b7ee-088fc33f56c7', 0, 1100),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a38', '2023-04-01', 'Travel Tips for Solo Explorers',
        'Essential advice for traveling alone.', 'Safety and enjoyment tips for solo travelers.', 1, '2023-03-25',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', 1, 1700),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a39', '2023-04-05', 'Cybersecurity Best Practices',
        'Protecting your digital life.', 'Guidelines for maintaining online security.', 1, '2023-03-30',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', '9cab6d2b-5179-11f0-b7ee-088fc33f56c7', 0, 2100);
/*!40000 ALTER TABLE `blog`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `blogmedia`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blogmedia`
(
    `id`            varchar(36)            NOT NULL,
    `blog_id`       varchar(36)            NOT NULL,
    `media_type`    enum ('image','video') NOT NULL,
    `file_path`     varchar(1000)          NOT NULL,
    `caption`       text,
    `display_order` int DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FK_BlogMedia_Blog` (`blog_id`),
    CONSTRAINT `FK_BlogMedia_Blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `blogmedia` WRITE;
/*!40000 ALTER TABLE `blogmedia`
    DISABLE KEYS */;
INSERT INTO `blogmedia`
VALUES ('9ada8b90-5179-11f0-b7ee-088fc33f56c7', 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'image', 'img/ai_future_1.jpg',
        'Image cái này là gì', 1),
       ('9add06d8-5179-11f0-b7ee-088fc33f56c7', 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'image', 'img/ai_future_2.jpg',
        'cái này là gì', 2),
       ('9adf45d7-5179-11f0-b7ee-088fc33f56c7', 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'video',
        'img/ai_future_summary.mp4', 'cái này là gì', 3),
       ('9ae18913-5179-11f0-b7ee-088fc33f56c7', 'b3d1acf8-397a-11f0-84a1-088fc33f56c7', 'video', 'img/python_intro.mp4',
        'cái này là gì', 1),
       ('9ae3dfbf-5179-11f0-b7ee-088fc33f56c7', 'b3d1b01b-397a-11f0-84a1-088fc33f56c7', 'image',
        'img/quantum_model.jpg', 'cái này là gì', 1),
       ('9ae5fc05-5179-11f0-b7ee-088fc33f56c7', 'b3d1b01b-397a-11f0-84a1-088fc33f56c7', 'image',
        'img/quantum_result.jpg', 'cái này là gì', 2),
       ('9ae82d33-5179-11f0-b7ee-088fc33f56c7', 'b3d1acf8-397a-11f0-84a1-088fc33f56c7', 'image',
        'img/python_diagram.jpg', 'cái này là gì', 2),
       ('9aea7e40-5179-11f0-b7ee-088fc33f56c7', 'b3d1acf8-397a-11f0-84a1-088fc33f56c7', 'image',
        'img/python_usecases.jpg', 'cái này là gì', 3),
       ('9aed32f5-5179-11f0-b7ee-088fc33f56c7', 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'image', 'img/ai_vision.jpg',
        'cái này là gì', 4),
       ('9aef946d-5179-11f0-b7ee-088fc33f56c7', 'b3d1b01b-397a-11f0-84a1-088fc33f56c7', 'video',
        'img/quantum_explained.mp4', 'cái này là gì', 3);

/*!40000 ALTER TABLE `blogmedia`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `contact`
DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contact`
(
    `id`      varchar(36)  NOT NULL,
    `name`    varchar(255) NOT NULL,
    `link`    json         NOT NULL,
    `email`   varchar(255) NOT NULL,
    `phone`   varchar(11)  NOT NULL,
    `address` varchar(500) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`),
    CONSTRAINT `contact_chk_2` CHECK (regexp_like(`email`,
                                                  _utf8mb4'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')),
    CONSTRAINT `contact_chk_4` CHECK (regexp_like(`phone`, _utf8mb4'^\\+?[0-9]{10,15}$'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact`
    DISABLE KEYS */;
INSERT INTO `contact`
VALUES ('9b179f19-5179-11f0-b7ee-088fc33f56c7', 'Tech Solutions Inc.', '{
  \"website\": \"https://techsolutions.com\",
  \"linkedin\": \"https://linkedin.com/company/techsolutions\"
}', 'info@techsolutions.com', '0901112223', '123 Tech Avenue, Innovation City'),
       ('9b1a6f2d-5179-11f0-b7ee-088fc33f56c7', 'Global Education Ltd.', '{
         \"website\": \"https://globaledu.org\",
         \"facebook\": \"https://facebook.com/globaledu\"
       }', 'contact@globaledu.org', '0912223334', '456 Learning Street, Knowledge Town'),
       ('9b1d47f8-5179-11f0-b7ee-088fc33f56c7', 'Innovate Consulting', '{
         \"twitter\": \"https://twitter.com/innovatecon\",
         \"website\": \"https://innovateconsulting.net\"
       }', 'support@innovateconsulting.net', '0923334445', '789 Idea Road, Creative Hub'),
       ('9b1ffae9-5179-11f0-b7ee-088fc33f56c7', 'Health & Wellness Co.', '{
         \"website\": \"https://healthwellness.co\",
         \"instagram\": \"https://instagram.com/healthwellness\"
       }', 'care@healthwellness.co', '0934445556', '101 Health Lane, Vitality Village'),
       ('9b224efc-5179-11f0-b7ee-088fc33f56c7', 'Digital Marketing Pros', '{
         \"website\": \"https://digitalmarketing.com\",
         \"youtube\": \"https://youtube.com/digitalmarketing\"
       }', 'hello@digitalmarketing.com', '0945556667', '202 Digital Boulevard, Marketing City'),
       ('9b24cd1f-5179-11f0-b7ee-088fc33f56c7', 'Creative Design Studio', '{
         \"website\": \"https://creativedesign.studio\",
         \"pinterest\": \"https://pinterest.com/creativedesign\"
       }', 'design@creativedesign.studio', '0956667778', '303 Art Street, Design District'),
       ('9b2734ef-5179-11f0-b7ee-088fc33f56c7', 'Finance Experts LLC', '{
         \"website\": \"https://financeexperts.com\",
         \"linkedin\": \"https://linkedin.com/company/financeexperts\"
       }', 'advisory@financeexperts.com', '0967778889', '404 Wealth Plaza, Capital City'),
       ('9b29abd3-5179-11f0-b7ee-088fc33f56c7', 'Eco-Friendly Solutions', '{
         \"website\": \"https://ecosolutions.org\",
         \"facebook\": \"https://facebook.com/ecosolutions\"
       }', 'info@ecosolutions.org', '0978889990', '505 Green Avenue, Sustainability Town'),
       ('9b2c5688-5179-11f0-b7ee-088fc33f56c7', 'Event Planners Hub', '{
         \"website\": \"https://eventplanners.com\",
         \"instagram\": \"https://instagram.com/eventplanners\"
       }', 'plan@eventplanners.com', '0990001112', '707 Celebration Lane, Event Village'),
       ('9b2e9e4a-5179-11f0-b7ee-088fc33f56c7', 'Support Team A', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportA@example.com', '0901234567', '123 Support St, Hanoi'),
       ('9b30cc62-5179-11f0-b7ee-088fc33f56c7', 'Support Team B', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportB@example.com', '0902345678', '456 Service Rd, Ho Chi Minh'),
       ('9b32fbf0-5179-11f0-b7ee-088fc33f56c7', 'Support Team C', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportC@example.com', '0903456789', '789 Help Ave, Da Nang'),
       ('9b3520b7-5179-11f0-b7ee-088fc33f56c7', 'Support Team D', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportD@example.com', '0904567890', '101 Assistance Blv, Can Tho'),
       ('9b37472d-5179-11f0-b7ee-088fc33f56c7', 'Support Team E', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportE@example.com', '0905678901', '202 Query Lane, Hai Phong'),
       ('9b3996d9-5179-11f0-b7ee-088fc33f56c7', 'Legal Aid Services', '{
         \"twitter\": \"https://twitter.com/legalaid\",
         \"website\": \"https://legalaid.com\"
       }', 'consult@legalaid.com', '0989990001', '606 Justice Road, Law City');
/*!40000 ALTER TABLE `contact`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `course`
DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course`
(
    `id`                varchar(36)  NOT NULL,
    `title`             varchar(255) NOT NULL,
    `duration`          float        NOT NULL,
    `status`            tinyint(1)   NOT NULL DEFAULT '1',
    `description`       varchar(500) NOT NULL,
    `created_date`      date         NOT NULL,
    `updated_date`      date                  DEFAULT NULL,
    `thumbnail_url`     varchar(1000)         DEFAULT NULL,
    `number_of_lessons` int          NOT NULL,
    `topic_id`          varchar(36)  NOT NULL,
    `contact`           varchar(36)  NOT NULL,
    `expert_id`         varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKCourse6215` (`contact`),
    KEY `FKCourse968779` (`topic_id`),
    KEY `fk_course_expert` (`expert_id`),
    CONSTRAINT `fk_course_expert` FOREIGN KEY (`expert_id`) REFERENCES `account` (`id`),
    CONSTRAINT `FKCourse6215` FOREIGN KEY (`contact`) REFERENCES `contact` (`id`),
    CONSTRAINT `FKCourse968779` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course`
    DISABLE KEYS */;
INSERT INTO `course`
VALUES ('9ce8ab9c-5179-11f0-b7ee-088fc33f56c7', 'Advanced Algebra', 15.5, 1,
        'In-depth study of advanced algebraic concepts.', '2023-01-10', '2023-01-20', 'img/algebra_course.jpg', 20,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9c9d5b45-5179-11f0-b7ee-088fc33f56c7'),
       ('9cea8b0d-5179-11f0-b7ee-088fc33f56c7', 'Introduction to Data Structures', 20, 1,
        'Learn fundamental data structures and algorithms.', '2023-02-01', '2023-02-15',
        'img/datastructures_course.jpg', 25, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86',
        '9b179f19-5179-11f0-b7ee-088fc33f56c7', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7'),
       ('9cec66b1-5179-11f0-b7ee-088fc33f56c7', 'Classical Mechanics', 18, 1,
        'Explore the principles of classical mechanics.', '2023-03-01', '2023-03-10', 'img/mechanics_course.jpg', 22,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9ca187f4-5179-11f0-b7ee-088fc33f56c7'),
       ('9cee332c-5179-11f0-b7ee-088fc33f56c7', 'Organic Chemistry Basics', 12, 1,
        'Foundational knowledge of organic chemistry.', '2023-04-01', '2023-04-05', 'img/organicchem_course.jpg', 18,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7'),
       ('9cf0366e-5179-11f0-b7ee-088fc33f56c7', 'Calculus I', 25, 1,
        'First course in differential and integral calculus.', '2023-05-01', '2023-05-10', 'img/calculus_course.jpg',
        30, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9ca59aff-5179-11f0-b7ee-088fc33f56c7'),
       ('9cf27a21-5179-11f0-b7ee-088fc33f56c7', 'Genetics and Evolution', 17.5, 1,
        'Study of heredity and evolutionary processes.', '2023-06-01', '2023-06-05', 'img/genetics_course.jpg', 20,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9ca766b5-5179-11f0-b7ee-088fc33f56c7'),
       ('9cf47b58-5179-11f0-b7ee-088fc33f56c7', 'Microeconomics Principles', 14, 1,
        'Introduction to economic principles at a micro level.', '2023-07-01', '2023-07-05',
        'img/microeconomics_course.jpg', 15, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80',
        '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7', '9ca98304-5179-11f0-b7ee-088fc33f56c7'),
       ('9cf6ccb3-5179-11f0-b7ee-088fc33f56c7', 'World History: Ancient Civilizations', 22, 1,
        'Comprehensive look at ancient world history.', '2023-08-01', '2023-08-10', 'img/worldhistory_course.jpg', 28,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9ca187f4-5179-11f0-b7ee-088fc33f56c7'),
       ('9cf91818-5179-11f0-b7ee-088fc33f56c7', 'Introduction to Thermodynamics', 16, 1,
        'Basic concepts and laws of thermodynamics.', '2023-09-01', '2023-09-05', 'img/thermo_course.jpg', 19,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9cad56fd-5179-11f0-b7ee-088fc33f56c7'),
       ('9cfb5ff5-5179-11f0-b7ee-088fc33f56c7', 'Poetry Analysis', 10, 1,
        'Techniques for analyzing and understanding poetry.', '2023-10-01', '2023-10-05', 'img/poetry_course.jpg', 12,
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '9caf51fa-5179-11f0-b7ee-088fc33f56c7'),
       ('b2b861ab-397a-11f0-84a1-088fc33f56c7', 'Introduction to Algebra', 10.5, 1, 'Covers basic algebraic concepts.',
        '2023-03-01', '2023-03-10', 'img/algebra_course.jpg', 15, 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        '9b2e9e4a-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7'),
       ('b2bffc19-397a-11f0-84a1-088fc33f56c7', 'Advanced Data Structures', 20, 1,
        'Deep dive into complex data structures.', '2023-04-01', '2023-04-15', 'img/ds_course.jpg', 25,
        'b24459d6-397a-11f0-84a1-088fc33f56c7', '9b30cc62-5179-11f0-b7ee-088fc33f56c7',
        'b287bbd5-397a-11f0-84a1-088fc33f56c7'),
       ('b2c30ab5-397a-11f0-84a1-088fc33f56c7', 'Classical Mechanics', 15, 1, 'Fundamental principles of mechanics.',
        '2023-05-01', '2023-05-10', 'img/mechanics_course.jpg', 20, 'b249f6b8-397a-11f0-84a1-088fc33f56c7',
        '9b32fbf0-5179-11f0-b7ee-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7'),
       ('b2c629c9-397a-11f0-84a1-088fc33f56c7', 'Organic Chemistry I', 18, 1, 'Introduction to organic compounds.',
        '2023-06-01', '2023-06-12', 'img/orgchem_course.jpg', 22, 'b253366f-397a-11f0-84a1-088fc33f56c7',
        '9b3520b7-5179-11f0-b7ee-088fc33f56c7', 'b2932a61-397a-11f0-84a1-088fc33f56c7'),
       ('b2cea174-397a-11f0-84a1-088fc33f56c7', 'Basic Genetics', 12, 1, 'Understanding heredity and genes.',
        '2023-07-01', '2023-07-08', 'img/genetics_course.jpg', 18, 'b25cd42b-397a-11f0-84a1-088fc33f56c7',
        '9b37472d-5179-11f0-b7ee-088fc33f56c7', 'b2971e45-397a-11f0-84a1-088fc33f56c7'),
       ('b2cea175-397a-11f0-84a1-088fc33f56c7', 'Data Architecture And Algorithms ', 60, 1,
        'Understanding about Data architecture and Algorithms', '2023-07-01', '2023-07-08', 'img/genetics_course.jpg',
        18, 'b25cd42b-397a-11f0-84a1-088fc33f56c7', '9b37472d-5179-11f0-b7ee-088fc33f56c7',
        'b2971e45-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `course`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `learningmaterial`
DROP TABLE IF EXISTS `learningmaterial`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learningmaterial`
(
    `id`                varchar(36)   NOT NULL,
    `title`             varchar(255)  NOT NULL,
    `updated_date`      date          DEFAULT NULL,
    `duration`          int           NOT NULL,
    `video_content_url` varchar(1000) DEFAULT NULL,
    `html_content`      varchar(1000) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FKLearningMa384166` FOREIGN KEY (`id`) REFERENCES `lesson` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `learningmaterial` WRITE;
/*!40000 ALTER TABLE `learningmaterial`
    DISABLE KEYS */;
INSERT INTO `learningmaterial`
VALUES ('9d015f05-5179-11f0-b7ee-088fc33f56c7', 'Video: Introduction to Advanced Algebra', '2023-01-22', 600,
        'http://example.com/video1.mp4', 'HTML content for video 1'),
       ('9d038003-5179-11f0-b7ee-088fc33f56c7', 'Text: Linear Equations Explained', '2023-01-23', 300, NULL,
        'HTML content for text 1 on linear equations.'),
       ('9d076edb-5179-11f0-b7ee-088fc33f56c7', 'Video: Understanding Arrays', '2023-02-17', 720,
        'http://example.com/video2.mp4', 'HTML content for video 2'),
       ('9d099fde-5179-11f0-b7ee-088fc33f56c7', 'Text: Stacks and Queues Deep Dive', '2023-02-18', 400, NULL,
        'HTML content for text 2 on stacks and queues.'),
       ('9d0bf29d-5179-11f0-b7ee-088fc33f56c7', 'Video: Dynamics and Newton\'s Laws', '2023-03-12', 900,
        'http://example.com/video3.mp4', 'HTML content for video 3'),
       ('9d0e129c-5179-11f0-b7ee-088fc33f56c7', 'Text: Energy Conservation', '2023-03-13', 350, NULL,
        'HTML content for text 3 on energy conservation.'),
       ('9d104608-5179-11f0-b7ee-088fc33f56c7', 'Video: All About Hydrocarbons', '2023-04-07', 650,
        'http://example.com/video4.mp4', 'HTML content for video 4'),
       ('9d122c9f-5179-11f0-b7ee-088fc33f56c7', 'Text: Exploring Functional Groups', '2023-04-08', 380, NULL,
        'HTML content for text 4 on functional groups.'),
       ('9d13f686-5179-11f0-b7ee-088fc33f56c7', 'Video: Limits and Their Properties', '2023-05-12', 800,
        'http://example.com/video5.mp4', 'HTML content for video 5'),
       ('9d18a41a-5179-11f0-b7ee-088fc33f56c7', 'Text: Rules of Differentiation', '2023-05-13', 420, NULL,
        'HTML content for text 5 on derivatives.'),
       ('b2de3b10-397a-11f0-84a1-088fc33f56c7', 'Algebra Basics Video', '2024-01-20', 60,
        '[http://example.com/algebra_video.mp4](http://example.com/algebra_video.mp4)',
        '<h1>Algebra Basics</h1><p>This lesson covers the fundamentals of algebra.</p>'),
       ('b2e2cb06-397a-11f0-84a1-088fc33f56c7', 'Quiz Guide: Algebra', '2024-01-24', 10, NULL,
        '<h1>Quiz Instructions</h1><p>Read questions carefully before answering.</p>'),
       ('b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 'Linked List Tutorial', '2024-02-15', 90,
        '[http://example.com/linkedlist_video.mp4](http://example.com/linkedlist_video.mp4)',
        '<h1>Linked Lists</h1><p>An in-depth look at linked lists.</p>'),
       ('b2ef4be4-397a-11f0-84a1-088fc33f56c7', 'Quiz Prep: Data Structures', '2024-01-30', 15, NULL,
        '<h1>Data Structures Quiz Prep</h1><p>Review linked lists and arrays.</p>'),
       ('b2f36d41-397a-11f0-84a1-088fc33f56c7', 'Newton\'s Laws Lecture', '2024-03-05', 75,
        '[http://example.com/newton_video.mp4](http://example.com/newton_video.mp4)',
        '<h1>Newtonian Mechanics</h1><p>Lecture on Newton\'s three laws.</p>');
/*!40000 ALTER TABLE `learningmaterial`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `lesson`
DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson`
(
    `id`             varchar(36)  NOT NULL,
    `status`         tinyint(1)   NOT NULL DEFAULT '1',
    `name`           varchar(255) NOT NULL,
    `course_id`      varchar(36)  NOT NULL,
    `lesson_type_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKLesson764776` (`course_id`),
    KEY `lesson types` (`lesson_type_id`),
    CONSTRAINT `FKLesson764776` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
    CONSTRAINT `lesson types` FOREIGN KEY (`lesson_type_id`) REFERENCES `setting` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson`
    DISABLE KEYS */;
INSERT INTO `lesson`
VALUES ('9d015f05-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 1: Introduction to Advanced Algebra',
        '9ce8ab9c-5179-11f0-b7ee-088fc33f56c7', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       ('9d038003-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 2: Linear Equations', '9ce8ab9c-5179-11f0-b7ee-088fc33f56c7',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       ('9d076edb-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 1: Arrays and Linked Lists',
        '9cea8b0d-5179-11f0-b7ee-088fc33f56c7', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       ('9d099fde-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 2: Stacks and Queues',
        '9cea8b0d-5179-11f0-b7ee-088fc33f56c7', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       ('9d0bf29d-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 1: Newton\'s Laws', '9cec66b1-5179-11f0-b7ee-088fc33f56c7',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       ('9d0e129c-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 2: Work, Energy, and Power',
        '9cec66b1-5179-11f0-b7ee-088fc33f56c7', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       ('9d104608-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 1: Hydrocarbons', '9cee332c-5179-11f0-b7ee-088fc33f56c7',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       ('9d122c9f-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 2: Functional Groups',
        '9cee332c-5179-11f0-b7ee-088fc33f56c7', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       ('9d13f686-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 1: Limits and Continuity',
        '9cf0366e-5179-11f0-b7ee-088fc33f56c7', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       ('9d18a41a-5179-11f0-b7ee-088fc33f56c7', 1, 'Lesson 2: Derivatives', '9cf0366e-5179-11f0-b7ee-088fc33f56c7',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       ('b2de3b10-397a-11f0-84a1-088fc33f56c7', 1, 'Algebra Fundamentals', 'b2b861ab-397a-11f0-84a1-088fc33f56c7',
        'b1a7d752-397a-11f0-84a1-088fc33f56c7'),
       ('b2e2cb06-397a-11f0-84a1-088fc33f56c7', 1, 'Quiz: Basic Algebra', 'b2b861ab-397a-11f0-84a1-088fc33f56c7',
        'b1abcc32-397a-11f0-84a1-088fc33f56c7'),
       ('b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 1, 'Linked Lists', 'b2bffc19-397a-11f0-84a1-088fc33f56c7',
        'b1a7d752-397a-11f0-84a1-088fc33f56c7'),
       ('b2ef4be4-397a-11f0-84a1-088fc33f56c7', 1, 'Quiz: Data Structures I', 'b2bffc19-397a-11f0-84a1-088fc33f56c7',
        'b1abcc32-397a-11f0-84a1-088fc33f56c7'),
       ('b2f36d41-397a-11f0-84a1-088fc33f56c7', 1, 'Newton\'s Laws', 'b2c30ab5-397a-11f0-84a1-088fc33f56c7',
        'b1a7d752-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `lesson`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `option`
DROP TABLE IF EXISTS `option`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `option`
(
    `id`          varchar(36)  NOT NULL,
    `content`     varchar(255) NOT NULL,
    `is_true`     tinyint(1)   NOT NULL DEFAULT '0',
    `explanation` varchar(255) NOT NULL,
    `question_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKOption204933` (`question_id`),
    CONSTRAINT `FKOption204933` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `option` WRITE;
/*!40000 ALTER TABLE `option`
    DISABLE KEYS */;
INSERT INTO `option`
VALUES ('b347e001-397a-11f0-84a1-088fc33f56c7', '4', 1, 'Correct arithmetic operation.',
        'b3240dfd-397a-11f0-84a1-088fc33f56c7'),
       ('b34f104c-397a-11f0-84a1-088fc33f56c7', '5', 0, 'Incorrect.', 'b3240dfd-397a-11f0-84a1-088fc33f56c7'),
       ('b3544f73-397a-11f0-84a1-088fc33f56c7',
        'A linear data structure where elements are not stored at contiguous memory locations.', 1,
        'Correct definition of a linked list.', 'b329e268-397a-11f0-84a1-088fc33f56c7'),
       ('b35b5770-397a-11f0-84a1-088fc33f56c7',
        'An object at rest stays at rest and an object in motion stays in motion with the same speed and in the same direction unless acted upon by an unbalanced force.',
        1, 'Correct statement of Newton\'s first law.', 'b32fda1f-397a-11f0-84a1-088fc33f56c7'),
       ('b35f143b-397a-11f0-84a1-088fc33f56c7', 'x = 5', 1, 'Solving the linear equation.',
        'b3348944-397a-11f0-84a1-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'x = 5', 1, '2x + 5 = 15 => 2x = 10 => x = 5',
        '9d958cdc-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'x = 10', 0, 'Incorrect calculation',
        '9d97ffe2-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63', 'Dynamic size, efficient insertions/deletions', 1,
        'Linked lists can grow or shrink in size and are efficient for insertions/deletions.',
        '9d9a41c2-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64', 'Fixed size, random access', 0,
        'This describes arrays, not linked lists.', '9d9d4dd6-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a65', 'For every action, there is an equal and opposite reaction.', 1,
        'This is the correct statement of Newton\'s Third Law.', '9da059c8-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66', 'Force equals mass times acceleration.', 0,
        'This is Newton\'s Second Law.', '9da2c9ad-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a67', 'CH4', 1,
        'Methane is a simple hydrocarbon with one carbon and four hydrogen atoms.',
        '9da534ac-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a68', 'C6H12O6', 0, 'This is the chemical formula for glucose.',
        '9da78ab3-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a69', '2x + 3', 1,
        'Using the power rule and constant multiple rule, the derivative is 2x + 3.',
        '9daa76e1-5179-11f0-b7ee-088fc33f56c7'),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70', 'x^3/3 + 3x^2/2', 0, 'This is the integral, not the derivative.',
        '9dace039-5179-11f0-b7ee-088fc33f56c7');
/*!40000 ALTER TABLE `option`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `personalcourse`
DROP TABLE IF EXISTS `personalcourse`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personalcourse`
(
    `id`          varchar(36) NOT NULL,
    `account_id`  varchar(36) NOT NULL,
    `course_id`   varchar(36) NOT NULL,
    `expire_date` date                 DEFAULT NULL,
    `enroll_date` date        NOT NULL,
    `progress`    int         NOT NULL DEFAULT '0',
    `status`      varchar(255)         DEFAULT 'SENT',
    PRIMARY KEY (`id`),
    KEY `FKPersonalCo602046` (`course_id`),
    KEY `FKPersonalCo728981` (`account_id`),
    CONSTRAINT `FKPersonalCo602046` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
    CONSTRAINT `FKPersonalCo728981` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `personalcourse_chk_1` CHECK ((`status` in
                                              (_utf8mb4'PAID', _utf8mb4'SENT', _utf8mb4'ACCEPTED', _utf8mb4'DECLINED')))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `personalcourse` WRITE;
/*!40000 ALTER TABLE `personalcourse`
    DISABLE KEYS */;
INSERT INTO `personalcourse`
VALUES ('9befe344-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2b861ab-397a-11f0-84a1-088fc33f56c7', '2024-06-01', '2023-01-10', 50, 'PAID'),
       ('9befe9c3-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2bffc19-397a-11f0-84a1-088fc33f56c7', '2024-06-01', '2023-01-10', 50, 'PAID'),
       ('9befeb2a-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2c30ab5-397a-11f0-84a1-088fc33f56c7', '2024-10-01', '2023-02-15', 20, 'SENT'),
       ('9befec1a-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2c629c9-397a-11f0-84a1-088fc33f56c7', '2024-08-01', '2023-03-20', 80, 'ACCEPTED'),
       ('9befed04-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2cea174-397a-11f0-84a1-088fc33f56c7', '2024-12-01', '2023-04-25', 10, NULL),
       ('9befedf0-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2cea175-397a-11f0-84a1-088fc33f56c7', '2024-09-01', '2023-05-01', 0, NULL),
       ('9befeec9-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b2bffc19-397a-11f0-84a1-088fc33f56c7', '2024-10-01', '2023-02-15', 20, 'SENT'),
       ('9befefbd-5179-11f0-b7ee-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7',
        'b2c30ab5-397a-11f0-84a1-088fc33f56c7', '2024-08-01', '2023-03-20', 80, 'ACCEPTED'),
       ('9beff178-5179-11f0-b7ee-088fc33f56c7', 'b2932a61-397a-11f0-84a1-088fc33f56c7',
        'b2c629c9-397a-11f0-84a1-088fc33f56c7', '2024-12-01', '2023-04-25', 10, NULL),
       ('9beff28a-5179-11f0-b7ee-088fc33f56c7', 'b2971e45-397a-11f0-84a1-088fc33f56c7',
        'b2c629c9-397a-11f0-84a1-088fc33f56c7', '2024-10-01', '2023-02-15', 20, 'SENT'),
       ('9beff3b4-5179-11f0-b7ee-088fc33f56c7', 'b2971e45-397a-11f0-84a1-088fc33f56c7',
        'b2cea174-397a-11f0-84a1-088fc33f56c7', '2024-09-01', '2023-05-01', 0, NULL),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9cfb5ff5-5179-11f0-b7ee-088fc33f56c7', '2024-10-01', '2024-04-01', 65, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a71', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9ce8ab9c-5179-11f0-b7ee-088fc33f56c7', '2024-01-01', '2023-07-01', 50, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a72', '9cad56fd-5179-11f0-b7ee-088fc33f56c7',
        '9cea8b0d-5179-11f0-b7ee-088fc33f56c7', '2024-02-01', '2023-08-01', 75, 'ACCEPTED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a73', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7',
        '9cec66b1-5179-11f0-b7ee-088fc33f56c7', '2024-03-01', '2023-09-01', 30, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a74', '9ca187f4-5179-11f0-b7ee-088fc33f56c7',
        '9cee332c-5179-11f0-b7ee-088fc33f56c7', '2024-04-01', '2023-10-01', 90, 'ACCEPTED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a75', '9ca59aff-5179-11f0-b7ee-088fc33f56c7',
        '9cf0366e-5179-11f0-b7ee-088fc33f56c7', '2024-05-01', '2023-11-01', 10, 'SENT'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a76', '9caf51fa-5179-11f0-b7ee-088fc33f56c7',
        '9cf27a21-5179-11f0-b7ee-088fc33f56c7', '2024-06-01', '2023-12-01', 60, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a77', '9ca98304-5179-11f0-b7ee-088fc33f56c7',
        '9cf47b58-5179-11f0-b7ee-088fc33f56c7', '2024-07-01', '2024-01-01', 20, 'DECLINED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a78', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9cf6ccb3-5179-11f0-b7ee-088fc33f56c7', '2024-08-01', '2024-02-01', 80, 'ACCEPTED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a79', '9cab6d2b-5179-11f0-b7ee-088fc33f56c7',
        '9cf91818-5179-11f0-b7ee-088fc33f56c7', '2024-09-01', '2024-03-01', 45, 'PAID');
/*!40000 ALTER TABLE `personalcourse`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `personallesson`
DROP TABLE IF EXISTS `personallesson`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personallesson`
(
    `id`         varchar(36) NOT NULL,
    `account_id` varchar(36) NOT NULL,
    `lesson_id`  varchar(36) NOT NULL,
    `isDone`     tinyint(1)  NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FKPersonalLe846456` (`lesson_id`),
    KEY `FKPersonalLe96067` (`account_id`),
    CONSTRAINT `FKPersonalLe846456` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`id`),
    CONSTRAINT `FKPersonalLe96067` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `personallesson` WRITE;
/*!40000 ALTER TABLE `personallesson`
    DISABLE KEYS */;
INSERT INTO `personallesson`
VALUES ('9bfe560b-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2de3b10-397a-11f0-84a1-088fc33f56c7', 1),
       ('9bfe5afc-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2e2cb06-397a-11f0-84a1-088fc33f56c7', 0),
       ('9bfe5c41-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 1),
       ('9bfe5d18-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 0),
       ('9bfe5de8-5179-11f0-b7ee-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7',
        'b2f36d41-397a-11f0-84a1-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80', '9ca766b5-5179-11f0-b7ee-088fc33f56c7',
        '9d0bf29d-5179-11f0-b7ee-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9d18a41a-5179-11f0-b7ee-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', '9ca98304-5179-11f0-b7ee-088fc33f56c7',
        '9d015f05-5179-11f0-b7ee-088fc33f56c7', 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7',
        '9d038003-5179-11f0-b7ee-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', '9ca187f4-5179-11f0-b7ee-088fc33f56c7',
        '9d099fde-5179-11f0-b7ee-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7',
        '9d0e129c-5179-11f0-b7ee-088fc33f56c7', 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9d104608-5179-11f0-b7ee-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9d104608-5179-11f0-b7ee-088fc33f56c7', 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', '9ca59aff-5179-11f0-b7ee-088fc33f56c7',
        '9d099fde-5179-11f0-b7ee-088fc33f56c7', 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7',
        '9d13f686-5179-11f0-b7ee-088fc33f56c7', 0);
/*!40000 ALTER TABLE `personallesson`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `personaloption`
DROP TABLE IF EXISTS `personaloption`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personaloption`
(
    `id`         varchar(36) NOT NULL,
    `option_id`  varchar(36) NOT NULL,
    `account_id` varchar(36) NOT NULL,
    `has_chosen` tinyint(1)  NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FKPersonalOp32617` (`account_id`),
    KEY `FKPersonalOp130168` (`option_id`),
    CONSTRAINT `FKPersonalOp130168` FOREIGN KEY (`option_id`) REFERENCES `option` (`id`),
    CONSTRAINT `FKPersonalOp32617` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `personaloption` WRITE;
/*!40000 ALTER TABLE `personaloption`
    DISABLE KEYS */;
INSERT INTO `personaloption`
VALUES ('9c1226df-5179-11f0-b7ee-088fc33f56c7', 'b347e001-397a-11f0-84a1-088fc33f56c7',
        'b283bfb8-397a-11f0-84a1-088fc33f56c7', 1),
       ('9c122b4a-5179-11f0-b7ee-088fc33f56c7', 'b34f104c-397a-11f0-84a1-088fc33f56c7',
        'b283bfb8-397a-11f0-84a1-088fc33f56c7', 0),
       ('9c122cf2-5179-11f0-b7ee-088fc33f56c7', 'b3544f73-397a-11f0-84a1-088fc33f56c7',
        'b287bbd5-397a-11f0-84a1-088fc33f56c7', 1),
       ('9c122e94-5179-11f0-b7ee-088fc33f56c7', 'b35b5770-397a-11f0-84a1-088fc33f56c7',
        'b28b4752-397a-11f0-84a1-088fc33f56c7', 1),
       ('9c122fc8-5179-11f0-b7ee-088fc33f56c7', 'b35f143b-397a-11f0-84a1-088fc33f56c7',
        'b287bbd5-397a-11f0-84a1-088fc33f56c7', 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a90', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70',
        '9c9d5b45-5179-11f0-b7ee-088fc33f56c7', 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a91', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61',
        '9c9d5b45-5179-11f0-b7ee-088fc33f56c7', 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a92', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62',
        '9ca187f4-5179-11f0-b7ee-088fc33f56c7', 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a93', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63',
        '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a94', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64',
        '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a95', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a65',
        '9ca59aff-5179-11f0-b7ee-088fc33f56c7', 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a96', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66',
        '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a97', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a67',
        '9ca766b5-5179-11f0-b7ee-088fc33f56c7', 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a98', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a68',
        '9ca187f4-5179-11f0-b7ee-088fc33f56c7', 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a99', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a69',
        '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', 1);
/*!40000 ALTER TABLE `personaloption`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `personalquestion`
DROP TABLE IF EXISTS `personalquestion`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personalquestion`
(
    `id`          varchar(36) NOT NULL,
    `account_id`  varchar(36) NOT NULL,
    `question_id` varchar(36) NOT NULL,
    `has_answer`  tinyint(1)  NOT NULL DEFAULT '0',
    `is_mark`     tinyint(1)  NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FKPersonalQu428012` (`question_id`),
    KEY `FKPersonalQu214880` (`account_id`),
    CONSTRAINT `FKPersonalQu214880` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `FKPersonalQu428012` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `personalquestion` WRITE;
/*!40000 ALTER TABLE `personalquestion`
    DISABLE KEYS */;
INSERT INTO `personalquestion`
VALUES ('9c20e727-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b3240dfd-397a-11f0-84a1-088fc33f56c7', 1, 0),
       ('9c20eb5f-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b3240dfd-397a-11f0-84a1-088fc33f56c7', 0, 1),
       ('9c20ed03-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b329e268-397a-11f0-84a1-088fc33f56c7', 1, 0),
       ('9c20eec5-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b329e268-397a-11f0-84a1-088fc33f56c7', 1, 0),
       ('9c20f021-5179-11f0-b7ee-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7',
        'b3348944-397a-11f0-84a1-088fc33f56c7', 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a00', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9dace039-5179-11f0-b7ee-088fc33f56c7', 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', '9ca187f4-5179-11f0-b7ee-088fc33f56c7',
        '9d958cdc-5179-11f0-b7ee-088fc33f56c7', 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', '9ca98304-5179-11f0-b7ee-088fc33f56c7',
        '9d97ffe2-5179-11f0-b7ee-088fc33f56c7', 1, 1),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9d9a41c2-5179-11f0-b7ee-088fc33f56c7', 0, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7',
        '9d9d4dd6-5179-11f0-b7ee-088fc33f56c7', 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', '9ca766b5-5179-11f0-b7ee-088fc33f56c7',
        '9da059c8-5179-11f0-b7ee-088fc33f56c7', 1, 1),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a06', '9ca59aff-5179-11f0-b7ee-088fc33f56c7',
        '9da2c9ad-5179-11f0-b7ee-088fc33f56c7', 0, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a07', '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7',
        '9da534ac-5179-11f0-b7ee-088fc33f56c7', 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a08', '9ca187f4-5179-11f0-b7ee-088fc33f56c7',
        '9da78ab3-5179-11f0-b7ee-088fc33f56c7', 1, 1),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a09', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7',
        '9daa76e1-5179-11f0-b7ee-088fc33f56c7', 0, 0);
/*!40000 ALTER TABLE `personalquestion`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `personalquiz`
DROP TABLE IF EXISTS `personalquiz`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personalquiz`
(
    `id`                         varchar(36) NOT NULL,
    `account_id`                 varchar(36) NOT NULL,
    `quiz_id`                    varchar(36) NOT NULL,
    `has_passed`                 tinyint(1)  NOT NULL DEFAULT '0',
    `mark`                       int                  DEFAULT NULL,
    `number_of_correct_question` int                  DEFAULT NULL,
    `taken_date`                 date        NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKPersonalQu344927` (`account_id`),
    KEY `FKPersonalQu344926` (`quiz_id`),
    CONSTRAINT `FKPersonalQu344926` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`),
    CONSTRAINT `FKPersonalQu344927` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `personalquiz` WRITE;
/*!40000 ALTER TABLE `personalquiz`
    DISABLE KEYS */;
INSERT INTO `personalquiz`
VALUES ('9c2fdc8d-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2de3b10-397a-11f0-84a1-088fc33f56c7', 0, 6, 6, '2024-03-01'),
       ('9c2fe15a-5179-11f0-b7ee-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b2de3b10-397a-11f0-84a1-088fc33f56c7', 0, 5, 5, '2024-05-10'),
       ('9c2fe312-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b2f36d41-397a-11f0-84a1-088fc33f56c7', 0, 7, 7, '2024-05-15'),
       ('9c2fe407-5179-11f0-b7ee-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b2f36d41-397a-11f0-84a1-088fc33f56c7', 0, 8, 8, '2024-04-01'),
       ('9c2fe4e4-5179-11f0-b7ee-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7',
        'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 1, 18, 18, '2024-05-01'),
       ('9dc2eb6e-5179-11f0-b7ee-088fc33f56c7', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9d77f3c3-5179-11f0-b7ee-088fc33f56c7', 1, 80, 8, '2023-06-01'),
       ('9dd09031-5179-11f0-b7ee-088fc33f56c7', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7',
        '9d7a2512-5179-11f0-b7ee-088fc33f56c7', 1, 90, 14, '2023-06-05'),
       ('9dd8b412-5179-11f0-b7ee-088fc33f56c7', '9ca766b5-5179-11f0-b7ee-088fc33f56c7',
        '9d7d4b1f-5179-11f0-b7ee-088fc33f56c7', 0, 70, 13, '2023-06-07'),
       ('9ddaf7c7-5179-11f0-b7ee-088fc33f56c7', '9ca98304-5179-11f0-b7ee-088fc33f56c7',
        '9d7fc099-5179-11f0-b7ee-088fc33f56c7', 1, 75, 6, '2023-06-10'),
       ('9ddcee43-5179-11f0-b7ee-088fc33f56c7', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7',
        '9d8201b8-5179-11f0-b7ee-088fc33f56c7', 0, 60, 10, '2023-06-12'),
       ('9ddef715-5179-11f0-b7ee-088fc33f56c7', '9ca187f4-5179-11f0-b7ee-088fc33f56c7',
        '9d83c31e-5179-11f0-b7ee-088fc33f56c7', 1, 85, 10, '2023-06-15'),
       ('9de178e8-5179-11f0-b7ee-088fc33f56c7', '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7',
        '9d85eea8-5179-11f0-b7ee-088fc33f56c7', 0, 55, 5, '2023-06-18'),
       ('9de3ad4c-5179-11f0-b7ee-088fc33f56c7', '9cad56fd-5179-11f0-b7ee-088fc33f56c7',
        '9d8878c0-5179-11f0-b7ee-088fc33f56c7', 1, 95, 24, '2023-06-20'),
       ('9de595ec-5179-11f0-b7ee-088fc33f56c7', '9ca98304-5179-11f0-b7ee-088fc33f56c7',
        '9d8ace0b-5179-11f0-b7ee-088fc33f56c7', 0, 68, 10, '2023-06-22'),
       ('9de7ac50-5179-11f0-b7ee-088fc33f56c7', '9caf51fa-5179-11f0-b7ee-088fc33f56c7',
        '9d8d1750-5179-11f0-b7ee-088fc33f56c7', 1, 70, 7, '2023-06-25');
/*!40000 ALTER TABLE `personalquiz`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `pricepackage`
DROP TABLE IF EXISTS `pricepackage`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pricepackage`
(
    `id`              varchar(36)  NOT NULL,
    `course_id`       varchar(36)  NOT NULL,
    `title`           varchar(255) NOT NULL,
    `price`           int          NOT NULL,
    `sale_price`      int          NOT NULL CHECK ( `sale_price` <= 100 ),
    `access_duration` int          NOT NULL,
    `status`          tinyint(1)   NOT NULL DEFAULT '0',
    `description`     varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`, `course_id`),
    KEY `FKPricePacka152688` (`course_id`),
    CONSTRAINT `FKPricePacka152688` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `pricepackage` WRITE;
/*!40000 ALTER TABLE `pricepackage`
    DISABLE KEYS */;
INSERT INTO `pricepackage`
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', '9cfb5ff5-5179-11f0-b7ee-088fc33f56c7', 'Poetry Enthusiast', 2300000,
        20, 60, 1, '60 days access to Poetry Analysis course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', '9ce8ab9c-5179-11f0-b7ee-088fc33f56c7', 'Standard Access - Algebra',
        5500000, 10, 90, 1, '90 days access to Advanced Algebra course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', '9cea8b0d-5179-11f0-b7ee-088fc33f56c7', 'Premium Data Structures',
        3200000, 30, 180, 1, '180 days access with premium support for Data Structures.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', '9cec66b1-5179-11f0-b7ee-088fc33f56c7', 'Mechanics Basic', 1600000,
        40, 60, 1, '60 days access to Classical Mechanics course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', '9cee332c-5179-11f0-b7ee-088fc33f56c7', 'Organic Chemistry Trial',
        1100000, 50, 30, 0, '30 days trial access to Organic Chemistry Basics.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9cf0366e-5179-11f0-b7ee-088fc33f56c7', 'Calculus Full Access', 4800000,
        70, 365, 1, 'Full year access to Calculus I course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', '9cf27a21-5179-11f0-b7ee-088fc33f56c7', 'Genetics Gold', 2800000,
        15, 120, 1, '120 days access to Genetics and Evolution with extra resources.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', '9cf47b58-5179-11f0-b7ee-088fc33f56c7', 'Microeconomics Standard',
        1899000, 35, 90, 1, '90 days access to Microeconomics Principles.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', '9cf6ccb3-5179-11f0-b7ee-088fc33f56c7', 'History Pro', 4000000, 10,
        240, 1, '240 days access to World History: Ancient Civilizations.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9cf91818-5179-11f0-b7ee-088fc33f56c7', 'Thermodynamics Basic', 2000000,
        80, 75, 1, '75 days access to Introduction to Thermodynamics.'),
       ('b2d700aa-397a-11f0-84a1-088fc33f56c7', 'b2b861ab-397a-11f0-84a1-088fc33f56c7', 'Basic Access Algebra', 500000,
        25, 90, 1, '90 days access to Algebra course'),
       ('b2d70810-397a-11f0-84a1-088fc33f56c7', 'b2b861ab-397a-11f0-84a1-088fc33f56c7', 'Premium Access Algebra',
        900000, 15, 365, 1, 'Full year access to Algebra course with bonus content'),
       ('b2d70ab6-397a-11f0-84a1-088fc33f56c7', 'b2bffc19-397a-11f0-84a1-088fc33f56c7', 'Standard DS Package', 700000,
        25, 180, 1, '180 days access to Data Structures course'),
       ('b2d70c43-397a-11f0-84a1-088fc33f56c7', 'b2c30ab5-397a-11f0-84a1-088fc33f56c7', 'Mechanics Starter', 400000,
        30, 60, 1, '60 days access to Mechanics course'),
       ('b2d70d9c-397a-11f0-84a1-088fc33f56c7', 'b2c629c9-397a-11f0-84a1-088fc33f56c7', 'Organic Chem Pro', 800000,
        45, 270, 1, '270 days access to Organic Chemistry I');
/*!40000 ALTER TABLE `pricepackage`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `question`
DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question`
(
    `id`       varchar(36)  NOT NULL,
    `content`  varchar(255) NOT NULL,
    `topic_id` varchar(36) DEFAULT NULL,
    `quiz_id`  varchar(36) DEFAULT NULL,
    `level`    varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKQuestion523038` (`topic_id`),
    KEY `FKQuestion112697` (`quiz_id`),
    CONSTRAINT `FKQuestion112697` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`),
    CONSTRAINT `FKQuestion523038` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question`
    DISABLE KEYS */;
INSERT INTO `question`
VALUES ('9d958cdc-5179-11f0-b7ee-088fc33f56c7', 'What is the value of x in 2x + 5 = 15?',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', '9d77f3c3-5179-11f0-b7ee-088fc33f56c7', 'Beginner'),
       ('9d97ffe2-5179-11f0-b7ee-088fc33f56c7', 'Describe the properties of a linked list.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', '9d7a2512-5179-11f0-b7ee-088fc33f56c7', 'Intermediate'),
       ('9d9a41c2-5179-11f0-b7ee-088fc33f56c7', 'State Newton\'s Third Law of Motion.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', '9d7d4b1f-5179-11f0-b7ee-088fc33f56c7', 'Advanced'),
       ('9d9d4dd6-5179-11f0-b7ee-088fc33f56c7', 'What is the chemical formula for methane?',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', '9d7fc099-5179-11f0-b7ee-088fc33f56c7', 'Beginner'),
       ('9da059c8-5179-11f0-b7ee-088fc33f56c7', 'Find the derivative of f(x) = x^2 + 3x.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', '9d8201b8-5179-11f0-b7ee-088fc33f56c7', 'Intermediate'),
       ('9da2c9ad-5179-11f0-b7ee-088fc33f56c7', 'Explain the concept of genetic mutation.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', '9d83c31e-5179-11f0-b7ee-088fc33f56c7', 'Intermediate'),
       ('9da534ac-5179-11f0-b7ee-088fc33f56c7', 'Define supply and demand.', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80',
        '9d85eea8-5179-11f0-b7ee-088fc33f56c7', 'Beginner'),
       ('9da78ab3-5179-11f0-b7ee-088fc33f56c7', 'Discuss the impact of the Roman Empire.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', '9d8878c0-5179-11f0-b7ee-088fc33f56c7', 'Advanced'),
       ('9daa76e1-5179-11f0-b7ee-088fc33f56c7', 'What is the First Law of Thermodynamics?',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', '9d8ace0b-5179-11f0-b7ee-088fc33f56c7', 'Intermediate'),
       ('9dace039-5179-11f0-b7ee-088fc33f56c7', 'Analyze the symbolism in a chosen poem.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', '9d8d1750-5179-11f0-b7ee-088fc33f56c7', 'Advanced'),
       ('9e639b7b-5179-11f0-b7ee-088fc33f56c7', 'What is a variable in programming?',
        'b24459d6-397a-11f0-84a1-088fc33f56c7', NULL, 'Beginner'),
       ('9e660024-5179-11f0-b7ee-088fc33f56c7', 'Explain the difference between Stack and Queue.',
        'b24459d6-397a-11f0-84a1-088fc33f56c7', NULL, 'Intermediate'),
       ('9e68173b-5179-11f0-b7ee-088fc33f56c7', 'What is Big O notation?', 'b24459d6-397a-11f0-84a1-088fc33f56c7', NULL,
        'Intermediate'),
       ('9e69d4e4-5179-11f0-b7ee-088fc33f56c7', 'Define kinetic energy.', 'b249f6b8-397a-11f0-84a1-088fc33f56c7', NULL,
        'Beginner'),
       ('9e6bd02c-5179-11f0-b7ee-088fc33f56c7', 'What is Newton\'s second law of motion?',
        'b249f6b8-397a-11f0-84a1-088fc33f56c7', NULL, 'Intermediate'),
       ('9e6dc44c-5179-11f0-b7ee-088fc33f56c7', 'Explain the concept of polymorphism in OOP.', NULL, NULL, 'Advanced'),
       ('9e6f7002-5179-11f0-b7ee-088fc33f56c7', 'What is a foreign key in a database?', NULL, NULL, 'Beginner'),
       ('9e713b8a-5179-11f0-b7ee-088fc33f56c7', 'Describe the process of normalization in databases.', NULL, NULL,
        'Intermediate'),
       ('9e730195-5179-11f0-b7ee-088fc33f56c7', 'What are the main components of an atom?',
        'b249f6b8-397a-11f0-84a1-088fc33f56c7', NULL, 'Beginner'),
       ('9e74d85f-5179-11f0-b7ee-088fc33f56c7', 'Explain the principle of a lever.',
        'b249f6b8-397a-11f0-84a1-088fc33f56c7', NULL, 'Intermediate'),
       ('9e76a5e2-5179-11f0-b7ee-088fc33f56c7', 'What is 7 multiplied by 8?', 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        NULL, 'Beginner'),
       ('9e78e882-5179-11f0-b7ee-088fc33f56c7', 'Solve for y: 2y + 10 = 24', 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        NULL, 'Beginner'),
       ('9e7add4d-5179-11f0-b7ee-088fc33f56c7', 'What is a class in Object-Oriented Programming?', NULL, NULL,
        'Beginner'),
       ('9e7cae92-5179-11f0-b7ee-088fc33f56c7', 'Describe inheritance with an example.', NULL, NULL, 'Intermediate'),
       ('9e7ec6fe-5179-11f0-b7ee-088fc33f56c7', 'What is recursion?', 'b24459d6-397a-11f0-84a1-088fc33f56c7', NULL,
        'Intermediate'),
       ('9e8062f0-5179-11f0-b7ee-088fc33f56c7', 'What is the purpose of an ERD diagram?', NULL, NULL, 'Beginner'),
       ('9e822c2c-5179-11f0-b7ee-088fc33f56c7', 'Explain the concept of \"scope\" in JavaScript.', NULL, NULL,
        'Intermediate'),
       ('9e846f8b-5179-11f0-b7ee-088fc33f56c7', 'What is a CPU cache?', NULL, NULL, 'Advanced'),
       ('9e865797-5179-11f0-b7ee-088fc33f56c7', 'Define \"potential energy\".', 'b249f6b8-397a-11f0-84a1-088fc33f56c7',
        NULL, 'Beginner'),
       ('9e881469-5179-11f0-b7ee-088fc33f56c7', 'What is a binary search tree?', 'b24459d6-397a-11f0-84a1-088fc33f56c7',
        NULL, 'Intermediate'),
       ('b3240dfd-397a-11f0-84a1-088fc33f56c7', 'What is 2 + 2?', 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        'b2e2cb06-397a-11f0-84a1-088fc33f56c7', 'Beginner'),
       ('b329e268-397a-11f0-84a1-088fc33f56c7', 'Explain the concept of a linked list.',
        'b24459d6-397a-11f0-84a1-088fc33f56c7', 'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 'Intermediate'),
       ('b32fda1f-397a-11f0-84a1-088fc33f56c7', 'State Newton\'s first law of motion.',
        'b249f6b8-397a-11f0-84a1-088fc33f56c7', 'b2f36d41-397a-11f0-84a1-088fc33f56c7', 'Intermediate'),
       ('b3348944-397a-11f0-84a1-088fc33f56c7', 'Solve for x: 3x - 5 = 10', 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        'b2e2cb06-397a-11f0-84a1-088fc33f56c7', 'Beginner'),
       ('b3388f76-397a-11f0-84a1-088fc33f56c7', 'What is the primary difference between an array and a linked list?',
        'b24459d6-397a-11f0-84a1-088fc33f56c7', 'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 'Intermediate');
/*!40000 ALTER TABLE `question`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `quiz`
DROP TABLE IF EXISTS `quiz`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz`
(
    `id`                 varchar(36)  NOT NULL,
    `duration`           int          NOT NULL,
    `status`             tinyint(1)   NOT NULL DEFAULT '0',
    `pass_rate`          float                 DEFAULT NULL,
    `updated_date`       date         NOT NULL,
    `number_of_question` int          NOT NULL DEFAULT '0',
    `description`        varchar(255) NOT NULL,
    `title`              varchar(255) NOT NULL,
    `subject_id`         varchar(36)  NOT NULL,
    `type`               varchar(36)  NOT NULL,
    `level`              varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKQuiz471131` (`level`),
    KEY `FKQuiz75766` (`type`),
    KEY `FKQuiz767071` (`subject_id`),
    CONSTRAINT `FKQuiz471131` FOREIGN KEY (`level`) REFERENCES `quizlevel` (`id`),
    CONSTRAINT `FKQuiz75766` FOREIGN KEY (`type`) REFERENCES `quiztype` (`id`),
    CONSTRAINT `FKQuiz767071` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dumping data for table `quiz`
LOCK TABLES `quiz` WRITE;
/*!40000 ALTER TABLE `quiz`
    DISABLE KEYS */;
INSERT INTO `quiz`
VALUES ('9d77f3c3-5179-11f0-b7ee-088fc33f56c7', 30, 1, 0.7, '2023-05-15', 10, 'Basic algebra concepts quiz.',
        'Algebra Fundamentals Quiz', 'b19fb878-397a-11f0-84a1-088fc33f56c7', '9d5ed5ed-5179-11f0-b7ee-088fc33f56c7',
        '9d419a71-5179-11f0-b7ee-088fc33f56c7'),
       ('9d7a2512-5179-11f0-b7ee-088fc33f56c7', 45, 1, 0.75, '2023-05-16', 15,
        'Test your knowledge on data structures.', 'Data Structures Mastery', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7',
        '9d5ed5ed-5179-11f0-b7ee-088fc33f56c7', '9d43f153-5179-11f0-b7ee-088fc33f56c7'),
       ('9d7d4b1f-5179-11f0-b7ee-088fc33f56c7', 60, 1, 0.8, '2023-05-17', 20,
        'Comprehensive quiz on classical mechanics.', 'Mechanics Challenge', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7',
        '9d5ed5ed-5179-11f0-b7ee-088fc33f56c7', '9d4657e8-5179-11f0-b7ee-088fc33f56c7'),
       ('9d7fc099-5179-11f0-b7ee-088fc33f56c7', 20, 1, 0.6, '2023-05-18', 8, 'Quick check on organic chemistry basics.',
        'Organic Chemistry Quick Test', 'b19fbe33-397a-11f0-84a1-088fc33f56c7', '9d5ed5ed-5179-11f0-b7ee-088fc33f56c7',
        '9d491452-5179-11f0-b7ee-088fc33f56c7'),
       ('9d8201b8-5179-11f0-b7ee-088fc33f56c7', 50, 1, 0.7, '2023-05-19', 18, 'Calculus I concepts assessment.',
        'Calculus I Assessment', 'b19fb878-397a-11f0-84a1-088fc33f56c7', '9d60a407-5179-11f0-b7ee-088fc33f56c7',
        '9d4b555a-5179-11f0-b7ee-088fc33f56c7'),
       ('9d83c31e-5179-11f0-b7ee-088fc33f56c7', 35, 1, 0.65, '2023-05-20', 12, 'Genetics principles quiz.',
        'Genetics Essentials', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7', '9d728aff-5179-11f0-b7ee-088fc33f56c7',
        '9d4d91cc-5179-11f0-b7ee-088fc33f56c7'),
       ('9d85eea8-5179-11f0-b7ee-088fc33f56c7', 25, 1, 0.7, '2023-05-21', 10, 'Quiz on microeconomic concepts.',
        'Microeconomics Fundamentals', 'b19fb878-397a-11f0-84a1-088fc33f56c7', '9d60a407-5179-11f0-b7ee-088fc33f56c7',
        '9d50904d-5179-11f0-b7ee-088fc33f56c7'),
       ('9d8878c0-5179-11f0-b7ee-088fc33f56c7', 75, 1, 0.85, '2023-05-22', 25,
        'Extensive quiz on ancient civilizations.', 'Ancient History Exam', 'b19fbe33-397a-11f0-84a1-088fc33f56c7',
        '9d669528-5179-11f0-b7ee-088fc33f56c7', '9d52e40a-5179-11f0-b7ee-088fc33f56c7'),
       ('9d8ace0b-5179-11f0-b7ee-088fc33f56c7', 40, 1, 0.7, '2023-05-23', 15, 'Thermodynamics principles quiz.',
        'Thermo Principles', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7', '9d626aa4-5179-11f0-b7ee-088fc33f56c7',
        '9d556522-5179-11f0-b7ee-088fc33f56c7'),
       ('9d8d1750-5179-11f0-b7ee-088fc33f56c7', 30, 1, 0.6, '2023-05-24', 10, 'Poetry analysis and understanding quiz.',
        'Poetry Comprehension', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7', '9d648e66-5179-11f0-b7ee-088fc33f56c7',
        '9d589e64-5179-11f0-b7ee-088fc33f56c7'),
       ('b2de3b10-397a-11f0-84a1-088fc33f56c7', 20, 1, 0.6, '2024-01-20', 8, 'Introduction to algebraic expressions.',
        'Intro to Algebra Concepts', 'b19fb878-397a-11f0-84a1-088fc33f56c7', 'b1927452-397a-11f0-84a1-088fc33f56c7',
        'b18d1436-397a-11f0-84a1-088fc33f56c7'),
       ('b2e2cb06-397a-11f0-84a1-088fc33f56c7', 30, 0, 0.7, '2024-01-25', 10, 'A quiz on fundamental algebra concepts.',
        'Algebra Basics Quiz', 'b19fb878-397a-11f0-84a1-088fc33f56c7', 'b1927165-397a-11f0-84a1-088fc33f56c7',
        'b18d1436-397a-11f0-84a1-088fc33f56c7'),
       ('b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 35, 1, 0.65, '2024-02-10', 12,
        'Questions on different types of linked lists.', 'Linked List Deep Dive',
        'b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'b1927519-397a-11f0-84a1-088fc33f56c7',
        'b18d5f4d-397a-11f0-84a1-088fc33f56c7'),
       ('b2ef4be4-397a-11f0-84a1-088fc33f56c7', 45, 0, 0.75, '2024-02-01', 15,
        'Test your knowledge on linked lists and arrays.', 'Data Structures Quiz 1',
        'b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'b1927165-397a-11f0-84a1-088fc33f56c7',
        'b18d5f4d-397a-11f0-84a1-088fc33f56c7'),
       ('b2f36d41-397a-11f0-84a1-088fc33f56c7', 60, 1, 0.8, '2024-03-01', 20,
        'Comprehensive quiz on Newton\'s Laws of Motion.', 'Newtonian Mechanics Quiz',
        'b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'b1927165-397a-11f0-84a1-088fc33f56c7',
        'b18d5f4d-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `quiz`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `quiz_question_source_config`
DROP TABLE IF EXISTS `quiz_question_source_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz_question_source_config`
(
    `id`                  varchar(36) NOT NULL,
    `quiz_id`             varchar(36) NOT NULL,
    `source_type`         varchar(10) NOT NULL,
    `source_id`           varchar(36) NOT NULL,
    `number_of_questions` int         NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_QuizQuestionSourceConfig_Quiz` (`quiz_id`),
    CONSTRAINT `FK_QuizQuestionSourceConfig_Quiz` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `quiz_question_source_config` WRITE;
/*!40000 ALTER TABLE `quiz_question_source_config`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `quiz_question_source_config`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `quizlevel`
DROP TABLE IF EXISTS `quizlevel`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quizlevel`
(
    `id`   varchar(36) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `quizlevel` WRITE;
/*!40000 ALTER TABLE `quizlevel`
    DISABLE KEYS */;
INSERT INTO `quizlevel`
VALUES ('9d419a71-5179-11f0-b7ee-088fc33f56c7', 'Beginner'),
       ('9d43f153-5179-11f0-b7ee-088fc33f56c7', 'Intermediate'),
       ('9d4657e8-5179-11f0-b7ee-088fc33f56c7', 'Advanced'),
       ('9d491452-5179-11f0-b7ee-088fc33f56c7', 'Expert'),
       ('9d4b555a-5179-11f0-b7ee-088fc33f56c7', 'Master'),
       ('9d4d91cc-5179-11f0-b7ee-088fc33f56c7', 'Foundational'),
       ('9d50904d-5179-11f0-b7ee-088fc33f56c7', 'Specialized'),
       ('9d52e40a-5179-11f0-b7ee-088fc33f56c7', 'General'),
       ('9d556522-5179-11f0-b7ee-088fc33f56c7', 'Practice'),
       ('9d589e64-5179-11f0-b7ee-088fc33f56c7', 'Challenge'),
       ('b18d1436-397a-11f0-84a1-088fc33f56c7', 'Beginner'),
       ('b18d5f4d-397a-11f0-84a1-088fc33f56c7', 'Intermediate'),
       ('b18d62ce-397a-11f0-84a1-088fc33f56c7', 'Advanced'),
       ('b18d634d-397a-11f0-84a1-088fc33f56c7', 'Expert'),
       ('b18d63a5-397a-11f0-84a1-088fc33f56c7', 'Master');
/*!40000 ALTER TABLE `quizlevel`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `quiztype`
DROP TABLE IF EXISTS `quiztype`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiztype`
(
    `id`   varchar(36) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `quiztype` WRITE;
/*!40000 ALTER TABLE `quiztype`
    DISABLE KEYS */;
INSERT INTO `quiztype`
VALUES ('9d5ed5ed-5179-11f0-b7ee-088fc33f56c7', 'Multiple Choice'),
       ('9d60a407-5179-11f0-b7ee-088fc33f56c7', 'True/False'),
       ('9d626aa4-5179-11f0-b7ee-088fc33f56c7', 'Fill in the Blanks'),
       ('9d648e66-5179-11f0-b7ee-088fc33f56c7', 'Matching'),
       ('9d669528-5179-11f0-b7ee-088fc33f56c7', 'Short Answer'),
       ('9d690516-5179-11f0-b7ee-088fc33f56c7', 'Essay'),
       ('9d6af932-5179-11f0-b7ee-088fc33f56c7', 'Coding Challenge'),
       ('9d6d4dbe-5179-11f0-b7ee-088fc33f56c7', 'Verbal Response'),
       ('9d70a18f-5179-11f0-b7ee-088fc33f56c7', 'Practical Test'),
       ('9d728aff-5179-11f0-b7ee-088fc33f56c7', 'Diagnostic'),
       ('b1927165-397a-11f0-84a1-088fc33f56c7', 'Multiple Choice'),
       ('b1927452-397a-11f0-84a1-088fc33f56c7', 'True/False'),
       ('b1927519-397a-11f0-84a1-088fc33f56c7', 'Fill-in-the-blank'),
       ('b1927581-397a-11f0-84a1-088fc33f56c7', 'Short Answer'),
       ('b19275d8-397a-11f0-84a1-088fc33f56c7', 'Matching');
/*!40000 ALTER TABLE `quiztype`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `setting`
DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setting`
(
    `id`              varchar(36)  NOT NULL,
    `value`           varchar(255) NOT NULL,
    `status`          tinyint(1)   NOT NULL,
    `description`     varchar(500) DEFAULT NULL,
    `updated_date`    date         DEFAULT NULL,
    `setting_type_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKSetting908739` (`setting_type_id`),
    CONSTRAINT `FKSetting908739` FOREIGN KEY (`setting_type_id`) REFERENCES `settingtype` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `setting` WRITE;
/*!40000 ALTER TABLE `setting`
    DISABLE KEYS */;
INSERT INTO `setting`
VALUES ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', 'Business', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc98-9c0b-4ef8-bb6d-6bb9bd380a16'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', 'Process', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc98-9c0b-4ef8-bb6d-6bb9bd380a16'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', 'Initialing', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', 'Planning', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Practice', 1, 'Quiz type: practice', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'Admin', 1, 'Administrator role', '2023-01-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Expert', 1, 'Expert user role', '2023-01-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a30', 'Marketer', 1, 'Marketer user role', '2025-07-15',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a31', 'Sale', 1, 'Sales user role', '2025-07-15',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'User', 1, 'User account role', '2024-03-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', 'Student', 1, 'Student user role', '2023-01-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'Technology', 1, 'Blog category for technology topics', '2023-02-15',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', 'Life Style', 1, 'Blog category for life style topics', '2023-02-15',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', 'Video', 1, 'Lesson type: video', '2023-03-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', 'Text', 1, 'Lesson type: text', '2023-03-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', 'Easy', 1, 'Quiz level: easy', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', 'Medium', 1, 'Quiz level: medium', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14'),
       ('b1a7d752-397a-11f0-84a1-088fc33f56c7', 'Video Lesson', 1, 'Standard video lecture format', '2024-01-15',
        'b1a3ef04-397a-11f0-84a1-088fc33f56c7'),
       ('b1abcc32-397a-11f0-84a1-088fc33f56c7', 'Quiz Lesson', 1, 'Interactive quiz session', '2024-01-20',
        'b1a3ef04-397a-11f0-84a1-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'Article', 1, 'General article category for blogs', '2024-02-01',
        'b1a3f45e-397a-11f0-84a1-088fc33f56c7'),
       ('b1b320dd-397a-11f0-84a1-088fc33f56c7', 'Tutorial', 1, 'Step-by-step tutorial category for blogs', '2024-02-05',
        'b1a3f45e-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `setting`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `setting_question`
DROP TABLE IF EXISTS `setting_question`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setting_question`
(
    `setting_id`  varchar(36) NOT NULL,
    `question_id` varchar(36) NOT NULL,
    PRIMARY KEY (`setting_id`, `question_id`),
    KEY `question level` (`question_id`),
    CONSTRAINT `question level` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
    CONSTRAINT `test type` FOREIGN KEY (`setting_id`) REFERENCES `setting` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `setting_question` WRITE;
/*!40000 ALTER TABLE `setting_question`
    DISABLE KEYS */;
INSERT INTO `setting_question`
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', '9d958cdc-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9d97ffe2-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9d9a41c2-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', '9d9d4dd6-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9da059c8-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9da2c9ad-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', '9da534ac-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9da78ab3-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9daa76e1-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', '9dace039-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', '9e639b7b-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9e660024-5179-11f0-b7ee-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', '9e68173b-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9e69d4e4-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', '9e6bd02c-5179-11f0-b7ee-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', '9e6bd02c-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', '9e6dc44c-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9e6f7002-5179-11f0-b7ee-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', '9e713b8a-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9e730195-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9e74d85f-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9e74d85f-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', '9e76a5e2-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9e78e882-5179-11f0-b7ee-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', '9e7add4d-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9e7cae92-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', '9e7ec6fe-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9e7ec6fe-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', '9e8062f0-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9e822c2c-5179-11f0-b7ee-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', '9e846f8b-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9e865797-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9e881469-5179-11f0-b7ee-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', '9e881469-5179-11f0-b7ee-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b3240dfd-397a-11f0-84a1-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b329e268-397a-11f0-84a1-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b32fda1f-397a-11f0-84a1-088fc33f56c7'),
       ('b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b3348944-397a-11f0-84a1-088fc33f56c7'),
       ('b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b3388f76-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `setting_question`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `setting_subject`
DROP TABLE IF EXISTS `setting_subject`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `setting_subject`
(
    `setting_id` varchar(36) NOT NULL,
    `subject_id` varchar(36) NOT NULL,
    PRIMARY KEY (`setting_id`, `subject_id`),
    KEY `FKSetting_Su420121` (`subject_id`),
    CONSTRAINT `FKSetting_Su420121` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`),
    CONSTRAINT `Subject dimension` FOREIGN KEY (`setting_id`) REFERENCES `setting` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `setting_subject` WRITE;
/*!40000 ALTER TABLE `setting_subject`
    DISABLE KEYS */;
INSERT INTO `setting_subject`
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9cc22eb1-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9cc48af2-5179-11f0-b7ee-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', '9cc69bdd-5179-11f0-b7ee-088fc33f56c7'),
       ('9c930bcd-5179-11f0-b7ee-088fc33f56c7', '9cc8be78-5179-11f0-b7ee-088fc33f56c7'),
       ('9c97aedf-5179-11f0-b7ee-088fc33f56c7', '9cc8be78-5179-11f0-b7ee-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', '9cc8be78-5179-11f0-b7ee-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', '9ccabe85-5179-11f0-b7ee-088fc33f56c7'),
       ('9c910e7c-5179-11f0-b7ee-088fc33f56c7', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('9c9519e0-5179-11f0-b7ee-088fc33f56c7', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'b19fbe33-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbe33-397a-11f0-84a1-088fc33f56c7'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `setting_subject`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `settingtype`
DROP TABLE IF EXISTS `settingtype`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settingtype`
(
    `id`   varchar(36)  NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `settingtype` WRITE;
/*!40000 ALTER TABLE `settingtype`
    DISABLE KEYS */;
INSERT INTO `settingtype`
VALUES ('a0eebc98-9c0b-4ef8-bb6d-6bb9bd380a16', 'Group'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a10', 'Blog Status'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'User Role'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Blog Category'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Lesson Type'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Quiz Level'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Quiz Type'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Domain'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Question Level'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Course Status'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Personal Course Status'),
       ('b1a3ef04-397a-11f0-84a1-088fc33f56c7', 'Lesson Type'),
       ('b1a3f45e-397a-11f0-84a1-088fc33f56c7', 'Blog Category'),
       ('b1a3f58a-397a-11f0-84a1-088fc33f56c7', 'Question Level'),
       ('b1a3f5e8-397a-11f0-84a1-088fc33f56c7', 'Account Role'),
       ('b1a3f640-397a-11f0-84a1-088fc33f56c7', 'Subject Dimension');
/*!40000 ALTER TABLE `settingtype`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `slider`
DROP TABLE IF EXISTS `slider`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slider`
(
    `id`           varchar(36)  NOT NULL,
    `title`        varchar(255) NOT NULL,
    `image_url`    varchar(500) NOT NULL,
    `backlink_url` varchar(500)          DEFAULT NULL,
    `status`       tinyint(1)   NOT NULL DEFAULT '1',
    `account_id`   varchar(36)  NOT NULL,
    `note`         varchar(500)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKSlider777469` (`account_id`),
    CONSTRAINT `FKSlider777469` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `slider` WRITE;
/*!40000 ALTER TABLE `slider`
    DISABLE KEYS */;
INSERT INTO `slider`
VALUES ('9df7803e-5179-11f0-b7ee-088fc33f56c7', 'Discover Exclusive Deals You CanΓÇÖt Miss This Week!',
        'img/slider1.jpg', 'http://example.com/promo1', 1, '9c9d5b45-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9df95d48-5179-11f0-b7ee-088fc33f56c7', 'Step Into the Future with Our Latest 2025 Collection',
        'img/slider2.jpg', 'http://example.com/promo2', 1, '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9dfb52b8-5179-11f0-b7ee-088fc33f56c7', 'Massive Discounts Just for You ΓÇô Limited Time Only!',
        'img/slider3.jpg', 'http://example.com/promo3', 1, '9ca187f4-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9dfd2cc4-5179-11f0-b7ee-088fc33f56c7', 'Unmatched Quality Meets Unbeatable Prices ΓÇô Shop Now',
        'img/slider4.jpg', 'http://example.com/promo4', 1, '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9dff22e5-5179-11f0-b7ee-088fc33f56c7', 'Top-Selling Products of the Week ΓÇô Grab Them Fast!',
        'img/slider5.jpg', 'http://example.com/promo5', 1, '9c9d5b45-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9e013c1b-5179-11f0-b7ee-088fc33f56c7', 'Welcome Summer with Fresh Styles and Hot Promotions',
        'img/slider6.jpg', 'http://example.com/promo6', 1, '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9e0386da-5179-11f0-b7ee-088fc33f56c7', 'Enjoy Seamless Shopping with Big Savings Every Day', 'img/slider7.jpg',
        'http://example.com/promo7', 1, '9ca59aff-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9e05a91f-5179-11f0-b7ee-088fc33f56c7', 'New Arrivals Are Here ΓÇô Limited Stock Available!', 'img/slider8.jpg',
        'http://example.com/promo8', 1, '9c9f87a3-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9e077641-5179-11f0-b7ee-088fc33f56c7', 'Save More with Our Value Combos and Special Offers', 'img/slider9.jpg',
        'http://example.com/promo9', 1, '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('9e09b446-5179-11f0-b7ee-088fc33f56c7', 'Upgrade Your Lifestyle ΓÇô Discover WhatΓÇÖs New Today',
        'img/slider10.jpg', 'http://example.com/promo10', 1, '9ca98304-5179-11f0-b7ee-088fc33f56c7', NULL),
       ('b3dcacb3-397a-11f0-84a1-088fc33f56c7', 'Spring Promotion', 'img/slider1.jpg', 'http://example.com/promo1', 1,
        'b283bfb8-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb132-397a-11f0-84a1-088fc33f56c7', 'New Courses Available', 'img/slider2.jpg',
        'http://example.com/new_courses', 1, 'b287bbd5-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb2ee-397a-11f0-84a1-088fc33f56c7', 'Big Sale Event', 'img/slider3.jpg', 'http://example.com/sale', 0,
        'b28b4752-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb3cc-397a-11f0-84a1-088fc33f56c7', 'Emerging Web Development Trends in 2024', 'img/slider4.jpg',
        'http://example.com/partners', 1, 'b2932a61-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb470-397a-11f0-84a1-088fc33f56c7', 'Effective Study Habits for Lifelong Learning', 'img/slider5.jpg',
        'http://example.com/testimonials', 1, 'b2971e45-397a-11f0-84a1-088fc33f56c7', NULL);
/*!40000 ALTER TABLE `slider`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `subject`
DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject`
(
    `id`            varchar(36)  NOT NULL,
    `name`          varchar(255) NOT NULL,
    `thumbnail_url` varchar(500) DEFAULT NULL,
    `feature_flag`  tinyint      DEFAULT '0',
    `author`        varchar(36)  NOT NULL,
    `created_date`  date         DEFAULT NULL,
    `updated_date`  date         DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKSubject873139` (`author`),
    CONSTRAINT `FKSubject873139` FOREIGN KEY (`author`) REFERENCES `contact` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject`
    DISABLE KEYS */;
INSERT INTO `subject`
VALUES ('9cc22eb1-5179-11f0-b7ee-088fc33f56c7', 'History', 'img/course-1.jpg', 0,
        '9b24cd1f-5179-11f0-b7ee-088fc33f56c7', '2022-11-02', '2022-11-04'),
       ('9cc48af2-5179-11f0-b7ee-088fc33f56c7', 'Literature', 'img/course-2.jpg', 0,
        '9b24cd1f-5179-11f0-b7ee-088fc33f56c7', '2023-10-22', '2022-04-02'),
       ('9cc69bdd-5179-11f0-b7ee-088fc33f56c7', 'Economics', 'img/economics_thumb.jpg', 0,
        '9b2734ef-5179-11f0-b7ee-088fc33f56c7', '2022-02-02', '2022-06-24'),
       ('9cc8be78-5179-11f0-b7ee-088fc33f56c7', 'Art & Design', 'img/art_thumb.jpg', 1,
        '9b29abd3-5179-11f0-b7ee-088fc33f56c7', '2022-04-14', '2022-11-02'),
       ('9ccabe85-5179-11f0-b7ee-088fc33f56c7', 'MAD101', 'img/course-1.jpg', 0, '9b2c5688-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cccfa43-5179-11f0-b7ee-088fc33f56c7', 'EXE102', 'img/cat-1.jpg', 0, '9b32fbf0-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9ccf5032-5179-11f0-b7ee-088fc33f56c7', 'EXE101', 'img/cat-1.jpg', 0, '9b1a6f2d-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cd1715c-5179-11f0-b7ee-088fc33f56c7', 'PRJ', 'img/cat-3.jpg', 0, '9b2c5688-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cd377f6-5179-11f0-b7ee-088fc33f56c7', 'MAS391', 'img/john.jpg', 0, '9b30cc62-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cd5ef92-5179-11f0-b7ee-088fc33f56c7', 'PRF', 'img/cat-1.jpg', 0, '9b1ffae9-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cd7fa0c-5179-11f0-b7ee-088fc33f56c7', 'SWP391', 'img/cat-1.jpg', 0, '9b29abd3-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cd9dd8c-5179-11f0-b7ee-088fc33f56c7', 'SWT302', 'img/cat-1.jpg', 0, '9b2734ef-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cdbe0a9-5179-11f0-b7ee-088fc33f56c7', 'SWR301', 'img/cat-1.jpg', 0, '9b1d47f8-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('9cddd888-5179-11f0-b7ee-088fc33f56c7', 'SWE', 'img/cat-1.jpg', 0, '9b2e9e4a-5179-11f0-b7ee-088fc33f56c7',
        '2024-11-02', '2025-02-02'),
       ('b19fb878-397a-11f0-84a1-088fc33f56c7', 'Mathematics', 'img/subject-thumb-1.jpg', 1,
        '9b2734ef-5179-11f0-b7ee-088fc33f56c7', '2025-01-02', '2025-01-04'),
       ('b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'Computer Science', 'img/subject-thumb-2.jpg', 1,
        '9b29abd3-5179-11f0-b7ee-088fc33f56c7', '2024-02-12', '2025-01-02'),
       ('b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'Physics', 'img/subject-thumb-3.jpg', 1,
        '9b2c5688-5179-11f0-b7ee-088fc33f56c7', '2025-01-12', '2025-01-22'),
       ('b19fbe33-397a-11f0-84a1-088fc33f56c7', 'Chemistry', 'img/subject-thumb-4.jpg', 1,
        '9b1d47f8-5179-11f0-b7ee-088fc33f56c7', '2025-03-04', '2025-05-10'),
       ('b19fbe8f-397a-11f0-84a1-088fc33f56c7', 'Biology', 'img/subject-thumb-5.jpg', 1,
        '9b1ffae9-5179-11f0-b7ee-088fc33f56c7', '2024-07-22', '2025-01-12');
/*!40000 ALTER TABLE `subject`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `subject_tagline`
DROP TABLE IF EXISTS `subject_tagline`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject_tagline`
(
    `subject_id` varchar(36) NOT NULL,
    `tagline_id` varchar(36) NOT NULL,
    PRIMARY KEY (`subject_id`, `tagline_id`),
    KEY `FKSubject_Ta221833` (`tagline_id`),
    CONSTRAINT `FKSubject_Ta221833` FOREIGN KEY (`tagline_id`) REFERENCES `tagline` (`id`),
    CONSTRAINT `FKSubject_Ta283943` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `subject_tagline` WRITE;
/*!40000 ALTER TABLE `subject_tagline`
    DISABLE KEYS */;
INSERT INTO `subject_tagline`
VALUES ('b19fb878-397a-11f0-84a1-088fc33f56c7', '9e0f8595-5179-11f0-b7ee-088fc33f56c7'),
       ('b19fbc5e-397a-11f0-84a1-088fc33f56c7', '9e120724-5179-11f0-b7ee-088fc33f56c7'),
       ('b19fbdc2-397a-11f0-84a1-088fc33f56c7', '9e146c7c-5179-11f0-b7ee-088fc33f56c7'),
       ('b19fbe33-397a-11f0-84a1-088fc33f56c7', '9e16dc6f-5179-11f0-b7ee-088fc33f56c7'),
       ('b19fbe8f-397a-11f0-84a1-088fc33f56c7', '9e18f49a-5179-11f0-b7ee-088fc33f56c7'),
       ('9cc22eb1-5179-11f0-b7ee-088fc33f56c7', '9e1b6c1f-5179-11f0-b7ee-088fc33f56c7'),
       ('9cc48af2-5179-11f0-b7ee-088fc33f56c7', '9e1e27b3-5179-11f0-b7ee-088fc33f56c7'),
       ('9cc69bdd-5179-11f0-b7ee-088fc33f56c7', '9e209820-5179-11f0-b7ee-088fc33f56c7'),
       ('9cc8be78-5179-11f0-b7ee-088fc33f56c7', '9e231145-5179-11f0-b7ee-088fc33f56c7'),
       ('9ccabe85-5179-11f0-b7ee-088fc33f56c7', '9e259036-5179-11f0-b7ee-088fc33f56c7'),
       ('b19fb878-397a-11f0-84a1-088fc33f56c7', 'b1970363-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'b19706b5-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'b19707dc-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbe33-397a-11f0-84a1-088fc33f56c7', 'b197083e-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbe8f-397a-11f0-84a1-088fc33f56c7', 'b1970897-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `subject_tagline`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `tagline`
DROP TABLE IF EXISTS `tagline`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tagline`
(
    `id`   varchar(36)  NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tagline` WRITE;
/*!40000 ALTER TABLE `tagline`
    DISABLE KEYS */;
INSERT INTO `tagline`
VALUES ('9e0f8595-5179-11f0-b7ee-088fc33f56c7', 'Learn Anytime, Anywhere'),
       ('9e120724-5179-11f0-b7ee-088fc33f56c7', 'Unlock Your Potential'),
       ('9e146c7c-5179-11f0-b7ee-088fc33f56c7', 'Expert-Led Courses'),
       ('9e16dc6f-5179-11f0-b7ee-088fc33f56c7', 'Interactive Learning'),
       ('9e18f49a-5179-11f0-b7ee-088fc33f56c7', 'Achieve Your Goals'),
       ('9e1b6c1f-5179-11f0-b7ee-088fc33f56c7', 'Global Community'),
       ('9e1e27b3-5179-11f0-b7ee-088fc33f56c7', 'Personalized Education'),
       ('9e209820-5179-11f0-b7ee-088fc33f56c7', 'Future-Proof Your Skills'),
       ('9e231145-5179-11f0-b7ee-088fc33f56c7', 'Master New Concepts'),
       ('9e259036-5179-11f0-b7ee-088fc33f56c7', 'Knowledge at Your Fingertips'),
       ('b1970363-397a-11f0-84a1-088fc33f56c7', 'Learn Anywhere'),
       ('b19706b5-397a-11f0-84a1-088fc33f56c7', 'Master New Skills'),
       ('b19707dc-397a-11f0-84a1-088fc33f56c7', 'Unlock Your Potential'),
       ('b197083e-397a-11f0-84a1-088fc33f56c7', 'Future-Proof Your Career'),
       ('b1970897-397a-11f0-84a1-088fc33f56c7', 'Knowledge at Your Fingertips');
/*!40000 ALTER TABLE `tagline`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `topic`
DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topic`
(
    `id`         varchar(36)  NOT NULL,
    `name`       varchar(255) NOT NULL,
    `subject_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKTopic873139` (`subject_id`),
    CONSTRAINT `FKTopic873139` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `topic` WRITE;
/*!40000 ALTER TABLE `topic`
    DISABLE KEYS */;
INSERT INTO `topic`
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80', 'Microeconomics', '9cc69bdd-5179-11f0-b7ee-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', 'Algebra', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', 'Calculus', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', 'Mechanics', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', 'Thermodynamics', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', 'Organic Chemistry', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', 'Data Structures', 'b19fbe33-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', 'Genetics', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', 'World History', '9cc22eb1-5179-11f0-b7ee-088fc33f56c7'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', 'Poetry', '9cc48af2-5179-11f0-b7ee-088fc33f56c7'),
       ('b240fae5-397a-11f0-84a1-088fc33f56c7', 'Algebra', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('b24459d6-397a-11f0-84a1-088fc33f56c7', 'Data Structures', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('b249f6b8-397a-11f0-84a1-088fc33f56c7', 'Mechanics', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'),
       ('b253366f-397a-11f0-84a1-088fc33f56c7', 'Organic Chemistry', 'b19fbe33-397a-11f0-84a1-088fc33f56c7'),
       ('b25cd42b-397a-11f0-84a1-088fc33f56c7', 'Genetics', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `topic`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `userlog`
DROP TABLE IF EXISTS `userlog`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userlog`
(
    `id`           varchar(36) NOT NULL,
    `created_date` date        NOT NULL,
    `account_id`   varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKUserLog508255` (`account_id`),
    CONSTRAINT `FKUserLog508255` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `userlog` WRITE;
/*!40000 ALTER TABLE `userlog`
    DISABLE KEYS */;
INSERT INTO `userlog`
VALUES ('9e2ed53f-5179-11f0-b7ee-088fc33f56c7', '2023-01-01', '9c9d5b45-5179-11f0-b7ee-088fc33f56c7'),
       ('9e309341-5179-11f0-b7ee-088fc33f56c7', '2023-01-02', '9c9f87a3-5179-11f0-b7ee-088fc33f56c7'),
       ('9e32a28c-5179-11f0-b7ee-088fc33f56c7', '2023-01-03', '9ca187f4-5179-11f0-b7ee-088fc33f56c7'),
       ('9e3459d4-5179-11f0-b7ee-088fc33f56c7', '2023-01-04', '9ca3a7f6-5179-11f0-b7ee-088fc33f56c7'),
       ('9e363223-5179-11f0-b7ee-088fc33f56c7', '2023-01-05', '9ca59aff-5179-11f0-b7ee-088fc33f56c7'),
       ('9e3826db-5179-11f0-b7ee-088fc33f56c7', '2023-01-06', '9ca766b5-5179-11f0-b7ee-088fc33f56c7'),
       ('9e3a2a4e-5179-11f0-b7ee-088fc33f56c7', '2023-01-07', '9ca98304-5179-11f0-b7ee-088fc33f56c7'),
       ('9e3c6d5b-5179-11f0-b7ee-088fc33f56c7', '2023-01-08', '9cab6d2b-5179-11f0-b7ee-088fc33f56c7'),
       ('9e3e94d6-5179-11f0-b7ee-088fc33f56c7', '2023-01-09', '9cad56fd-5179-11f0-b7ee-088fc33f56c7'),
       ('9e406d24-5179-11f0-b7ee-088fc33f56c7', '2023-01-10', '9caf51fa-5179-11f0-b7ee-088fc33f56c7'),
       ('b3d89048-397a-11f0-84a1-088fc33f56c7', '2024-05-20', 'b283bfb8-397a-11f0-84a1-088fc33f56c7'),
       ('b3d8976b-397a-11f0-84a1-088fc33f56c7', '2024-05-20', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'),
       ('b3d898fc-397a-11f0-84a1-088fc33f56c7', '2024-05-21', 'b28b4752-397a-11f0-84a1-088fc33f56c7'),
       ('b3d89a77-397a-11f0-84a1-088fc33f56c7', '2024-05-21', 'b2932a61-397a-11f0-84a1-088fc33f56c7'),
       ('b3d89ba0-397a-11f0-84a1-088fc33f56c7', '2024-05-22', 'b2971e45-397a-11f0-84a1-088fc33f56c7');
/*!40000 ALTER TABLE `userlog`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `personalsubject`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
create table PersonalSubject
(
    id                varchar(36) not null unique,
    account_id        varchar(36) not null,
    subject_id        varchar(36) not null,
    status            varchar(50) not null default 'sent', /*0 for sent, 1 for paid, 2 for accepted*/
    registration_date date        not null,
    package_name      varchar(255),
    valid_from        date,
    valid_to          date,
    price             double,
    primary key (id),
    foreign key (account_id) references Account (id),
    foreign key (subject_id) references Subject (id)
);
LOCK TABLES `personalsubject` WRITE;
/*!40000 ALTER TABLE `personalsubject`
    DISABLE KEYS */;
SET @psubject_id1 = UUID();
SET @psubject_id2 = UUID();
SET @psubject_id3 = UUID();
SET @psubject_id4 = UUID();
SET @psubject_id5 = UUID();
SET @psubject_id6 = UUID();
SET @psubject_id7 = UUID();
INSERT INTO `personalsubject`
VALUES (@psubject_id1, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b19fb878-397a-11f0-84a1-088fc33f56c7', 'sent', NOW(),
        'Bronze', NOW(), NOW() + INTERVAL 2 DAY, 2000),
       (@psubject_id2, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'paid', NOW(),
        'Sliver', NOW(), NOW() + INTERVAL 2 DAY, 2000),
       (@psubject_id3, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'accepted',
        NOW(), 'Gold', NOW(), NOW() + INTERVAL 2 DAY, 2000),
       (@psubject_id4, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b19fbe33-397a-11f0-84a1-088fc33f56c7', 'sent', NOW(),
        'Bronze', NOW(), NOW() + INTERVAL 2 DAY, 2000),
       (@psubject_id5, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7', 'paid', NOW(),
        'Sliver', NOW(), NOW() + INTERVAL 2 DAY, 2000),
       (@psubject_id6, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7', 'accepted',
        NOW(), 'Gold', NOW(), NOW() + INTERVAL 2 DAY, 2000),
       (@psubject_id7, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', '9cd5ef92-5179-11f0-b7ee-088fc33f56c7', 'sent', NOW(),
        'Bronze', NOW(), NOW() + INTERVAL 2 DAY, 2000);
/*!40000 ALTER TABLE `personalsubject`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;