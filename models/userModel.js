// userModel.js
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
});

const initDatabase = async () => {
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    
    // Create users table with UUID as primary key
    await client.query(`
      CREATE TABLE IF NOT EXISTS users (
        user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        name VARCHAR(255) NOT NULL,
        username VARCHAR(255) NOT NULL UNIQUE,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        verify_otp VARCHAR(255) DEFAULT '',
        verify_otp_expires_at BIGINT DEFAULT 0,
        is_verified BOOLEAN DEFAULT FALSE,
        reset_otp VARCHAR(255) DEFAULT '',
        reset_otp_expires_at BIGINT DEFAULT 0,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      )
    `);

     // Create history table with foreign key relation to users
     await client.query(`
      CREATE TABLE IF NOT EXISTS history (
        history_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        user_id UUID NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        video_id INTEGER NOT NULL,  
        duration DOUBLE PRECISION,
        paused_at DOUBLE PRECISION,
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
      )
    `);
     

    await client.query('COMMIT');
    console.log('Database tables initialized successfully');
  } catch (error) {
    await client.query('ROLLBACK');
    console.error('Error initializing database tables:', error);
    throw error; // Re-throw to handle in server initialization
  } finally {
    client.release();
  }
};

export { pool, initDatabase };