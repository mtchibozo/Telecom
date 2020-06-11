# -*- coding: utf-8 -*-
"""
Éditeur de Spyder

Ceci est un script temporaire.
"""

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

def displayLife(universityList): # prend en argument une liste des string de noms d'universités
    coordinates=[]    
    for uni in universityList:
        uniId=d.getId(d.disambiguate(uni))
        (lat,lon)=coordinatesFromUniversityId(uniId)
        coordinates.append([float(lat),float(lon)])
    
    fig=plt.figure()
    ax=fig.add_axes([0.1,0.1,0.8,0.8])
#    m = Basemap(llcrnrlon=-30.,llcrnrlat=20.,urcrnrlon=60.,urcrnrlat=80.,\
#            rsphere=(6378137.00,6356752.3142),\
#            resolution='l',projection='merc',\
#            lat_0=40.,lon_0=-20.,lat_ts=20.) #just Europe
    m = Basemap(projection='mill',lon_0=0) # Whole map    
#    m = Basemap(width=12000000,height=9000000,projection='lcc',
#            resolution='c',lat_1=45.,lat_2=55,lat_0=50,lon_0=-107.) 
    m.drawcoastlines()
    m.fillcontinents()

    for item in coordinates:
        deblat=item[0]
        deblon=item[1]
        xpt,ypt = m(deblat,deblon) #convert to lat/lon
        lonpt1, latpt1 = m(xpt,ypt,inverse=True) # plot a blue dot there
        m.plot(xpt,ypt,'bo') # plot a blue dot there      
    ax.set_title('Parcours du Chercheur')
    plt.show()        

    
def displayTrip(university1, university2):
    
    id1=d.getId(d.disambiguate(university1))
    id2=d.getId(d.disambiguate(university2))
    (lat1,lon1)=coordinatesFromUniversityId(id1)
    (lat2,lon2)=coordinatesFromUniversityId(id2)
<<<<<<< HEAD
    display(float(lat1),float(lon1),float(lat2),float(lon2))
=======

    display(lat1,lon1,lat2,lon2)
>>>>>>> refs/remotes/origin/data_mining
    
def display(lat1,lon1,lat2,lon2):
# create new figure, axes instances.
    fig=plt.figure()
    ax=fig.add_axes([0.1,0.1,0.8,0.8])
    # setup mercator map projection.
    m = Basemap(projection='mill',lon_0=0) # Whole map
#    
#     
#    m = Basemap(llcrnrlon=-30.,llcrnrlat=20.,urcrnrlon=60.,urcrnrlat=80.,\
#            rsphere=(6378137.00,6356752.3142),\
#            resolution='l',projection='merc',\
#            lat_0=40.,lon_0=-20.,lat_ts=20.) #just Europe
#    
    # nylat, nylon are lat/lon of New York
    deblat = lat1; deblon = lon1
    # lonlat, lonlon are lat/lon of London.
    finlat = lat2; finlon = lon2
    # draw great circle route between NY and London
    m.drawgreatcircle(deblon,deblat,finlon,finlat,linewidth=2,color='r')
    m.drawcoastlines()
    m.fillcontinents()
    # draw parallels
    m.drawparallels(np.arange(10,90,20),labels=[1,1,0,1])
    # draw meridians
    m.drawmeridians(np.arange(-180,180,30),labels=[1,1,0,1])
    
    xpt1,ypt1 = m(lon1,lat1)
    xpt2,ypt2 = m(lon2,lat2)

    lonpt1, latpt1 = m(xpt1,ypt1,inverse=True) # plot a blue dot there
    lonpt2, latpt2 = m(xpt2,ypt2,inverse=True) # plot a blue dot there

    m.plot(xpt1,ypt1,'bo')  # plot a blue dot there
    m.plot(xpt2,ypt2,'bo')  # plot a blue dot there

    # put some text next to the dot, offset a little bit
    # (the offset is in map projection coordinates)
    plt.text(xpt1+100000,ypt1+100000,'Universite' % (lonpt1,latpt1)) 
    plt.text(xpt1+100000,ypt1+100000,'Universite' % (lonpt2,latpt2)) 

    ax.set_title('Parcours du Chercheur')
    plt.show()
