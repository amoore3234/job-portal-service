CREATE TABLE IF NOT EXISTS job_postings (
  id SERIAL PRIMARY KEY,
  firstName VARCHAR(200),
  lastName VARCHAR(255),
  userEmail VARCHAR(255),
  userPassword VARCHAR(100),
  createdTimestamp Timestamptz,
  updatedTimestamp Timestamptz
);