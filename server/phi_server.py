import flask
import cv2
import os
import base64
from flask import Flask
from functs.phiV2 import phi
from functs.morphV2 import morph
from functs.reconstruct import finish_face
from functs.delete_files import del_files
import werkzeug

app = Flask(__name__)

@app.route('/')
def moduleX():
	return "Not a valid space!"

@app.route('/phi_morph', methods=['GET', 'POST'])
def phi_morph():
	del_files()

	input_path = './data_store/img/'
	output_path = './data_store/output/output.png'

	image = flask.request.files['image']
	image_name = werkzeug.utils.secure_filename(image.filename)
	image.save(input_path + image_name)
	try:
		morph()

		with open(output_path, "rb") as img:
			resp = base64.b64encode(img.read())
			return resp
	except (FileNotFoundError, Exception) as e:
		print(e)
		return "Try another Photo"


@app.route('/reconstruct', methods=['GET', 'POST'])
def reconst_face():
	del_files()

	input_path = './data_store/img/'
	mask_path = './data_store/mask_img/'
	output_path = './data_store/output/output.png'

	image = flask.request.files['image']
	mask = flask.request.files['mask']

	image_name = werkzeug.utils.secure_filename(image.filename)
	image.save(input_path + image_name)

	mask_name = werkzeug.utils.secure_filename(mask.filename)
	mask.save(mask_path + mask_name)

	try:
		finish_face()
		
		with open(output_path, "rb") as img:
			resp = base64.b64encode(img.read())
			return resp
	except (FileNotFoundError, Exception) as e:
		print(e)
		return "Try another Photo"


if __name__ == '__main__':
	app.run(host='0.0.0.0', port=5000, debug=True)
