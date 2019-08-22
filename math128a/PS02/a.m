function result = a()
result = cell(2,1);
result{1} = @(t) 2*sin(t) - t;
result{2} = @(t) 2*cos(t) - 1;