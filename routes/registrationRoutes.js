import express from 'express';
import multer from 'multer';
import { registerUser, getUserFiles } from '../controllers/registrationController.js';

const router = express.Router();

// Use memory storage instead of disk storage
const storage = multer.memoryStorage(); 
const upload = multer({ storage: storage });

// Route for registering user with file uploads
// route.js or wherever this code is
router.post(
    '/add',
    upload.fields([
      { name: 'resume', maxCount: 1 },
      { name: 'certificates', maxCount: 5 }
    ]),
    (req, res, next) => {
      console.log('Request Body:', req.body);
      console.log('Uploaded Files:', req.files);
      registerUser(req, res, next); // call your controller
    }
  );

router.get('/get/:userId', getUserFiles); // Assuming you have a function to get user files

  

export default router;
