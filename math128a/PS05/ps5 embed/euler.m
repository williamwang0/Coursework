function [u, uHist] = euler(a, b, ya, f, n)
    % a, b: interval endpoints with a < b
    % n: number of steps with h = (b - a)/n
    % ya: vector y(a) of initial conditions
    % f: function handle f(t, y) to integrate (y is a vector)
    % u: output approximation to the final solution vector y(b)
    h = (b - a) / n;
    u = ya;
    uHist = zeros(length(ya), n + 1);
    uHist(:, 1) = u;
    tCurr = a;
    for i = 1:n
        u = u + h * f(tCurr, u);
        % Assumes u is a column vector.
        uHist(:, i + 1) = u;
        tCurr = tCurr + h;
    end
end
