CREATE TABLE jobs (
    jobId uuid NOT NULL,
    status enum('UNPROCESSED', 'IN_PROGRESS', 'PROCESSED') NOT NULL,
    PRIMARY KEY (jobId)
);

CREATE TABLE items (
    jobId uuid NOT NULL,
    itemId int NOT NULL,
    processed boolean NOT NULL,
    PRIMARY KEY (jobId, itemId)
);