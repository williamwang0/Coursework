function p = pleg(t,n)
if n == 0
    p = 1;
elseif n == 1
    p = t;
else
    last1 = t;
    last2 = 1;
    c = 2;
    while c <= n
         p = t*last1 - ((c-1)^2 / (4*(c-1)^2 - 1)*last2);
         last2 = last1;
         last1 = p;
         c = c + 1;
    end
end
end
