
 getIndex=function (x){
  #### usage: id = getIndex (x)
  #### returns the index ID such that GRILLE[ID] 
     if (any(x < min(GRILLE)-2^(-EXPO2) - 2^(-EXPO2 - 1)  )|| any(x > max(GRILLE)  + 2^(-EXPO2 - 1)) ){
         warning(' x should be within  the range of GRILLE ');
     }
     id = round(2^EXPO2 * (x-1))
     id = sapply(id, function(x){max(x,1)})
     ## max(id,1)
     id = sapply(id, function(x){min(x, length(GRILLE))})
     ##min(id, length(GRILLE))
     
     return(id)
}


getAbsc = function(x){
  #### usage: absc = getAbsc (x)
  #### returns the closest number to x which writes r = GRILLE[ID] 
  #### where ID is an integer. 
  id = getIndex(x) ; 
  absc  = GRILLE[id] ## id * 2^(-EXPO2) ;
return(absc)
}


evalHeight=function(x){
  ## USAGE : f = evalHeight (x)
  ## x:   a vector [1, n] : the abscissas where one wants to evaluate
  ##    the function Height
  ## VALUE : 
  ## f:   a vector of same size than x : the values of Height at 
  ##      at the closest points in the 'GRILLE' vector. 
  id = getIndex(x)
  f = MESURES[id]
    return(f)
}
