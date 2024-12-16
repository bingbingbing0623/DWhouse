-- 创建Genres表
CREATE TABLE Genres (
    genre_id INT PRIMARY KEY AUTO_INCREMENT,
    genre_name VARCHAR(255) NOT NULL
);

-- 创建Movies表
CREATE TABLE Movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    subtitles VARCHAR(255),
    language VARCHAR(50),
    release_date DATE,
    release_date_resource VARCHAR(255),
    studio VARCHAR(255),
    rated VARCHAR(50),
    runtime VARCHAR(50),
    media_format VARCHAR(255),
    genre_id INT,
    rating_id INT,
    FOREIGN KEY (genre_id) REFERENCES Genres(genre_id)
    FOREIGN KEY (rating_id) REFERENCES Genres(rating_id)
);


CREATE TABLE Reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    review VARCHAR(255),
    FOREIGN KEY (movie_id) REFERENCES Movies(movie_id)
)
-- 创建Directors表
CREATE TABLE Directors (
    director_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);


-- 创建Actors表
CREATE TABLE Actors (
    actor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

-- 创建Movie_Actors表（多对多关系：Movies和Actors）
CREATE TABLE Movie_Actors (
    movie_id INT,
    actor_id INT,
    FOREIGN KEY (movie_id) REFERENCES Movies(movie_id),
    FOREIGN KEY (actor_id) REFERENCES Actors(actor_id),
    PRIMARY KEY (movie_id, actor_id)
);

-- 创建Movie_Directors表（多对多关系：Movies和Directors）
CREATE TABLE Movie_Directors (
    movie_id INT,
    director_id INT,
    FOREIGN KEY (movie_id) REFERENCES Movies(movie_id),
    FOREIGN KEY (director_id) REFERENCES Directors(director_id),
    PRIMARY KEY (movie_id, director_id)
);

-- 创建Collaborations表
CREATE TABLE Collaborations (
    collaboration_id INT PRIMARY KEY AUTO_INCREMENT,
    actor1_id INT,
    actor2_id INT,
    director_id INT,
    movie_count INT DEFAULT 1,
    is_driAndActor BOOLEAN,
    FOREIGN KEY (actor1_id) REFERENCES Actors(actor_id),
    FOREIGN KEY (actor2_id) REFERENCES Actors(actor_id),
    FOREIGN KEY (director_id) REFERENCES Directors(director_id)
);

-- 创建Editions表
CREATE TABLE Editions (
    edition_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    format VARCHAR(255),
    region_code VARCHAR(50),
    FOREIGN KEY (movie_id) REFERENCES Movies(movie_id)
);

-- 创建Ratings表
CREATE TABLE Ratings (
    rating_id INT PRIMARY KEY AUTO_INCREMENT,
    user_rating DECIMAL(3, 1),
);
