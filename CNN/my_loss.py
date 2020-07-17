import torch

class Make():
    def __init__(self,Type='CrossEntropy',cuda=False):
        loss_dict = {'MSE':torch.nn.MSELoss(),'CrossEntropy':torch.nn.CrossEntropyLoss()}
        self.loss_fcn = loss_dict[Type]
        self.one_hot = Type=='MSE'
        self.cuda = cuda

    def compute(self,targets,outputs):
        if self.one_hot:
            one_hot_targets = torch.tensor([[0.0,0.0,0.0,0.0,0.0]]*len(targets))
            for (array,target) in zip(one_hot_targets,targets): array[target] = 1
            targets = one_hot_targets
            if self.cuda: targets = targets.cuda()

        return self.loss_fcn(outputs,targets)
