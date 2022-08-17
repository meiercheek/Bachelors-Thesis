FROM python:3

RUN mkdir src
WORKDIR /src/
COPY /training-notebook .
RUN mkdir models
COPY /models /models
RUN pip install --no-cache-dir -r requirements.txt
ENV JUPYTER_ENABLE_LAB=yes
ENV JUPYTER_TOKEN=docker
EXPOSE 8888 6006 6007 6008 
CMD ["jupyter", "lab", "--port=8888", "--no-browser", "--NotebookApp.token=''","--NotebookApp.password=''", "--ip=0.0.0.0", "--allow-root"]