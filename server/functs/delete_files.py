import os
import glob

def del_files():
	# Paths are with respect to phi_server.py
	paths = ['./data_store/img', './data_store/mask_img', './data_store/output']
	for path in paths:
		files = os.listdir(path)
		for fptr in files:
			os.remove(path + '/' + fptr)


if __name__ == '__main__':
	del_files()