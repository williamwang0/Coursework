function result = summat(a, b, f)
result = 0;
while a <= b
    result = result + f(a);
    a = a + 1;
end