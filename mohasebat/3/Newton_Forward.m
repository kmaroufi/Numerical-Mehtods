function [pol_rslt,value] = Newton_Forward(f,X,Y,optionalPoint,v,d,isFEntered)
value = 0;
    format short
    
    if(isFEntered)
        for i = 1:length(X)
            Y(i) = double(subs(f,'x', X(i)));
        end
    end
    
    n = size(X,2);
    h = X(2) - X(1) ;
    r = [ 1 , -X(1) ] / h ;
    a = zeros (n,n);
    a(:,1) = Y ;
    
    rslt = zeros (1,n) ;
    rslt(1) = Y(1) ;
    
    for j = 2:n 
       for i = j:n 
            a(i,j) = ( a(i,j-1) - a(i-1,j-1) ) ;
       end
       rslt(j) = a(j,j);
    end
    
    rslt = round(rslt,d) ;
    
    pol_rslt = zeros(1,n) ;
    pol_rslt(n) = rslt(1) ;
    
    for i = 2:n
        pol = zeros (1,n-i+1) ;
        pol(n-i+1) = rslt(i) ;
        for j = 1:i-1
           pol = conv ( pol , [r(1),r(2)-j+1] );
           pol = pol / j ;
           pol = round(pol,d);
        end
        pol_rslt = pol_rslt + pol ;
        pol_rslt = round(pol_rslt,d);
    end
    
    while ( pol_rslt(1) < 0.0000001 )
            pol_rslt = pol_rslt(2:end) ;
    end
    
    pl = min(X)-10*h:h/10:max(X)+10*h;
    plot ( pl , polyval(pol_rslt,pl) , X , Y , '*' );
   
    
    if(optionalPoint)
        value = polyval(pol_rslt,v);
    end

    pol_rslt = vpa(poly2sym(pol_rslt));
    pol_rslt = char(pol_rslt);
end

% '', [0.1 0.2 0.3 0.4 0.5] , [1.4 1.56 1.76 2 2.28] , 0.25