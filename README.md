# Facial_Reconstruction

  This project is an extended application of Image Inpainting ( refer [this](https://www.nvidia.com/research/inpainting/) demo by NVIDIA), 
wherein the aim was to reconstruct a damaged face.

Steps:
1. A picture is taken or chosen from gallery
2. Mask is drawn over facial injuries so as to form a 'hole'
3. A Generator (part of a GAN) is used to fill the holes created by the mask, hence getting a normal face.
