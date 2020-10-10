USE PRIVATE NETWORK!!

Activating the venv

anaconda -

conda activate phi


Installing packages

install python
pip install opencv-contrib-python
pip install scikit-image
pip install mtcnn
pip install tensorflow
pip install imutils


Installing dlib

use anaconda
conda install -c conda-forge dlib


Running phi score

cd <path to phi.py>
python phi.py sample.png




---- BELOW CODE FOR PRESENTATION, BACKUP PURPOSE ONLY ----

"""
image = cv2.imread(input_file+".png")
	cv2.rectangle(image, (x, y), (x+w, y+h), (0,0,0), 2)
	cv2.circle(image,(keypoints['left_eye']), 2, (0,0,255), 2)
	cv2.circle(image,(keypoints['right_eye']), 2, (0,0,255), 2)
	cv2.circle(image,(keypoints['nose']), 2, (0,0,255), 2)
	cv2.circle(image,(keypoints['mouth_left']), 2, (0,0,255), 2)
	cv2.circle(image,(keypoints['mouth_right']), 2, (0,0,255), 2)
	cv2.imwrite("output.png",image)


#cv2.rectangle(image, (h1, v1), (h4, v3), (0,0,0), 2)
#cv2.rectangle(image, (h1, v3), (h4, v4), (0,0,0), 2)
#cv2.rectangle(image, (h1, v1), (h4, v3), (0,0,0), 2)
#cv2.rectangle(image, (h1, v1), (h4, v2), (0,0,0), 2)

#cv2.rectangle(image, (h1, y), (h3, v4), (0,0,0), 2)
#cv2.rectangle(image, (h2, y), (h3, v4), (0,0,0), 2)
#cv2.rectangle(image, (h1, y), (h4, v4), (0,0,0), 2)
#cv2.rectangle(image, (h1, y), (h3, v4), (0,0,0), 2)



"""


def imgResize(img1, img2):
	''' resize img2 to img1 dimensions'''
	W = img1.shape[1]
	H = img1.shape[0]
	dim = (W, H)

	return cv2.resize(img2, dim, interpolation = cv2.INTER_CUBIC)

