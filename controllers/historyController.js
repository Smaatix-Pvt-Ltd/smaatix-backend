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

export const addUserHistory = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { userId } = req.params;
    const { videoId, duration, pausedAt } = req.body;
    console.log("params:", req.params);
    console.log("body:", req.body);

    // Validate input
    if (!userId || !videoId) {
      return res.status(400).json({ 
        message: 'User ID and Video ID are required' 
      });
    }

    // Check if the user exists
    const userResult = await client.query(
      'SELECT * FROM users WHERE user_id = $1',
      [userId]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ 
        message: 'User not found' 
      });
    }

    // Begin transaction
    await client.query('BEGIN');

    // Check if a history entry for this video already exists
    const existingHistoryResult = await client.query(
      'SELECT * FROM history WHERE user_id = $1 AND video_id = $2',
      [userId, videoId]
    );

    let historyEntry;

    if (existingHistoryResult.rows.length > 0) {
      // Update existing history entry
      const result = await client.query(
        `UPDATE history 
         SET duration = $1, paused_at = $2, created_at = CURRENT_TIMESTAMP 
         WHERE user_id = $3 AND video_id = $4
         RETURNING *`,
        [duration || null, pausedAt || null, userId, videoId]
      );
      historyEntry = result.rows[0];
    } else {
      // Add new history entry
      const result = await client.query(
        `INSERT INTO history (user_id, video_id, duration, paused_at)
         VALUES ($1, $2, $3, $4)
         RETURNING *`,
        [userId, videoId, duration || null, pausedAt || null]
      );
      historyEntry = result.rows[0];
    }

    // Commit transaction
    await client.query('COMMIT');

    return res.status(200).json({
      message: 'Video history updated successfully',
      history: {
        videoId: historyEntry.video_id,
        duration: historyEntry.duration,
        pausedAt: historyEntry.paused_at,
        createdAt: historyEntry.created_at
      }
    });

  } catch (error) {
    // Rollback in case of error
    await client.query('ROLLBACK');
    console.error('Error adding video to history:', error);
    
    return res.status(500).json({ 
      message: 'Internal server error', 
      error: error.message 
    });
  } finally {
    client.release();
  }
};

export const recentWatches = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { userId } = req.params;

    // Validate userId
    if (!userId) {
      return res.status(400).json({ message: 'User ID is required' });
    }

    // Check if the user exists
    const userResult = await client.query(
      'SELECT * FROM users WHERE user_id = $1',
      [userId]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ message: 'User not found' });
    }

    // Get recent watches with most recent first, limited to 10
    const recentHistoryResult = await client.query(
      `SELECT * FROM history 
       WHERE user_id = $1 
       ORDER BY created_at DESC 
       LIMIT 10`,
      [userId]
    );

    // Get video details for each history item along with course and domain info
    const formattedVideos = await Promise.all(recentHistoryResult.rows.map(async (historyItem) => {
      const videoResult = await client.query(
        `SELECT 
            v.id,
            v.title,
            v.description,
            v.video_id,
            v.video_url,
            v.img_url,
            c.id as course_id,
            c.name as course_name,
            d.id as domain_id,
            d.name as domain_name
         FROM videos v
         JOIN courses c ON v.course_id = c.id
         JOIN domains d ON c.domain_id = d.id
         WHERE v.video_id = $1`,
        [historyItem.video_id]
      );

      if (videoResult.rows.length === 0) return null;

      const video = videoResult.rows[0];
      
      return {
        videoId: video.video_id,
        videoTitle: video.title,
        description: video.description,
        imgUrl: video.img_url,
        videoUrl: video.video_url,
        pausedAt: historyItem.paused_at || 0,
        duration: historyItem.duration || 0,
        watchedAt: historyItem.created_at,
        // course: {
        //   id: video.course_id,
        //   name: video.course_name
        // },
        // domain: {
        //   id: video.domain_id,
        //   name: video.domain_name
        // }
      };
    }));

    // Remove any null entries (videos not found)
    const validVideos = formattedVideos.filter(video => video !== null);

    res.status(200).json(validVideos);
  } catch (error) {
    console.error('Error in recentWatches:', error);
    res.status(500).json({ message: 'Internal server error', error: error.message });
  } finally {
    client.release();
  }
};