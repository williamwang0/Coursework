function [w,t] = gaussint(N)
% N: highest Number of Gauss weights and points
% w: shape(n,n). weight corresponding n = 1:N
% t: shape(n,n). roots of legendre polynomials corresponding n = 1:N


  % find roots, stored in variable t.
  t = nan(N);
  tol = 1e-12;

  for n = 1:N
    for j = 1:n
      if n == 1
        left = -1;
        right = 1;
      elseif j == 1
        left = -1;
        right = t(n-1,1);
      elseif j == n
        left = t(n-1,n-1);
        right = 1;
      else
        left = t(n-1,j-1);
        right = t(n-1,j);
      end
      t(n,j) = bisection(left, right, @pleg, n, tol);
    end
  end

  % find weights, stored in variable w.
  w = nan(N);
  p = struct;
  p.t = t;
  for n = 1:N
    for j = 1:n  
      p.n = n;
      p.j = j;
      %w(n,j) = gadap(-1, 1, @Lj2, p, tol);
      x = linspace(-1,1,8*n+1);
      w(n,j) = dot( ectr(8*n, 2*n+1)/n/4, Lj2(x,p));
    end
  end
end
