function [u, uHist] = ieuler(a, b, ya, f, n, p)
    % a, b: interval endpoints with a < b
    % n: number of steps with h = (b - a)/n
    % ya: vector y(a) of initial conditions
    % f: function handle f(t, y) to integrate (y is a vector)
    % u: output approximation to the final solution vector y(b)
    % p: extra parameters (p = [L]
    h = (b - a) / n;
    u = ya;
    uHist = zeros(length(ya), n + 1);
    uHist(:, 1) = u;
    tCurr = a;
    for i = 1:n
        tCurr = tCurr + h;
        unext = (u + h*p(1)*cos(30*tCurr) - h*sin(30*tCurr)) / (1 + h*p(1));
        u = u + h * f(tCurr, unext);
        % Assumes u is a column vector.
        uHist(:, i + 1) = u;
    end
end
