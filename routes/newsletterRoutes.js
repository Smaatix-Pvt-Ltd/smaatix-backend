// newsletterRoutes.js
import express from 'express';
import { addEmail, deleteEmail } from './../controllers/newsletterController.js';

const router = express.Router();

router.post('/add', addEmail);              // Add email
router.delete('/:email', deleteEmail);   // Delete email by email address

export default router;
