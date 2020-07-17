import torch
from torchvision.models import mobilenet_v2

class Make(torch.nn.Module):
    def __init__(self,N_classes=5,pretrained=True):
        super(Make,self).__init__()
        self.original_model = mobilenet_v2(pretrained=pretrained)
        self.regression = torch.nn.Sequential(torch.nn.Linear(1000,N_classes))

    def forward(self,x):
        x = self.original_model(x)
        return torch.sigmoid(self.regression(x))
