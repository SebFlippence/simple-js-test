var dependencyVarConverterResponse = "";
if (dependencies) {
    for (dependencyIndex in dependencies) {
        dependencyVarConverterResponse = dependencyVarConverterResponse == "" ?
                                                dependencies[dependencyIndex] :
                                                dependencyVarConverterResponse + "," + dependencies[dependencyIndex];
    }
}
