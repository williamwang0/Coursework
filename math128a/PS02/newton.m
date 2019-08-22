function r = newton(x0, f, p, n)

options = optimset('Display','off');

func = f{1};
deriv = f{2};
temp = x0 - (func(x0)/deriv(x0));
numCorrectBits = floor(log2(fsolve(func, 2, options) - temp))
steps = [temp, n];

%if abs(x0 - temp) <= eps^3
 %   r = [temp, n];
  %  r = temp;
if n > 1000
    r = 'oscilate';
elseif x0 == temp
    r = [temp, n];
    %r = temp;
else
    r = newton(temp, f, p, n+1);
end