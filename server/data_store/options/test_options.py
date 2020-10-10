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
import argparse
import os
import time

class TestOptions:
    def __init__(self):
        self.parser = argparse.ArgumentParser()
        self.initialized = False

    def initialize(self):
        self.parser.add_argument('--dataset', type=str, default='celebahq',
                                 help='The dataset of the experiment.')
        self.parser.add_argument('--data_file', type=str, default='./data_store/img', help='the file storing testing file paths')
        self.parser.add_argument('--test_dir', type=str, default='./data_store/output', help='output saved here')
        self.parser.add_argument('--load_model_dir', type=str, default='./data_store/checkpoints/celebahq_512x512_rect', help='pretrained models are given here')
        self.parser.add_argument('--model_prefix', type=str, default='snap')
        self.parser.add_argument('--seed', type=int, default=1, help='random seed')
        self.parser.add_argument('--mask_path', type=str, default='./data_store/mask_img')

        self.parser.add_argument('--model', type=str, default='gmcnn')
        self.parser.add_argument('--random_mask', type=int, default=0,
                                 help='using random mask')

        self.parser.add_argument('--img_shapes', type=str, default='512,512,3',
                                 help='given shape parameters: h,w,c or h,w')
        self.parser.add_argument('--mask_shapes', type=str, default='256,256',
                                 help='given mask parameters: h,w')
        self.parser.add_argument('--mask_type', type=str, default='stroke')
        self.parser.add_argument('--test_num', type=int, default=-1)
        self.parser.add_argument('--mode', type=str, default='save')

        # for generator
        self.parser.add_argument('--g_cnum', type=int, default=64,
                                 help='# of generator filters in first conv layer')
        self.parser.add_argument('--d_cnum', type=int, default=64,
                                 help='# of discriminator filters in first conv layer')

    def parse(self):
        if not self.initialized:
            self.initialize()
        self.opt = self.parser.parse_args()

        if self.opt.data_file != '':
            self.opt.dataset_path = self.opt.data_file

        if os.path.exists(self.opt.test_dir) is False:
            os.mkdir(self.opt.test_dir)

        assert self.opt.random_mask in [0, 1]
        self.opt.random_mask = True if self.opt.random_mask == 1 else False

        assert self.opt.mask_type in ['rect', 'stroke']

        str_img_shapes = self.opt.img_shapes.split(',')
        self.opt.img_shapes = [int(x) for x in str_img_shapes]

        str_mask_shapes = self.opt.mask_shapes.split(',')
        self.opt.mask_shapes = [int(x) for x in str_mask_shapes]

        # model name and date
        self.opt.date_str = 'test_'+time.strftime('%Y%m%d-%H%M%S')
        self.opt.model_folder = self.opt.date_str + '_' + self.opt.dataset + '_' + self.opt.model
        self.opt.model_folder += '_s' + str(self.opt.img_shapes[0]) + 'x' + str(self.opt.img_shapes[1])
        self.opt.model_folder += '_gc' + str(self.opt.g_cnum)
        self.opt.model_folder += '_randmask-' + self.opt.mask_type if self.opt.random_mask else ''
        if self.opt.random_mask:
            self.opt.model_folder += '_seed-' + str(self.opt.seed)
        self.opt.saving_path = self.opt.test_dir

        if os.path.exists(self.opt.saving_path) is False and self.opt.mode == 'save':
            os.mkdir(self.opt.saving_path)

        return self.opt
