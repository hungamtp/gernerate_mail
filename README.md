#Build project by maven  

1.mvn clean compile assembly:single  

2.mvn install 

#After building project ,  the jar file is located in target folder 

3.cd target 

#Run </br>

4.java -cp sendEmail-1.0-SNAPSHOT.jar com.sendmail.Main <template_file_path> <customer_file_path> <outout_folder> <error_file_path> 


ex :java -cp sendEmail-1.0-SNAPSHOT.jar com.sendmail.Main ../input/email_template.json ../input/customers.csv ../output ../errors/errors.csv </br>
