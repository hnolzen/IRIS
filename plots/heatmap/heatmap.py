import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from PIL import Image

data = pd.read_csv('iris_output.csv', header = 0)

heat_array = np.zeros((12, 12))
frames = []

def plot_heatmap():
    plt.figure(dpi = 80)
    plt.imshow(heat_array, cmap = 'viridis', vmin = 0, vmax = max_questing_nymphs)
    plt.colorbar()
    
    canvas = plt.get_current_fig_manager().canvas
    canvas.draw()
    img = Image.frombytes('RGB', canvas.get_width_height(), canvas.tostring_rgb())
    plt.close()
    
    frames.append(img)

max_questing_nymphs = data.questing_nymphs.max()   

current_tick = 0 

for row in data.itertuples():
    if current_tick != row.tick:
        plot_heatmap()
        
        print(current_tick)
        current_tick = row.tick
        
    heat_array[row.y, row.x] = row.questing_nymphs

plot_heatmap()

frames[0].save(
    "heatmap.gif",
    format = "gif",
    save_all = True,
    append_images = frames[1:],
    duration = 70,
    loop = 0,
)