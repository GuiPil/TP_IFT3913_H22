Authors: Guillaume Pilon (1055643), Jérémie Tousignant (1038551)

Repository : https://github.com/GuiPil/TP_IFT3913_H22/tree/main/TP1

Goal : Developing a tool that extract metric, useful into the analysis of the quality of code

Compile: Our project is using jdk-17, the source file is at ../TP1/src, to compile the project you can either create at
jar files with the help of your favorite IDE, the entry class is Main, or you can compile using
"javac -classpath . src/Main.java".

Execute: To execute the code, the following command "java -jar TP1.jar arg1 arg2 arg3" should be used to ensure an
optimal execution.The arguments corresponding to file_path(arg1), extension(arg2) and output_path(arg3) or the command
line "java ../Main arg1 arg2 arg3". They are described in more details in the "Use" section below.

Here is an exemple command to run the jar file :
java -jar TP1.jar path/to/project/to/analyse .java output/path/for/csv/

Use : Our program extract in all the files containing the specified extension metric useful to analyse the quality of
the code. the first argument should be the path of the folder that is analysed.The second one, should be the extension
that we want to analyse (ex: .java, .py, .c, etc.)(Note : only .java works in that implementation). The third argument,
is the path were the resulting file should be stored. More than a third argument will be ignored. Once the programme is
done running, two .csv files are created at the indicated path by the third argument. One name classes.csv contains all
the metrics for the class files and the paquets.csv all the metrics for the packages in the path indicated by the first
argument. In this integration of the project it is preferable to delete the .csv before running the program again. If
not, the new information will be under the old one, generating large .csv and frankly not that readable.