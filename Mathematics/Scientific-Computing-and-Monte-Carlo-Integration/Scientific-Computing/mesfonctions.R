
runge = function(x){
  1/(1+5*x**2)
}

dividif=function(x,y){
  n = length(x)-1
  d = y
  if (n==0){return(d)}
  M=matrix(ncol = n+1,nrow = n+1)
  M[,1] = y
  for (j in 2:(n+1)){
    M[,j] = M[,j-1]
    for (i in (j):(n+1)){
      M[i,j] = (M[i,j-1]-M[i-1,j-1])/(x[i]-x[i-j+1])
    }
  }
  return(M[,n+1])
}

hornerNewton = function(a,x,z){
  n <- length(x) - 1
  if((n<0)||(length(a) != (n+1)))
  {
    stop('at least one interpolating point is needed,
         a and x should have same length')
  }
  f <- a[n+1]*rep(1,length(z))
  if (n >= 1){
    for (k in 1:n){
      f <- a[n+1-k]+f*(z - x[n+1-k])
    }
  }
  return(f)
}

interpolDividif = function(x,y,z){
  hornerNewton(dividif(x,y),x,z)
}

interpolLagrange =function(n, a, b, neval, nodes = 'equi', FUN, Plot){
  ## Generic Lagrange interpolation, with equidistant or Chebyshev nodes. 
  ## @param n : the degree of the interpolating polynomial on each
  ## subinterval
  ## @param a : left end-point of the interval
  ## @param b : right end-point of the interval
  ## @param neval :number of evaluation points (a regular grid will be
  ## used on [a,b]
  ## @param nodes :string, either "equi" (default) for equidistant
  ## Lagrange interpolation (on each subinterval) or "cheby" for
  ## using Chebyshev nodes.
  ## @param FUN: the function to be interpolated 
  ## @param Plot : logical. Setting 'Plot' to TRUE produces a plot
  ## showing the graph of
  ## the true functions and its interpolation.  
  ## @return : vector of size 'neval': the values of the Lagrange
  ## polynomial on an equi-distant grid.
  
  if (nodes == "equi"){
    x =  seq(a,b, length.out = n+1) ## Complete 
  }
  else if (nodes == "cheby"){
      x = ((a+b)+(b-a)*cos(((0:n)+1/2)*pi/(n+1)))/2
    
  }
  else{stop("the nodes must be either 'equi' or 'cheby'") }
  
  z = seq(a,b,length.out = neval)
  y = sapply(x,FUN)
  f = interpolDividif(x,y,z)


    ##
  ## Complete the code: compute a vector 'f' containing
  ## the interpolated  values on an equidistant
  ## evaluation grid 'z'. 
  ##
  ##
  if( Plot ){
    if (nodes == "equi"){ methodName = " equidistant "}
    else {   methodName = " Chebyshev "}
    
    plot(z, sapply(z,FUN), type="l", ylim=range(c(y,f)) )
    title(main = paste("Lagrange interpolation with ",
                       toString(n+1), methodName,
                       " nodes", sep=""))
    lines(z,f, col = 'blue') 
    
    legend('topright', legend=c('function','interpolation'),
           col = c('black','red'), lwd=1)
    
  }
  return(f)              
}

errorRank = function(nodes){
  ## param @nodes : "equi" or 'cheby"
  ## returns the integer of the rank which minimizes the error on the evaluation of
  ## the evalHeight function over 10**4 points in [1,2.5]
  erreur_list = seq(1,1,length.out = 50) #On conjecture que le rang est inferieur ou egal a 50
  for (i in 1:50){
    erreur = max(abs(interpolLagrange(i,1,2.5,10**4,nodes,evalHeight,FALSE)-evalHeight(seq(1,2.5,length.out = 10**4))))
    erreur_list[i] <- erreur
  }
  
  c(which.min(erreur_list) , erreur_list)
}

piecewiseInterpol=function(n,nInt,a,b,neval, nodes = "equi", FUN, Plot){
  ## @param n : the degree of the interpolating polynomial on each
  ## subinterval
  ## @param nInt :  the number of sub-intervals
  ## @param a, b : endpoints of the interval
  ## @param neval : the number of points on the interpolating grid (on
  ## each subinterval)
  ## @param nodes : string, either "equi" (default) for equidistant
  ## Lagrange interpolation (on each subinterval) or "cheby" for
  ## chebyshev nodes.
  ## @param FUN the function to be interpolated
  ## @param Plot : logical. Should the result be plotted ?
  ## @return : a matrix with 2 rows and neval * nInt -neval + 1:
  ## values of the interpolated funtion on a regular grid (first row)
  ## and the corresponding abscissas (second row).
  
  intEndPoints = seq(a,b,length.out = nInt+1)
  f = c()
  z = c()
  for (m in 1:nInt){
    A = intEndPoints[m]; B = intEndPoints[m+1] 
    
    fm = interpolLagrange(n,A,B,neval,"equi",FUN,FALSE) ## complete the code 
    zm = seq(A,B,length.out = neval)## complete the code 
      
      if( m >= 2){
        ## remove first element of zm, fm to avoid
        ## duplicate values of the  interpolating vector
        zm <- c(zm[2:length(zm)])
        fm <- c(fm[2:length(fm)])
      }
    z = c(z,zm)
    f = c(f,fm)
  }
  
  if (Plot == 1){
    if (nodes == "equi") {methodName = " equidistant "}
    else  {methodName = " Chebyshev "}
    
    
    plot(z, sapply(z,FUN),type="l")
    title(main = paste("Piecewise  Lagrange  interpolation with ",
                       toString(n+1), methodName, " nodes  on ",
                       toString(nInt), " Intervals", sep=""))
    lines(z,f, col='red', lwd=2)
    legend('topright', legend = c('function','interpolation'),
           lwd=c(1,2), col=c('black','red'))
  }
  return(rbind(f,z) )
}

errorRank2 = function(neval){
  ## param @nodes : "equi" or 'cheby"
  ## param @type : number of evaluation points
  ## returns the integer of the rank which minimizes the error on the evaluation of
  ## the evalHeight function over 10**4 points in [1,2.5]
  # Order and error under equi
  erreur_equi_list = seq(1,1,length.out = 50) #On conjecture que le rang est inferieur ou egal a 50
  for (i in 1:50){
    erreur_equi = max(abs(interpolLagrange(i,1,2.5,neval,'equi',evalHeight,FALSE)-evalHeight(seq(1,2.5,length.out = neval))))
    erreur_equi_list[i] <- erreur_equi
  }
  # Order and error under cheby
  erreur_cheby_list = seq(1,1,length.out = 50) #On conjecture que le rang est inferieur ou egal a 50
  for (i in 1:50){
    erreur_cheby = max(abs(interpolLagrange(i,1,2.5,neval,'cheby',evalHeight,FALSE)-evalHeight(seq(1,2.5,length.out = neval))))
    erreur_cheby_list[i] <- erreur_cheby
  }  
  if (min(erreur_equi_list) < min(erreur_cheby_list)){
    return(c(which.min(erreur_equi_list),'equi'))
  }
  return(c(which.min(erreur_cheby_list) , 'cheby'))
}

###Partie 2###



trapezeInt =function(FUN,a,b,M){
  ##' TRAPEZOIDAL INTEGRATION RULE (COMPOSITE)
  ##' @param FUN : the function to be integrated
  ##' @param a, b : interval end points 
  ##' @param M : number of intervals (each of size (b-a)/M)
  ##' @return: the value of the composite trapezoidal quadrature. 
  x = seq(a,b, length.out= M+1)
  y = sapply(x, FUN)
  h = (b-a)/M 
  
  w = seq(1,1,length.out = M+1)
  w[1] <- 1/2
  w[M+1] <- 1/2
  
  
  q = h*sum(w*y) ## Complete the code here. Use vector operations, not a loop !
  
  return(q)
}

refineTrapeze=function(FUN,a,b,M,q){
  ##' refinement of the subdivision step: incremental method
  ##' @param FUN : the function to be integrated
  ##' @param a, b : interval end points 
  ##' @param M : initial number of intervals (each of size (b-a)/M)
  ##'  having been used to compute q
  ##' @param  q : the value of the trapezoidal  quadrature method
  ##'  of stepsize (b-a)/M
  ##' @return : the value of the quadrature for a stepsize h' = h/2
  h = (b-a)/M
  x = a + (2*(0:(M-1))+1)*(h/2)
  ## complete here : 
  ##  x : a vector of size M :
  ##     the additional abscissas where 'fun' must be evaluated.
  y = sapply(x, FUN)
  
  Q = h/2*sum(y) + 1/2*q##  complete here : a function of y, h and q. 
  return(Q)
}

simpsonInt = function(a,b,FUN,M){
  qth = trapezeInt(FUN,a,b,M)
  qth2 = refineTrapeze(FUN,a,b,M,qth)
  simps_h = (4/3)*(qth2 - (1/4)*qth)
  simps_h
}

evalErrSimpson=function(FUN,a,b,M){
  qth = trapezeInt(FUN,a,b,M)
  qth2 = refineTrapeze(FUN,a,b,M,qth)
  qth4 = refineTrapeze(FUN,a,b,2*M,qth2)
  simps_h = 4/3*(qth2-1/4*qth)
  simps_h2 = 4/3*(qth4-1/4*qth2)
  q = simps_h2
  E = abs(simps_h - simps_h2)/15
    return(c(E,q))
}

evalErrSimpsonLoop=function(precision){
  M = 5
  error = evalErrSimpson(evalHeight,1,2.5,M)[1]
  
  while (error > 10**(-5)){
    if (M > 1000){
      break
    }
    M <- M*2
    error = evalErrSimpson(evalHeight,1,2.5,M)[1]
  }
  ##We now know that the precision threshold is met between M and M/2
  for (i in (M/2):M){
    if (evalErrSimpson(evalHeight,1,2.5,i)[1] < precision) {
      return(i)
    }
  }
  return(M)
}


###Partie 3###

evalHeightTranslate = function(x){ return(evalHeight(x+1))}

richardson = function(FUN,n,t,delta){
  ## Calcule le tableau des differences divisees en 0 du
  ## polynome d'interpolation en t,delta t, ... delta^n t
  ## renvoie un vecteur de taille n+1:
  ## le vecteur des A_{k,k}, k= 0 .. n
  ## (pas la matrice).
  ## La meilleure approximation est le dernier element A[n+1].
  ##
  lx = log(t) + log(delta) *(0:n)
  x = exp(lx)
  A = sapply(x,FUN)
  
  M=matrix(ncol = n+1,nrow = n+1)
  M[,1] = A
  for (j in 2:(n+1)){
    M[,j] = M[,j-1]
    for (i in (j):(n+1)){
      M[i,j] =  1/(1-delta**j)*(M[i,j-1]-delta**j*M[i-1,j-1])
    }
  }
  return(diag(M))
}
plotRichardson = function(n,t,delta){
  A=richardson(evalHeightTranslate,n,t,delta)
  lx=log(t)+log(delta)*(0:n)
  x = exp(lx)
  y = sapply(x,evalHeightTranslate)
  
  plot(0:n,y,col='blue',type='l')
  lines(0:n,A,col='red');
  legend('topright',legend=c("erreur naive","erreur Richardson"),col=c('blue','red'),lwd=2)
}