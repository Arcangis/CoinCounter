import torch
import torchvision
import pandas as pd
from PIL import Image


class Make(torch.nn.Module):
    def __init__(self,images_path, ann_path,output_size=[224,224]):
        super(Make,self).__init__()
        self.value_convert = {'5':0,'10':1,'25':2,'50':3,'100':4}
        
        self.output_size = output_size
        self.images_path = images_path
        self.ann_list = pd.read_csv(ann_path)

    def __len__(self): return len(self.ann_list)

    def __getitem__(self,x):
        label = self.ann_list.iloc[x]
        image = Image.open(self.images_path+label['name']).resize((self.output_size[0],self.output_size[1]), Image.BILINEAR)
        value = self.value_convert[str(label['value'])]
        
        
        #image = torchvision.transforms.functional.to_tensor(image)
        transforms = torchvision.transforms.Compose([torchvision.transforms.RandomHorizontalFlip(),
                                                    torchvision.transforms.ToTensor(),
                                                    torchvision.transforms.Normalize(mean=[0.485, 0.456, 0.406],std=[0.229, 0.224, 0.225])])
        value = torch.tensor(value).type(torch.float32)
        
        return {'image':transforms(image),'value':value}