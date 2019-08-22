function result = bern(n)
if n == 0
    result = -1;
else
    result = (-1/2 * (1 / factorial(2*n))) - summat(0, n-1, @(k) bern(k) / factorial(2*n - (2*k) + 1));
end
end