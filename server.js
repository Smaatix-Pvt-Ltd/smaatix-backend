// server.js - Main Express server file
import express from 'express';
import cors from 'cors';
import * as db from './db.js';
import dotenv from 'dotenv';
import cookieParser from 'cookie-parser';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

import { initDatabase } from './models/userModel.js';
import { initDomainTables } from './models/courseModel.js';
import { initRegistrationTable } from './models/registrationModel.js';
import { createSocialLinksTable } from './models/linksModel.js'; // Import the social links model
import { createNewsletterTable } from './models/newsletterModel.js';

import newsletter from './routes/newsletterRoutes.js'; // Import the newsletter route
import authRouter from './routes/authRoutes.js';
import UserRouter from './routes/userRoutes.js';
import CourseRouter from './routes/courseRoutes.js';
import HistoryRouter from './routes/historyRoutes.js';
import registrationRoutes from './routes/registrationRoutes.js';
import socialLinksRoutes from './routes/linksRoutes.js'; // Import the social links route

dotenv.config();

const app = express();

app.use(cors({
  origin: ['http://192.168.137.1:5173', 'http://localhost:5173', 'http://192.168.1.221:5173', 'http://192.168.1.221:5174', 'http://smaatix.com', 'https://smaatix.com', 'http://192.168.137.1:5173' ],
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'],
  allowedHeaders: [
    'Content-Type', 'Authorization', 'X-Requested-With',
    'If-None-Match', 'Cache-Control'
  ],
  exposedHeaders: ['ETag', 'Content-Length', 'X-Total-Count'],
  maxAge: 86400
}));

app.use(cookieParser());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Ensure uploads folder exists
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const uploadsDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadsDir)) {
  fs.mkdirSync(uploadsDir);
}

// Serve uploaded files
app.use('/uploads', express.static(uploadsDir));

// Routes
app.use('/api/register', registrationRoutes);
app.use('/api/newsletter', newsletter);
app.use('/api/auth', authRouter);
app.use('/api/user', UserRouter);
app.use('/api/course', CourseRouter);
app.use('/api/history', HistoryRouter);
app.use('/api/social-links', socialLinksRoutes); // <-- Add this line

app.get('/', (req, res) => {
  res.send('Server is running');
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  initDatabase();
  initDomainTables();
  initRegistrationTable();
  createSocialLinksTable(); // Initialize the social links table
  createNewsletterTable();
  console.log(`Server running on port ${PORT}`);
});