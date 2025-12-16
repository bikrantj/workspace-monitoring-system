-- Create database
DROP DATABASE IF EXISTS workspace_monitor;

CREATE DATABASE workspace_monitor;
USE workspace_monitor;

-- Table: workspace_admins (users who create and manage workspaces)
CREATE TABLE IF NOT EXISTS workspace_admins
(
    id            VARCHAR(36) PRIMARY KEY      DEFAULT (UUID()),
    username      VARCHAR(50)         NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    salt          VARCHAR(32)         NOT NULL DEFAULT '',
    email         VARCHAR(100) UNIQUE NOT NULL,
    created_at    TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    last_login    TIMESTAMP           NULL,
    is_active     BOOLEAN                      DEFAULT TRUE
);

-- Table: workspaces (created by workspace admins)
CREATE TABLE IF NOT EXISTS workspaces
(
    id          VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name        VARCHAR(100) UNIQUE NOT NULL,
    uniqueId    VARCHAR(100) UNIQUE NOT NULL,                   -- Unique identifier for joining the workspace
    description TEXT,
    admin_id    VARCHAR(36)         NOT NULL,                   -- The admin who created this workspace
    created_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    is_active   BOOLEAN                 DEFAULT TRUE,

    FOREIGN KEY (admin_id) REFERENCES workspace_admins (id) ON DELETE CASCADE,
    UNIQUE KEY unique_workspace_name_per_admin (admin_id, name) -- Same admin can't have duplicate workspace names
);

-- Table: clients (computers that join workspaces)
CREATE TABLE IF NOT EXISTS clients
(
    id                VARCHAR(36) PRIMARY KEY    DEFAULT (UUID()),
    workspace_id      VARCHAR(36)         NOT NULL,
    client_name       VARCHAR(100)        NOT NULL,          -- Name given by admin to identify this computer
    client_identifier VARCHAR(255) UNIQUE NOT NULL,          -- Unique machine ID (MAC address or generated UUID)
    os_info           VARCHAR(255),
    last_ip_address   VARCHAR(45),
    status            ENUM ('online', 'offline') DEFAULT 'offline',
    last_heartbeat    TIMESTAMP           NULL,
    created_at        TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (workspace_id) REFERENCES workspaces (id) ON DELETE CASCADE,
    UNIQUE KEY unique_client_identifier (client_identifier), -- Same computer can't join multiple workspaces
    INDEX idx_client_workspace (workspace_id),
    INDEX idx_client_status (status),
    INDEX idx_client_heartbeat (last_heartbeat)
);
CREATE TABLE monitoring_snapshots
(
    id           VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    client_id    VARCHAR(36) NOT NULL,
    workspace_id VARCHAR(36) NOT NULL,
    collected_at TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE,
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id) ON DELETE CASCADE
);
-- Table: screenshots (store screenshot metadata)
CREATE TABLE IF NOT EXISTS screenshots
(
    id           VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    client_id    VARCHAR(36)  NOT NULL,
    workspace_id VARCHAR(36)  NOT NULL,
    file_path    VARCHAR(500) NOT NULL, -- Path where screenshot is stored on server
    file_size    BIGINT,
    capture_time TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    snapshot_id  VARCHAR(36)  NOT NULL UNIQUE,

    FOREIGN KEY (snapshot_id) REFERENCES monitoring_snapshots (id) ON DELETE CASCADE,

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE,
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id) ON DELETE CASCADE,
    INDEX idx_screenshot_client (client_id),
    INDEX idx_screenshot_time (capture_time)
);


-- Table: processes (store running processes from clients)
CREATE TABLE IF NOT EXISTS processes
(
    id           VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    client_id    VARCHAR(36)  NOT NULL,
    workspace_id VARCHAR(36)  NOT NULL,
    snapshot_id  VARCHAR(36)  NOT NULL,

    process_name VARCHAR(255) NOT NULL,
    process_id   INT,
    memory_usage BIGINT,
    cpu_usage    DECIMAL(5, 2),
    window_title TEXT,
    collected_at TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (snapshot_id) REFERENCES monitoring_snapshots (id) ON DELETE CASCADE,

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE,
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id) ON DELETE CASCADE,
    INDEX idx_processes_client (client_id),
    INDEX idx_processes_time (collected_at)
);

-- Event for marking clients as offline
DELIMITER //

CREATE EVENT IF NOT EXISTS update_client_status
    ON SCHEDULE EVERY 1 MINUTE
    DO BEGIN
    -- Mark clients as offline if no heartbeat in 2 minutes
    UPDATE clients
    SET status = 'offline'
    WHERE last_heartbeat < DATE_SUB(NOW(), INTERVAL 2 MINUTE)
      AND status = 'online';
END//

DELIMITER ;

-- Enable event scheduler if not already enabled
SET GLOBAL event_scheduler = ON;