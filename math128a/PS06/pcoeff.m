function [p,q] = pcoeff(t,n,k)

p = zeros(1,k);
for j = 1:k
    f = @(x) (1);
    for i = 1:k
        if (i ~= j)
            f = @(x) ( ( f(x).* (x - t(n-i+1)) / (t(n-j+1)-t(n-i+1))));             
        end
    end
    p(j) = integral(f,t(n), t(n+1));
end

q = zeros(1,k);
for j = 1:k
    f = @(x) (1);
    for i = 1:k
        if (i ~= j)
            f = @(x) ( ( f(x).* (x - t(n-i+2)) / (t(n-j+2)-t(n-i+2))));             
        end
    end
    q(j) = integral(f,t(n), t(n+1));
end

end