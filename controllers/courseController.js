import Domain from "../models/courseModel.js";
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

// Controller to get all domain names
export const getDomainNames = async (req, res) => {
  const client = await pool.connect();
  
  try {
    // Get all domain names from the domains table
    const result = await client.query('SELECT name FROM domains');
    
    // Extract domain names from the result
    const domainNames = result.rows.map(domain => domain.name);
    console.log(domainNames);
    
    res.json(domainNames);
  } catch (error) {
    res.status(500).json({ 
      message: "Error retrieving domain names", 
      error: error.message 
    });
  } finally {
    client.release();
  }
};

// Controller to get course names for a specific domain
export const getCourseNames = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const domainName = decodeURIComponent(req.params.domainName);
    
    // Find the domain ID by name
    const domainResult = await client.query(
      'SELECT id FROM domains WHERE name = $1',
      [domainName]
    );
    
    if (domainResult.rows.length === 0) {
      console.log("Domain not found", domainName);
      return res.status(404).json({ message: "Domain not found" });
    }
    
    const domainId = domainResult.rows[0].id;
    
    // Get all course names for this domain
    const coursesResult = await client.query(
      'SELECT name FROM courses WHERE domain_id = $1',
      [domainId]
    );
    
    // Extract course names from the result
    const courseNames = coursesResult.rows.map(course => course.name);
    
    if (courseNames.length === 0) {
      return res.json([]);
    }
    
    res.json(courseNames);
  } catch (error) {
    res.status(500).json({ 
      message: "Error retrieving course names", 
      error: error.message 
    });
  } finally {
    client.release();
  }
};

// Controller to get video details for a specific domain and course
export const getCourseDetails = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { domainName, courseName } = req.params;
    console.log(domainName, courseName);
    
    // Find the domain ID by name
    const domainResult = await client.query(
      'SELECT id FROM domains WHERE name = $1',
      [domainName]
    );
    
    if (domainResult.rows.length === 0) {
      return res.status(404).json({ message: "Domain not found" });
    }
    
    const domainId = domainResult.rows[0].id;
    
    // Find the course ID by name and domain ID
    const courseResult = await client.query(
      'SELECT id FROM courses WHERE name = $1 AND domain_id = $2',
      [courseName, domainId]
    );
    
    if (courseResult.rows.length === 0) {
      return res.status(404).json({ message: "Course not found" });
    }
    
    const courseId = courseResult.rows[0].id;
    
    // Get all videos for this course
    const videosResult = await client.query(
      `SELECT video_url, video_id, img_url, title, description 
       FROM videos 
       WHERE course_id = $1`,
      [courseId]
    );
    
    // Transform video details to match the requested format
    const videos = videosResult.rows.map(video => ({
      videoUrl: video.video_url,
      videoId: video.video_id,
      imgUrl: video.img_url,
      title: video.title,
      description: video.description
    }));
    
    console.log(videos);
    res.json(videos);
  } catch (error) {
    res.status(500).json({ 
      message: "Server Error", 
      error: error.message 
    });
  } finally {
    client.release();
  }
};
