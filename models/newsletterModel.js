// newsletterModel.js
import { pool } from '../db.js'; // Import the database connection pool
import dotenv from 'dotenv';  

// Function to create the newsletter table
export const createNewsletterTable = async () => {
  const query = `
    CREATE TABLE IF NOT EXISTS newsletter (
      id SERIAL PRIMARY KEY,
      email VARCHAR(255) UNIQUE NOT NULL
    );
  `;

  try {
    await pool.query(query);
    console.log('✅ Newsletter table is ready');
  } catch (err) {
    console.error('❌ Error creating newsletter table:', err);
  }
};
