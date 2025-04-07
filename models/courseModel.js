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

// Initialize database with domain schema tables
const initDomainTables = async () => {
  const client = await pool.connect();
  
  try {
    // Create domains table
    await client.query(`
      CREATE TABLE IF NOT EXISTS domains (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL
      )
    `);

    // Create courses table
    await client.query(`
      CREATE TABLE IF NOT EXISTS courses (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        domain_id INTEGER NOT NULL,
        FOREIGN KEY (domain_id) REFERENCES domains(id) ON DELETE CASCADE
      )
    `);

    // Create videos table
    await client.query(`
      CREATE TABLE IF NOT EXISTS videos (
        id SERIAL PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        description TEXT NOT NULL,
        video_id INTEGER NOT NULL UNIQUE,
        video_url TEXT NOT NULL,
        img_url TEXT NOT NULL,
        course_id INTEGER NOT NULL,
        FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
      )
    `);

    console.log('Domain schema tables initialized successfully');
  } catch (error) {
    console.error('Error initializing domain tables:', error);
  } finally {
    client.release();
  }
};

// Domain model functions
const Domain = {
  // Create a new domain
  create: async (domainData) => {
    const client = await pool.connect();
    try {
      const result = await client.query(
        'INSERT INTO domains (name) VALUES ($1) RETURNING *',
        [domainData.name]
      );
      return result.rows[0];
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Get all domains with courses and videos
  findAll: async () => {
    const client = await pool.connect();
    try {
      // Get all domains
      const domainsResult = await client.query('SELECT * FROM domains');
      const domains = domainsResult.rows;

      // For each domain, get its courses and videos
      for (let domain of domains) {
        // Get courses for this domain
        const coursesResult = await client.query(
          'SELECT * FROM courses WHERE domain_id = $1',
          [domain.id]
        );
        domain.courses = coursesResult.rows;

        // For each course, get its videos
        for (let course of domain.courses) {
          const videosResult = await client.query(
            'SELECT * FROM videos WHERE course_id = $1',
            [course.id]
          );
          course.videos = videosResult.rows;
        }
      }

      return domains;
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Find a domain by ID with all courses and videos
  findById: async (id) => {
    const client = await pool.connect();
    try {
      // Get domain
      const domainResult = await client.query(
        'SELECT * FROM domains WHERE id = $1',
        [id]
      );
      
      if (domainResult.rows.length === 0) {
        return null;
      }
      
      const domain = domainResult.rows[0];
      
      // Get courses for this domain
      const coursesResult = await client.query(
        'SELECT * FROM courses WHERE domain_id = $1',
        [domain.id]
      );
      domain.courses = coursesResult.rows;
      
      // For each course, get its videos
      for (let course of domain.courses) {
        const videosResult = await client.query(
          'SELECT * FROM videos WHERE course_id = $1',
          [course.id]
        );
        course.videos = videosResult.rows;
      }
      
      return domain;
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Find domain containing a specific video
  findByVideoId: async (videoId) => {
    const client = await pool.connect();
    try {
      const query = `
        SELECT d.*, c.id as course_id, c.name as course_name, v.*
        FROM domains d
        JOIN courses c ON d.id = c.domain_id
        JOIN videos v ON c.id = v.course_id
        WHERE v.video_id = $1
      `;
      
      const result = await client.query(query, [videoId]);
      
      if (result.rows.length === 0) {
        return null;
      }
      
      // Format the result to match expected structure
      const row = result.rows[0];
      const domain = {
        id: row.id,
        name: row.name,
        courses: [{
          id: row.course_id,
          name: row.course_name,
          domain_id: row.id,
          videos: [{
            id: row.id,
            title: row.title,
            description: row.description,
            video_id: row.video_id,
            video_url: row.video_url,
            img_url: row.img_url,
            course_id: row.course_id
          }]
        }]
      };
      
      return domain;
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Add a course to a domain
  addCourse: async (domainId, courseData) => {
    const client = await pool.connect();
    try {
      const result = await client.query(
        'INSERT INTO courses (name, domain_id) VALUES ($1, $2) RETURNING *',
        [courseData.name, domainId]
      );
      return result.rows[0];
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Add a video to a course
  addVideo: async (courseId, videoData) => {
    const client = await pool.connect();
    try {
      const result = await client.query(
        'INSERT INTO videos (title, description, video_id, video_url, img_url, course_id) VALUES ($1, $2, $3, $4, $5, $6) RETURNING *',
        [videoData.title, videoData.description, videoData.videoId, videoData.videoUrl, videoData.imgUrl, courseId]
      );
      return result.rows[0];
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Update a domain
  update: async (id, domainData) => {
    const client = await pool.connect();
    try {
      const result = await client.query(
        'UPDATE domains SET name = $1 WHERE id = $2 RETURNING *',
        [domainData.name, id]
      );
      return result.rows[0];
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  },

  // Delete a domain and all related courses and videos
  delete: async (id) => {
    const client = await pool.connect();
    try {
      // Due to cascade delete, we only need to delete the domain
      const result = await client.query(
        'DELETE FROM domains WHERE id = $1 RETURNING *',
        [id]
      );
      return result.rows[0];
    } catch (error) {
      throw error;
    } finally {
      client.release();
    }
  }
};

export default Domain;
export { initDomainTables };