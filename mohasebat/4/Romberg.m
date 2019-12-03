function result = Romberg(f,a,b,n,ac)
    format long ;
    arr = zeros (n,n+1);
    arr(1,1) = b-a ;
    for i = 2:n
        arr(i,1) = arr(i-1,1) / 2 ;
    end
    
    for i = 1:n
        sum = 0 ;
        h = arr(i,1) ;
        for j = a:h:b
            x = j ;
            Y = eval(f) ;
            if ( j ~= a && j ~= b )
                sum = sum + 2*Y ;
            else
                sum = sum + Y ;
            end
            sum = round( sum *(10^ac) )/(10^ac);
        end
        arr(i,2) = h/2 * sum ;
    end
    
    for j = 3:n+1
        for i = j-1:n
            arr(i,j) = 4^(j-2)*arr(i,j-1) - arr(i-1,j-1 ) ;
            arr(i,j) = arr(i,j) / ( 4^(j-2) -1) ;
            arr(i,j) = round( arr(i,j) *(10^ac) )/(10^ac);
        end
    end
    result = arr(n,n+1) ;
    
    result = string(result);
end

% 'x^4' , 0 , 1 , 2 , 5