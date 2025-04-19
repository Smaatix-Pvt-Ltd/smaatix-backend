// routes/socialLinksRoutes.js
import express from 'express';
import { getSocialLinks } from '../controllers/linksController.js';

const router = express.Router();

router.get('/get', getSocialLinks);

export default router;
