// db.js - Database connection configuration
import pg from 'pg';
import dotenv from 'dotenv';

const { Pool } = pg;
dotenv.config();

// Create a connection pool using environment variables
const pool = new Pool({
  user: process.env.DB_USER,
  host: process.env.DB_HOST,
  database: process.env.DB_NAME,
  password: process.env.DB_PASSWORD,
  port: process.env.DB_PORT || 5432, // Default to 5432 if not specified
});

// Test the connection
pool.query('SELECT NOW()', (err) => {
  if (err) {
    console.error('PostgreSQL connection error:', err);
  } else {
    console.log('PostgreSQL connected successfully');
  }
});

// Export functions
export const query = (text, params) => pool.query(text, params);
export { pool };