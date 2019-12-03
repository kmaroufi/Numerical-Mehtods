function [finalValue,absErr,valueAbsErr,relErr,valueRelErr,initialValues,initialErrors,initialRelativeErrors] = calculating(equation,parameters,initialValues,initialValuesState,initialErrors,initialErrorsState, n)
parameters = cellstr(parameters);
parameters = transpose(parameters);
[value,abserr,absErrSym,fa2,formulArray] = Hel_parse(equation,0);
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

finalValue = vpa(finalValue,n);
finalValue = string(char(finalValue));


%calculating and converting valueAbsErr to String
u = subs(absErrSym,parameters,initialValues);
u = subs(u,parametersym,initialErrors);
valueAbsErr = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, n);
    valueAbsErr(i) = char(a);
end

%converintg absErr to String
absErr = string('a');
for i = 1:length(absErrSym)
    absErr(i) = char(absErrSym(i));
end

% calculating and converitng initialRelativeErrors to String
initialRelativeErrors = initialErrors ./ initialValues;
v = initialRelativeErrors;
initialRelativeErrors = string('a');
for i = 1:length(v)
    a = v(i);
    a = vpa(a, n);
    initialRelativeErrors(i) = char(a);
end

%calculating symRelErr and symValueRelErr
symRelErr = absErrSym ./ formulArray;
symValueRelErr = subs(symRelErr,parameters,initialValues);
symValueRelErr = subs(symValueRelErr,parametersym,initialErrors);

%converting relError to String
relErr = string('a');
for i = 1:length(symRelErr)
    a = symRelErr(i);
    a = vpa(a, n);
    relErr(i) = char(a);
end

%converting valueRelError to String
valueRelErr = string('a');
for i = 1:length(symValueRelErr)
    a = symValueRelErr(i);
    a = vpa(a, n);
    valueRelErr(i) = char(a);
end

%converting intialValues to String
t = initialValues;
initialValues = string('a');
for i = 1:length(t)
    a = t(i);
    a = vpa(a, n);
    initialValues(i) = char(a);
end

%converting intialErrors to String
u = initialErrors;
initialErrors = string('a');
for i = 1:length(u)
    a = u(i);
    a = vpa(a, n);
    initialErrors(i) = char(a);
end

end

        
        