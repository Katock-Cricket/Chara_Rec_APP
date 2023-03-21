import pickle

import torch
import torchvision.transforms as transforms
from PIL import Image
from java.lang import String


def demo(img="fo2.png", modelPth="model.pt", char_dict="char_dict"):
    transform = transforms.Compose(
        [transforms.Resize((32, 32)), transforms.ToTensor(),
         transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])])
    img = Image.open(img).convert('RGB')
    img = transform(img)
    img = img.unsqueeze(0)
    model = torch.jit.load(modelPth)

    with torch.no_grad():
        output = model(img)
    _, pred = torch.max(output.data, 1)
    f = open(char_dict, 'rb')
    dic = pickle.load(f)
    ret = ""
    for cha in dic:
        if dic[cha] == int(pred):
            ret = cha
            print('predict: ', ret)

    f.close()
    return ret
