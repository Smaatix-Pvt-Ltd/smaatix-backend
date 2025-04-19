import pg from 'pg';
import dotenv from 'dotenv';

const { Pool } = pg;
dotenv.config();

const pool = new Pool({
  user: process.env.DB_USER,
  host: process.env.DB_HOST,
  database: process.env.DB_NAME,
  password: process.env.DB_PASSWORD,
  port: process.env.DB_PORT || 5432,
  ssl: {
    rejectUnauthorized: false, // set to true if you have a CA cert
  },
});

const initRegistrationTable = async () => {
  const client = await pool.connect();
  try {
    await client.query('BEGIN');

    await client.query(`
      CREATE EXTENSION IF NOT EXISTS "pgcrypto";
    `);

    await client.query(`
      CREATE TABLE IF NOT EXISTS registrations (
        registration_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        full_name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL UNIQUE,
        phone VARCHAR(20),
        dob DATE,
        gender VARCHAR(10),
        address TEXT,
        job_title VARCHAR(255),
        experience VARCHAR(50),
        expected_salary VARCHAR(50),
        preferred_role VARCHAR(100),
        preferred_location VARCHAR(100),
        notice_period VARCHAR(50),
        linkedin TEXT,
        github TEXT,
        qualification VARCHAR(255),
        specialization VARCHAR(255),
        university VARCHAR(255),
        passing_year VARCHAR(4),
        skills TEXT[],
        tools TEXT[],
        certifications TEXT,
        cover_letter TEXT,
        relocation BOOLEAN DEFAULT FALSE,
        shifts BOOLEAN DEFAULT FALSE,
        source VARCHAR(100),
        resume_data BYTEA,  -- Changed from resume_path TEXT to store binary data
        certificates_data BYTEA,  -- Changed from certificates_path TEXT to store binary data
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      )
    `);

    await client.query('COMMIT');
    console.log('Registration table initialized successfully');
  } catch (error) {
    await client.query('ROLLBACK');
    console.error('Error initializing registration table:', error);
    throw error;
  } finally {
    client.release();
  }
};

export { pool, initRegistrationTable };
