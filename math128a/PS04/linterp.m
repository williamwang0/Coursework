function l = linterp(i, t)
l = @(x)1;
[~,tcol] = size(t);
for y = 1:tcol  
    if y ~= i
        l = @(x) ( ( l(x).* (x-t(y)) / (t(i)-t(y)) ) );
    end
end
end
