# Facial_Reconstruction

This project is an extended application of Image Inpainting ( refer [this](https://www.nvidia.com/research/inpainting/) demo by NVIDIA), 
wherein the aim was to reconstruct an injured face.

Steps:
1. A picture is taken or chosen from gallery
2. Mask is drawn over facial injuries so as to form a 'hole'
3. A Generator (part of a GAN) is used to fill the holes created by the mask, hence getting a normal face.

### Repository Configuration

* The **server directory** contains a simple **flask server** which is used for hosting the generator model.
* Every other directory belongs to the app (User Interface) developed in **Android Studio (Java)**.

### Results

* The **model was trained on 512x512 HQ images**, therefore the results are subject to change with the image quality and the size of the mask (Hole). A sample is given below

![Face with Injury](https://github.com/sudhamsugurijala/Facial_Reconstruction/blob/master/server/backup/can%20be%20used%20for%20report/before.jpg) ![Face with Mask](https://github.com/sudhamsugurijala/Facial_Reconstruction/blob/master/server/backup/can%20be%20used%20for%20report/with_mask.jpg)
![Generated Face](https://github.com/sudhamsugurijala/Facial_Reconstruction/blob/master/server/backup/can%20be%20used%20for%20report/gen_img.jpg) ![Ground Truth](https://github.com/sudhamsugurijala/Facial_Reconstruction/blob/master/server/backup/can%20be%20used%20for%20report/ground_truth.jpg)

### Acknowledgements

 The implementation of inpainting is mostly based on the tensorflow implementation of [inpainting_gmcnn by shepnerd](https://github.com/shepnerd/inpainting_gmcnn). 
Slight modifications were made to accomodate custom masks drawn by users. 
