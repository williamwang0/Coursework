function [t,u] = pcode(a,b,ua,f,r,k,N)

t = zeros(N, 1);

h = (b-a) / N;
i = 1;
t(1) = a;
h1 = (b-a)*((h/(b-a))^(k/2));

while h1 < h
    i = i + 1;
    t(i) = t(i-1) + h1;
    h1 = h1 * (1 + (1/k));
end

h = (b - t(i))/(N-i);
t(i:N) = linspace(t(i), b, N-i);

for n = 1:N-1
    k1 = min(k,n);
    if (n < i + (2*k))
        [p,q] = pcoeff(t,n,k1);
        u = ua(n);
        for qind = 1:k1
            u(n+1) = u(n) + (q(qind + 1) * f(n - qind + 2));
        end
    end    
end

end