'''
MIT License

Copyright (c) 2018 yiwang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
'''
import numpy as np
import cv2
import os
import subprocess
import glob
import tensorflow as tf
from PIL import Image
from data_store.options.test_options import TestOptions
from data_store.net.network import GMCNNModel

def finish_face():
	config = TestOptions().parse()

	###FOR IMAGE
	if os.path.isfile(config.dataset_path):
	    pathfile = open(config.dataset_path, 'rt').read().splitlines()
	elif os.path.isdir(config.dataset_path):
		#Supports jpg, png, jpeg
		pathfile = glob.glob(os.path.join(config.dataset_path, '*.[jpg][png][jpeg]'))
	else:
	    print('Invalid testing data file/folder path.')
	    return 1 # error

	###FOR MASK
	if os.path.isfile(config.mask_path):
	    maskfile = open(config.mask_path, 'rt').read().splitlines()
	elif os.path.isdir(config.mask_path):
		#Supports jpg, png, jpeg
		maskfile = glob.glob(os.path.join(config.mask_path, '*.[jpg][png][jpeg]'))
	else:
	    print('Invalid mask file/folder path.')
	    return 1

	total_number = len(pathfile)
	total_mask = len(maskfile)
	if total_number == 0:
		print('No faces found!')
		return 1
	if total_mask == 0:
		print('No masks found!')
		return 1
	test_num = total_number if config.test_num == -1 else min(total_number, config.test_num)
	print('The total number of testing images is {}, and we take {} for test.'.format(total_number, test_num))

	model = GMCNNModel()

	reuse = False
	sess_config = tf.ConfigProto()
	sess_config.gpu_options.allow_growth = False
	with tf.Session(config=sess_config) as sess:
	    input_image_tf = tf.placeholder(dtype=tf.float32, shape=[1, config.img_shapes[0], config.img_shapes[1], 3])
	    input_mask_tf = tf.placeholder(dtype=tf.float32, shape=[1, config.img_shapes[0], config.img_shapes[1], 1])

	    output = model.evaluate(input_image_tf, input_mask_tf, config=config, reuse=reuse)
	    output = (output + 1) * 127.5
	    output = tf.minimum(tf.maximum(output[:, :, :, ::-1], 0), 255)
	    output = tf.cast(output, tf.uint8)

	    # load pretrained model
	    vars_list = tf.get_collection(tf.GraphKeys.GLOBAL_VARIABLES)
	    assign_ops = list(map(lambda x: tf.assign(x, tf.contrib.framework.load_variable(config.load_model_dir, x.name)),
	                          vars_list))
	    sess.run(assign_ops)
	    print('Model loaded.')
	    total_time = 0

	    if config.random_mask:
	        np.random.seed(config.seed)

	    for i in range(test_num):
	    	image = cv2.imread(pathfile[i])
	    	image = cv2.resize(image, (512, 512))
	    	mask = Image.open(maskfile[i]).resize((512, 512)).convert('L') 	#GRAYSCALE
	    	mask = np.array(mask).reshape(512, 512, 1) 						#Dimension match with tensor
	    	mask = mask/255 												#Normalise values to 0 and 1
	    	mask = 1 - mask

	    	## mask is white on black sheet
	    	image = image * (1-mask) + 255 * mask
	    	#cv2.imwrite(os.path.join(config.mask_path, 'input.png'), image.astype(np.uint8))
	    	assert image.shape[:2] == mask.shape[:2]

	    	h, w = image.shape[:2]
	    	grid = 4
	    	image = image[:h // grid * grid, :w // grid * grid, :]
	    	mask = mask[:h // grid * grid, :w // grid * grid, :]

	    	image = np.expand_dims(image, 0)
	    	mask = np.expand_dims(mask, 0)
	    	result = sess.run(output, feed_dict={input_image_tf: image, input_mask_tf: mask})
	    	cv2.imwrite(os.path.join(config.saving_path, 'output.png'), result[0][:, :, ::-1])
	    	print(' > {} / {}'.format(i+1, test_num))
	print('done.')
	return 0

	
if __name__ == '__main__':
	finish_face()