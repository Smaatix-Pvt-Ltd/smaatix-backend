import jwt from 'jsonwebtoken';

const userAuth = async (req, res, next) => {
    try {
        // Check if token exists in cookies
        const token = req.cookies.token; // Changed from destructuring
        if (!token) {
            return res.status(401).json({
                success: false,
                message: 'Please log in.',
            });
        }

        // Verify JWT Token
        const tokenDecode = jwt.verify(token, process.env.JWT_SECRET);
        
        if (!tokenDecode?.id) { // Optional chaining for cleaner check
            return res.status(401).json({
                success: false,
                message: 'Invalid token',
            });
        }

        // Attach userId to request object (not body)
        req.userId = tokenDecode.id;
        next();
    } catch (error) {
        console.error('JWT Verification Error:', error.message);
        
        const message = error.name === 'TokenExpiredError' 
            ? 'Token expired. Please log in again.' 
            : 'Invalid token';
            
        return res.status(401).json({
            success: false,
            message,
        });
    }
};

export default userAuth;