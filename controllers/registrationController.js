import multer from 'multer';
import pg from 'pg';
import path from 'path';
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

// Setup multer storage (memoryStorage to keep files in memory for insertion)
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

// Endpoint to handle registration with file upload
export const registerUser = async (req, res) => {
    const {
      full_name, email, phone, dob, gender, address, job_title, experience,
      expected_salary, preferred_role, preferred_location, notice_period,
      linkedin, github, qualification, specialization, university, passing_year,
      skills, tools, certifications, cover_letter, relocation, shifts, source
    } = req.body;
  
    // Fix for the file handling
    const resumeFile = req.files && req.files.resume ? req.files.resume[0] : null;
    const certificatesFile = req.files && req.files.certificates ? req.files.certificates[0] : null;
  
    // Parse skills and tools into arrays
    const skillsArray = skills ? skills.replace(/{|}/g, '').split(',') : [];
    const toolsArray = tools ? tools.replace(/{|}/g, '').split(',') : [];
  
    // Validate passing_year length
    if (passing_year && passing_year.length !== 4) {
      return res.status(400).json({ message: 'Passing year must be 4 digits.' });
    }
  
    // Read file data into a buffer
    const resumeData = resumeFile ? resumeFile.buffer : null;
    const certificatesData = certificatesFile ? certificatesFile.buffer : null;
  
    try {
      const query = `
        INSERT INTO registrations (
          full_name, email, phone, dob, gender, address, job_title, experience, 
          expected_salary, preferred_role, preferred_location, notice_period, linkedin, 
          github, qualification, specialization, university, passing_year, skills, tools, 
          certifications, cover_letter, relocation, shifts, source, resume_data, certificates_data
        )
        VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21, $22, $23, $24, $25, $26, $27)
        RETURNING *
      `;
  
      const values = [
        full_name, email, phone, dob, gender, address, job_title, experience, 
        expected_salary, preferred_role, preferred_location, notice_period, linkedin, 
        github, qualification, specialization, university, passing_year, skillsArray, toolsArray, 
        certifications, cover_letter, relocation, shifts, source, resumeData, certificatesData
      ];
  
      console.log('SQL Query:', query);
      console.log('Values length:', values.length);
  
      const result = await pool.query(query, values);
  
      res.status(201).json({ message: 'Registration successful', user: result.rows[0] });
    } catch (error) {
      console.error('Error registering user:', error);
      res.status(500).json({ message: 'Error registering user' });
    }
};
  
// Middleware for handling file uploads
export const uploadFiles = upload.fields([
  { name: 'resume', maxCount: 1 }, 
  { name: 'certificates', maxCount: 5 }  // If you want to allow multiple certificates
]);

// Get user files by user ID
export const getUserFiles = async (req, res) => {
  const userId = req.params.id;  // Assuming userId is passed in the URL

  try {
    const result = await pool.query('SELECT resume_data, certificates_data FROM registrations WHERE registration_id = $1', [userId]);

    if (result.rows.length > 0) {
      const user = result.rows[0];

      // Serve resume data (in this case, PDF)
      if (user.resume_data) {
        res.setHeader('Content-Type', 'application/pdf');
        res.send(user.resume_data);
      }

      // Serve certificates data (in this case, PNG)
      if (user.certificates_data) {
        res.setHeader('Content-Type', 'image/png');  // Adjust based on actual file type (e.g., PDF, PNG)
        res.send(user.certificates_data);
      }
    } else {
      res.status(404).json({ message: 'User not found' });
    }
  } catch (error) {
    console.error('Error retrieving files:', error);
    res.status(500).json({ message: 'Error retrieving files' });
  }
};