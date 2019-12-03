function [pol_rslt,value] = Newton_Backward(f,X,Y,optionalPoint,v)
value = 0;
    format short
    n = size(X,2);
    a = zeros (n,n);
    a(:,1) = Y ;
    
    rslt = zeros (1,n) ;
    rslt(1) = Y(1) ;
    
    for j = 2:n 
       for i = j:n 
            a(i,j) = ( a(i-1,j-1) - a(i,j-1) ) / ( X(i-j+1) - X(i) ) ;
       end
       rslt(j) = a(j,j);
    end
    
    pol_rslt = zeros(1,n) ;
    pol_rslt(n) = 1 * rslt(1) ;
    
    %disp(Y) ;
    %disp(rslt) ;
    
    for i = 2:n
        pol = zeros (1,n-i+1) ;
        pol(n-i+1) = 1 * rslt(i) ;
        for j = 1:i-1
           pol = conv ( pol , [ 1 , -X(j)] ); 
        end
        pol_rslt = pol_rslt + pol ;
    end
    
    while ( pol_rslt(1) < 0.0000001 )
            pol_rslt = pol_rslt(2:end) ;
    end
    
    pl = min(X)-10:1:max(X)+10;
    plot ( pl , polyval(rslt,pl) , X , Y , '*' );

    if(optionalPoint)
        value = polyval(pol_rslt,v);
    end

    pol_rslt = poly2sym ( pol_rslt ) ;
    pol_rslt = char(pol_rslt);
end

