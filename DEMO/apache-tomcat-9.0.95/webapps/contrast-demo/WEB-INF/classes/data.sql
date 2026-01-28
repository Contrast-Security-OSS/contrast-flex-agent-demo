INSERT INTO users (name, email, comment) VALUES 
('John Doe', 'john.doe@example.com', 'Test user for demonstrations'),
('Jane Smith', 'jane.smith@example.com', 'Another sample user'),
('Bob Johnson', 'bob.johnson@example.com', 'Security testing account'),
('Alice Wilson', 'alice.wilson@example.com', 'Demo data for Contrast Security'),
('Charlie Brown', 'charlie.brown@example.com', 'Sample user with vulnerable queries');

-- Admin users with plain text passwords (VULNERABILITY)
INSERT INTO admin_users (username, password, role, active) VALUES 
('admin', 'admin123', 'ADMIN', TRUE),
('testadmin', 'password', 'ADMIN', TRUE),
('demo', 'demo123', 'ADMIN', FALSE);
