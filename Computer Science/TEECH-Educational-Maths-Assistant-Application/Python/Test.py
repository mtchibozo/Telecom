import Algo0 as alg
import clustering2 as cl
import numpy as np
from sklearn import metrics
from sklearn.datasets.samples_generator import make_blobs
from sklearn.preprocessing import StandardScaler

Rexo=[[0,[0.5,0.2,0.2,0.1],[1,1,2,4]],[1,[0.6,0.1,0.2,0.1],[1,1,2,4]],[2,[0.4,0.3,0.2,0.1],[1,1,2,4]],[3,[0.5,0.4,0.0,0.1],[1,1,2,4]],[4,[0.7,0.2,0.0,0.1],[1,1,2,4]],[5,[0.4,0.2,0.2,0.2],[1,1,2,4]]]

S=alg.exos_a_traiter (0, 1, Rexo)

P= alg.exos_similaires(S,1)

e_u = [5,[0.5,0.1,0.3,0.1],[1,1,2,4]]

Rec = alg.recommandation(0,1,Rexo,e_u)
