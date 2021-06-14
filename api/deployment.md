###### _To deploy the api in Linux, we are using Docker._

First step is to create a docker image. For that we have added the Dockerfile.
It defines the steps to create a docker image.

Once docker image is created, it shall be containerized. For that we need a server.
We are using our linux server provided by university.

**steps:** 

_First create the environment in the server._ 
1) Install java 1.8
2) Install docker cli
3) pull docker image from dockerhub. Login to dockerHub first from cli
4) create container
5) run the container 

**Note:** use -p option while container creation to open the link of the docker with host port
    else, it will not be accessible over the internet, but locally.
