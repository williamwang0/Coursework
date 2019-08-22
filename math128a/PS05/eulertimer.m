function time = eulertimer(n)
t = cputime;
euler(0,17.06521656015796,[0.994 0 0 -2.00158510637908],@(t,u,r) satellite(t,u,r), 0,n)
time = cputime-t;
end