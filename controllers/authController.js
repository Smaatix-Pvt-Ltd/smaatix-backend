import { v4 as uuidv4 } from 'uuid';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import pg from 'pg';
import dotenv from 'dotenv';
import transporter from '../config/nodemailer.js';

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

export const register = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { name, username, email, password } = req.body;
    
    if (!name || !username || !email || !password) {
      return res.status(400).json({ success: false, message: 'Please fill in all fields' });
    }

    // Check if user already exists
    const existingUserResult = await client.query(
      'SELECT * FROM users WHERE email = $1 OR username = $2',
      [email, username]
    );

    if (existingUserResult.rows.length > 0) {
      return res.status(400).json({ 
        success: false, 
        message: 'User with this email or username already exists' 
      });
    }

    const hashedPassword = await bcrypt.hash(password, 12);

    // Insert new user (let PostgreSQL generate the UUID)
    const insertUserResult = await client.query(
      `INSERT INTO users (name, username, email, password)
       VALUES ($1, $2, $3, $4)
       RETURNING user_id`,
      [name, username, email, hashedPassword]
    );

    const userId = insertUserResult.rows[0].user_id;
    const token = jwt.sign({ id: userId }, process.env.JWT_SECRET, { expiresIn: '7d' });

    res.cookie('token', token, {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: process.env.NODE_ENV === 'production' ? 'none' : 'strict',
      maxAge: 7 * 24 * 60 * 60 * 1000,
    });

    // Sending welcome email
    const mailOption = {
      from: process.env.SENDER_EMAIL,
      to: email,
      subject: 'Welcome to our app',
      text: `Hello ${name}, Welcome to our app. We are glad to have you.`,
    };

    transporter.sendMail(mailOption, (error, info) => {
      if (error) {
        console.error('Error sending email:', error);
      } else {
        console.log('Email sent:', info.response);
      }
    });

    return res.status(201).json({
      success: true,
      message: 'User registered successfully',
      userId,
    });
    
  } catch (error) {
    console.error('Error registering user:', error);
    res.status(500).json({ success: false, message: 'Server error' });
  } finally {
    client.release();
  }
};

export const login = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { email, password } = req.body;
    
    if (!email || !password) {
      return res.status(400).json({ success: false, message: 'Please fill in all fields' });
    }

    // Find user by email
    const userResult = await client.query(
      'SELECT user_id, name, email, username, password FROM users WHERE email = $1',
      [email]
    );

    if (userResult.rows.length === 0) {
      return res.status(400).json({ success: false, message: 'Invalid credentials' });
    }

    const user = userResult.rows[0];
    const isMatch = await bcrypt.compare(password, user.password);
    
    if (!isMatch) {
      return res.status(400).json({ success: false, message: 'Invalid credentials' });
    }

    const token = jwt.sign({ id: user.user_id }, process.env.JWT_SECRET, {
      expiresIn: '7d',
    });
    
    // Set cookie
    res.cookie('token', token, {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'lax', // Changed to 'lax' for better compatibility
      maxAge: 7 * 24 * 60 * 60 * 1000,
    });

    // Return user data (excluding password)
    const userData = {
      id: user.user_id,
      name: user.name,
      email: user.email,
      username: user.username
    };

    console.log('User logged in:', userData);

    return res.status(200).json({
      success: true,
      message: 'Login successful',
      user: userData,
      token // Optional: Only include if you need it for non-cookie auth
    });
    
  } catch (error) {
    console.error('Login error:', error);
    return res.status(500).json({
      success: false,
      message: 'Internal server error',
    });
  } finally {
    client.release();
  }
};

export const logout = async (req, res) => {
  try {
    res.clearCookie('token', {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: process.env.NODE_ENV === 'production' ? 'none' : 'strict',
    });
    
    return res.status(200).json({
      success: true,
      message: 'User logged out successfully',
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: 'Server error: ' + error,
    });
  }
};

export const sendVerificationEmail = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { userId } = req.body;

    // Find user by userId
    const userResult = await client.query(
      'SELECT * FROM users WHERE user_id = $1',
      [userId]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'User not found',
      });
    }

    const user = userResult.rows[0];

    if (user.is_verified) {
      return res.json({
        success: false,
        message: 'Account already verified',
      });
    }

    const otp = String(Math.floor(100000 + Math.random() * 900000));
    const otpExpiry = Date.now() + 10 * 60 * 1000;

    // Update user with verification OTP
    await client.query(
      `UPDATE users 
       SET verify_otp = $1, verify_otp_expires_at = $2 
       WHERE user_id = $3`,
      [otp, otpExpiry, userId]
    );

    const mailOptions = {
      from: process.env.SENDER_EMAIL,
      to: user.email,
      subject: 'Email Verification',
      text: `Your verification code is: ${otp}`,
    };
    
    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.error('Error sending email:', error);
      } else {
        console.log('Email sent:', info.response);
      }
    });

    return res.status(200).json({
      success: true,
      message: 'Verification email sent successfully',
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: 'Server error: ' + error,
    });
  } finally {
    client.release();
  }
};

