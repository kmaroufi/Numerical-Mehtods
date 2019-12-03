function [pol_rslt,value] = Newton_Divided_Differences(f,X,Y,optionalPoint,v,d,isFEntered)
value = 0;
    format short
    
    if(isFEntered)
        for i = 1:length(X)
            Y(i) = double(subs(f,'x', X(i)));
        end
    end
    
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
    
    rslt = round(rslt,d);
    
    pol_rslt = zeros(1,n) ;
    pol_rslt(n) = 1 * rslt(1) ;
    
    %disp(Y) ;
    %disp(rslt) ;
    
    for i = 2:n
        pol = zeros (1,n-i+1) ;
        pol(n-i+1) = 1 * rslt(i) ;
        for j = 1:i-1
           pol = conv ( pol , [ 1 , -X(j)] ); 
           pol = round(pol,d);
        end
        pol_rslt = pol_rslt + pol ;
        pol_rslt = round(pol_rslt,d);
    end
    
    while ( pol_rslt(1) < 0.0000001 )
            pol_rslt = pol_rslt(2:end) ;
    end
    
    h = ( max(X) - min(X) ) / (n-1) ;
    pl = min(X)-10*h:h/10:max(X)+10*h;
    plot ( pl , polyval(rslt,pl) , X , Y , '*' );

    
    if(optionalPoint)
        value = polyval(pol_rslt,v);
    end
    pol_rslt = vpa(poly2sym ( pol_rslt )) ;
    pol_rslt = char(pol_rslt);
end

