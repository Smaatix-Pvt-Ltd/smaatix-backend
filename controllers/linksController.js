// controllers/socialLinksController.js
import { pool } from '../db.js';

// Fetch all social links (assuming only one row for global links)
export const getSocialLinks = async (req, res) => {
  try {
    const query = `SELECT * FROM social_links;`;
    const result = await pool.query(query);

    res.status(200).json({ links: result.rows });
  } catch (err) {
    console.error('❌ Error fetching social links:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
};
