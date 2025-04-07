import pg from 'pg';
import dotenv from 'dotenv';

const { Pool } = pg;
dotenv.config();

// Create a connection pool
const pool = new Pool({
  user: process.env.DB_USER,
  host: process.env.DB_HOST,
  database: process.env.DB_NAME,
  password: process.env.DB_PASSWORD,
  port: process.env.DB_PORT || 5432,
});


export const getUserData = async (req, res) => {
  const client = await pool.connect();
  const { userId } = req.body; // Get userId from auth middleware
  
  try {
    // Get user data (excluding sensitive information)
    const userResult = await client.query(
      `SELECT user_id, name, username, email, created_at 
       FROM users WHERE user_id = $1`,
      [userId]
    );
    
    if (userResult.rows.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }
    
    const user = userResult.rows[0];
    
    // Get user history
    const historyResult = await client.query(
      `SELECT * FROM user_history 
       WHERE user_id = $1 
       ORDER BY created_at DESC`,
      [userId]
    );
    
    // Combine user and history
    user.history = historyResult.rows;
    
    return res.status(200).json({
      success: true,
      user
    });
  } catch (error) {
    console.error('Error getting user data:', error);
    return res.status(500).json({
      success: false,
      message: 'Internal server error'
    });
  } finally {
    client.release();
  }
};