import cv2
import numpy as np
import sys
import dlib
import glob
import os
from skimage import io
from imutils import face_utils
import imutils
from scipy.spatial import Delaunay;

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('shape_predictor_68_face_landmarks.dat')


def get_facial_landmarks(filename):
    image = cv2.imread(filename);
    # detect face(s)
    dets = detector(image, 1);
    for k, d in enumerate(dets):
        # Get the landmarks/parts for the face in box d.
        face_shape = predictor(image, d);
        face_shape = face_utils.shape_to_np(face_shape);

    return face_shape;

