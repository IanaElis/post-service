CREATE TABLE pages(
                      id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description VARCHAR(255),
                      department_id INT,
                      faculty_id INT,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE page_followers(
                               id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               user_id INT NOT NULL,
                               page_id INT NOT NULL REFERENCES pages(id) ON DELETE CASCADE,
                               UNIQUE (user_id, page_id)
);

CREATE TABLE page_moderators(
                                id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                user_id INT NOT NULL,
                                page_id INT NOT NULL REFERENCES pages(id) ON DELETE CASCADE,
                                assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                UNIQUE (user_id, page_id)
);

CREATE TABLE posts(
                      id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      page_id INT NOT NULL REFERENCES pages(id) ON DELETE CASCADE,
                      author_id INT NOT NULL ,
                      moderator_id INT,
                      status VARCHAR(50) NOT NULL,
                      content_text TEXT NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_media(
                           id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           post_id INT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                           media_type VARCHAR(50),
                           media_url VARCHAR(255),
                           file_size INT
);
