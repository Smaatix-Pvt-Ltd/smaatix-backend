import express from "express";
import { addUserHistory, recentWatches } from "../controllers/historyController.js";

const router = express.Router();

router.post('/add-history/:userId', addUserHistory);
router.get('/recent-watches/:userId', recentWatches);

export default router;
