function result = d()
result = cell(2,1);
result{1} = @(t) atan(t - eps^2);
result{2} = @(t) 1 / (1 + (t - eps^2)^2);