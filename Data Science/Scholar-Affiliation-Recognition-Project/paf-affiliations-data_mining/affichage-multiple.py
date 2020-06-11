#!/usr/bin/env python2
# -*- coding: utf-8 -*-

import disambiguate as d
from mpl_toolkits.basemap import Basemap
import matplotlib.pyplot as plt
import numpy as np
import csv

'''
# create new figure, axes instances.
fig=plt.figure()
ax=fig.add_axes([0.1,0.1,0.8,0.8])
# setup mercator map projection.
m = Basemap(projection='mill',lon_0=0)

# nylat, nylon are lat/lon of New York
nylat = 48.8; nylon = 2.33 
# lonlat, lonlon are lat/lon of London.
lonlat = -37.90; lonlon = 145.13
# draw great circle route between NY and London
m.drawgreatcircle(nylon,nylat,lonlon,lonlat,linewidth=2,color='b')
m.drawcoastlines()
m.fillcontinents()
# draw parallels
m.drawparallels(np.arange(10,90,20),labels=[1,1,0,1])
# draw meridians
m.drawmeridians(np.arange(-180,180,30),labels=[1,1,0,1])
ax.set_title('Great Circle from Melbourne to Canberra')
plt.show()
'''


def coordinatesFromUniversityId(id):
    with open('addresses.csv','r') as f:
        reader=csv.reader(f)
        for row in reader:
            if id in row:
                return (row[4],row[5]) # latitude puis longitude

def universityFromCoordinates(lat,lon):
    with open('addresses.csv','r') as f:
        reader=csv.reader(f)
        for row in reader:
            if lat in row:
                return d.getUniversity(row[0])
        
def display2(L):
    coordinates=[]
    for uni in L:
        id1=d.getId(d.disambiguate(uni))
        (lat1,lon1)=coordinatesFromUniversityId(id1)
        coordinates.append((float(lat1),float(lon1)))
        
    fig=plt.figure()
    ax=fig.add_axes([0.1,0.1,0.8,0.8])
    
    # m = Basemap(projection='mill',lon_0=0) # World map
    m = Basemap(llcrnrlon=-30.,llcrnrlat=20.,urcrnrlon=60.,urcrnrlat=80.,\
        rsphere=(6378137.00,6356752.3142),\
        resolution='l',projection='merc',\
        lat_0=40.,lon_0=-20.,lat_ts=20.) #just Europe
                
    m.drawcoastlines()
    m.fillcontinents()
    m.drawparallels(np.arange(10,90,20),labels=[1,1,0,1])
    m.drawmeridians(np.arange(-180,180,30),labels=[1,1,0,1])
    ax.set_title('Parcours du Chercheur')

    if len(L)==1:
        deblat = coordinates[0][0]; deblon = coordinates[0][1]
        xpt1,ypt1 = m(deblon,deblat)
        lonpt1, latpt1 = m(xpt1,ypt1,inverse=True) # plot a blue dot there
        m.plot(xpt1,ypt1,'bo')  # plot a blue dot there
        plt.text(xpt1, ypt1, universityFromCoordinates(str(deblat),str(deblon)),fontsize=12,fontweight='bold',
                    ha='left',va='bottom',color='blue')
    else:
        deblat = coordinates[0][0]; deblon = coordinates[0][1]
        for item in coordinates:
            finlat = item[0]; finlon = item[1]
            
            m.drawgreatcircle(deblon,deblat,finlon,finlat,linewidth=2,color='r')
            
            xpt2,ypt2 = m(finlon,finlat)
            lonpt2, latpt2 = m(xpt2,ypt2,inverse=True) # plot a blue dot there
            m.plot(xpt2,ypt2,'bo')  # plot a blue dot there

            
            xpt1,ypt1 = m(deblon,deblat)
            lonpt1, latpt1 = m(xpt1,ypt1,inverse=True) # plot a blue dot there
            m.plot(xpt1,ypt1,'bo')  # plot a blue dot there

            deblat = item[0]; deblon = item[1]            
            plt.text(xpt2, ypt2, universityFromCoordinates(str(finlat),str(finlon)),fontsize=12,fontweight='bold',
                    ha='left',va='bottom',color='blue')
    plt.show()