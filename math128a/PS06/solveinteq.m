function [t,u] = solveinteq(a,b,kernel,rhs,p,n)

t = zeros(n);
u = zeros(n,1);

weights = gaussint(n);
f = rhs(t, p);

start = a;
for i = 1:n
    s = (b-a)/n;
    sum = 0;
    for j = 1:n
        sum = sum + (kernel(t, s) * weights(j) * u(j));
    end
    u(i) = rhs(i,p) - sum;
end

end