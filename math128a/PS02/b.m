function result = b()
result = cell(2,1);
result{1} = @(t) 1/t + log(t) - 2;
result{2} = @(t) -1 / (t^2) + 1/t;