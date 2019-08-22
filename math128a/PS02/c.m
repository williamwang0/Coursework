function result = c()
result = cell(2,1);
result{1} = @(t) (t - eps^3)^3;
result{2} = @(t) 3*(t - eps^3)^2;