from os import listdir
from os.path import isfile, join
import numpy as np
import pandas as pd 
import matplotlib.pyplot as plt

path = "C:\\Users\\kaursima\\Desktop\\CSV Data"
list_of_files = listdir(path)

for i in list_of_files:
	table = pd.read_csv(path + "\\" + i)
	new_table = table.groupby(by="Tab").sum()
	#finding the title
	dash = i.find("-")
	title = i[0:dash]
	#making graph for tabs and counts
	tabs = table['Tab']
	counts = table['Count']
	plt.bar(x=tabs.values, height=counts.values)
	plt.title(title)
	plt.xticks(rotation=90)
	plt.show()