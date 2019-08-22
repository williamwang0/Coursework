function result = gquad5(a,b,f)
x1 = sqrt(((10/9) + sqrt((100/81) - (20/21)))/2);
x3 = sqrt(((10/9) - sqrt((100/81) - (20/21)))/2);
legroots5 = [-x1 -x3 0 x3  x1];
w1 = 1/900*(322 + 13*sqrt(70));
w2 = 1/900*(322 - 13*sqrt(70));
weights = [w2 w1 128/225 w1 w2];
result = (b - a)/2 * summat(1,5,@(k) weights(k) * f((a+b)/2 + legroots5(k)*(b-a)/2));
end