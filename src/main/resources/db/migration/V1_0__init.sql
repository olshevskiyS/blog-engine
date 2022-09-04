CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT,
  is_moderator TINYINT NOT NULL,
  reg_time TIMESTAMP NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  code VARCHAR(255),
  photo TEXT,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS posts (
  id INT NOT NULL AUTO_INCREMENT,
  is_active TINYINT NOT NULL,
  moderation_status VARCHAR(255) NOT NULL,
  moderator_id INT,
  user_id INT NOT NULL,
  time TIMESTAMP NOT NULL,
  title VARCHAR(255) NOT NULL,
  text TEXT NOT NULL,
  view_count INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (moderator_id) REFERENCES users (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS post_votes (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  time TIMESTAMP NOT NULL,
  value TINYINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (post_id) REFERENCES posts (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS post_comments (
  id INT NOT NULL AUTO_INCREMENT,
  parent_id INT,
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  time TIMESTAMP NOT NULL,
  text TEXT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (parent_id) REFERENCES post_comments (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (post_id) REFERENCES posts (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS tags (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS tag2post (
  post_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (post_id, tag_id),
  FOREIGN KEY (post_id) REFERENCES posts (id),
  FOREIGN KEY (tag_id) REFERENCES tags (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS captcha_codes (
  id INT NOT NULL AUTO_INCREMENT,
  time TIMESTAMP NOT NULL,
  code TINYTEXT NOT NULL,
  secret_code TINYTEXT NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS global_settings (
  id INT NOT NULL AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  value VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;