function [r, h] = bisection(a, b, f, p, t)
h = [];
cond = true;
while cond
    assert(sign(f(a)) ~= sign(f(b)),"bounds wrong")
    
    if 0 < a && 0 < b
        m = sqrt(a*b);
    elseif a < 0 && b < 0
        m = -sqrt(a*b);
    elseif a == 0
        m = realmin;
    elseif b == 0
        m = -realmin;
    elseif a < 0 && b > 0
        m = 0;
    end
    
    h = [h [a;b;f(m)]];
    
    if m == a || m == b
        cond = false;
    end
    if (b-a) <= t*min(abs(a), abs(b))
        cond = false;
    end
    if f(m) == 0
        cond = false;
    end
    
    if sign(f(m)) == sign(f(a)) || m == realmin
        a = m;
    elseif sign(f(m)) == sign(f(b)) || m == -realmin
        b = m;
    end
end
r = m;