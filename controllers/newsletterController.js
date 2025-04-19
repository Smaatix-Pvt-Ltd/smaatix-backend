// newsletterController.js
import { pool } from '../db.js';

// Add email
export const addEmail = async (req, res) => {
  const { email } = req.body;

  if (!email) {
    return res.status(400).json({ error: 'Email is required' });
  }

  try {
    const existing = await pool.query('SELECT * FROM newsletter WHERE email = $1', [email]);
    if (existing.rows.length > 0) {
      return res.status(409).json({ message: 'Email already subscribed' });
    }

    await pool.query('INSERT INTO newsletter (email) VALUES ($1)', [email]);
    res.status(201).json({ message: 'Email added to newsletter' });
  } catch (err) {
    console.error('Add email error:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
};

// Delete email
export const deleteEmail = async (req, res) => {
  const { email } = req.params;

  try {
    const result = await pool.query('DELETE FROM newsletter WHERE email = $1', [email]);

    if (result.rowCount === 0) {
      return res.status(404).json({ message: 'Email not found' });
    }

    res.json({ message: 'Email deleted from newsletter' });
  } catch (err) {
    console.error('Delete email error:', err);
    res.status(500).json({ error: 'Internal server error' });
  }
};
