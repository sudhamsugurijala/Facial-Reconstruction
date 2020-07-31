import cv2
from mtcnn.mtcnn import MTCNN
import sys
import imutils
import glob
import os
import traceback
import numpy as np

#self made package
from measurements.getMeasure import calculateScore

def phi():
	try:
		#photo
		folder_path = './data_store/img'
		pic_name = glob.glob(os.path.join(folder_path, '*.[jpg][png][jpeg]'))
		detector = MTCNN()
		image1 = cv2.imread(pic_name[0])
		image1 = cv2.resize(image1, (512, 512))
		#detect face, face landmarks (MTCNN)
		result = detector.detect_faces(image1)
		#calculate face scores and store in json file, self made function
		calculateScore(result)

	except Exception as e:
		print("Internal error!")
		traceback.print_exc()


if __name__ == '__main__':
	phi()
	