export const verifyAccount = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { userId, otp } = req.body;
    
    if (!userId) {
      return res.json({
        success: false,
        message: 'Missing UserId Details',
      });
    }
    
    if (!otp) {
      return res.json({
        success: false,
        message: 'Fill the otp',
      });
    }

    // Find user by userId
    const userResult = await client.query(
      'SELECT * FROM users WHERE user_id = $1',
      [userId]
    );

    if (userResult.rows.length === 0) {
      return res.json({
        success: false,
        message: 'User not found',
      });
    }

    const user = userResult.rows[0];

    if (user.is_verified) {
      return res.json({
        success: false,
        message: 'Account already verified',
      });
    }

    if (user.verify_otp !== otp || user.verify_otp === '') {
      return res.json({
        success: false,
        message: 'Invalid OTP',
      });
    }

    if (user.verify_otp_expires_at < Date.now()) {
      return res.json({
        success: false,
        message: 'OTP has expired',
      });
    }

    // Update user as verified
    await client.query(
      `UPDATE users 
       SET is_verified = TRUE, verify_otp = '', verify_otp_expires_at = 0 
       WHERE user_id = $1`,
      [userId]
    );

    return res.status(200).json({
      success: true,
      message: 'Account verified successfully',
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: 'Server error: ' + error,
    });
  } finally {
    client.release();
  }
};

export const isAuthenticated = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { userId } = req.body;

    // Find user by userId
    const userResult = await client.query(
      'SELECT is_verified FROM users WHERE user_id = $1',
      [userId]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'User not found',
      });
    }

    const isVerified = userResult.rows[0].is_verified;
    
    if (!isVerified) {
      return res.json({
        success: false,
        message: 'User is not authenticated',
      });
    }
    
    return res.status(200).json({
      success: true,
      message: 'User is authenticated',
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: 'Server error: ' + error,
    });
  } finally {
    client.release();
  }
};

export const sendResetOtp = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { email } = req.body;

    if (!email) {
      return res.status(400).json({ 
        success: false, 
        message: 'Please provide email' 
      });
    }

    // Find user by email
    const userResult = await client.query(
      'SELECT * FROM users WHERE email = $1',
      [email]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ 
        success: false, 
        message: 'User not found' 
      });
    }

    const otp = String(Math.floor(100000 + Math.random() * 900000));
    const otpExpiry = Date.now() + 10 * 60 * 1000;

    // Update user with reset OTP
    await client.query(
      `UPDATE users 
       SET reset_otp = $1, reset_otp_expires_at = $2 
       WHERE email = $3`,
      [otp, otpExpiry, email]
    );

    const emailOptions = {
      from: process.env.SENDER_EMAIL,
      to: email,
      subject: 'Reset Password',
      html: `
        <h1>Reset Password</h1>
        <p>Your reset password otp is: ${otp}</p>
      `,
    };

    transporter.sendMail(emailOptions);

    return res.status(200).json({
      success: true,
      message: 'OTP sent successfully',
    });
  } catch (error) {
    return res.status(500).json({ 
      success: false, 
      message: 'Server error: ' + error 
    });
  } finally {
    client.release();
  }
};

export const resetPassword = async (req, res) => {
  const client = await pool.connect();
  
  try {
    const { email, otp, newPassword } = req.body;

    if (!email || !otp || !newPassword) {
      return res.status(400).json({ 
        success: false, 
        message: 'Missing required fields' 
      });
    }

    // Find user by email
    const userResult = await client.query(
      'SELECT * FROM users WHERE email = $1',
      [email]
    );

    if (userResult.rows.length === 0) {
      return res.status(404).json({ 
        success: false, 
        message: 'User not found' 
      });
    }

    const user = userResult.rows[0];

    if (user.reset_otp !== otp || user.reset_otp === '') {
      return res.status(400).json({ 
        success: false, 
        message: 'Invalid OTP' 
      });
    }

    if (user.reset_otp_expires_at < Date.now()) {
      return res.status(400).json({ 
        success: false, 
        message: 'OTP has expired' 
      });
    }

    const hashedPassword = await bcrypt.hash(newPassword, 12);

    // Update user password and clear reset OTP
    await client.query(
      `UPDATE users 
       SET password = $1, reset_otp = '', reset_otp_expires_at = 0 
       WHERE email = $2`,
      [hashedPassword, email]
    );

    return res.status(200).json({
      success: true,
      message: 'Password reset successfully',
    });
  } catch (error) {
    return res.status(500).json({ 
      success: false, 
      message: 'Server error: ' + error 
    });
  } finally {
    client.release();
  }
};