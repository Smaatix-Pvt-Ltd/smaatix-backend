import nodemailer from 'nodemailer';
import dotenv from 'dotenv';
dotenv.config();

const transporter = nodemailer.createTransport({
    host: 'smtp-relay.brevo.com',
    port: 587, // Use 465 for SSL
    secure: false, // true for SSL, false for TLS
    auth: {
        user: '88e9cf002@smtp-brevo.com', // Replace with your Brevo SMTP login
        pass: 'AjRNPvGKfTsmz7pg', // Replace with your Brevo master password
    },
});

export default transporter;
