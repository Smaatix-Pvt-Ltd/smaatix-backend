// server.js - Main Express server file
import express from 'express';
import cors from 'cors';
import * as db from './db.js';
import dotenv from 'dotenv';
import cookieParser from 'cookie-parser'; // Add cookie parser if you're using cookies

import { initDatabase } from './models/userModel.js';
import { initDomainTables } from './models/courseModel.js'; // Ensure this function exists

import authRouter from './routes/authRoutes.js';
import UserRouter from './routes/userRoutes.js';
import CourseRouter from './routes/courseRoutes.js';
import HistoryRouter from './routes/historyRoutes.js';

dotenv.config();

// Create Express application
const app = express();

// CORS middleware with credentials support
app.use(cors({
  origin: ['http://192.168.137.1:5173', 'http://localhost:5173'],
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'],
  allowedHeaders: [
    'Content-Type', 
    'Authorization', 
    'X-Requested-With',
    'If-None-Match',  // Add this for conditional requests
    'Cache-Control'   // Often needed with If-None-Match
  ],
  exposedHeaders: [
    'ETag',          // Expose ETag header to client
    'Content-Length',
    'X-Total-Count'  // If you use pagination headers
  ],
  maxAge: 86400      // Cache preflight response for 24 hours
}));

// Middleware
app.use(cookieParser()); // Add if you're using cookies
app.use(express.json()); // Parse JSON request bodies

// Example routes
app.get('/', (req, res) => {
  res.send('Server is running');
});

app.use('/api/auth', authRouter);
app.use('/api/user', UserRouter);
app.use('/api/course', CourseRouter);
app.use('/api/history', HistoryRouter);

// Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  initDatabase();
  initDomainTables();
  console.log(`Server running on port ${PORT}`);
});