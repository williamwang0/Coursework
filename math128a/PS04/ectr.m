function w = ectr(n,k)

w = ones(1, n);
w(1) = 1/2;
w(n) = 1/2;

if k > 2

    list = 0:k-2;
    difnums = fornberg(k-3,0,list);

% w = ones(5,n);
% w(1,1) = 1/2;
% w(1,n) = 1/2;
% 
% for i = 4:2:10
%    difnums = fornberg(i-3,0,0:i-2);
%    for j = 1:i-1
%        w(i/2,j) = w(i/2,j) - (bern(i-3) * difnums(j));
%        w(i/2,n-j+1) = w(i/2,j);
%    end
% end

    for i = 1:k-1
        w(i) = w(i) - (bern(k-3) * difnums(i));
        w(n-i+1) = w(i);
    end  
end
end