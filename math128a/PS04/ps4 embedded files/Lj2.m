function y = Lj2(x,p)
% square of lagrange polynomials, as functions
  n = p.n;
  j = p.j;
  t = p.t;
  y = ones(size(x));
  for i = 1:n
    if i ~= j
      y = y .* (x-t(n,i)) ./ (t(n,j)-t(n,i));
    end
  end  
  y = y.^2;
end   
