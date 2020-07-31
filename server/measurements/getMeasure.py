import json
import cv2
import os
import glob

def getRatio(h, w):
	'''return ratios'''
	ratio = h/w
	score = (ratio/1.618)*100
	return score


def getVerticalPts(keypoints):
	'''y-axis ratios(2)'''
	x1, y1 = keypoints['left_eye']
	x2, y2 = keypoints['right_eye']
	# approximation for line
	v1 = int((y1+y2)/2)

	x2, v2 = keypoints['nose']

	x1, y1 = keypoints['mouth_left']
	x2, y2 = keypoints['mouth_right']
	#approx line
	v3 = int((y1+y2)/2)

	return v1, v2, v3


def getHorizontalPts(keypoints):
	'''x-axis ratios(2)'''
	h2, y = keypoints['left_eye']
	h3, y = keypoints['right_eye']
	return h2, h3


def calculateScore(result):
	'''write scores to file'''	
	output_path = './data_store/output'
	x,y,w,h = result[0]['box']
	keypoints = result[0]['keypoints']

	ratio1 = getRatio(h, w)

	#vertical ratios
	v1, v2, v3 = getVerticalPts(keypoints)
	v4 = y+h
	ratio2 = getRatio(v3-v1, v2-v1)
	ratio3 = getRatio(v4-v1, v3-v1)

	#horizontal ratios
	h1 = x
	h4 = x+w
	h2, h3 = getHorizontalPts(keypoints)
	ratio4 = getRatio(h3-h1, h3-h2)
	ratio5 = getRatio(h4-h1, h3-h1)

	face_det = {
		'score1':ratio1,
		'score2':ratio2,
		'score3':ratio3,
		'score4':ratio4,
		'score5':ratio5
	}

	with open(os.path.join(output_path,"face_det.txt"),'w') as out:
		json.dump(face_det, out)
