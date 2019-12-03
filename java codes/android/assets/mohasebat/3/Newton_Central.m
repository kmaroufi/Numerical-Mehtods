function [rslt,value] = Newton_Central(f,X,Y,optionalPoint,v)
value = 0;
    format short
    
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
                %disp( l(i,:) ) ;
            end
        end
        %disp( l(i,:) );
    end

    rslt = zeros(1,n);
    for i = 1:n
        rslt = rslt + Y(i) * l(i,:) ;  
    end
    
    while ( rslt(1) < 0.0000001 )
        rslt = rslt(2:end) ;
    end

    disp ( polyval(rslt,v) ) ;
   
    
    pl = min(X)-10:1:max(X)+10;
    plot ( pl , polyval(rslt,pl) , X , Y , '*' );
    
    if(optionalPoint)
        value = polyval(rslt,v);
    end
    rslt = poly2sym ( rslt ) ;
    rslt = char(rslt);
end