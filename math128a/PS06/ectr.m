function [w] = ectr(n, k)
kk = k;
nn = n;
%Set base trapezoid rule
w = ones(size(0:n));
w(1) = .5;
w(end) = .5;
if k>2
    %Compute b-values for b_1 through b_4
    m = 1:4;
    b = 1./factorial(2*m+1)-1./(2*factorial(2*m));
    for m = 2:4
        for j = 1:(m-1)
            b(m) = b(m) - b(j)/factorial(2*(m-j)+1);
        end
    end
    %Compute Delta values for m=1,3,5,7
    M = k - 3;
    N = k - 2;
    Deltas = zeros(N+1,M);
    D_start = zeros(N+1,M+2);
    D_start(1, 2) = 1;
    X = 0:N;
    for h = 1:N+1
        x = X;
        x([1 h]) = x([h 1]);
        D = D_start;
        for j = 2:M+2
            for i = 2:N+1
                m = j - 2;
                D(i,j) = (D(i-1, j-1)*m - D(i-1,j)*x(i))/(x(1)-x(i));
            end
        end
        Deltas(h,:) = D(end,3:end);
    end
    %Multiply b by Deltas to get weight adjustments
    wa = zeros(k-1,1);
    for m = 1:(k/2-1)
        wa = wa + b(m) * Deltas(:, 2*m-1);
    end
    w(1:k-1) = w(1:k-1) - wa';
    w(n-k+3:n+1) = w(n-k+3:n+1) - flip(wa)';
end
end