// models/socialLinksModel.js
import { pool } from '../db.js';

export const createSocialLinksTable = async () => {
  const query = `
    CREATE TABLE IF NOT EXISTS social_links (
      id SERIAL PRIMARY KEY,
      instagram TEXT,
      twitter TEXT,
      linkedin TEXT,
      youtube TEXT,
      facebook TEXT
    );
  `;

  try {
    await pool.query(query);
    console.log('✅ Social links table is ready');
  } catch (err) {
    console.error('❌ Error creating social links table:', err);
  }
};
