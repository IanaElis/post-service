DROP TABLE page_moderators;

ALTER TABLE posts DROP COLUMN moderator_id;
ALTER TABLE posts ALTER COLUMN author_id TYPE BIGINT;

CREATE INDEX idx_page_followers_user_id ON page_followers(user_id);
CREATE INDEX idx_page_followers_page_id ON page_followers(page_id);

CREATE INDEX idx_posts_complex ON posts(page_id, status, created_at DESC);
CREATE INDEX idx_posts_idAuthor ON posts(id, author_id);

ALTER TABLE post_media DROP COLUMN media_url;
ALTER TABLE post_media DROP COLUMN file_size;
ALTER TABLE post_media DROP COLUMN media_type;
ALTER TABLE post_media ADD COLUMN media_id INT;
ALTER TABLE posts ALTER COLUMN status TYPE VARCHAR(255);
ALTER TABLE posts ADD COLUMN username VARCHAR(255);

