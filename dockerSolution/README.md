# docker-solution-shrooms

Requirements: Docker and Docker Compose, internet connection

Folder structure:
 * frontend - contains code for the react app
 * models - contains serialized model for TF Serving
 * training-notebook - contains experiments, training notebooks and the datasets
### Instructions

 * Run solution with: `docker-compose up`



web app runs on `localhost:8080`

jupyter lab runs on `localhost:8888`, while reserving ports 6006, 6007, 6008 for TensorBoards

_note that running the demo training notebook requires at least 16GB of RAM installed in your system_

