-- Table structure for table `settingtype`
CREATE TABLE `settingtype`
(
    `id`   varchar(36)  NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);
INSERT INTO `settingtype`
VALUES ('b1a3ef04-397a-11f0-84a1-088fc33f56c7', 'Lesson Type'),
       ('b1a3f45e-397a-11f0-84a1-088fc33f56c7', 'Blog Category'),
       ('b1a3f58a-397a-11f0-84a1-088fc33f56c7', 'Question Level'),
       ('b1a3f5e8-397a-11f0-84a1-088fc33f56c7', 'Account Role'),
       ('b1a3f640-397a-11f0-84a1-088fc33f56c7', 'Subject Dimension');

-- Table structure for table `setting`
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
);
INSERT INTO `setting`
VALUES ('b1a7d752-397a-11f0-84a1-088fc33f56c7', 'Video Lesson', 1, 'Standard video lecture format', '2024-01-15',
        'b1a3ef04-397a-11f0-84a1-088fc33f56c7'),
       ('b1abcc32-397a-11f0-84a1-088fc33f56c7', 'Quiz Lesson', 1, 'Interactive quiz session', '2024-01-20',
        'b1a3ef04-397a-11f0-84a1-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'Article', 1, 'General article category for blogs', '2024-02-01',
        'b1a3f45e-397a-11f0-84a1-088fc33f56c7'),
       ('b1b320dd-397a-11f0-84a1-088fc33f56c7', 'Tutorial', 1, 'Step-by-step tutorial category for blogs', '2024-02-05',
        'b1a3f45e-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'Admin', 1, 'Administrator account role', '2024-03-01',
        'b1a3f5e8-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `account`
CREATE TABLE `account`
(
    `id`           varchar(36)  NOT NULL,
    `email`        varchar(255) NOT NULL UNIQUE,
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
    KEY `role` (`role_id`),
    CONSTRAINT `role` FOREIGN KEY (`role_id`) REFERENCES `setting` (`id`),
    CONSTRAINT `account_chk_1` CHECK ((`gender` in (0, 1, 2))),
    CONSTRAINT `account_chk_2` CHECK (regexp_like(`email`,
                                                  _utf8mb4'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')),
    CONSTRAINT `account_chk_3` CHECK (regexp_like(`password`,
                                                  _utf8mb4'^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;"\'<>,.?/]).{8,}$')),
    CONSTRAINT `account_chk_4` CHECK (regexp_like(`phone`, _utf8mb4'^\\+?[0-9]{10,15}$'))
);
INSERT INTO `account`
VALUES ('b283bfb8-397a-11f0-84a1-088fc33f56c7', 'user@gmail.com', 'John Doe', 'SecureP@ss1', '1990-01-01', 0,
        '2023-01-01', 1, '0911111111', 'img/john.jpg',
        'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b287bbd5-397a-11f0-84a1-088fc33f56c7', 'hoana5k44nknd@gmail.com', 'Jane Smith', 'StrongP@ss2', '1992-05-15', 1,
        '2023-01-02', 1, '0922222222', 'img/jane.jpg',
        'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b28b4752-397a-11f0-84a1-088fc33f56c7', 'peter.jones@example.com', 'Peter Jones', 'MyP@ssw0rd3', '1988-11-30',
        0, '2023-01-03', 1, '0933333333', 'img/peter.jpg',
        'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b2932a61-397a-11f0-84a1-088fc33f56c7', 'alice.white@example.com', 'Alice White', 'Passw0rd!4', '1995-03-22', 1,
        '2023-01-04', 1, '0944444444', 'img/alice.jpg',
        'b1b69765-397a-11f0-84a1-088fc33f56c7'),
       ('b2971e45-397a-11f0-84a1-088fc33f56c7', 'bob.brown@example.com', 'Bob Brown', 'SecureP@ss5!', '1991-07-07', 0,
        '2023-01-05', 1, '0955555555', 'img/bob.jpg',
        'b1b69765-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `blog`
CREATE TABLE `blog`
(
    `id`            varchar(36)  NOT NULL,
    `updated_date`  date                  DEFAULT NULL,
    `thumbnail_url` varchar(1000)         DEFAULT NULL,
    `title`         varchar(255) NOT NULL,
    `brief_info`    varchar(255) NOT NULL,
    `content`       TEXT                  DEFAULT NULL,
    `status`        tinyint(1)   NOT NULL DEFAULT true,
    `created_date`  date         NOT NULL,
    `category`      varchar(36)  NOT NULL,
    `account_id`    varchar(36)  NOT NULL,
    `flag_feature`  tinyint(1)            DEFAULT true,
    `views`         int          NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `FKBlog922018` (`account_id`),
    KEY `post categories` (`category`),
    CONSTRAINT `FKBlog922018` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `post categories` FOREIGN KEY (`category`) REFERENCES `setting` (`id`)
);
INSERT INTO `blog`
VALUES ('b3d1a1b5-397a-11f0-84a1-088fc33f56c7', '2024-03-01',
        'img/blog1_thumb.jpg',
        'The Future of Artificial Intelligence: Navigating a World Transformed by Smart Technologies',
        'Artificial Intelligence is no longer a futuristic concept—it transforming how we live, work, and interact. Discover how AI will shape the next decade of global innovation.',
        'Artificial Intelligence (AI) is rapidly evolving from a supporting technology to a driving force behind transformative change across every sector of society. In this article, we examine the major trends shaping the future of AI—from the rise of generative models and neural networks to the ethical concerns surrounding privacy, bias, and transparency.
		We explore real-world use cases such as AI-assisted healthcare diagnoses, autonomous transportation, and intelligent automation in manufacturing. Experts predict that AI will not only augment human capabilities but also create new roles and industries that were previously unimaginable.
		However, the rapid development of AI technologies also raises critical questions. How do we ensure that these systems are transparent and accountable? What regulatory frameworks are needed to prevent misuse? How will AI affect job markets and economic inequality?
		By examining both the promises and the risks, this article aims to provide a balanced view of how AI is shaping a smarter, faster, and more connected future.',
        1, '2024-02-20',
        'b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 1, 2500),
       ('b3d1acf8-397a-11f0-84a1-088fc33f56c7', '2024-03-10',
        'img/blog2_thumb.jpg', 'Mastering Python in 30 Days: A Full Roadmap from Beginner to Confident Developer',
        'Want to learn Python fast but effectively? Here’s a comprehensive 30-day plan with hands-on projects, learning milestones, and real-world applications.', 'Python is one of the most beginner-friendly and versatile programming languages today. Whether you`re an aspiring software developer, data analyst, or automation enthusiast, Python has something to offer.
		This 30-day learning plan is crafted to take you from zero experience to writing structured, functional, and object-oriented Python code. The plan is divided into five 6-day modules covering core concepts:

		Week 1: Data types, variables, loops, and conditionals

		Week 2: Functions, error handling, and file I/O

		Week 3: Object-oriented programming and modules

		Week 4: Working with APIs, data manipulation (pandas), and basic plotting (matplotlib)

		Final Days: Building a mini project (to-do app, budget tracker, or a web scraper)
		Each week includes coding exercises, reading materials, and small projects to reinforce learning. By the end of the 30 days, you’ll have a GitHub portfolio and the confidence to tackle Python-based projects or dive deeper into fields like web development, data science, or machine learning.',
        1, '2024-03-05',
        'b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 0, 2600),
       ('b3d1b01b-397a-11f0-84a1-088fc33f56c7', '2024-03-15',
        'img/blog3_thumb.jpg', 'Quantum Physics Demystified: Making Sense of the Universe’s Most Puzzling Concepts',
        'Quantum physics often seems abstract and intimidating. This guide breaks down its core ideas in a simple, relatable way—even for those with no science background.',
        'Quantum physics challenges everything we think we know about how the universe works. It`s a realm where particles can be in multiple places at once, where events seem to occur without cause, and where observation can alter reality.
		But despite its weirdness, quantum mechanics is the foundation of modern technology—powering everything from lasers and semiconductors to quantum computing and MRI machines.',
        1, '2024-03-12',
        'b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7', 0, 4200),
       ('b3d1b1eb-397a-11f0-84a1-088fc33f56c7', '2024-03-20',
        'img/blog4_thumb.jpg', 'Web Development in 2024: Top Trends, Tools, and Techniques You Need to Know',
        'The web is evolving fast. Here are the most important web development trends, frameworks, and best practices that developers must embrace this year.',
        '2024 marks a turning point in the web development landscape, with modern users demanding faster, more secure, and more personalized experiences. Developers are rising to the challenge by adopting new frameworks, architectures, and AI-powered tools.',
        1, '2024-03-18',
        'b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b2932a61-397a-11f0-84a1-088fc33f56c7', 0, 2800),
       ('b3d1b36e-397a-11f0-84a1-088fc33f56c7', '2024-03-25',
        'img/blog5_thumb.jpg', 'Effective Study Habits',
        'Tips and tricks for better learning.', 'Strategies for effective studying...', 1, '2024-03-22',
        'b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b2971e45-397a-11f0-84a1-088fc33f56c7', 1, 5800);

-- Table structure for table `blog`
CREATE TABLE `blogmedia`
(
    `id`            varchar(36)             NOT NULL,
    `blog_id`       varchar(36)             NOT NULL,
    `media_type`    ENUM ('image', 'video') NOT NULL,
    `file_path`     varchar(1000)           NOT NULL,
    `caption`       TEXT DEFAULT NULL,
    `display_order` INT  DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `FK_BlogMedia_Blog` (`blog_id`),
    CONSTRAINT `FK_BlogMedia_Blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`) ON DELETE CASCADE
);

SET @media1 = UUID();
SET @media2 = UUID();
SET @media3 = UUID();
SET @media4 = UUID();
SET @media5 = UUID();
SET @media6 = UUID();
SET @media7 = UUID();
SET @media8 = UUID();
SET @media9 = UUID();
SET @media10 = UUID();
INSERT INTO `blogmedia`
VALUES (@media1, 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'image', 'img/ai_future_1.jpg', 'Ảnh mô tả AI', 1),
       (@media2, 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'image', 'img/ai_future_2.jpg', 'Biểu đồ thống kê', 2),
       (@media3, 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'video', 'img/ai_future_summary.mp4', 'Video tóm tắt AI', 3),
       (@media4, 'b3d1acf8-397a-11f0-84a1-088fc33f56c7', 'video', 'img/python_intro.mp4', 'Giới thiệu Python', 1),
       (@media5, 'b3d1b01b-397a-11f0-84a1-088fc33f56c7', 'image', 'img/quantum_model.jpg', 'Mô hình lượng tử', 1),
       (@media6, 'b3d1b01b-397a-11f0-84a1-088fc33f56c7', 'image', 'img/quantum_result.jpg', 'Kết quả mô phỏng lượng tử',
        2),
       (@media7, 'b3d1acf8-397a-11f0-84a1-088fc33f56c7', 'image', 'img/python_diagram.jpg', 'Biểu đồ kiến trúc Python',
        2),
       (@media8, 'b3d1acf8-397a-11f0-84a1-088fc33f56c7', 'image', 'img/python_usecases.jpg',
        'Ứng dụng thực tế của Python', 3),
       (@media9, 'b3d1a1b5-397a-11f0-84a1-088fc33f56c7', 'image', 'img/ai_vision.jpg', 'Tầm nhìn AI trong tương lai',
        4),
       (@media10, 'b3d1b01b-397a-11f0-84a1-088fc33f56c7', 'video', 'img/quantum_explained.mp4',
        'Video giải thích lượng tử', 3);


-- Table structure for table `chat`
CREATE TABLE `chat`
(
    `id`            varchar(36) NOT NULL,
    `first_person`  varchar(36) NOT NULL,
    `second_person` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKChat197105` (`second_person`),
    KEY `FKChat395442` (`first_person`),
    CONSTRAINT `FKChat197105` FOREIGN KEY (`second_person`) REFERENCES `account` (`id`),
    CONSTRAINT `FKChat395442` FOREIGN KEY (`first_person`) REFERENCES `account` (`id`)
);
INSERT INTO `chat`
VALUES ('b399c16e-397a-11f0-84a1-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7',
        'b287bbd5-397a-11f0-84a1-088fc33f56c7'),
       ('b39d8cb6-397a-11f0-84a1-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7',
        'b2932a61-397a-11f0-84a1-088fc33f56c7'),
       ('b3a36da7-397a-11f0-84a1-088fc33f56c7', 'b2971e45-397a-11f0-84a1-088fc33f56c7',
        'b283bfb8-397a-11f0-84a1-088fc33f56c7'),
       ('b3abf35e-397a-11f0-84a1-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7',
        'b28b4752-397a-11f0-84a1-088fc33f56c7'),
       ('b3b1167c-397a-11f0-84a1-088fc33f56c7', 'b2932a61-397a-11f0-84a1-088fc33f56c7',
        'b2971e45-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `chatdetails`
CREATE TABLE `chatdetails`
(
    `id`        varchar(36)  NOT NULL,
    `message`   varchar(255) NOT NULL,
    `timestamp` date         NOT NULL,
    `sender`    varchar(255) NOT NULL,
    `chat_id`   varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKChatDetail513929` (`chat_id`),
    CONSTRAINT `FKChatDetail513929` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`)
);
INSERT INTO `chatdetails`
VALUES ('b3be6638-397a-11f0-84a1-088fc33f56c7', 'Hi Jane, how are you?', '2024-05-20',
        'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b399c16e-397a-11f0-84a1-088fc33f56c7'),
       ('b3be6e54-397a-11f0-84a1-088fc33f56c7', 'I am good, John! How about you?', '2024-05-20',
        'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b399c16e-397a-11f0-84a1-088fc33f56c7'),
       ('b3be7105-397a-11f0-84a1-088fc33f56c7', 'Meeting at 3 PM today?', '2024-05-21',
        'b28b4752-397a-11f0-84a1-088fc33f56c7', 'b39d8cb6-397a-11f0-84a1-088fc33f56c7'),
       ('b3be72d3-397a-11f0-84a1-088fc33f56c7', 'Yes, see you there.', '2024-05-21',
        'b2932a61-397a-11f0-84a1-088fc33f56c7', 'b39d8cb6-397a-11f0-84a1-088fc33f56c7'),
       ('b3be7619-397a-11f0-84a1-088fc33f56c7', 'Checking in on the project.', '2024-05-22',
        'b2971e45-397a-11f0-84a1-088fc33f56c7', 'b3a36da7-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `contact`
CREATE TABLE `contact`
(
    `id`      varchar(36)  NOT NULL,
    `name`    varchar(255) NOT NULL,
    `link`    json         NOT NULL,
    `email`   varchar(255) NOT NULL UNIQUE,
    `phone`   varchar(11)  NOT NULL,
    `address` varchar(500) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `contact_chk_2` CHECK (regexp_like(`email`,
                                                  _utf8mb4'^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')),
    CONSTRAINT `contact_chk_4` CHECK (regexp_like(`phone`, _utf8mb4'^\\+?[0-9]{10,15}$'))
);
INSERT INTO `contact`
VALUES ('b19b605b-397a-11f0-84a1-088fc33f56c7', 'Support Team A', '{
  \"website\": \"http://yourwebsite.com/main_image.png\",
  \"social media\": \"http://facebook.com/your_page_image.jpg\"
}', 'supportA@example.com', '0901234567', '123 Support St, Hanoi'),
       ('b19bb3b8-397a-11f0-84a1-088fc33f56c7', 'Support Team B', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportB@example.com', '0902345678', '456 Service Rd, Ho Chi Minh'),
       ('b19bb4fc-397a-11f0-84a1-088fc33f56c7', 'Support Team C', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportC@example.com', '0903456789', '789 Help Ave, Da Nang'),
       ('b19bb588-397a-11f0-84a1-088fc33f56c7', 'Support Team D', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportD@example.com', '0904567890', '101 Assistance Blv, Can Tho'),
       ('b19bb60c-397a-11f0-84a1-088fc33f56c7', 'Support Team E', '{
         \"website\": \"http://yourwebsite.com/main_image.png\",
         \"social media\": \"http://facebook.com/your_page_image.jpg\"
       }', 'supportE@example.com', '0905678901', '202 Query Lane, Hai Phong');

-- Table structure for table `subject`
CREATE TABLE `subject`
(
    `id`            varchar(36)  NOT NULL,
    `name`          varchar(255) NOT NULL,
    `thumbnail_url` varchar(500) DEFAULT NULL,
    `feature_flag`  tinyint      DEFAULT 0,
    PRIMARY KEY (`id`)
);
INSERT INTO `subject`
VALUES ('b19fb878-397a-11f0-84a1-088fc33f56c7', 'Mathematics', 'img/subject-thumb-1.jpg', 1),
       ('b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'Computer Science', 'img/subject-thumb-2.jpg', 1),
       ('b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'Physics', 'img/subject-thumb-3.jpg', 1),
       ('b19fbe33-397a-11f0-84a1-088fc33f56c7', 'Chemistry', 'img/subject-thumb-4.jpg', 1),
       ('b19fbe8f-397a-11f0-84a1-088fc33f56c7', 'Biology', 'img/subject-thumb-5.jpg', 1);
SET @subject_id1 = 'b19fb878-397a-11f0-84a1-088fc33f56c7'; -- Mathematics
SET @subject_id2 = 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'; -- Computer Science
SET @subject_id3 = 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'; -- Physics
SET @subject_id4 = 'b19fbe33-397a-11f0-84a1-088fc33f56c7'; -- Chemistry
SET @subject_id5 = 'b19fbe8f-397a-11f0-84a1-088fc33f56c7';
-- Biology

-- Table structure for table `topic`
CREATE TABLE `topic`
(
    `id`         varchar(36)  NOT NULL,
    `name`       varchar(255) NOT NULL,
    `subject_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKTopic873139` (`subject_id`),
    CONSTRAINT `FKTopic873139` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
);
INSERT INTO `topic`
VALUES ('b240fae5-397a-11f0-84a1-088fc33f56c7', 'Algebra', 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       ('b24459d6-397a-11f0-84a1-088fc33f56c7', 'Data Structures', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('b249f6b8-397a-11f0-84a1-088fc33f56c7', 'Mechanics', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'),
       ('b253366f-397a-11f0-84a1-088fc33f56c7', 'Organic Chemistry', 'b19fbe33-397a-11f0-84a1-088fc33f56c7'),
       ('b25cd42b-397a-11f0-84a1-088fc33f56c7', 'Genetics', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `course`
CREATE TABLE `course`
(
    `id`                varchar(36)   NOT NULL,
    `title`             varchar(255)  NOT NULL,
    `duration`          float         NOT NULL,
    `status`            tinyint(1)    NOT NULL DEFAULT TRUE,
    `description`       varchar(500)  NOT NULL,
    `created_date`      date          NOT NULL,
    `updated_date`      date          NULL,
    `thumbnail_url`     varchar(1000) NULL,
    `number_of_lessons` int           NOT NULL,
    `topic_id`          varchar(36)   NOT NULL,
    `contact`           varchar(36)   NOT NULL,
    `expert_id`         varchar(36)   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKCourse6215` (`contact`),
    KEY `FKCourse968779` (`topic_id`),
    KEY `fk_course_expert` (`expert_id`),
    CONSTRAINT `fk_course_expert` FOREIGN KEY (`expert_id`) REFERENCES `account` (`id`),
    CONSTRAINT `FKCourse6215` FOREIGN KEY (`contact`) REFERENCES `contact` (`id`),
    CONSTRAINT `FKCourse968779` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
);
INSERT INTO `course`
VALUES ('b2b861ab-397a-11f0-84a1-088fc33f56c7', 'Introduction to Algebra', 10.5, 1, 'Covers basic algebraic concepts.',
        '2023-03-01', '2023-03-10', 'img/algebra_course.jpg',
        15, 'b240fae5-397a-11f0-84a1-088fc33f56c7', 'b19b605b-397a-11f0-84a1-088fc33f56c7',
        'b283bfb8-397a-11f0-84a1-088fc33f56c7'),
       ('b2bffc19-397a-11f0-84a1-088fc33f56c7', 'Advanced Data Structures', 20, 1,
        'Deep dive into complex data structures.', '2023-04-01', '2023-04-15',
        'img/ds_course.jpg', 25,
        'b24459d6-397a-11f0-84a1-088fc33f56c7', 'b19bb3b8-397a-11f0-84a1-088fc33f56c7',
        'b287bbd5-397a-11f0-84a1-088fc33f56c7'),
       ('b2c30ab5-397a-11f0-84a1-088fc33f56c7', 'Classical Mechanics', 15, 1, 'Fundamental principles of mechanics.',
        '2023-05-01', '2023-05-10',
        'img/mechanics_course.jpg', 20,
        'b249f6b8-397a-11f0-84a1-088fc33f56c7', 'b19bb4fc-397a-11f0-84a1-088fc33f56c7',
        'b28b4752-397a-11f0-84a1-088fc33f56c7'),
       ('b2c629c9-397a-11f0-84a1-088fc33f56c7', 'Organic Chemistry I', 18, 1, 'Introduction to organic compounds.',
        '2023-06-01', '2023-06-12', 'img/orgchem_course.jpg',
        22, 'b253366f-397a-11f0-84a1-088fc33f56c7', 'b19bb588-397a-11f0-84a1-088fc33f56c7',
        'b2932a61-397a-11f0-84a1-088fc33f56c7'),
       ('b2cea174-397a-11f0-84a1-088fc33f56c7', 'Basic Genetics', 12, 1, 'Understanding heredity and genes.',
        '2023-07-01', '2023-07-08', 'img/genetics_course.jpg',
        18, 'b25cd42b-397a-11f0-84a1-088fc33f56c7', 'b19bb60c-397a-11f0-84a1-088fc33f56c7',
        'b2971e45-397a-11f0-84a1-088fc33f56c7'),
       ('b2cea175-397a-11f0-84a1-088fc33f56c7', 'Data Architecture And Algorithms ', 60, 1,
        'Understanding about Data architecture and Algorithms', '2023-07-01', '2023-07-08',
        'img/genetics_course.jpg', 18,
        'b25cd42b-397a-11f0-84a1-088fc33f56c7', 'b19bb60c-397a-11f0-84a1-088fc33f56c7',
        'b2971e45-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `lesson`
CREATE TABLE `lesson`
(
    `id`             varchar(36)  NOT NULL,
    `status`         tinyint(1)   NOT NULL DEFAULT TRUE,
    `name`           varchar(255) NOT NULL,
    `course_id`      varchar(36)  NOT NULL,
    `lesson_type_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKLesson764776` (`course_id`),
    KEY `lesson types` (`lesson_type_id`),
    CONSTRAINT `FKLesson764776` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
    CONSTRAINT `lesson types` FOREIGN KEY (`lesson_type_id`) REFERENCES `setting` (`id`)
);
INSERT INTO `lesson`
VALUES ('b2de3b10-397a-11f0-84a1-088fc33f56c7', 1, 'Algebra Fundamentals', 'b2b861ab-397a-11f0-84a1-088fc33f56c7',
        'b1a7d752-397a-11f0-84a1-088fc33f56c7'),
       ('b2e2cb06-397a-11f0-84a1-088fc33f56c7', 1, 'Quiz: Basic Algebra', 'b2b861ab-397a-11f0-84a1-088fc33f56c7',
        'b1abcc32-397a-11f0-84a1-088fc33f56c7'),
       ('b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 1, 'Linked Lists', 'b2bffc19-397a-11f0-84a1-088fc33f56c7',
        'b1a7d752-397a-11f0-84a1-088fc33f56c7'),
       ('b2ef4be4-397a-11f0-84a1-088fc33f56c7', 1, 'Quiz: Data Structures I', 'b2bffc19-397a-11f0-84a1-088fc33f56c7',
        'b1abcc32-397a-11f0-84a1-088fc33f56c7'),
       ('b2f36d41-397a-11f0-84a1-088fc33f56c7', 1, 'Newton\'s Laws', 'b2c30ab5-397a-11f0-84a1-088fc33f56c7',
        'b1a7d752-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `learningmaterial`
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
);
INSERT INTO `learningmaterial`
VALUES ('b2de3b10-397a-11f0-84a1-088fc33f56c7', 'Algebra Basics Video', '2024-01-20', 60,
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

-- Table structure for table `notification`
CREATE TABLE `notification`
(
    `id`         varchar(36)  NOT NULL,
    `link`       varchar(255) NOT NULL,
    `message`    varchar(255) NOT NULL,
    `title`      varchar(255) NOT NULL,
    `status`     tinyint(1)   NOT NULL DEFAULT FALSE,
    `account_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKNotificati558491` (`account_id`),
    CONSTRAINT `FKNotificati558491` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
);
INSERT INTO `notification`
VALUES ('b3c6096d-397a-11f0-84a1-088fc33f56c7', '[http://example.com/new_course](http://example.com/new_course)',
        'A new course on AI is available!', 'New Course Alert', 1, 'b283bfb8-397a-11f0-84a1-088fc33f56c7'),
       ('b3c60e91-397a-11f0-84a1-088fc33f56c7', '[http://example.com/quiz_result](http://example.com/quiz_result)',
        'Your quiz results are ready.', 'Quiz Results', 1, 'b287bbd5-397a-11f0-84a1-088fc33f56c7'),
       ('b3c60fea-397a-11f0-84a1-088fc33f56c7', '[http://example.com/update](http://example.com/update)',
        'System maintenance scheduled for tomorrow.', 'System Update', 0, 'b28b4752-397a-11f0-84a1-088fc33f56c7'),
       ('b3c6109b-397a-11f0-84a1-088fc33f56c7', '[http://example.com/promo](http://example.com/promo)',
        'Get 20% off all courses this week!', 'Limited Time Offer', 1, 'b2932a61-397a-11f0-84a1-088fc33f56c7'),
       ('b3c61463-397a-11f0-84a1-088fc33f56c7', '[http://example.com/forum](http://example.com/forum)',
        'Someone replied to your forum post.', 'Forum Activity', 1, 'b2971e45-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `quizlevel`
CREATE TABLE `quizlevel`
(
    `id`   varchar(36) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);
INSERT INTO `quizlevel`
VALUES ('b18d1436-397a-11f0-84a1-088fc33f56c7', 'Beginner'),
       ('b18d5f4d-397a-11f0-84a1-088fc33f56c7', 'Intermediate'),
       ('b18d62ce-397a-11f0-84a1-088fc33f56c7', 'Advanced'),
       ('b18d634d-397a-11f0-84a1-088fc33f56c7', 'Expert'),
       ('b18d63a5-397a-11f0-84a1-088fc33f56c7', 'Master');

-- Table structure for table `quiztype`
CREATE TABLE `quiztype`
(
    `id`   varchar(36) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);
INSERT INTO `quiztype`
VALUES ('b1927165-397a-11f0-84a1-088fc33f56c7', 'Multiple Choice'),
       ('b1927452-397a-11f0-84a1-088fc33f56c7', 'True/False'),
       ('b1927519-397a-11f0-84a1-088fc33f56c7', 'Fill-in-the-blank'),
       ('b1927581-397a-11f0-84a1-088fc33f56c7', 'Short Answer'),
       ('b19275d8-397a-11f0-84a1-088fc33f56c7', 'Matching');

-- Table structure for table `quiz`
CREATE TABLE `quiz`
(
    `id`                 varchar(36)  NOT NULL,
    `duration`           int          NOT NULL,
    `status`             tinyint(1)   NOT NULL DEFAULT FALSE, # khong chon gi auto private
    `pass_rate`          float                 DEFAULT NULL,
    `updated_date`       date         NOT NULL,
    `number_of_question` int          NOT NULL DEFAULT 0,
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
);
-- Bản ghi hợp lệ với subject_id từ bảng `subject`
INSERT INTO quiz (id, duration, status, pass_rate, updated_date, number_of_question, description, title, subject_id,
                  type, level)
VALUES
-- Intro to Algebra Concepts
('b2de3b10-397a-11f0-84a1-088fc33f56c7', 20, 1, 0.6, '2024-01-20', 8, 'Introduction to algebraic expressions.',
 'Intro to Algebra Concepts', 'b19fb878-397a-11f0-84a1-088fc33f56c7', 'b1927452-397a-11f0-84a1-088fc33f56c7',
 'b18d1436-397a-11f0-84a1-088fc33f56c7'),

-- Algebra Basics Quiz
('b2e2cb06-397a-11f0-84a1-088fc33f56c7', 30, 0, 0.7, '2024-01-25', 10, 'A quiz on fundamental algebra concepts.',
 'Algebra Basics Quiz', 'b19fb878-397a-11f0-84a1-088fc33f56c7', 'b1927165-397a-11f0-84a1-088fc33f56c7',
 'b18d1436-397a-11f0-84a1-088fc33f56c7'),

-- Linked List Deep Dive
('b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 35, 1, 0.65, '2024-02-10', 12,
 'Questions on different types of linked lists.', 'Linked List Deep Dive',
 'b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'b1927519-397a-11f0-84a1-088fc33f56c7',
 'b18d5f4d-397a-11f0-84a1-088fc33f56c7'),

-- Data Structures Quiz 1
('b2ef4be4-397a-11f0-84a1-088fc33f56c7', 45, 0, 0.75, '2024-02-01', 15,
 'Test your knowledge on linked lists and arrays.', 'Data Structures Quiz 1',
 'b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'b1927165-397a-11f0-84a1-088fc33f56c7',
 'b18d5f4d-397a-11f0-84a1-088fc33f56c7'),

-- Newtonian Mechanics Quiz
('b2f36d41-397a-11f0-84a1-088fc33f56c7', 60, 1, 0.8, '2024-03-01', 20,
 'Comprehensive quiz on Newton\'s Laws of Motion.', 'Newtonian Mechanics Quiz',
 'b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'b1927165-397a-11f0-84a1-088fc33f56c7',
 'b18d5f4d-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `question`
CREATE TABLE `question`
(
    `id`       varchar(36)  NOT NULL,
    `content`  varchar(255) NOT NULL,
    `topic_id` varchar(36)  NOT NULL,
    `quiz_id`  varchar(36)  NOT NULL,
    `level`    varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKQuestion523038` (`topic_id`),
    KEY `FKQuestion112697` (`quiz_id`),
    CONSTRAINT `FKQuestion112697` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`),
    CONSTRAINT `FKQuestion523038` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
);
INSERT INTO `question`
VALUES ('b3240dfd-397a-11f0-84a1-088fc33f56c7', 'What is 2 + 2?', 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        'b2e2cb06-397a-11f0-84a1-088fc33f56c7', 'Beginner'),
       ('b329e268-397a-11f0-84a1-088fc33f56c7', 'Explain the concept of a linked list.',
        'b24459d6-397a-11f0-84a1-088fc33f56c7', 'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 'Intermediate'),
       ('b32fda1f-397a-11f0-84a1-088fc33f56c7', 'State Newton\'s first law of motion.',
        'b249f6b8-397a-11f0-84a1-088fc33f56c7', 'b2f36d41-397a-11f0-84a1-088fc33f56c7', 'Intermediate'),
       ('b3348944-397a-11f0-84a1-088fc33f56c7', 'Solve for x: 3x - 5 = 10', 'b240fae5-397a-11f0-84a1-088fc33f56c7',
        'b2e2cb06-397a-11f0-84a1-088fc33f56c7', 'Beginner'),
       ('b3388f76-397a-11f0-84a1-088fc33f56c7', 'What is the primary difference between an array and a linked list?',
        'b24459d6-397a-11f0-84a1-088fc33f56c7', 'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 'Intermediate');

-- Table structure for table `option`
CREATE TABLE `option`
(
    `id`          varchar(36)  NOT NULL,
    `content`     varchar(255) NOT NULL,
    `is_true`     tinyint(1)   NOT NULL DEFAULT FALSE,
    `explanation` varchar(255) NOT NULL,
    `question_id` varchar(36)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKOption204933` (`question_id`),
    CONSTRAINT `FKOption204933` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);
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
        'b3348944-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `personalcourse`
CREATE TABLE `personalcourse`
(
    `id`          varchar(36)  NOT NULL,

    `account_id`  varchar(36)  NOT NULL,
    `course_id`   varchar(36)  NOT NULL,
    `expire_date` date                  DEFAULT NULL,
    `enroll_date` date         NOT NULL,
    `progress`    int          NOT NULL DEFAULT '0',
    `status`      varchar(255) NULL     DEFAULT 'SENT',
    PRIMARY KEY (`id`),
    KEY `FKPersonalCo602046` (`course_id`),
    CONSTRAINT `FKPersonalCo602046` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
    CONSTRAINT `FKPersonalCo728981` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `personalcourse_chk_1` CHECK ((`status` in
                                              (_utf8mb4'PAID', _utf8mb4'SENT', _utf8mb4'ACCEPTED', _utf8mb4'DECLINED')))
);
INSERT INTO `personalcourse`
VALUES (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2b861ab-397a-11f0-84a1-088fc33f56c7', '2024-06-01',
        '2023-01-10', 50,
        'PAID'),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2bffc19-397a-11f0-84a1-088fc33f56c7', '2024-06-01',
        '2023-01-10', 50,
        'PAID'),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2c30ab5-397a-11f0-84a1-088fc33f56c7', '2024-10-01',
        '2023-02-15', 20,
        'SENT'),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2c629c9-397a-11f0-84a1-088fc33f56c7', '2024-08-01',
        '2023-03-20', 80,
        'ACCEPTED'),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2cea174-397a-11f0-84a1-088fc33f56c7', '2024-12-01',
        '2023-04-25', 10,
        NULL),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2cea175-397a-11f0-84a1-088fc33f56c7', '2024-09-01',
        '2023-05-01', 0,
        NULL),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b2bffc19-397a-11f0-84a1-088fc33f56c7', '2024-10-01',
        '2023-02-15', 20,
        'SENT'),
       (UUID(), 'b28b4752-397a-11f0-84a1-088fc33f56c7', 'b2c30ab5-397a-11f0-84a1-088fc33f56c7', '2024-08-01',
        '2023-03-20', 80,
        'ACCEPTED'),
       (UUID(), 'b2932a61-397a-11f0-84a1-088fc33f56c7', 'b2c629c9-397a-11f0-84a1-088fc33f56c7', '2024-12-01',
        '2023-04-25', 10,
        NULL),
       (UUID(), 'b2971e45-397a-11f0-84a1-088fc33f56c7', 'b2c629c9-397a-11f0-84a1-088fc33f56c7', '2024-10-01',
        '2023-02-15', 20,
        'SENT'),
       (UUID(), 'b2971e45-397a-11f0-84a1-088fc33f56c7', 'b2cea174-397a-11f0-84a1-088fc33f56c7', '2024-09-01',
        '2023-05-01', 0,
        NULL);

-- Table structure for table `personallesson`
CREATE TABLE `personallesson`
(
    `id`         varchar(36) NOT NULL,

    `account_id` varchar(36) NOT NULL,
    `lesson_id`  varchar(36) NOT NULL,
    `isDone`     tinyint(1)  NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    KEY `FKPersonalLe846456` (`lesson_id`),
    CONSTRAINT `FKPersonalLe846456` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`id`),
    CONSTRAINT `FKPersonalLe96067` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
);
INSERT INTO `personallesson`
VALUES (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2de3b10-397a-11f0-84a1-088fc33f56c7', 1),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2e2cb06-397a-11f0-84a1-088fc33f56c7', 0),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b2e6bbf1-397a-11f0-84a1-088fc33f56c7', 1),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 0),
       (UUID(), 'b28b4752-397a-11f0-84a1-088fc33f56c7', 'b2f36d41-397a-11f0-84a1-088fc33f56c7', 1);

-- Table structure for table `personaloption`
CREATE TABLE `personaloption`
(
    `id`         varchar(36) NOT NULL,
    `option_id`  varchar(36) NOT NULL,
    `account_id` varchar(36) NOT NULL,
    `has_chosen` tinyint(1)  NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    KEY `FKPersonalOp32617` (`account_id`),
    CONSTRAINT `FKPersonalOp130168` FOREIGN KEY (`option_id`) REFERENCES `option` (`id`),
    CONSTRAINT `FKPersonalOp32617` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
);
INSERT INTO `personaloption`
VALUES (UUID(), 'b347e001-397a-11f0-84a1-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 1),
       (UUID(), 'b34f104c-397a-11f0-84a1-088fc33f56c7', 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 0),
       (UUID(), 'b3544f73-397a-11f0-84a1-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 1),
       (UUID(), 'b35b5770-397a-11f0-84a1-088fc33f56c7', 'b28b4752-397a-11f0-84a1-088fc33f56c7', 1),
       (UUID(), 'b35f143b-397a-11f0-84a1-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 1);

-- Table structure for table `personalquestion`
CREATE TABLE `personalquestion`
(
    `id`          varchar(36) NOT NULL,
    `account_id`  varchar(36) NOT NULL,
    `question_id` varchar(36) NOT NULL,
    `has_answer`  tinyint(1)  NOT NULL DEFAULT FALSE,
    `is_mark`     tinyint(1)  NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    KEY `FKPersonalQu428012` (`question_id`),
    CONSTRAINT `FKPersonalQu214880` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `FKPersonalQu428012` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);
INSERT INTO `personalquestion`
VALUES (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b3240dfd-397a-11f0-84a1-088fc33f56c7', 1, 0),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b3240dfd-397a-11f0-84a1-088fc33f56c7', 0, 1),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b329e268-397a-11f0-84a1-088fc33f56c7', 1, 0),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b329e268-397a-11f0-84a1-088fc33f56c7', 1, 0),
       (UUID(), 'b28b4752-397a-11f0-84a1-088fc33f56c7', 'b3348944-397a-11f0-84a1-088fc33f56c7', 1, 0);

-- Table structure for table `personalquiz`
CREATE TABLE `personalquiz`
(
    `id`                         varchar(36) NOT NULL,
    `account_id`                 varchar(36) NOT NULL,
    `quiz_id`                    varchar(36) NOT NULL,
    `has_passed`                 tinyint(1)  NOT NULL DEFAULT FALSE,
    `mark`                       int                  DEFAULT NULL,
    `number_of_correct_question` int                  DEFAULT NULL,
    `taken_date`                 date        NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FKPersonalQu344927` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
    CONSTRAINT `FKPersonalQu344926` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
);
INSERT INTO `personalquiz`
VALUES (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2de3b10-397a-11f0-84a1-088fc33f56c7', 0, 6, 6, '2024-03-01'),
       (UUID(), 'b283bfb8-397a-11f0-84a1-088fc33f56c7', 'b2de3b10-397a-11f0-84a1-088fc33f56c7', 0, 5, 5, '2024-05-10'),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b2f36d41-397a-11f0-84a1-088fc33f56c7', 0, 7, 7, '2024-05-15'),
       (UUID(), 'b287bbd5-397a-11f0-84a1-088fc33f56c7', 'b2f36d41-397a-11f0-84a1-088fc33f56c7', 0, 8, 8, '2024-04-01'),
       (UUID(), 'b28b4752-397a-11f0-84a1-088fc33f56c7', 'b2ef4be4-397a-11f0-84a1-088fc33f56c7', 1, 18, 18,
        '2024-05-01');

-- Table structure for table `pricepackage`
CREATE TABLE `pricepackage`
(
    `id`              varchar(36)  NOT NULL,
    `course_id`       varchar(36)  NOT NULL,
    `title`           varchar(255) NOT NULL,
    `price`           int          NOT NULL,
    `sale_price`      int          NOT NULL,
    `access_duration` int          NOT NULL,
    `status`          tinyint(1)   NOT NULL DEFAULT FALSE,
    `description`     varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`, `course_id`),
    KEY `FKPricePacka152688` (`course_id`),
    CONSTRAINT `FKPricePacka152688` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
);
INSERT INTO `pricepackage`
VALUES ('b2d700aa-397a-11f0-84a1-088fc33f56c7', 'b2b861ab-397a-11f0-84a1-088fc33f56c7', 'Basic Access Algebra', 500000,
        450000, 90, 1, '90 days access to Algebra course'),
       ('b2d70810-397a-11f0-84a1-088fc33f56c7', 'b2b861ab-397a-11f0-84a1-088fc33f56c7', 'Premium Access Algebra',
        900000, 750000, 365, 1, 'Full year access to Algebra course with bonus content'),
       ('b2d70ab6-397a-11f0-84a1-088fc33f56c7', 'b2bffc19-397a-11f0-84a1-088fc33f56c7', 'Standard DS Package', 700000,
        600000, 180, 1, '180 days access to Data Structures course'),
       ('b2d70c43-397a-11f0-84a1-088fc33f56c7', 'b2c30ab5-397a-11f0-84a1-088fc33f56c7', 'Mechanics Starter', 400000,
        350000, 60, 1, '60 days access to Mechanics course'),
       ('b2d70d9c-397a-11f0-84a1-088fc33f56c7', 'b2c629c9-397a-11f0-84a1-088fc33f56c7', 'Organic Chem Pro', 800000,
        700000, 270, 1, '270 days access to Organic Chemistry I');

-- Table structure for table `setting_question`
CREATE TABLE `setting_question`
(
    `setting_id`  varchar(36) NOT NULL,
    `question_id` varchar(36) NOT NULL,
    PRIMARY KEY (`setting_id`, `question_id`),
    KEY `question level` (`question_id`),
    CONSTRAINT `question level` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
    CONSTRAINT `test type` FOREIGN KEY (`setting_id`) REFERENCES `setting` (`id`)
);
INSERT INTO `setting_question`
VALUES ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b3240dfd-397a-11f0-84a1-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b329e268-397a-11f0-84a1-088fc33f56c7'),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', 'b32fda1f-397a-11f0-84a1-088fc33f56c7'),
       ('b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b3348944-397a-11f0-84a1-088fc33f56c7'),
       ('b1b320dd-397a-11f0-84a1-088fc33f56c7', 'b3388f76-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `setting_subject`
CREATE TABLE `setting_subject`
(
    `setting_id` varchar(36) NOT NULL,
    `subject_id` varchar(36) NOT NULL,
    PRIMARY KEY (`setting_id`, `subject_id`),
    KEY `FKSetting_Su420121` (`subject_id`),
    CONSTRAINT `FKSetting_Su420121` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`),
    CONSTRAINT `Subject dimension` FOREIGN KEY (`setting_id`) REFERENCES `setting` (`id`)
);
INSERT INTO `setting_subject`
VALUES ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbe33-397a-11f0-84a1-088fc33f56c7'),
       ('b1b69765-397a-11f0-84a1-088fc33f56c7', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `slider`
CREATE TABLE `slider`
(
    `id`           varchar(36)  NOT NULL,
    `title`        varchar(255) NOT NULL,
    `image_url`    varchar(500) NOT NULL,
    `backlink_url` varchar(500)          DEFAULT NULL,
    `status`       tinyint(1)   NOT NULL DEFAULT TRUE,
    `account_id`   varchar(36)  NOT NULL,
    `note`         varchar(500)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKSlider777469` (`account_id`),
    CONSTRAINT `FKSlider777469` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
);
INSERT INTO `slider`
VALUES ('b3dcacb3-397a-11f0-84a1-088fc33f56c7', 'Spring Promotion', 'img/slider1.jpg',
        'http://example.com/promo1', 1, 'b283bfb8-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb132-397a-11f0-84a1-088fc33f56c7', 'New Courses Available', 'img/slider2.jpg',
        'http://example.com/new_courses', 1, 'b287bbd5-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb2ee-397a-11f0-84a1-088fc33f56c7', 'Big Sale Event', 'img/slider3.jpg',
        'http://example.com/sale', 0, 'b28b4752-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb3cc-397a-11f0-84a1-088fc33f56c7', 'Emerging Web Development Trends in 2024', 'img/slider4.jpg',
        'http://example.com/partners', 1, 'b2932a61-397a-11f0-84a1-088fc33f56c7', NULL),
       ('b3dcb470-397a-11f0-84a1-088fc33f56c7', 'Effective Study Habits for Lifelong Learning', 'img/slider5.jpg',
        'http://example.com/testimonials', 1,
        'b2971e45-397a-11f0-84a1-088fc33f56c7', NULL);

-- Table structure for table `tagline`
CREATE TABLE `tagline`
(
    `id`   varchar(36)  NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);
INSERT INTO `tagline`
VALUES ('b1970363-397a-11f0-84a1-088fc33f56c7', 'Learn Anywhere'),
       ('b19706b5-397a-11f0-84a1-088fc33f56c7', 'Master New Skills'),
       ('b19707dc-397a-11f0-84a1-088fc33f56c7', 'Unlock Your Potential'),
       ('b197083e-397a-11f0-84a1-088fc33f56c7', 'Future-Proof Your Career'),
       ('b1970897-397a-11f0-84a1-088fc33f56c7', 'Knowledge at Your Fingertips');

-- Table structure for table `subject_tagline`
CREATE TABLE `subject_tagline`
(
    `subject_id` varchar(36) NOT NULL,
    `tagline_id` varchar(36) NOT NULL,
    PRIMARY KEY (`subject_id`, `tagline_id`),
    KEY `FKSubject_Ta221833` (`tagline_id`),
    CONSTRAINT `FKSubject_Ta221833` FOREIGN KEY (`tagline_id`) REFERENCES `tagline` (`id`),
    CONSTRAINT `FKSubject_Ta283943` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
);
INSERT INTO `subject_tagline`
VALUES ('b19fb878-397a-11f0-84a1-088fc33f56c7', 'b1970363-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbc5e-397a-11f0-84a1-088fc33f56c7', 'b19706b5-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbdc2-397a-11f0-84a1-088fc33f56c7', 'b19707dc-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbe33-397a-11f0-84a1-088fc33f56c7', 'b197083e-397a-11f0-84a1-088fc33f56c7'),
       ('b19fbe8f-397a-11f0-84a1-088fc33f56c7', 'b1970897-397a-11f0-84a1-088fc33f56c7');

-- Table structure for table `userlog`
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
INSERT INTO `userlog`
VALUES ('b3d89048-397a-11f0-84a1-088fc33f56c7', '2024-05-20', 'b283bfb8-397a-11f0-84a1-088fc33f56c7'),
       ('b3d8976b-397a-11f0-84a1-088fc33f56c7', '2024-05-20', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'),
       ('b3d898fc-397a-11f0-84a1-088fc33f56c7', '2024-05-21', 'b28b4752-397a-11f0-84a1-088fc33f56c7'),
       ('b3d89a77-397a-11f0-84a1-088fc33f56c7', '2024-05-21', 'b2932a61-397a-11f0-84a1-088fc33f56c7'),
       ('b3d89ba0-397a-11f0-84a1-088fc33f56c7', '2024-05-22', 'b2971e45-397a-11f0-84a1-088fc33f56c7');

-- Records for settingtype table
INSERT INTO `settingtype` (`id`, `name`)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'User Role'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Blog Category'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Lesson Type'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Quiz Level'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Quiz Type'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Domain'),
       ('a0eebc98-9c0b-4ef8-bb6d-6bb9bd380a16', 'Group'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Question Level'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Course Status'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Personal Course Status'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a10', 'Blog Status');

-- Records for setting table
SET @setting_id1 = UUID();
SET @setting_id2 = UUID();
SET @setting_id3 = UUID();
SET @setting_id4 = UUID();
INSERT INTO `setting` (`id`, `value`, `status`, `description`, `updated_date`, `setting_type_id`)
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'Admin', 1, 'Administrator role', '2023-01-01',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Expert', 1, 'Expert user role', '2023-01-01',
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
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Practice', 1, 'Quiz type: practice', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15'),
       (@setting_id1, 'Business', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc98-9c0b-4ef8-bb6d-6bb9bd380a16'), # Dimension of subject
       (@setting_id2, 'Process', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc98-9c0b-4ef8-bb6d-6bb9bd380a16'), # Dimension of subject
       (@setting_id3, 'Initialing', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16'), # Dimension of subject
       (@setting_id4, 'Planning', 1, 'Dimension of subject', '2023-04-10',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16');
# Dimension of subject

-- Records for account table
Set @account_id1 = UUID();
Set @account_id2 = UUID();
Set @account_id3 = UUID();
Set @account_id4 = UUID();
Set @account_id5 = UUID();
Set @account_id6 = UUID();
Set @account_id7 = UUID();
Set @account_id8 = UUID();
Set @account_id9 = UUID();
Set @account_id10 = UUID();
INSERT INTO `account` (`id`, `email`, `full_name`, `password`, `dob`, `gender`, `created_date`, `status`, `phone`,
                       `image_url`, `role_id`)
VALUES (@account_id1, 'john.doe@example.com', 'John Doe', 'Abc@12345', '1990-05-15', 1,
        '2023-01-01', 1, '+84123456789', 'img/john.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       (@account_id2, 'user1@example.com', 'Jane Smith', 'Xyz@56789', '1985-11-20', 0,
        '2023-01-05', 1, '+84987654321', 'img/jane.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       (@account_id3, 'user2@example.com', 'Peter Jones', 'Pqr!12345', '1992-03-25', 1,
        '2023-01-10', 1, '+84777888999', 'img/peter.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       (@account_id4, 'user3@example.com', 'Alice Brown', 'Def#67890', '1998-07-01', 0,
        '2023-01-15', 1, '+84333444555', 'img/alice.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21'),
       (@account_id5, 'user4@example.com', 'Bob White', 'Ghi$12345', '1980-01-30', 1,
        '2023-01-20', 1, '+84666777888', 'img/bob.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       (@account_id6, 'user5@example.com', 'Charlie Green', 'Jkl%98765', '1995-09-10',
        1, '2023-01-25', 1, '+84111222333', 'img/charlie.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       (@account_id7, 'user6@example.com', 'Diana Ross', 'Mno^54321', '1987-04-05', 0,
        '2023-01-30', 1, '+84555666777', 'img/diana.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       (@account_id8, 'user7@example.com', 'Frank Adams', 'Pqr&87654', '1993-12-12', 1,
        '2023-02-01', 1, '+84999000111', 'img/frank.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23'),
       (@account_id9, 'user8@example.com', 'Grace Lee', 'Stu*34567', '1991-06-28', 0,
        '2023-02-05', 1, '+84222333444', 'img/grace.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22'),
       (@account_id10, 'user9@example.com', 'Henry King', 'Vwx(90123', '1982-08-08', 1,
        '2023-02-10', 1, '+84888999000', 'img/henry.jpg', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23');

-- Records for blog table
INSERT INTO `blog` (`id`, `updated_date`, `thumbnail_url`, `title`, `brief_info`, `content`, `status`, `created_date`,
                    `category`, `account_id`, `flag_feature`, `views`)
VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a31', '2023-03-01', 'img/thumb1.jpg', 'The Future of AI',
        'A brief overview of AI advancements.', 'Detailed content about AI and its future.', 1, '2023-02-20',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @account_id2, 1, 1500),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a32', '2023-03-05', 'img/thumb2.jpg', 'Healthy Eating Habits',
        'Tips for a healthier lifestyle.', 'Comprehensive guide to healthy eating.', 1, '2023-02-25',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @account_id1, 0, 2300),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', '2023-03-10', 'img/thumb3.jpg', 'Introduction to Python',
        'Get started with Python programming.', 'Basics of Python programming for beginners.', 1, '2023-03-01',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @account_id3, 1, 800),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a34', '2023-03-15', 'img/thumb4.jpg',
        'Mindfulness for Beginners', 'Simple practices to achieve mindfulness.',
        'Daily exercises for mindfulness and stress reduction.', 1, '2023-03-05',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @account_id2, 0, 1200),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a35', '2023-03-20', 'img/thumb5.jpg', 'Web Development Trends',
        'Latest trends in web development.', 'Discussing modern web development technologies.', 1, '2023-03-10',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @account_id4, 1, 1900),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a36', '2023-03-25', 'img/thumb6.jpg', 'Benefits of Regular Exercise',
        'Why staying active is crucial.', 'Impact of exercise on physical and mental health.', 1, '2023-03-15',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @account_id3, 0, 2500),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a37', '2023-03-30', 'img/thumb7.jpg', 'Understanding Blockchain',
        'A simple explanation of blockchain technology.', 'How blockchain works and its applications.', 1, '2023-03-20',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @account_id5, 0, 1100),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a38', '2023-04-01', 'img/thumb8.jpg',
        'Travel Tips for Solo Explorers', 'Essential advice for traveling alone.',
        'Safety and enjoyment tips for solo travelers.', 1, '2023-03-25', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25',
        @account_id4, 1, 1700),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a39', '2023-04-05', 'img/thumb9.jpg',
        'Cybersecurity Best Practices', 'Protecting your digital life.', 'Guidelines for maintaining online security.',
        1, '2023-03-30', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @account_id8, 0, 2100),
       ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a30', '2023-04-10', 'img/thumb10.jpg', 'Home Gardening Guide',
        'Start your own vegetable garden.', 'Detailed steps for successful home gardening.', 1, '2023-04-01',
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @account_id5, 0, 950);

-- Records for chat table
INSERT INTO `chat` (`id`, `first_person`, `second_person`)
VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', @account_id1,
        @account_id2),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a42', @account_id1,
        @account_id4),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a43', @account_id7,
        @account_id6),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', @account_id8,
        @account_id8),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45', @account_id2,
        @account_id7),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a46', @account_id2,
        @account_id3),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a47', @account_id1,
        @account_id1),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a48', @account_id3,
        @account_id1),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a49', @account_id8,
        @account_id6),
       ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a40', @account_id2,
        @account_id4);

-- Records for chatdetails table
INSERT INTO `chatdetails` (`id`, `message`, `timestamp`, `sender`, `chat_id`)
VALUES ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51', 'Hi John, how are you?', '2023-05-01', 'Jane Smith',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52', 'I am good, thanks! How about you?', '2023-05-01', 'John Doe',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53', 'Checking in on your progress.', '2023-05-02', 'Peter Jones',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a42'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54', 'All good here, thanks for asking!', '2023-05-02', 'John Doe',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a42'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'Meeting at 10 AM tomorrow.', '2023-05-03', 'Alice Brown',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a43'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56', 'Got it, see you then.', '2023-05-03', 'Jane Smith',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a43'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a57', 'Can you send me the report?', '2023-05-04', 'Bob White',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a58', 'Sure, sending it now.', '2023-05-04', 'Charlie Green',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a59', 'How is the new project going?', '2023-05-05', 'Diana Ross',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45'),
       ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a50', 'It is on track, thanks!', '2023-05-05', 'Frank Adams',
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45');

-- Records for contact table
INSERT INTO `contact` (`id`, `name`, `link`, `email`, `phone`, `address`)
VALUES ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'Tech Solutions Inc.', '{
  "website": "https://techsolutions.com",
  "linkedin": "https://linkedin.com/company/techsolutions"
}', 'info@techsolutions.com', '0901112223', '123 Tech Avenue, Innovation City'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'Global Education Ltd.', '{
         "website": "https://globaledu.org",
         "facebook": "https://facebook.com/globaledu"
       }', 'contact@globaledu.org', '0912223334', '456 Learning Street, Knowledge Town'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63', 'Innovate Consulting', '{
         "website": "https://innovateconsulting.net",
         "twitter": "https://twitter.com/innovatecon"
       }', 'support@innovateconsulting.net', '0923334445', '789 Idea Road, Creative Hub'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64', 'Health & Wellness Co.', '{
         "website": "https://healthwellness.co",
         "instagram": "https://instagram.com/healthwellness"
       }', 'care@healthwellness.co', '0934445556', '101 Health Lane, Vitality Village'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a65', 'Digital Marketing Pros', '{
         "website": "https://digitalmarketing.com",
         "youtube": "https://youtube.com/digitalmarketing"
       }', 'hello@digitalmarketing.com', '0945556667', '202 Digital Boulevard, Marketing City'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66', 'Creative Design Studio', '{
         "website": "https://creativedesign.studio",
         "pinterest": "https://pinterest.com/creativedesign"
       }', 'design@creativedesign.studio', '0956667778', '303 Art Street, Design District'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a67', 'Finance Experts LLC', '{
         "website": "https://financeexperts.com",
         "linkedin": "https://linkedin.com/company/financeexperts"
       }', 'advisory@financeexperts.com', '0967778889', '404 Wealth Plaza, Capital City'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a68', 'Eco-Friendly Solutions', '{
         "website": "https://ecosolutions.org",
         "facebook": "https://facebook.com/ecosolutions"
       }', 'info@ecosolutions.org', '0978889990', '505 Green Avenue, Sustainability Town'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a69', 'Legal Aid Services', '{
         "website": "https://legalaid.com",
         "twitter": "https://twitter.com/legalaid"
       }', 'consult@legalaid.com', '0989990001', '606 Justice Road, Law City'),
       ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a60', 'Event Planners Hub', '{
         "website": "https://eventplanners.com",
         "instagram": "https://instagram.com/eventplanners"
       }', 'plan@eventplanners.com', '0990001112', '707 Celebration Lane, Event Village');

-- Records for subject table

SET @subject_id6 = UUID();
SET @subject_id7 = UUID();
SET @subject_id8 = UUID();
SET @subject_id9 = UUID();
SET @subject_id10 = UUID();
INSERT INTO `subject` (`id`, `name`, `thumbnail_url`, `feature_flag`)
VALUES (@subject_id6, 'History', 'img/history_thumb.jpg', 0),
       (@subject_id7, 'Literature', 'img/literature_thumb.jpg', 0),
       (@subject_id8, 'Economics', 'img/economics_thumb.jpg', 0),
       (@subject_id9, 'Art & Design', 'img/art_thumb.jpg', 1),
       (@subject_id10, 'Music', 'img/music_thumb.jpg', 1);

-- Records for topic table
INSERT INTO `topic` (`id`, `name`, `subject_id`)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', 'Algebra', @subject_id1),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', 'Calculus', @subject_id1),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', 'Mechanics', @subject_id2),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', 'Thermodynamics', @subject_id2),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', 'Organic Chemistry', @subject_id3),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', 'Data Structures', @subject_id4),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', 'Genetics', @subject_id5),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', 'World History', @subject_id6),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', 'Poetry', @subject_id7),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80', 'Microeconomics', @subject_id8);
-- Records for course table
SET @course_id1 = UUID();
SET @course_id2 = UUID();
SET @course_id3 = UUID();
SET @course_id4 = UUID();
SET @course_id5 = UUID();
SET @course_id6 = UUID();
SET @course_id7 = UUID();
SET @course_id8 = UUID();
SET @course_id9 = UUID();
SET @course_id10 = UUID();
INSERT INTO `course` (`id`, `title`, `duration`, `status`, `description`, `created_date`, `updated_date`,
                      `thumbnail_url`, `number_of_lessons`, `topic_id`, `contact`, `expert_id`)
VALUES (@course_id1, 'Advanced Algebra', 15.5, 1,
        'In-depth study of advanced algebraic concepts.', '2023-01-10', '2023-01-20',
        'http://example.com/algebra_course.jpg', 20, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id1),
       (@course_id2, 'Introduction to Data Structures', 20.0, 1,
        'Learn fundamental data structures and algorithms.', '2023-02-01', '2023-02-15',
        'http://example.com/datastructures_course.jpg', 25, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', @account_id2),
       (@course_id3, 'Classical Mechanics', 18.0, 1,
        'Explore the principles of classical mechanics.', '2023-03-01', '2023-03-10',
        'http://example.com/mechanics_course.jpg', 22, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id3),
       (@course_id4, 'Organic Chemistry Basics', 12.0, 1,
        'Foundational knowledge of organic chemistry.', '2023-04-01', '2023-04-05',
        'http://example.com/organicchem_course.jpg', 18, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id4),
       (@course_id5, 'Calculus I', 25.0, 1,
        'First course in differential and integral calculus.', '2023-05-01', '2023-05-10',
        'http://example.com/calculus_course.jpg', 30, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id5),
       (@course_id6, 'Genetics and Evolution', 17.5, 1,
        'Study of heredity and evolutionary processes.', '2023-06-01', '2023-06-05',
        'http://example.com/genetics_course.jpg', 20, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id6),
       (@course_id7, 'Microeconomics Principles', 14.0, 1,
        'Introduction to economic principles at a micro level.', '2023-07-01', '2023-07-05',
        'http://example.com/microeconomics_course.jpg', 15, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id7),
       (@course_id8, 'World History: Ancient Civilizations', 22.0, 1,
        'Comprehensive look at ancient world history.', '2023-08-01', '2023-08-10',
        'http://example.com/worldhistory_course.jpg', 28, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id3),
       (@course_id9, 'Introduction to Thermodynamics', 16.0, 1,
        'Basic concepts and laws of thermodynamics.', '2023-09-01', '2023-09-05',
        'http://example.com/thermo_course.jpg', 19, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id9),
       (@course_id10, 'Poetry Analysis', 10.0, 1,
        'Techniques for analyzing and understanding poetry.', '2023-10-01', '2023-10-05',
        'http://example.com/poetry_course.jpg', 12, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89',
        'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', @account_id10);

-- Records for lesson table
SET @lession_id1 = UUID();
SET @lession_id2 = UUID();
SET @lession_id3 = UUID();
SET @lession_id4 = UUID();
SET @lession_id5 = UUID();
SET @lession_id6 = UUID();
SET @lession_id7 = UUID();
SET @lession_id8 = UUID();
SET @lession_id9 = UUID();
SET @lession_id10 = UUID();
INSERT INTO `lesson` (`id`, `status`, `name`, `course_id`, `lesson_type_id`)
VALUES (@lession_id1, 1, 'Lesson 1: Introduction to Advanced Algebra',
        @course_id1, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       (@lession_id2, 1, 'Lesson 2: Linear Equations', @course_id1,
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       (@lession_id3, 1, 'Lesson 1: Arrays and Linked Lists',
        @course_id2, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       (@lession_id4, 1, 'Lesson 2: Stacks and Queues',
        @course_id2, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       (@lession_id5, 1, 'Lesson 1: Newton''s Laws', @course_id3,
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       (@lession_id6, 1, 'Lesson 2: Work, Energy, and Power',
        @course_id3, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       (@lession_id7, 1, 'Lesson 1: Hydrocarbons', @course_id4,
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       (@lession_id8, 1, 'Lesson 2: Functional Groups',
        @course_id4, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27'),
       (@lession_id9, 1, 'Lesson 1: Limits and Continuity',
        @course_id5, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26'),
       (@lession_id10, 1, 'Lesson 2: Derivatives', @course_id5,
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27');

-- Records for learningmaterial table
INSERT INTO `learningmaterial` (`id`, `title`, `updated_date`, `duration`, `video_content_url`, `html_content`)
VALUES (@lession_id1, 'Video: Introduction to Advanced Algebra', '2023-01-22', 600,
        'http://example.com/video1.mp4', 'HTML content for video 1'),
       (@lession_id2, 'Text: Linear Equations Explained', '2023-01-23', 300, NULL,
        'HTML content for text 1 on linear equations.'),
       (@lession_id3, 'Video: Understanding Arrays', '2023-02-17', 720,
        'http://example.com/video2.mp4', 'HTML content for video 2'),
       (@lession_id4, 'Text: Stacks and Queues Deep Dive', '2023-02-18', 400, NULL,
        'HTML content for text 2 on stacks and queues.'),
       (@lession_id5, 'Video: Dynamics and Newton''s Laws', '2023-03-12', 900,
        'http://example.com/video3.mp4', 'HTML content for video 3'),
       (@lession_id6, 'Text: Energy Conservation', '2023-03-13', 350, NULL,
        'HTML content for text 3 on energy conservation.'),
       (@lession_id7, 'Video: All About Hydrocarbons', '2023-04-07', 650,
        'http://example.com/video4.mp4', 'HTML content for video 4'),
       (@lession_id8, 'Text: Exploring Functional Groups', '2023-04-08', 380, NULL,
        'HTML content for text 4 on functional groups.'),
       (@lession_id9, 'Video: Limits and Their Properties', '2023-05-12', 800,
        'http://example.com/video5.mp4', 'HTML content for video 5'),
       (@lession_id10, 'Text: Rules of Differentiation', '2023-05-13', 420, NULL,
        'HTML content for text 5 on derivatives.');

-- Records for notification table
SET @notification_id1 = UUID();
SET @notification_id2 = UUID();
SET @notification_id3 = UUID();
SET @notification_id4 = UUID();
SET @notification_id5 = UUID();
SET @notification_id6 = UUID();
SET @notification_id7 = UUID();
SET @notification_id8 = UUID();
SET @notification_id9 = UUID();
SET @notification_id10 = UUID();
INSERT INTO `notification` (`id`, `link`, `message`, `title`, `status`, `account_id`)
VALUES (@notification_id1, 'http://example.com/newcourse',
        'A new course "Calculus II" has been added.', 'New Course Available', 0,
        @account_id1),
       (@notification_id2, 'http://example.com/blogupdate',
        'New blog post: "Mastering Python for Data Science".', 'Blog Update', 0,
        @account_id2),
       (@notification_id3, 'http://example.com/quizready', 'Your weekly quiz is now available!',
        'Quiz Time!', 0, @account_id1),
       (@notification_id4, 'http://example.com/lessoncomplete',
        'You completed Lesson 3 in Data Structures.', 'Lesson Completion', 1, @account_id1),
       (@notification_id5, 'http://example.com/accountupdate',
        'Your profile information has been updated.', 'Profile Update', 1, @account_id2),
       (@notification_id6, 'http://example.com/message', 'You have a new message from Jane Smith.',
        'New Message', 0, @account_id1),
       (@notification_id7, 'http://example.com/coursereminder',
        'Reminder: Your course "Calculus I" starts soon.', 'Course Reminder', 0,
        @account_id1),
       (@notification_id8, 'http://example.com/systemalert',
        'System maintenance scheduled for 2 AM UTC.', 'System Alert', 0, @account_id4),
       (@notification_id9, 'http://example.com/promotion',
        'Exclusive discount on all physics courses!', 'Special Offer', 0, @account_id1),
       (@notification_id10, 'http://example.com/feedback',
        'We would love your feedback on our new feature.', 'Feedback Request', 0,
        @account_id2);

-- Records for quizlevel table
SET @quizlevel_id1 = UUID();
SET @quizlevel_id2 = UUID();
SET @quizlevel_id3 = UUID();
SET @quizlevel_id4 = UUID();
SET @quizlevel_id5 = UUID();
SET @quizlevel_id6 = UUID();
SET @quizlevel_id7 = UUID();
SET @quizlevel_id8 = UUID();
SET @quizlevel_id9 = UUID();
SET @quizlevel_id10 = UUID();
INSERT INTO `quizlevel` (`id`, `name`)
VALUES (@quizlevel_id1, 'Beginner'),
       (@quizlevel_id2, 'Intermediate'),
       (@quizlevel_id3, 'Advanced'),
       (@quizlevel_id4, 'Expert'),
       (@quizlevel_id5, 'Master'),
       (@quizlevel_id6, 'Foundational'),
       (@quizlevel_id7, 'Specialized'),
       (@quizlevel_id8, 'General'),
       (@quizlevel_id9, 'Practice'),
       (@quizlevel_id10, 'Challenge');

-- Records for quiztype table
SET @quiztype_id1 = UUID();
SET @quiztype_id2 = UUID();
SET @quiztype_id3 = UUID();
SET @quiztype_id4 = UUID();
SET @quiztype_id5 = UUID();
SET @quiztype_id6 = UUID();
SET @quiztype_id7 = UUID();
SET @quiztype_id8 = UUID();
SET @quiztype_id9 = UUID();
SET @quiztype_id10 = UUID();
INSERT INTO `quiztype` (`id`, `name`)
VALUES (@quiztype_id1, 'Multiple Choice'),
       (@quiztype_id2, 'True/False'),
       (@quiztype_id3, 'Fill in the Blanks'),
       (@quiztype_id4, 'Matching'),
       (@quiztype_id5, 'Short Answer'),
       (@quiztype_id6, 'Essay'),
       (@quiztype_id7, 'Coding Challenge'),
       (@quiztype_id8, 'Verbal Response'),
       (@quiztype_id9, 'Practical Test'),
       (@quiztype_id10, 'Diagnostic');

-- Records for quiz table
SET @quiz_id1 = UUID();
SET @quiz_id2 = UUID();
SET @quiz_id3 = UUID();
SET @quiz_id4 = UUID();
SET @quiz_id5 = UUID();
SET @quiz_id6 = UUID();
SET @quiz_id7 = UUID();
SET @quiz_id8 = UUID();
SET @quiz_id9 = UUID();
SET @quiz_id10 = UUID();

INSERT INTO `quiz` (`id`, `duration`, `status`, `pass_rate`, `updated_date`, `number_of_question`, `description`,
                    `title`, `subject_id`, `type`, `level`)
VALUES
-- Algebra → Mathematics
(@quiz_id1, 30, 1, 0.7, '2023-05-15', 10, 'Basic algebra concepts quiz.',
 'Algebra Fundamentals Quiz', 'b19fb878-397a-11f0-84a1-088fc33f56c7', @quiztype_id1, @quizlevel_id1),

-- Data Structures → Computer Science
(@quiz_id2, 45, 1, 0.75, '2023-05-16', 15, 'Test your knowledge on data structures.',
 'Data Structures Mastery', 'b19fbc5e-397a-11f0-84a1-088fc33f56c7', @quiztype_id1, @quizlevel_id2),

-- Mechanics → Physics
(@quiz_id3, 60, 1, 0.8, '2023-05-17', 20, 'Comprehensive quiz on classical mechanics.',
 'Mechanics Challenge', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7', @quiztype_id1, @quizlevel_id3),

-- Organic Chemistry → Chemistry
(@quiz_id4, 20, 1, 0.6, '2023-05-18', 8, 'Quick check on organic chemistry basics.',
 'Organic Chemistry Quick Test', 'b19fbe33-397a-11f0-84a1-088fc33f56c7', @quiztype_id1, @quizlevel_id4),

-- Calculus → Mathematics
(@quiz_id5, 50, 1, 0.7, '2023-05-19', 18, 'Calculus I concepts assessment.',
 'Calculus I Assessment', 'b19fb878-397a-11f0-84a1-088fc33f56c7', @quiztype_id2, @quizlevel_id5),

-- Genetics → Biology
(@quiz_id6, 35, 1, 0.65, '2023-05-20', 12, 'Genetics principles quiz.',
 'Genetics Essentials', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7', @quiztype_id10, @quizlevel_id6),

-- Microeconomics → Không có trong bảng subject hiện tại (cần thêm subject mới hoặc ánh xạ tạm)
(@quiz_id7, 25, 1, 0.7, '2023-05-21', 10, 'Quiz on microeconomic concepts.',
 'Microeconomics Fundamentals', 'b19fb878-397a-11f0-84a1-088fc33f56c7', @quiztype_id2,
 @quizlevel_id7), -- ánh xạ tạm sang Math

-- Ancient History → Không có subject tương ứng → ánh xạ sang Chemistry (tạm) hoặc thêm mới
(@quiz_id8, 75, 1, 0.85, '2023-05-22', 25,
 'Extensive quiz on ancient civilizations.', 'Ancient History Exam', 'b19fbe33-397a-11f0-84a1-088fc33f56c7',
 @quiztype_id5, @quizlevel_id8),

-- Thermodynamics → Physics
(@quiz_id9, 40, 1, 0.7, '2023-05-23', 15, 'Thermodynamics principles quiz.',
 'Thermo Principles', 'b19fbdc2-397a-11f0-84a1-088fc33f56c7', @quiztype_id3, @quizlevel_id9),

-- Poetry → Không có trong bảng subject → ánh xạ tạm sang Literature (nếu có), hoặc Biology tạm
(@quiz_id10, 30, 1, 0.6, '2023-05-24', 10, 'Poetry analysis and understanding quiz.',
 'Poetry Comprehension', 'b19fbe8f-397a-11f0-84a1-088fc33f56c7', @quiztype_id4, @quizlevel_id10);

-- Records for question table
SET @question_id1 = UUID();
SET @question_id2 = UUID();
SET @question_id3 = UUID();
SET @question_id4 = UUID();
SET @question_id5 = UUID();
SET @question_id6 = UUID();
SET @question_id7 = UUID();
SET @question_id8 = UUID();
SET @question_id9 = UUID();
SET @question_id10 = UUID();
INSERT INTO `question` (`id`, `content`, `topic_id`, `quiz_id`, `level`)
VALUES (@question_id1, 'What is the value of x in 2x + 5 = 15?',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', @quiz_id1, 'Beginner'),
       (@question_id2, 'Describe the properties of a linked list.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', @quiz_id2, 'Intermediate'),
       (@question_id3, 'State Newton''s Third Law of Motion.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', @quiz_id3, 'Advanced'),
       (@question_id4, 'What is the chemical formula for methane?',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', @quiz_id4, 'Beginner'),
       (@question_id5, 'Find the derivative of f(x) = x^2 + 3x.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', @quiz_id5, 'Intermediate'),
       (@question_id6, 'Explain the concept of genetic mutation.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', @quiz_id6, 'Intermediate'),
       (@question_id7, 'Define supply and demand.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80', @quiz_id7, 'Beginner'),
       (@question_id8, 'Discuss the impact of the Roman Empire.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', @quiz_id8, 'Advanced'),
       (@question_id9, 'What is the First Law of Thermodynamics?',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', @quiz_id9, 'Intermediate'),
       (@question_id10, 'Analyze the symbolism in a chosen poem.',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', @quiz_id10, 'Advanced');

-- Records for option table
INSERT INTO `option` (`id`, `content`, `is_true`, `explanation`, `question_id`)
VALUES ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'x = 5', 1, '2x + 5 = 15 => 2x = 10 => x = 5',
        @question_id1),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'x = 10', 0, 'Incorrect calculation',
        @question_id2),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63', 'Dynamic size, efficient insertions/deletions', 1,
        'Linked lists can grow or shrink in size and are efficient for insertions/deletions.',
        @question_id3),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64', 'Fixed size, random access', 0,
        'This describes arrays, not linked lists.', @question_id4),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a65', 'For every action, there is an equal and opposite reaction.', 1,
        'This is the correct statement of Newton''s Third Law.', @question_id5),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66', 'Force equals mass times acceleration.', 0,
        'This is Newton''s Second Law.', @question_id6),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a67', 'CH4', 1,
        'Methane is a simple hydrocarbon with one carbon and four hydrogen atoms.',
        @question_id7),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a68', 'C6H12O6', 0, 'This is the chemical formula for glucose.',
        @question_id8),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a69', '2x + 3', 1,
        'Using the power rule and constant multiple rule, the derivative is 2x + 3.',
        @question_id9),
       ('p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70', 'x^3/3 + 3x^2/2', 0, 'This is the integral, not the derivative.',
        @question_id10);

-- Records for personalcourse table
INSERT INTO `personalcourse` (`id`, `account_id`, `course_id`, `expire_date`, `enroll_date`, `progress`, `status`)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a71', @account_id1,
        @course_id1, '2024-01-01', '2023-07-01', 50, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a72', @account_id9,
        @course_id2, '2024-02-01', '2023-08-01', 75, 'ACCEPTED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a73', @account_id2,
        @course_id3, '2024-03-01', '2023-09-01', 30, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a74', @account_id3,
        @course_id4, '2024-04-01', '2023-10-01', 90, 'ACCEPTED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a75', @account_id5,
        @course_id5, '2024-05-01', '2023-11-01', 10, 'SENT'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a76', @account_id10,
        @course_id6, '2024-06-01', '2023-12-01', 60, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a77', @account_id7,
        @course_id7, '2024-07-01', '2024-01-01', 20, 'DECLINED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a78', @account_id1,
        @course_id8, '2024-08-01', '2024-02-01', 80, 'ACCEPTED'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a79', @account_id8,
        @course_id9, '2024-09-01', '2024-03-01', 45, 'PAID'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70', @account_id1,
        @course_id10, '2024-10-01', '2024-04-01', 65, 'PAID');
-- Records for personallesson table
INSERT INTO `personallesson` (`id`, `account_id`, `lesson_id`, `isDone`)
VALUES ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', @account_id1,
        @lession_id10, 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', @account_id7,
        @lession_id1, 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', @account_id2,
        @lession_id2, 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', @account_id3,
        @lession_id4, 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', @account_id4,
        @lession_id6, 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', @account_id1,
        @lession_id7, 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', @account_id1,
        @lession_id7, 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', @account_id5,
        @lession_id4, 1),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', @account_id2,
        @lession_id9, 0),
       ('r0eebc99-9c0b-4ef8-bb6d-6bb9bd380a80', @account_id6,
        @lession_id5, 1);

-- Records for personaloption table
INSERT INTO `personaloption` (`id`, `option_id`, `account_id`, `has_chosen`)
VALUES ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a91', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61',
        @account_id1, 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a92', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62',
        @account_id3, 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a93', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63',
        @account_id2, 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a94', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64',
        @account_id4, 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a95', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a65',
        @account_id5, 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a96', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66',
        @account_id2, 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a97', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a67',
        @account_id6, 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a98', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a68',
        @account_id3, 0),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a99', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a69',
        @account_id4, 1),
       ('s0eebc99-9c0b-4ef8-bb6d-6bb9bd380a90', 'p0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70',
        @account_id1, 0);

-- Records for personalquestion table
INSERT INTO `personalquestion` (`id`, `account_id`, `question_id`, `has_answer`, `is_mark`)
VALUES ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', @account_id3,
        @question_id1, 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', @account_id7,
        @question_id2, 1, 1),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', @account_id1,
        @question_id3, 0, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', @account_id2,
        @question_id4, 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', @account_id6,
        @question_id5, 1, 1),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a06', @account_id5,
        @question_id6, 0, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a07', @account_id4,
        @question_id7, 1, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a08', @account_id3,
        @question_id8, 1, 1),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a09', @account_id2,
        @question_id9, 0, 0),
       ('t0eebc99-9c0b-4ef8-bb6d-6bb9bd380a00', @account_id1,
        @question_id10, 1, 0);

-- Records for personalquiz table
SET @pq_id1 = UUID();
SET @pq_id2 = UUID();
SET @pq_id3 = UUID();
SET @pq_id4 = UUID();
SET @pq_id5 = UUID();
SET @pq_id6 = UUID();
SET @pq_id7 = UUID();
SET @pq_id8 = UUID();
SET @pq_id9 = UUID();
SET @pq_id10 = UUID();
INSERT INTO `personalquiz` (`id`, `account_id`, `quiz_id`, `has_passed`, `mark`, `number_of_correct_question`,
                            `taken_date`)
VALUES (@pq_id1, @account_id1,
        @quiz_id1, 1, 80, 8, '2023-06-01'),
       (@pq_id2, @account_id1,
        @quiz_id2, 1, 90, 14, '2023-06-05'),
       (@pq_id3, @account_id6,
        @quiz_id3, 0, 70, 13, '2023-06-07'),
       (@pq_id4, @account_id7,
        @quiz_id4, 1, 75, 6, '2023-06-10'),
       (@pq_id5, @account_id2,
        @quiz_id5, 0, 60, 10, '2023-06-12'),
       (@pq_id6, @account_id3,
        @quiz_id6, 1, 85, 10, '2023-06-15'),
       (@pq_id7, @account_id4,
        @quiz_id7, 0, 55, 5, '2023-06-18'),
       (@pq_id8, @account_id9,
        @quiz_id8, 1, 95, 24, '2023-06-20'),
       (@pq_id9, @account_id7,
        @quiz_id9, 0, 68, 10, '2023-06-22'),
       (@pq_id10, @account_id10,
        @quiz_id10, 1, 70, 7, '2023-06-25');

-- Records for pricepackage table
INSERT INTO `pricepackage` (`id`, `course_id`, `title`, `price`, `sale_price`, `access_duration`, `status`,
                            `description`)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', @course_id1, 'Standard Access - Algebra',
        100, 80, 90, 1, '90 days access to Advanced Algebra course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', @course_id2, 'Premium Data Structures', 150,
        120, 180, 1, '180 days access with premium support for Data Structures.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', @course_id3, 'Mechanics Basic', 75, 60, 60,
        1, '60 days access to Classical Mechanics course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @course_id4, 'Organic Chemistry Trial', 50,
        40, 30, 0, '30 days trial access to Organic Chemistry Basics.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @course_id5, 'Calculus Full Access', 200,
        160, 365, 1, 'Full year access to Calculus I course.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', @course_id6, 'Genetics Gold', 120, 100, 120,
        1, '120 days access to Genetics and Evolution with extra resources.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', @course_id7, 'Microeconomics Standard', 90,
        70, 90, 1, '90 days access to Microeconomics Principles.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', @course_id8, 'History Pro', 180, 140, 240, 1,
        '240 days access to World History: Ancient Civilizations.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @course_id9, 'Thermodynamics Basic', 80, 65,
        75, 1, '75 days access to Introduction to Thermodynamics.'),
       ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', @course_id10, 'Poetry Enthusiast', 60, 50, 60,
        1, '60 days access to Poetry Analysis course.');
-- Records for setting_question table
INSERT INTO `setting_question` (`setting_id`, `question_id`)
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', @question_id1),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id2),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id3),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', @question_id4),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id5),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id6),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', @question_id7),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id8),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id9),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', @question_id10);

-- Records for setting_subject table
INSERT INTO `setting_subject` (`setting_id`, `subject_id`)
VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @subject_id1),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @subject_id2),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @subject_id3),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', @subject_id4),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @subject_id5),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @subject_id6),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @subject_id7),
       ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', @subject_id8),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', @subject_id9),
       ('b1af95b3-397a-11f0-84a1-088fc33f56c7', @subject_id10),
       (@setting_id1, 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       (@setting_id3, 'b19fb878-397a-11f0-84a1-088fc33f56c7'),
       (@setting_id2, @subject_id9),
       (@setting_id4, @subject_id9);

-- Records for slider table
SET @slider_id1 = UUID();
SET @slider_id2 = UUID();
SET @slider_id3 = UUID();
SET @slider_id4 = UUID();
SET @slider_id5 = UUID();
SET @slider_id6 = UUID();
SET @slider_id7 = UUID();
SET @slider_id8 = UUID();
SET @slider_id9 = UUID();
SET @slider_id10 = UUID();
INSERT INTO `slider` (`id`, `title`, `image_url`, `backlink_url`, `status`, `account_id`, `note`)
VALUES (@slider_id1, 'Discover Exclusive Deals You Can’t Miss This Week!', 'img/slider1.jpg',
        'http://example.com/promo1', 1, @account_id1, NULL),
       (@slider_id2, 'Step Into the Future with Our Latest 2025 Collection', 'img/slider2.jpg',
        'http://example.com/promo2', 1, @account_id2, NULL),
       (@slider_id3, 'Massive Discounts Just for You – Limited Time Only!', 'img/slider3.jpg',
        'http://example.com/promo3', 1, @account_id3, NULL),
       (@slider_id4, 'Unmatched Quality Meets Unbeatable Prices – Shop Now', 'img/slider4.jpg',
        'http://example.com/promo4', 1, @account_id2, NULL),
       (@slider_id5, 'Top-Selling Products of the Week – Grab Them Fast!', 'img/slider5.jpg',
        'http://example.com/promo5', 1, @account_id1, NULL),
       (@slider_id6, 'Welcome Summer with Fresh Styles and Hot Promotions', 'img/slider6.jpg',
        'http://example.com/promo6', 1, @account_id4, NULL),
       (@slider_id7, 'Enjoy Seamless Shopping with Big Savings Every Day', 'img/slider7.jpg',
        'http://example.com/promo7', 1, @account_id5, NULL),
       (@slider_id8, 'New Arrivals Are Here – Limited Stock Available!', 'img/slider8.jpg',
        'http://example.com/promo8', 1, @account_id2, NULL),
       (@slider_id9, 'Save More with Our Value Combos and Special Offers', 'img/slider9.jpg',
        'http://example.com/promo9', 1, @account_id4, NULL),
       (@slider_id10, 'Upgrade Your Lifestyle – Discover What’s New Today', 'img/slider10.jpg',
        'http://example.com/promo10', 1, @account_id7, NULL);

-- Records for tagline table
SET @tagline_id1 = UUID();
SET @tagline_id2 = UUID();
SET @tagline_id3 = UUID();
SET @tagline_id4 = UUID();
SET @tagline_id5 = UUID();
SET @tagline_id6 = UUID();
SET @tagline_id7 = UUID();
SET @tagline_id8 = UUID();
SET @tagline_id9 = UUID();
SET @tagline_id10 = UUID();
INSERT INTO `tagline` (`id`, `name`)
VALUES (@tagline_id1, 'Learn Anytime, Anywhere'),
       (@tagline_id2, 'Unlock Your Potential'),
       (@tagline_id3, 'Expert-Led Courses'),
       (@tagline_id4, 'Interactive Learning'),
       (@tagline_id5, 'Achieve Your Goals'),
       (@tagline_id6, 'Global Community'),
       (@tagline_id7, 'Personalized Education'),
       (@tagline_id8, 'Future-Proof Your Skills'),
       (@tagline_id9, 'Master New Concepts'),
       (@tagline_id10, 'Knowledge at Your Fingertips');

-- Records for subject_tagline table
INSERT INTO `subject_tagline` (`subject_id`, `tagline_id`)
VALUES (@subject_id1, @tagline_id1),
       (@subject_id2, @tagline_id2),
       (@subject_id3, @tagline_id3),
       (@subject_id4, @tagline_id4),
       (@subject_id5, @tagline_id5),
       (@subject_id6, @tagline_id6),
       (@subject_id7, @tagline_id7),
       (@subject_id8, @tagline_id8),
       (@subject_id9, @tagline_id9),
       (@subject_id10, @tagline_id10);

-- Records for userlog table
SET @userlog_id1 = UUID();
SET @userlog_id2 = UUID();
SET @userlog_id3 = UUID();
SET @userlog_id4 = UUID();
SET @userlog_id5 = UUID();
SET @userlog_id6 = UUID();
SET @userlog_id7 = UUID();
SET @userlog_id8 = UUID();
SET @userlog_id9 = UUID();
SET @userlog_id10 = UUID();
INSERT INTO `userlog` (`id`, `created_date`, `account_id`)
VALUES (@userlog_id1, '2023-01-01', @account_id1),
       (@userlog_id2, '2023-01-02', @account_id2),
       (@userlog_id3, '2023-01-03', @account_id3),
       (@userlog_id4, '2023-01-04', @account_id4),
       (@userlog_id5, '2023-01-05', @account_id5),
       (@userlog_id6, '2023-01-06', @account_id6),
       (@userlog_id7, '2023-01-07', @account_id7),
       (@userlog_id8, '2023-01-08', @account_id8),
       (@userlog_id9, '2023-01-09', @account_id9),
       (@userlog_id10, '2023-01-10', @account_id10);

ALTER TABLE `question`
    MODIFY `quiz_id` varchar(36) NULL,
    MODIFY `topic_id` varchar(36) NULL;

CREATE TABLE `quiz_question_source_config`
(
    `id`                  VARCHAR(36) NOT NULL,
    `quiz_id`             VARCHAR(36) NOT NULL,
    `source_type`         VARCHAR(10) NOT NULL, -- Sẽ chứa các giá trị như 'topic', 'group', 'domain'
    `source_id`           VARCHAR(36) NOT NULL, -- ID của topic, hoặc ID của setting (cho group/domain)
    `number_of_questions` INT         NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_QuizQuestionSourceConfig_Quiz` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`)
    -- Bạn có thể cân nhắc thêm một UNIQUE constraint nếu cần, ví dụ:
    -- UNIQUE KEY `UQ_QuizSourceSelection` (`quiz_id`, `source_type`, `source_id`)
);

-- Step 1: Define UUIDs for the new questions
SET @nq1 = UUID();
SET @nq2 = UUID();
SET @nq3 = UUID();
SET @nq4 = UUID();
SET @nq5 = UUID();
SET @nq6 = UUID();
SET @nq7 = UUID();
SET @nq8 = UUID();
SET @nq9 = UUID();
SET @nq10 = UUID();
SET @nq11 = UUID();
SET @nq12 = UUID();
SET @nq13 = UUID();
SET @nq14 = UUID();
SET @nq15 = UUID();
SET @nq16 = UUID();
SET @nq17 = UUID();
SET @nq18 = UUID();
SET @nq19 = UUID();
SET @nq20 = UUID();

-- Assuming @setting_id1, @setting_id2, @setting_id3, @setting_id4 have been previously set
-- and used in your INSERT INTO setting statement.
-- If running in a new session, you might need to re-declare them with the actual UUIDs
-- that were inserted into the `setting` table. For example:
-- SET @setting_id1 = 'your_actual_uuid_for_business_setting';
-- SET @setting_id2 = 'your_actual_uuid_for_process_setting';
-- SET @setting_id3 = 'your_actual_uuid_for_initialing_setting';
-- SET @setting_id4 = 'your_actual_uuid_for_planning_setting';

-- For topic_id, using some existing ones from your original script.sql or NULL
SET @topic_algebra = 'b240fae5-397a-11f0-84a1-088fc33f56c7';
SET @topic_data_structures = 'b24459d6-397a-11f0-84a1-088fc33f56c7';
SET @topic_mechanics = 'b249f6b8-397a-11f0-84a1-088fc33f56c7';

-- Step 2: Insert new data into the `question` table (20 records)
INSERT INTO `question` (`id`, `content`, `topic_id`, `quiz_id`, `level`)
VALUES (@nq1, 'What is a variable in programming?', @topic_data_structures, NULL, 'Beginner'),
       (@nq2, 'Explain the difference between Stack and Queue.', @topic_data_structures, NULL, 'Intermediate'),
       (@nq3, 'What is Big O notation?', @topic_data_structures, NULL, 'Intermediate'),
       (@nq4, 'Define kinetic energy.', @topic_mechanics, NULL, 'Beginner'),
       (@nq5, 'What is Newton\'s second law of motion?', @topic_mechanics, NULL, 'Intermediate'),
       (@nq6, 'Explain the concept of polymorphism in OOP.', NULL, NULL, 'Advanced'),
       (@nq7, 'What is a foreign key in a database?', NULL, NULL, 'Beginner'),
       (@nq8, 'Describe the process of normalization in databases.', NULL, NULL, 'Intermediate'),
       (@nq9, 'What are the main components of an atom?', @topic_mechanics, NULL, 'Beginner'),
       (@nq10, 'Explain the principle of a lever.', @topic_mechanics, NULL, 'Intermediate'),
       (@nq11, 'What is 7 multiplied by 8?', @topic_algebra, NULL, 'Beginner'),
       (@nq12, 'Solve for y: 2y + 10 = 24', @topic_algebra, NULL, 'Beginner'),
       (@nq13, 'What is a class in Object-Oriented Programming?', NULL, NULL, 'Beginner'),
       (@nq14, 'Describe inheritance with an example.', NULL, NULL, 'Intermediate'),
       (@nq15, 'What is recursion?', @topic_data_structures, NULL, 'Intermediate'),
       (@nq16, 'What is the purpose of an ERD diagram?', NULL, NULL, 'Beginner'),
       (@nq17, 'Explain the concept of "scope" in JavaScript.', NULL, NULL, 'Intermediate'),
       (@nq18, 'What is a CPU cache?', NULL, NULL, 'Advanced'),
       (@nq19, 'Define "potential energy".', @topic_mechanics, NULL, 'Beginner'),
       (@nq20, 'What is a binary search tree?', @topic_data_structures, NULL, 'Intermediate');

-- Step 3: Insert data into `setting_question`
-- This links the newly added questions to your 'Group' and 'Domain' settings.
-- @setting_id1 (Value: 'Business', Type: 'Group')
-- @setting_id2 (Value: 'Process', Type: 'Group')
-- @setting_id3 (Value: 'Initialing', Type: 'Domain')
-- @setting_id4 (Value: 'Planning', Type: 'Domain')

-- Lấy lại ID cho 'Business' (Group)
-- Giả sử các biến @nq1 đến @nq20 đã được SET giá trị UUID hợp lệ:
-- SET @nq1 = UUID();
-- SET @nq2 = UUID();
-- ... (và các @nqX khác)
-- SET @nq20 = UUID();

INSERT INTO `setting_question` (`setting_id`, `question_id`)
VALUES
-- Gán câu hỏi cho Group 'Business' (@setting_id1)
(@setting_id1, @nq1),  -- What is a variable in programming?
(@setting_id1, @nq6),  -- Explain the concept of polymorphism in OOP.
(@setting_id1, @nq11), -- What is 7 multiplied by 8?
(@setting_id1, @nq16), -- What is the purpose of an ERD diagram?

-- Gán câu hỏi cho Group 'Process' (@setting_id2)
(@setting_id2, @nq2),  -- Explain the difference between Stack and Queue.
(@setting_id2, @nq7),  -- What is a foreign key in a database?
(@setting_id2, @nq12), -- Solve for y: 2y + 10 = 24
(@setting_id2, @nq17), -- Explain the concept of "scope" in JavaScript.

-- Gán câu hỏi cho Domain 'Initialing' (@setting_id3)
(@setting_id3, @nq3),  -- What is Big O notation?
(@setting_id3, @nq8),  -- Describe the process of normalization in databases.
(@setting_id3, @nq13), -- What is a class in Object-Oriented Programming?
(@setting_id3, @nq18), -- What is a CPU cache?

-- Gán câu hỏi cho Domain 'Planning' (@setting_id4)
(@setting_id4, @nq4),  -- Define kinetic energy.
(@setting_id4, @nq9),  -- What are the main components of an atom?
(@setting_id4, @nq14), -- Describe inheritance with an example.
(@setting_id4, @nq19), -- Define "potential energy".

-- Gán các câu hỏi vào nhiều setting khác nhau để tăng tính đa dạng
(@setting_id1, @nq5),  -- What is Newton's second law of motion? (Business Group)
(@setting_id3, @nq5),  -- What is Newton's second law of motion? (Initialing Domain)

(@setting_id2, @nq10), -- Explain the principle of a lever. (Process Group)
(@setting_id4, @nq10), -- Explain the principle of a lever. (Planning Domain)

(@setting_id1, @nq15), -- What is recursion? (Business Group)
(@setting_id4, @nq15), -- What is recursion? (Planning Domain)

(@setting_id2, @nq20), -- What is a binary search tree? (Process Group)
(@setting_id3, @nq20); -- What is a binary search tree? (Initialing Domain)