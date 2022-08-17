# Bachelor's Thesis

**images here**

Here you can find what I have been working on during my studies at Slovak University of Technology.

Full name: *Encapsulated Image Processing Environment for Fungi Recognition with a Mobile Application*

The main topic of my bachelor’s thesis was image recognition - creating an intelligent model that can recognize mushroom types (Python + TensorFlow), while deploying this model into feature-rich mobile application (Android/Kotlin) and a web application (React), while being all neatly packaged in a Docker Compose solution.

The project consists of a docker-compose solution that contains:
 * a training environment for the model
 * web-app for testing the model
 * a lightweight server of the model

Furthermore, we have created a mobile application for Android into which we have deployed said model.

For the full experience, you can download the .apk installation file for the mobile app, and the datasets from [this link](https://drive.google.com/drive/folders/1lIH9NosAWheQDalzXUCSFCi2mP_iRQfJ?usp=sharing).

Put dataset files into folders as such:
| Filename | Destination |
| ----------- | ----------- |
| `models.zip` | extract into `/dockerSolution` |
| `dataset.zip` and `exp1.zip` | put both into `/dockerSolution/training-notebook/datasets` |


