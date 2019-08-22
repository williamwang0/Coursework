function result = chebyshev(n)
lambda = 0:n;
result = (5 + 5*cos((2*lambda+1)*pi / ((2*n)+2))) / 2;
end