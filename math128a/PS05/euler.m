function u = euler(a,b,ya,f,r,n)

h = (b-a)/n;
t = a;
w = ya;
error = 0;
x = zeros(1,n);
y = zeros(1,n);

for i = 1:n
    w = w + h*f(t,w,r);
    t = a + i*h;
    x(i) = w(1);
    y(i) = w(2);
    error = max( [error, abs(w(1) - cos(t)), abs(w(2) - sin(t)), abs(w(3) + sin(t)), abs(w(4) - cos(t))] );
end

plot(x,y)

X = ['Max error over time steps: ', num2str(error)];
disp(X)

u = w;

end