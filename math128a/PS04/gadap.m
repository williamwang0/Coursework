function [int, abt] = gadap(a,b,f,r,tol)
n = 10000;
abt = cell(1,n);
int = 0;
queue = cell(1,n);
queue{1} = [a,b,gquad5(a,b,f)];
queueIndex = 2;
abtIndex = 1;
numFuncCalls = 0;
while queueIndex ~= 1
    x = queue{1};
    queue(1) = [];
    queueIndex = queueIndex - 1;
    
    numFuncCalls = numFuncCalls + 1;
    newa = x(1);
    mid = (x(1) + x(2)) / 2;
    newb = x(2);
    
    [~, col] = size(queue);
    if col > n
        break
    end
    
    errorest = abs(x(3) - (gquad5(newa,mid,f) + gquad5(mid,newb,f)));
    %disp(errorest / (tol * max(abs(x(3)), abs(gquad5(newa,mid,f)) + abs(gquad5(mid,newb,f)))))
    if errorest < tol * max(abs(x(3)), abs(gquad5(newa,mid,f)) + abs(gquad5(mid,newb,f)))               
        abt{abtIndex} = x;
        int = int + x(3);
        abtIndex = abtIndex + 1;        
    else        
        queue{queueIndex} = [newa mid gquad5(newa,mid,f)];       
        queue{queueIndex + 1} = [mid newb gquad5(mid,newb,f)];
        queueIndex = queueIndex + 2;       
    end
    
end

for i = 1:queueIndex - 1
    abt{abtIndex} = queue{i};
    abtIndex = abtIndex + 1;
    int = int + queue{i}(3);
end

end