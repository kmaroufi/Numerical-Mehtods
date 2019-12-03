function [finalValue,finalErr, folanArray,valueFolanArray] = calculating(equation,parameters,initialValues,initialValuesState,initialErrors,initialErrorsState)
parameters = cellstr(parameters);
parameters = transpose(parameters);
[value,abserr,v,fa2] = Hel_parse(equation,0);
for i = 1:length(initialErrors)
    parametersym(i)= sym(strcat('d' , parameters(i)));
   
end
initialErrors = sym(initialErrors);
for i = 1:length(initialErrorsState)
    if(initialErrorsState(i) == 0 && initialValuesState(i) == 1)
        initialErrors(i) = errorEstimation(initialValues(i));
    elseif(initialErrorsState(i) == 0 && initialValuesState(i) == 0)
        initialErrors(i) = parametersym(i);
    end
end
parameters = sym(parameters);
initialValues = sym(initialValues);
for i = 1:length(initialValuesState)
    if(initialValuesState(i) == 0)
        initialValues(i) = parameters(i);
    end
end
finalValue = subs(value,parameters,initialValues);
finalValue = char(finalValue);
finalErr = subs(abserr,parameters,initialValues);
finalErr = subs(finalErr,parametersym,initialErrors);
finalErr = char(finalErr);
u = subs(v,parameters,initialValues);
u = subs(u,parametersym,initialErrors);
valueFolanArray = string('a');
for i = 1:length(u)
    valueFolanArray(i) = char(u(i));
end
folanArray = string('a');
for i = 1:length(v)
    folanArray(i) = char(v(i));
end
end

        
        