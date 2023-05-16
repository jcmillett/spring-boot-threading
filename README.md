# Spring Boot Threading

### Description
This is a prototype for experimenting with spring boot, and the Executor framework in Java for multi-threading.

This application provides some REST endpoints for creating and monitoring jobs which contain items that need to be processed. The job and item data is stored in the h2 database. Then a scheduled process runs every 30 seconds to check for jobs that need processing. When a new job is found, all items within that job are submitted to a thread pool for processing.

This example could be expanded to provide the ability to clear out items that didn't process successfully. Maybe with a retry system.