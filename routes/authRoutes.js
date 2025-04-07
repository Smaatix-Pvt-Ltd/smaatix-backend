import express from 'express';
import {
    isAuthenticated,
    login,
    logout,
    register,
    resetPassword,
    sendResetOtp,
    sendVerificationEmail,
    verifyAccount,
} from '../controllers/authController.js';
import userAuth from '../middlewares/userAuth.js';

const authrouter = express.Router();

authrouter.post('/register', register);
authrouter.post('/login', login);
authrouter.post('/logout', logout);
authrouter.post('/send-verify-otp', userAuth, sendVerificationEmail);
authrouter.post('/verify-account', userAuth, verifyAccount);
authrouter.get('/is-verified', userAuth, isAuthenticated);
authrouter.post('/send-reset-otp', sendResetOtp);
authrouter.post('/reset-password', resetPassword);

export default authrouter;
