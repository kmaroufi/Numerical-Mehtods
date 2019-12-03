function [rslt, value] = Lagrange(f,X,Y,optionalPoint, v, d, isFEntered)
value = 0;
    format short
    
    if(isFEntered)
        for i = 1:length(X)
            Y(i) = double(subs(f,'x', X(i)));
        end
    end
    
    n = size(X,2);

    l = zeros(n,n) ;
    
    for i = 1:n
        l(i,n) = 1 ;
        for j = 1:n
            if ( j ~= i )
                a = [ 1 , -X(j) ] ; 
                b = conv ( l(i,:) , a ) ;
                l(i,:) = b(2:n+1) ;
                l(i,:) = l(i,:) / (X(i)-X(j) ) ;
                l = round(l,d);
                %disp( l(i,:) ) ;
            end
        end
        %disp( l(i,:) );
    end

    rslt = zeros(1,n);
    for i = 1:n
        rslt = rslt + Y(i) * l(i,:) ;  
        rslt = round(rslt,d);
    end
    
    while ( rslt(1) < 0.0000001 )
        rslt = rslt(2:end) ;
    end
   
    
    h = ( max(X) - min(X) ) / (n-1) ;
    pl = min(X)-10*h:h/10:max(X)+10*h;
    plot ( pl , polyval(rslt,pl) , X , Y , '*' );
    
    if(optionalPoint)
        value = polyval(rslt,v);
    end
    rslt = poly2sym ( rslt ) ;
    rslt = char(rslt);
end