from measurements.face_landmark import *
from mtcnn.mtcnn import MTCNN
# This program draws person's face over ideal face

def morph():
	# Preprocessing person's image to size of ideal face
	# ./ is the current working directory
	ideal_face_file = "./ideal.png"
	ideal_face = cv2.imread(ideal_face_file)
	folder_path = './data_store/img'
	output_path = './data_store/output'
	pic_name = glob.glob(os.path.join(folder_path, '*.[jpg][png][jpeg]'))
	# glob returns list of files, therefore 0 indes x to first file
	person = cv2.imread(pic_name[0])
	person = cv2.resize(person, (512, 512))

	# Get Landmarks
	points_ideal = get_facial_landmarks(ideal_face_file)
	points_person = get_facial_landmarks(pic_name[0])

	# Delauney Triangulation
	tri = Delaunay(points_ideal)

	ideal_cut = ideal_face.copy()

	# tri num is used for isolating remaining area(0)
	tri_num = 1
	# Create Mask
	binary_mask = np.zeros((person.shape[0], person.shape[1]), dtype='int8')
	# Warp Affine transform each triangle and paste it in the image
	for tri_points_indices in tri.simplices.copy():
		# Image coordinates to be warped (person)
		orig_person_pts = np.float32([points_person[tri_points_indices,0], points_person[tri_points_indices,1]]).transpose()
		to_be_warped_to = np.float32([points_ideal[tri_points_indices,0], points_ideal[tri_points_indices,1]]).transpose()

		# Compute the transformation matrix
		M = cv2.getAffineTransform(orig_person_pts, to_be_warped_to)

		# Temporary transformed image (person)
		person_temp = cv2.warpAffine(person, M, (person.shape[1], person.shape[0]))
		
		# fill face one triangle at a time
		cv2.fillConvexPoly(binary_mask, np.array([points_ideal[tri_points_indices,0], points_ideal[tri_points_indices,1]], 'int32').transpose(), tri_num)

		# Cut the parts from ideal picture
		ideal_cut[binary_mask == tri_num] = person_temp[binary_mask == tri_num]

		# increment tri, every time new set of triangles markes
		tri_num += 1

	# assign all other image area (unmarked area) to black
	ideal_cut[binary_mask == 0] = [0, 0, 0]

	cv2.imwrite(os.path.join(output_path,"output.png"), ideal_cut)

	
if __name__ == '__main__':
	morph()