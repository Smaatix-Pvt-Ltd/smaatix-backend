import express from "express";
import { 
  getDomainNames,
  getCourseNames,
  getCourseDetails 
} from "../controllers/courseController.js"; // Ensure .js extension

const router = express.Router();


router.get("/courses/:domainName", getCourseNames);// Updated route

router.get("/domains", getDomainNames); // New route

router.get("/courses/:domainName/:courseName", getCourseDetails); // New route

export default router;
