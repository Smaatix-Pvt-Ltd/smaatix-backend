import express from 'express';
import { getUserData } from '../controllers/userController.js';
import userAuth from '../middlewares/userAuth.js';
const UserRouter = express.Router();

UserRouter.get('/data', userAuth, getUserData);

export default UserRouter;
