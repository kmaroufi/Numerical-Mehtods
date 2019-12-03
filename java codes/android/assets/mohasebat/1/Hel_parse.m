function [ val,absErr,folanArray, fA2] = Hel_parse( e, depth )
%UNTITLED3 Summary of this function goes here
%   e.g. vals = [{'a', 'b', 'c'}, [1, 2, 3]]
%   e = a * b + c
    counter = 0;
    for i = 1:length(e)
        if(e(i) == '+' || e(i) == '-' || e(i) == '*' ||e(i) == '/' ||e(i) == '^' )
            counter = counter + 1;
        end
    end
    e = strrep(e , ' ', '');
    op_chars = {'+', '-', '*', '/', '^'};
    op_pris = [2, 2, 1, 1, 0];
    folanArray = sym.empty;
    fA2 = [];
    ops = containers.Map(op_chars, op_pris);
    if length(e) == 1
        val = sym(e);
        absErr = sym(strcat('d',e));
        folanArray = [folanArray,absErr];
        fA2 = [fA2, depth];
        return;
    end
    
    op = '';
    pri = -1;
    op_i = length(e);
    count = 0;
    for i = length(e) : -1 : 1
        if (e(i) == ')')
            count = count + 1;
            continue;
        end
        if (e(i) == '(')
            count = count - 1;
            continue;
        end
        if (count > 0)
            continue;
        end
       if (ops.isKey(e(i)))
          if (ops(e(i)) > pri)
              op = e(i);
              pri = ops(e(i));
              op_i = i;
          end
       end
    end
    
   if(pri == -1)
      [val,absErr,folanArray, fA2] =  Hel_parse(e(2:(length(e)-1)), depth+1);
       return
    end
    [val1,err1,folanArray1, fA21] = Hel_parse(e(1:op_i - 1), depth+1);
    [val2,err2,folanArray2, fA22] = Hel_parse(e(op_i + 1:length(e)), depth+1);
    
    switch(op)
        case '+'
            val = val1 + val2;
            absErr = err1 + err2;
            folanArray = [folanArray1,folanArray2];
            fA2 = [fA21, fA22];
            folanArray = [folanArray , absErr];
            fA2 = [fA2, depth];
            return
        case '-'
            val = val1 - val2;
            absErr = err1 + err2;
            folanArray = [folanArray1,folanArray2];
            fA2 = [fA21, fA22];
            folanArray = [folanArray , absErr];
            fA2 = [fA2, depth];
            return
        case '*'
            val = val1 * val2;
            absErr = err1*val2 + err2*val1;
            folanArray = [folanArray1,folanArray2];
            fA2 = [fA21, fA22];
            folanArray = [folanArray , absErr];
            fA2 = [fA2, depth];
            return
        case '/'
            val = val1 / val2;
            absErr = (val1*err2 + val2*err1)/val2^2;
            folanArray = [folanArray1,folanArray2];
            fA2 = [fA21, fA22];
            folanArray = [folanArray , absErr];
            fA2 = [fA2, depth];
            return
        case '^'
            val = val1 ^ val2;
            absErr = log(val1) * err2 + val2/val1 * err1;
            folanArray = [folanArray1,folanArray2];
            fA2 = [fA21, fA22];
            folanArray = [folanArray , absErr];
            fA2 = [fA2, depth];
            return
    end
end

