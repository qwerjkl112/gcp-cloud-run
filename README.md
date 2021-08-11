# gcp-cloud-run
a baisc java spring based webapp running on GCP cloud run

# cloud build
gcloud builds submit --tag gcr.io/frankguo-project/helloworld

# deploy
gcloud run deploy --image gcr.io/frankguo-project/helloworld

# test locally
*Only on machines with docker installed* **recommends CloudShell**
docker run -p 8080:8080 -d gcr.io/frankguo-project/hello-app 

curl http://localhost:8080
 
