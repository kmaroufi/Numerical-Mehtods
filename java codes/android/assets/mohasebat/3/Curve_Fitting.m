function [mn_f, value] = Curve_Fitting(f,X,Y,optionalPoint,v)
value = 0;
    sa = [ length(X) , sum(X) , sum(X.^2) ; sum(X) , sum(X.^2) , sum(X.^3) ; sum(X.^2) , sum(X.^3) , sum(X.^4) ] ;
    sy = [ sum(Y) ; sum(X.*Y) ; sum((X.^2).*Y) ] ;
    a = sa \ sy ;
    f = a(1) + a(2)*X +a(3)*(X.^2) ;
    str_f = [ num2str(a(3)) , '*x^2+' , num2str(a(2)) , '*x+' , num2str(a(1)) ] ;
    e = er(f,Y);
    
    mn = e ;
    mn_f = str_f ;
    
    new_x = X ;
    new_y = 1./Y ;
    sx = [ length(new_x) , sum(new_x) ; sum(new_x) , sum(new_x.^2)];
    sy = [ sum(new_y) ; sum(new_x .* new_y ) ];
    a = sx \ sy ;
    f = 1./( a(1) + a(2)*X ) ;
    str_f = [ '1/(' , num2str(a(2)) , '*x+' , num2str(a(1)) , ')' ] ;
    
    e = er(f,Y);
    
    if ( e < mn )
        mn = e ;
        mn_f = str_f;
    end
    
    new_x = log(X) ;
    new_y = Y ;
    sx = [ length(new_x) , sum(new_x) ; sum(new_x) , sum(new_x.^2)];
    sy = [ sum(new_y) ; sum(new_x .* new_y ) ];
    a = sx \ sy ;
    f = a(1) + a(2)*log(X) ;
    e = er(f,Y);
    str_f = [ num2str(a(2)) , '*log(x)+' num2str(a(1)) ] ;
    
    if ( e < mn )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = 1 ./X ;
    a = polyfit(X1, Y, 1);
    str_f = [ num2str(a(1)) , '/x+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( e < mn )
        mn = e ;
        mn_f = str_f;
    end
    
    
    
    pl = min(X)-10:1:max(X)+10;
    plot ( pl , subs( mn_f , pl ) , X , Y , '*' );


    if(optionalPoint)
        value = double(subs(mn_f,v));
    end
    mn_f = char(mn_f);
end


function rslt = er(a, b)
    sa = sum(( a - b ).^2) ;
    l = length(a) ;
	rslt = sqrt( sa / l ) ;
end

%p21('', [-4 -3 -2 -1 0 1 2 3 4 5] , [ 38 20 11 3 -1 2 6 14 26 44] , 0 )