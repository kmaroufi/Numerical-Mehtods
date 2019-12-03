function [mn_f, value] = Curve_Fitting(f,X,Y,optionalPoint,v,d,isFEntered,fnc)
value = 0;

    if(isFEntered)
        for i = 1:length(X)
            Y(i) = double(subs(f,'x', X(i)));
        end
    end

    h = X(2) - X(1) ;
    mn = inf ;
    div_zero = false ;

    sa = [ length(X) , sum(X) , sum(X.^2) ; sum(X) , sum(X.^2) , sum(X.^3) ; sum(X.^2) , sum(X.^3) , sum(X.^4) ] ;
    sy = [ sum(Y) ; sum(X.*Y) ; sum((X.^2).*Y) ] ;
    a = sa \ sy ;
    f = a(1) + a(2)*X +a(3)*(X.^2) ;
    a = round(a,d);
    str_f = [ num2str(a(3)) , '*x^2+' , num2str(a(2)) , '*x+' , num2str(a(1)) ] ;
    e = er(f,Y);
    
    if ( strcmp(fnc,'') || strcmp(fnc,'a*x^2+b*x+c') )
        mn = e ;
        mn_f = str_f ;
    end
    
    new_x = X ;
    new_y = 1./Y ;
    sx = [ length(new_x) , sum(new_x) ; sum(new_x) , sum(new_x.^2)];
    sy = [ sum(new_y) ; sum(new_x .* new_y ) ];
    a = sx \ sy ;
    f = 1./( a(1) + a(2)*X ) ;
    a = round(a,d);
    str_f = [ '1/(' , num2str(a(2)) , '*x+' , num2str(a(1)) , ')' ] ;
    
    e = er(f,Y);
    
     if ( ( e < mn && strcmp(fnc,'') ) || strcmp(fnc,'1/a*x+b') )
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
    a = round(a,d);
    str_f = [ num2str(a(2)) , '*log(x)+' num2str(a(1)) ] ;
    
     if ( ( e < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*log(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = 1./X ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '/x+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( e < mn && strcmp(fnc,'') ) || strcmp(fnc,'a/x+b') )
        mn = e ;
        mn_f = str_f;
        div_zero = true ;
    end
    
    Y1 = log(Y) ;
    a = polyfit(X, Y1, 1);
    a = round(a,d);
    str_f = [ num2str(exp(a(2))) , '*exp(' num2str(a(1)) , '*x)' ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    
    disp(e);
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*exp(b*x)') )
        mn = e ;
        mn_f = str_f;
    end
    
    
    %trigonometric functions :
    
    X1 = sin(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*sin(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*sin(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = cos(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*cos(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( e < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*cos(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = tan(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*tan(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*tan(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = asin(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*asin(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*asin(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = acos(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*acos(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;

    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*acos(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = atan(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*atan(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*atan(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = sinh(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*sinh(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( e < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*sinh(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = cosh(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*cosh(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'a*cosh(x)+b') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = tanh(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*tanh(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'tanh(x)') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = asinh(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*asinh(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'asinh(x)') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = acosh(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*acosh(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'acosh(x)') )
        mn = e ;
        mn_f = str_f;
    end
    
    X1 = atanh(X) ;
    a = polyfit(X1, Y, 1);
    a = round(a,d);
    str_f = [ num2str(a(1)) , '*atanh(x)+' num2str(a(2)) ] ;
    e = er( subs(str_f,X) , Y ) ;
    
    if ( ( vpa(e) < mn && strcmp(fnc,'') ) || strcmp(fnc,'atanh(x)') )
        mn = e ;
        mn_f = str_f;
    end
    
    pl = min(X)-10*h:h/10:max(X)+10*h;
    if ( div_zero )
        pl = pl(pl~=0);
    end
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